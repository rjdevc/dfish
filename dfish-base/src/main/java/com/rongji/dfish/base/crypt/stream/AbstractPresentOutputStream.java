package com.rongji.dfish.base.crypt.stream;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractPresentOutputStream extends FilterOutputStream {
    protected int TEXT_SIZE = 2;
    protected int BIN_SIZE = 1;
    protected byte[] block;
    protected int blockLen;
    private final byte[] singleByte = new byte[1];

    public AbstractPresentOutputStream(OutputStream out) {
        super(out);
        blockLen = 0;
    }

    @Override
    public void write(int b) throws IOException {
        singleByte[0] = (byte) b;
        this.write(singleByte);
    }

    @Override
    public void flush() throws IOException{
        if (blockLen > 0) {
            writeTail();
            blockLen=0;
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        byte[] res = new byte[(blockLen + len) * TEXT_SIZE / BIN_SIZE];
        int filled = 0;
        int consum = 0;
        //如果chunkSize不为0，则需要把上一轮的内容加入到这轮中进行计算。
        if (blockLen > 0) {
            consum = BIN_SIZE - blockLen;
            if(len<consum){
                System.arraycopy(b, 0, block, blockLen, len);
                blockLen +=len;
                return;
            }
            System.arraycopy(b, 0, block, blockLen, consum);
            writeBlock(block, 0, res, filled);
            blockLen =0;//使用了。
            filled += TEXT_SIZE;
        }
        int i = off + consum;
        for (; i <= len - BIN_SIZE; i += BIN_SIZE) {
            writeBlock(b, i, res, filled);
            filled += TEXT_SIZE;
        }
        if (i > len - BIN_SIZE) {
            blockLen = len - i;
            if(blockLen >0) {
                System.arraycopy(b, i, block, 0, blockLen);
            }
        }
        out.write(res, 0, filled);
    }

    /**
     * 在输出流中输入内容
     * @param in 二进制流读取块
     * @param inPos 二进制流开始读取位置
     * @param out 文本流写入块
     * @param outPos 文本流开始写入位置
     */
    protected abstract void writeBlock(byte[] in, int inPos, byte[] out, int outPos);

    public abstract void writeTail()throws IOException;
}
