package com.rongji.dfish.base.crypt.stream;

import java.io.IOException;
import java.io.InputStream;

public class HexInputStream extends AbstractPresentInputStream {
    private static final byte[] HEX_DE = { // 用于加速解密的cache
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 16
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 32
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, // 48
            0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, //64
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 80
            0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // 96

    public HexInputStream(InputStream in) {
        super(in);
        inBlock = new byte[2];
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        //可尝试双倍读取in 的内容，做解压缩
        byte[] buff = new byte[2 * len];
        int rread = in.read(buff);
        if (rread < 0) {
            return rread;
        }
        // 剩余的字符应该加入到洗一次read中去
        int read = 0;
        for (int i = 0; i < rread; i++) {
            byte by = buff[i];
            if (by < '0') { //空格 \r \n \t
                continue;
            }
            if (inBlockLen++ == 0) {
                inBlock[0] = HEX_DE[by];
            } else {
                byte v = (byte) (inBlock[0] << 4 | HEX_DE[by]);
                b[off + read++] = v;
                inBlockLen = 0;
            }
        }
        return read;
    }

    @Override
    protected int readBlock( byte[] in, int inPos,byte[] out, int outPos) {
        return -1;
    }

    @Override
    protected int readTail(byte[] b, int i) {
        return -1;
    }
}