package com.rongji.dfish.base.crypto.stream;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 加密完的数据通常呈现二进制流的状态。所以一般会用可打印字符表达。
 * 常见的有用16进制字符表达，一个二进制字节用2个HEX字符；
 * 用32进制字符表达。5个二进制字节用8个BASE32字符；
 * 用64进制字符表达。3个二进制字节用4个BASE64字符；
 *
 * 与AbstractPresentInputputStream 相反，它负责把原文写成编码过后的格式
 */
public abstract class AbstractPresentOutputStream extends FilterOutputStream {
    protected int TEXT_SIZE = 2;
    protected int BIN_SIZE = 1;
    protected byte[] buff;
    protected int buffLen;
    private final byte[] singleByte = new byte[1];

    /**
     * 构造函数
     * @param out
     */
    public AbstractPresentOutputStream(OutputStream out) {
        super(out);
        buffLen = 0;
    }

    @Override
    public void write(int b) throws IOException {
        singleByte[0] = (byte) b;
        this.write(singleByte);
    }

    @Override
    public void flush() throws IOException{
        if (buffLen > 0) {
            flushBuff();
            buffLen =0;
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        byte[] res = new byte[(buffLen + len) * TEXT_SIZE / BIN_SIZE];
        int filled = 0;
        int consum = 0;
        //如果chunkSize不为0，则需要把上一轮的内容加入到这轮中进行计算。
        if (buffLen > 0) {
            consum = BIN_SIZE - buffLen;
            if(len<consum){
                System.arraycopy(b, 0, buff, buffLen, len);
                buffLen +=len;
                return;
            }
            System.arraycopy(b, 0, buff, buffLen, consum);
            doChunk(buff, 0, res, filled);
            buffLen =0;//使用了。
            filled += TEXT_SIZE;
        }
        int i = off + consum;
        for (; i <= len - BIN_SIZE; i += BIN_SIZE) {
            doChunk(b, i, res, filled);
            filled += TEXT_SIZE;
        }
        if (i > len - BIN_SIZE) {
            buffLen = len - i;
            if(buffLen >0) {
                System.arraycopy(b, i, buff, 0, buffLen);
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
    protected abstract void doChunk(byte[] in, int inPos, byte[] out, int outPos);

    /**
     * 输出一半在buff里面的内容。
     * @throws IOException
     */
    public abstract void flushBuff()throws IOException;
}
