package com.rongji.dfish.base.crypt.stream;

import java.io.IOException;
import java.io.OutputStream;

public class Base32OutputStream extends AbstractPresentOutputStream {
    public Base32OutputStream(OutputStream out) {
        super(out);
        TEXT_SIZE = 8;
        BIN_SIZE = 5;
        buff = new byte[TEXT_SIZE];
    }

    private static final byte[] ALPHABET = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q',
            'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * 完整的5字节转8字节。独立出来，是为了性能。 完整的分组无需复杂计算。
     *
     * @param in
     * @param inPos
     * @param out
     * @param outPos
     */
    @Override
    protected void doChunk(byte[] in, int inPos, byte[] out, int outPos) {
        out[outPos] = ALPHABET[(in[inPos] & 0xF8) >> 3];
        out[outPos + 1] = ALPHABET[((in[inPos] & 0x07) << 2) | ((in[inPos + 1] & 0xC0) >> 6)];
        out[outPos + 2] = ALPHABET[(in[inPos + 1] & 0x3E) >> 1];
        out[outPos + 3] = ALPHABET[((in[inPos + 1] & 0x1) << 4) | ((in[inPos + 2] & 0xF0) >> 4)];
        out[outPos + 4] = ALPHABET[((in[inPos + 2] & 0x0F) << 1) | ((in[inPos + 3] & 0x80) >> 7)];
        out[outPos + 5] = ALPHABET[(in[inPos + 3] & 0x7C) >> 2];
        out[outPos + 6] = ALPHABET[((in[inPos + 3] & 0x03) << 3) | ((in[inPos + 4] & 0xE0) >> 5)];
        out[outPos + 7] = ALPHABET[in[inPos + 4] & 0x1F];
    }

    @Override
    public void flushBuff() throws IOException {
            //不管已存在的chunk是否成组，直接产生结果。
            //Crockford's Base32 不产生PAD
            byte[] chars = new byte[((buffLen * 8) / 5) + ((buffLen % 5) != 0 ? 1 : 0)];

            for (int i = 0, j = 0, index = 0; i < chars.length; i++) {
                if (index > 3) {
                    int b = buff[j] & (0xFF >> index);
                    index = (index + 5) % 8;
                    b <<= index;
                    if (j < buffLen - 1) {
                        b |= (buff[j + 1] & 0xFF) >> (8 - index);
                    }
                    chars[i] = ALPHABET[b];
                    j++;
                } else {
                    chars[i] = ALPHABET[((buff[j] >> (8 - (index + 5))) & 0x1F)];
                    index = (index + 5) % 8;
                    if (index == 0) {
                        j++;
                    }
                }
            }
            out.write(chars);
    }
}
