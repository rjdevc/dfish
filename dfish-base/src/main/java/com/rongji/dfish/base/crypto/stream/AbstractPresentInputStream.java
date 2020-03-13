package com.rongji.dfish.base.crypto.stream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 加密完的数据通常呈现二进制流的状态。所以一般会用可打印字符表达。
 * 常见的有用16进制字符表达，一个二进制字节用2个HEX字符；
 * 用32进制字符表达。5个二进制字节用8个BASE32字符；
 * 用64进制字符表达。3个二进制字节用4个BASE64字符；
 * 后两种，因为每次都是成块读取的。所以有时候可能和inputStre的块不完全对应。
 * 所以需要用inBlock 来记录，上一块处理完，输入字节中还有没处理的半块，等待下次处理的时候加入一并处理
 * 用outBlock来记录上次处理完，输出字节中还有没处理的半块。等待下次输出时一并输出。
 */
public abstract class AbstractPresentInputStream extends FilterInputStream {
    protected int TEXT_SIZE = 2;//文本流每单元长度
    protected int BIN_SIZE = 1;//2进制流每单元长度
    protected byte[] inBuff;
    protected int inBuffLen;
    protected int outBuffLen;
    protected byte[] outBuff;
    protected int outBuffOff;
    protected boolean end;



    private final byte[] SINGLE_BYTE = new byte[1];

    public AbstractPresentInputStream(InputStream in) {
        super(in);
        inBuffLen = 0;
        outBuffLen=0;
        outBuffOff =0;
        end=false;
    }

    @Override
    public int read() throws IOException {
        int read = read(SINGLE_BYTE);
        if (read <= 0) {//等0时是否要阻断
            return -1;
        }
        return 0xFF & SINGLE_BYTE[0];
    }

    @Override
    public synchronized void reset() throws IOException {
        this.inBuffLen=0;
        this.outBuffLen=0;
        super.reset();
    }

    //JUST FOR DEBUG
//    public int read(byte[] b, int off, int len) throws IOException {
//            int i=read2(b,off,len);
//            System.out.println("try to read "+len+" bytes,read "+i+" bytes;");
//            return i;
//    }
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        //尝试从已处理的数据中读取数据。
        int trunkRead=0;
        int totalRead=0;
        for(;;) {
            if (outBuffLen > 0) {
                int r = len < outBuffLen ? len : outBuffLen;
                System.arraycopy(outBuff, outBuffOff, b, off, r);
                outBuffOff += r;
                outBuffLen -= r;
                off+=r;
                len -= r;
                totalRead += r;
            }else if(end){
                return totalRead==0?-1:totalRead;
            }
            if(end||len<=0){
                break;
            }

            trunkRead = 0;
            loop:
            while (trunkRead < TEXT_SIZE) {
                int read = in.read();
                //因为用read 如果in没有缓冲会很慢。
                switch (read) {
                    case -1:
                        end = true;
                        break loop;
                    case '\r':
                    case '\n':
                    case '\t':
                    case ' ':
                        break;
                    default:
                        inBuff[trunkRead++] = (byte) read;
                }
            }
            inBuffLen = trunkRead ;
            doChunk();
        }
        return totalRead;
    }

    /**
     * 因为流模式，每次处理的时候，不一定缓冲区的字节数都适合算法整数倍处理。
     * 最后可能会剩下一段byte[] 数组需要单独处理。
     * 这段处理过程定位为doChunk
     */
    protected abstract void doChunk();
}