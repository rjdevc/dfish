package com.rongji.dfish.base.crypt.stream;

import java.io.InputStream;

public class Base64InputStream extends AbstractPresentInputStream {

    public Base64InputStream(InputStream in) {
        super(in);
        TEXT_SIZE = 4;
        BIN_SIZE = 3;
        inBlock = new byte[TEXT_SIZE];
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
    protected int readBlock( byte[] in, int inPos,byte[] out, int outPos) {
        if (in[inPos + 2] == '=' || in[inPos + 2] == '.') {
            out[outPos] = (byte) (((DECODE_TABLE[in[inPos]] & 0x3F) << 2) | ((DECODE_TABLE[in[inPos + 1]] & 0x30) >> 4));
            return 1;
        } else if (in[inPos + 3] == '=' || in[inPos + 3] == '.') {
            out[outPos] = (byte) (((DECODE_TABLE[in[inPos]] & 0x3F) << 2) | ((DECODE_TABLE[in[inPos + 1]] & 0x30) >> 4));
            out[outPos + 1] = (byte) (((DECODE_TABLE[in[inPos + 1]] & 0x0F) << 4) | ((DECODE_TABLE[in[inPos + 2]] & 0x3C) >> 2));
            return 2;
        } else {
            out[outPos] = (byte) (((DECODE_TABLE[in[inPos]] & 0x3F) << 2) | ((DECODE_TABLE[in[inPos + 1]] & 0x30) >> 4));
            out[outPos + 1] = (byte) (((DECODE_TABLE[in[inPos + 1]] & 0x0F) << 4) | ((DECODE_TABLE[in[inPos + 2]] & 0x3C) >> 2));
            out[outPos + 2] = (byte) (((DECODE_TABLE[in[inPos + 2]] & 0x03) << 6) | ((DECODE_TABLE[in[inPos + 3]] & 0x3F)));
            return 3;
        }
    }

    @Override
    protected int readTail(byte[] b, int off) {
        if (inBlockLen == 0) {
            return -1;
        }
        if (inBlockLen == 2) {
            b[0 + off] = (byte) (((DECODE_TABLE[inBlock[0]] & 0x3F) << 2) | ((DECODE_TABLE[inBlock[1]] & 0x30) >> 4));
            inBlockLen = 0;
            return 1;
        } else if (inBlockLen == 3) {
            b[0 + off] = (byte) (((DECODE_TABLE[inBlock[0]] & 0x3F) << 2) | ((DECODE_TABLE[inBlock[1]] & 0x30) >> 4));
            b[1 + off] = (byte) (((DECODE_TABLE[inBlock[0 + 1]] & 0x0F) << 4) | ((DECODE_TABLE[inBlock[2]] & 0x3C) >> 2));
            inBlockLen = 0;
            return 2;
        } else {
            inBlockLen = 0;
            return 0;
        }
    }
}
