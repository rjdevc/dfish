
package com.rongji.dfish.base.crypto.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 *  把原文输出成BASE64格式。同时不再使用URL上会转义的字符。
 *  由于JDK8.0 和dfish3.x版本 都是使用减号和下划线。唯一不同的是JDK8
 *  使用了=作为PADDING  但这个是一个要转义的字符。它并不合理，
 *  所以5.0版本。转成BASE53不再使用PADDING 但，不管有没有PADDING 是等号还是点号都能解析出原文。
 */
public class Base64UrlsafeOutputStream extends AbstractPresentOutputStream {
    /**
     * 构造函数
     * @param out 输出流
     */
    public Base64UrlsafeOutputStream(OutputStream out) {
        super(out);
        TEXT_SIZE = 4;
        BIN_SIZE = 3;
        buff = new byte[TEXT_SIZE];
    }

    private static final byte[] ALPHABET = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };

    /**
     * 完整的5字节转8字节。独立出来，是为了性能。 完整的分组无需复杂计算。
     *
     * @param in 输入流数据
     * @param inPos 输入流数据开始位置
     * @param out 输出流数据
     * @param outPos 输出流写入位置
     */
    @Override
    protected void doChunk(byte[] in, int inPos, byte[] out, int outPos) {
        out[outPos] = ALPHABET[(in[inPos] & 0xFC) >> 2];
        out[outPos + 1] = ALPHABET[(in[inPos] & 0x03) << 4 | ((in[inPos + 1] & 0xF0) >> 4)];
        out[outPos + 2] = ALPHABET[(in[inPos + 1] & 0x0F) << 2 | ((in[inPos + 2] & 0xC0) >> 6)];
        out[outPos + 3] = ALPHABET[(in[inPos + 2] & 0x3F)];
    }

    @Override
    public void flushBuff() throws IOException {
            //不管已存在的chunk是否成组，直接产生结果。
            byte[] chars = null;
            if (buffLen == 2) {
                chars = new byte[3];
                chars[0] = ALPHABET[(buff[0] & 0xFC) >> 2];
                chars[1] = ALPHABET[(buff[0] & 0x03) << 4 | ((buff[1] & 0xF0) >> 4)];
                chars[2] = ALPHABET[(buff[1] & 0x0F) << 2];
            }else if (buffLen == 1) {
                chars = new byte[2];
                chars[0] = ALPHABET[(buff[0] & 0xFC) >> 2];
                chars[1] = ALPHABET[(buff[0] & 0x03) << 4];
            } else if (buffLen == 3) {
                //仅仅为了容错;
                chars = new byte[4];
                chars[0] = ALPHABET[(buff[0] & 0xFC) >> 2];
                chars[1] = ALPHABET[(buff[0] & 0x03) << 4 | ((buff[1] & 0xF0) >> 4)];
                chars[2] = ALPHABET[(buff[1] & 0x0F) << 2 | ((buff[2] & 0x03) >> 6)];
                chars[3] = ALPHABET[(buff[2] & 0x3F)];
            }
            out.write(chars);
    }
}
