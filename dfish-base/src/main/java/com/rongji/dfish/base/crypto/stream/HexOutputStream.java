package com.rongji.dfish.base.crypto.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 *  把原文输出成16进制格式。
 */
public class HexOutputStream extends AbstractPresentOutputStream {
    /**
     * 构造函数
     * @param out
     */
    public HexOutputStream(OutputStream out) {
        super(out);
    }

    private static final byte[] HEX_EN_BYTE = { // 用于加速加密的cache
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
            'F'};

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        byte[] hex = new byte[len * 2];
        for (int i = 0; i < len; i++) {
            int inte = b[i + off] & 0xff;
            hex[i * 2] = HEX_EN_BYTE[inte >> 4];
            hex[i * 2 + 1] = HEX_EN_BYTE[inte & 0x0f];
        }
        out.write(hex);
    }

    @Override
    protected void doChunk(byte[] block, int i, byte[] res, int filled) {
    }

    @Override
    public void flushBuff() throws IOException {}
}
