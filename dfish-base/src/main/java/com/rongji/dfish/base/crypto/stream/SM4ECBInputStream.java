package com.rongji.dfish.base.crypto.stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * SM4算法的流模式适配器
 */
public class SM4ECBInputStream extends AbstractPresentInputStream {
    private long[] key;
    private int preread;

    /**
     * 构造函数
     * @param in 输入流
     * @param key 秘钥
     */
    public SM4ECBInputStream(InputStream in,byte[] key) {
        super(in);
        TEXT_SIZE = 16;
        BIN_SIZE = 16;
        inBuff = new byte[TEXT_SIZE];
        outBuff =new byte[BIN_SIZE];
        this.key=new long[32];
        SM4.setDecryptKey(this.key,key);
    }

    @Override
    protected void doChunk() {
        if(inBuffLen==0){
            outBuffLen=0;
            return;
        }
        SM4.oneRound(key,inBuff,0,outBuff,0);
        if(end){
            if(outBuff[15]>=16||outBuff[15]<0){
                throw new RuntimeException(new javax.crypto.BadPaddingException("Given final block not properly padded"));
            }
            outBuffLen = 16-outBuff[15];//把PADDING去除
        }else {
            outBuffLen = 16;
        }
        outBuffOff=0;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        //尝试从已处理的数据中读取数据。
        int trunkRead=0;
        int totalRead=0;
        preread=in.read();
        if(preread==-1){
            end=true;
        }
        for(;;) {
            if (outBuffLen > 0) {
                int r = len < outBuffLen ? len : outBuffLen;
                try {
                    System.arraycopy(outBuff, outBuffOff, b, off, r);
                }catch (Exception e){
                    System.out.println("outBuffOff="+outBuffOff+" off="+off);
                    throw e;
                }
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
            inBuff[trunkRead++] = (byte) preread;
            while (trunkRead < TEXT_SIZE) {
                int read = in.read();
                //因为用read 如果in没有缓冲会很慢。
                if(read<0) {
                    //几乎不会发生
                    end = true;
                    break ;
                }else{
                    inBuff[trunkRead++] = (byte) read;
                }
            }
            preread=in.read();
            if(preread==-1){
                //因为有padding的关系。如果不预读，永远不知道ending到了。
                end=true;
            }//先读一个数字出来，如果已经结束，最后一块，直接去除。
            inBuffLen = trunkRead ;
            doChunk();
        }
        return totalRead;
    }

}
