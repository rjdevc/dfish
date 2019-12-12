package com.rongji.dfish.base.crypt.stream;

import java.io.InputStream;
import java.util.Arrays;

/**
 * 每次读取读取8个字节的数据，并转化成5个字节，直到最后一组可能不是5的倍数。
 */
public class Base32InputStream extends AbstractPresentInputStream {

    public Base32InputStream(InputStream in) {
        super(in);
        TEXT_SIZE = 8;
        BIN_SIZE = 5;
        inBlock = new byte[TEXT_SIZE];
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
    protected int readBlock( byte[] in, int inPos,byte[] out, int outPos) {
        out[outPos] = (byte) (((DECODE_TABLE[in[inPos]] & 0x1F) << 3) | ((DECODE_TABLE[in[inPos + 1]] & 0x1C) >> 2));
        out[outPos + 1] = (byte) (((DECODE_TABLE[in[inPos + 1]] & 0x03) << 6) | ((DECODE_TABLE[in[inPos + 2]] & 0x1F) << 1) | ((DECODE_TABLE[in[inPos + 3]] & 0x10) >> 4));
        out[outPos + 2] = (byte) (((DECODE_TABLE[in[inPos + 3]] & 0x0F) << 4) | ((DECODE_TABLE[in[inPos + 4]] & 0x1E) >> 1));
        out[outPos + 3] = (byte) (((DECODE_TABLE[in[inPos + 4]] & 0x01) << 7) | ((DECODE_TABLE[in[inPos + 5]] & 0x1F) << 2) | ((DECODE_TABLE[in[inPos + 6]] & 0x18) >> 3));
        out[outPos + 4] = (byte) (((DECODE_TABLE[in[inPos + 6]] & 0x07) << 5) | ((DECODE_TABLE[in[inPos + 7]] & 0x1F)));
        return 5;
    }

    @Override
    protected int readTail(byte[] b, int off) {

        int range = inBlockLen * BIN_SIZE / TEXT_SIZE;//FIXME

        Arrays.fill(b, off, off + range, (byte) 0);
        for (int i = 0, j = 0, index = 0; i < inBlockLen; i++) {
            int val;
            try {
                val = DECODE_TABLE[inBlock[i]];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException("Illegal character");
            }
            if (index <= 3) {
                index = (index + 5) % 8;
                if (index == 0) {
                    b[off + j++] |= val;
                } else {
                    b[off + j] |= val << (8 - index);
                }
            } else {
                index = (index + 5) % 8;
                b[off + j++] |= (val >> index);
                if (j < range) {
                    b[off + j] |= val << (8 - index);
                }
            }
        }

        return range;
    }
}
