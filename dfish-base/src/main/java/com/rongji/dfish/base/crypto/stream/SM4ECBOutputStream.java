package com.rongji.dfish.base.crypto.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * SM4算法的流模式适配器
 */
public class SM4ECBOutputStream extends AbstractPresentOutputStream {
    private long[] key;
    private boolean flushed;

    /**
     * 构造函数
     * @param out
     * @param key
     */
    public SM4ECBOutputStream(OutputStream out,byte[] key) {
        super(out);
        super.TEXT_SIZE=16;
        super.BIN_SIZE=16;
        buff = new byte[TEXT_SIZE];
        this.key=new long[32];
        SM4.setEncryptKey(this.key,key);
        flushed=false;
    }

    @Override
    protected void doChunk(byte[] in, int inPos, byte[] out, int outPos) {
        SM4.oneRound(key,in,inPos,out,outPos);
    }


    @Override
    public void flushBuff() throws IOException {
        //PADDING.
        int p = 16 - buffLen;
        byte[]padded = new byte[16];
        byte[]ret = new byte[16];
        System.arraycopy(buff, 0, padded, 0,buffLen);
        for (int i = buffLen; i < 16; i++){
            padded[i] = (byte) p;
        }
        doChunk(padded,0,ret,0);
        out.write(ret);
    }

    @Override
    public void flush() throws IOException{
        if (!flushed ) {
            flushBuff();
            flushed=true;
        }
    }
}
