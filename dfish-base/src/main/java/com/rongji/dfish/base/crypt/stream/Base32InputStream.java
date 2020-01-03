package com.rongji.dfish.base.crypt.stream;

import java.io.InputStream;

/**
 * 每次读取读取8个字节的数据，并转化成5个字节，直到最后一组可能不是5的倍数。
 */
public class Base32InputStream extends AbstractPresentInputStream {

    public Base32InputStream(InputStream in) {
        super(in);
        TEXT_SIZE = 8;
        BIN_SIZE = 5;
        inBuff = new byte[TEXT_SIZE];
        outBuff = new byte[BIN_SIZE];
    }

    private static final byte[] DECODE_TABLE = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            //前0-31都是控制字符
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 1, 2, 3, 4, 5, 6, 7,
            8, 9, 0, 0, 0, 0, 0, 0,
            //数字32-63 中包含数字
            0, 10, 11, 12, 13, 14, 15, 16,//A-G
            17, 1, 18, 19, 1, 20, 21, 0,//H-O
            22, 23, 24, 25, 26, 0, 27, 28,//P-W
            29, 30, 31, 0, 0, 0, 0, 0,//XYZ
            //64-95 是大写字母
            0, 10, 11, 12, 13, 14, 15, 16,//a-g
            17, 1, 18, 19, 1, 20, 21, 0,//h-o
            22, 23, 24, 25, 26, 0, 27, 28,//p-w
            29, 30, 31, 0, 0, 0, 0, 0,//xyz
            //96-127 是小写字母
    };

    @Override
    protected void doChunk(){
        byte[] out=this.outBuff;
        byte[] in=this.inBuff;
        if(inBuffLen==0){
            outBuffLen=0;
        }else if(inBuffLen==2){
            out[0] = (byte) (((DECODE_TABLE[in[0]] & 0x1F) << 3) | ((DECODE_TABLE[in[1]] & 0x1C) >> 2));
        }else if(inBuffLen==4){
            out[0] = (byte) (((DECODE_TABLE[in[0]] & 0x1F) << 3) | ((DECODE_TABLE[in[1]] & 0x1C) >> 2));
            out[1] = (byte) (((DECODE_TABLE[in[1]] & 0x03) << 6) | ((DECODE_TABLE[in[2]] & 0x1F) << 1) | ((DECODE_TABLE[in[3]] & 0x10) >> 4));
        }else if(inBuffLen==5){
            out[0] = (byte) (((DECODE_TABLE[in[0]] & 0x1F) << 3) | ((DECODE_TABLE[in[1]] & 0x1C) >> 2));
            out[1] = (byte) (((DECODE_TABLE[in[1]] & 0x03) << 6) | ((DECODE_TABLE[in[2]] & 0x1F) << 1) | ((DECODE_TABLE[in[3]] & 0x10) >> 4));
            out[2] = (byte) (((DECODE_TABLE[in[3]] & 0x0F) << 4) | ((DECODE_TABLE[in[4]] & 0x1E) >> 1));
        }else if(inBuffLen==7){
            out[0] = (byte) (((DECODE_TABLE[in[0]] & 0x1F) << 3) | ((DECODE_TABLE[in[1]] & 0x1C) >> 2));
            out[1] = (byte) (((DECODE_TABLE[in[1]] & 0x03) << 6) | ((DECODE_TABLE[in[2]] & 0x1F) << 1) | ((DECODE_TABLE[in[3]] & 0x10) >> 4));
            out[2] = (byte) (((DECODE_TABLE[in[3]] & 0x0F) << 4) | ((DECODE_TABLE[in[4]] & 0x1E) >> 1));
            out[3] = (byte) (((DECODE_TABLE[in[4]] & 0x01) << 7) | ((DECODE_TABLE[in[5]] & 0x1F) << 2) | ((DECODE_TABLE[in[6]] & 0x18) >> 3));
        }else {
            out[0] = (byte) (((DECODE_TABLE[in[0]] & 0x1F) << 3) | ((DECODE_TABLE[in[1]] & 0x1C) >> 2));
            out[1] = (byte) (((DECODE_TABLE[in[1]] & 0x03) << 6) | ((DECODE_TABLE[in[2]] & 0x1F) << 1) | ((DECODE_TABLE[in[3]] & 0x10) >> 4));
            out[2] = (byte) (((DECODE_TABLE[in[3]] & 0x0F) << 4) | ((DECODE_TABLE[in[4]] & 0x1E) >> 1));
            out[3] = (byte) (((DECODE_TABLE[in[4]] & 0x01) << 7) | ((DECODE_TABLE[in[5]] & 0x1F) << 2) | ((DECODE_TABLE[in[6]] & 0x18) >> 3));
            out[4] = (byte) (((DECODE_TABLE[in[6]] & 0x07) << 5) | ((DECODE_TABLE[in[7]] & 0x1F)));
        }
        outBuffOff =0;
    }
}
