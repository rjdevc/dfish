package com.rongji.dfish.base.crypto.stream;

import java.io.InputStream;

/**
 *  把原文输出成BASE64格式。
 */
public class Base64InputStream extends AbstractPresentInputStream {
    /**
     * 构造函数
     * @param in
     */
    public Base64InputStream(InputStream in) {
        super(in);
        TEXT_SIZE = 4;
        BIN_SIZE = 3;
        inBuff = new byte[TEXT_SIZE];
        outBuff =new byte[BIN_SIZE];
    }

    private static final byte[] DECODE_TABLE = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            //前0-31都是控制字符
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 62, 0, 62, 0, 63,
            52, 53, 54, 55, 56, 57, 58, 59,
            60, 61, 0, 0, 0, 0, 0, 0,
            //数字32-63 中包含数字
            0, 0, 1, 2, 3, 4, 5, 6, //A-G
            7, 8, 9, 10, 11, 12, 13, 14,//H-O
            15, 16, 17, 18, 19, 20, 21, 22, //P-W
            23, 24, 25, 0, 0, 0, 0, 63,//XYZ
            //64-95 是大写字母
            0, 26, 27, 28, 29, 30, 31, 32, //a-g
            33, 34, 35, 36, 37, 38, 39, 40, //h-o
            41, 42, 43, 44, 45, 46, 47, 48, //p-w
            49, 50, 51, 0, 0, 0, 0, 0,//xyz
            //96-127 是小写字母
    };

    @Override
    protected void doChunk( ) {
        if (inBuffLen==0) {
            outBuffLen=0;
        }else if (inBuffLen==2 || inBuff[2] == '=' || inBuff[2] == '.') {
            outBuff[0] = (byte) (((DECODE_TABLE[inBuff[0]] & 0x3F) << 2) | ((DECODE_TABLE[inBuff[ 1]] & 0x30) >> 4));
            outBuffLen=1;
        } else if (inBuffLen==3 ||inBuff[ 3] == '=' || inBuff[ 3] == '.') {
            outBuff[0] = (byte) (((DECODE_TABLE[inBuff[0]] & 0x3F) << 2) | ((DECODE_TABLE[inBuff[ 1]] & 0x30) >> 4));
            outBuff[1] = (byte) (((DECODE_TABLE[inBuff[ 1]] & 0x0F) << 4) | ((DECODE_TABLE[inBuff[ 2]] & 0x3C) >> 2));
            outBuffLen=2;
        } else {
            outBuff[0] = (byte) (((DECODE_TABLE[inBuff[0]] & 0x3F) << 2) | ((DECODE_TABLE[inBuff[ 1]] & 0x30) >> 4));
            outBuff[1] = (byte) (((DECODE_TABLE[inBuff[ 1]] & 0x0F) << 4) | ((DECODE_TABLE[inBuff[2]] & 0x3C) >> 2));
            outBuff[2] = (byte) (((DECODE_TABLE[inBuff[ 2]] & 0x03) << 6) | ((DECODE_TABLE[inBuff[ 3]] & 0x3F)));
            outBuffLen=3;
        }
        outBuffOff =0;
    }
}
