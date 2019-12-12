package com.rongji.dfish.base.crypt.stream;

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
    protected int TEXT_SIZE = 2;
    protected int BIN_SIZE = 1;
    protected byte[] inBlock;
    protected int inBlockLen;
    protected int outBlockLen = 0;
    protected byte[] outBlock;



    private final byte[] SINGLE_BYTE = new byte[1];

    public AbstractPresentInputStream(InputStream in) {
        super(in);
        inBlockLen = 0;
    }

    @Override
    public int read() throws IOException {
        int read = read(SINGLE_BYTE);
        if (read <= 0) {//等0时是否要阻断
            return -1;
        }
        return 0xFF & SINGLE_BYTE[0];
    }
    //        public int read(byte[] b,int off,int len)throws IOException{

//            int res=read2(b,off,len);
//            System.out.println("b.len="+b.length+" off="+off+" len="+len+" readlen="+res);
//            ArrayList l=new ArrayList();
//            for(int i=off;i<off+res;i++){
//                l.add(Integer.toHexString(b[i]&0xFF));
//            }
//            System.out.println(l);
//            return res;
//        }
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        //尝试从已处理的数据中读取数据。
        int readFromOutBlock = Math.min(b.length - off, len);
        readFromOutBlock = Math.min(readFromOutBlock - off, outBlockLen);
        if (readFromOutBlock > 0) {
            System.arraycopy(outBlock, outBlock.length - outBlockLen, b, off, readFromOutBlock);
        }
        outBlockLen -= readFromOutBlock;
        int readBin = readFromOutBlock;

        if (off >= b.length) {
            return off;
        }

        byte[] res = new byte[(len - readFromOutBlock + BIN_SIZE - 1) / BIN_SIZE * TEXT_SIZE - inBlockLen];//确保需要这么多的内容。
        int readText = in.read(res);
        if (readText == -1) {
            if (inBlockLen == 0) {
                return -1;
            } else {
                int ret=tryRead(b,off+readBin,len,(realOut,realOutPos)->{return readTail(realOut,realOutPos);});
                inBlockLen=0;
                return  ret;
            }
        }

        int readFromInBlock = 0;
        //如果chunkSize不为0，则需要把上一轮的内容加入到这轮中进行计算。
        if (inBlockLen > 0) {
            readFromInBlock = TEXT_SIZE - inBlockLen;
            System.arraycopy(res, 0, inBlock, inBlockLen, readFromInBlock);
            readBin += tryRead(b,off+readBin,len,(realOut,realOutPos)->{return readBlock(res,0,realOut,realOutPos);});
        }
        int i = readFromInBlock;
        for (; i <= readText - TEXT_SIZE; i += TEXT_SIZE) {
            int copyI=i;
            // 因为lambda中这个必须是final的，所以只能定一个变量。这个变量在过程中是没有变化的。
            readBin += tryRead(b,off+readBin,len,(realOut,realOutPos)->{return readBlock(res,copyI,realOut,realOutPos);});
        }
        if (i > readText - TEXT_SIZE) {
            inBlockLen = readText - i;
            if(inBlockLen >0) {
                System.arraycopy(res, i, inBlock, 0, inBlockLen);
            }
        }
        if (readBin <= len - BIN_SIZE && inBlockLen > 0) {
            //如果有足够的空间，尝试，直接把结果压到最后一组中去
            //因为还有足够空间，这时候是不需要考虑outBlock的影响的。
            int ret= readBin + tryRead(b,off+readBin,len,(realOut,realOutPos)->{return readTail(realOut,realOutPos);});//tryReadTail(b, off + readBin,len);
            inBlockLen=0;
            return ret;
        }
        return readBin;
    }

    /**
     * 防止out并不够长。比如说zip经常只读取一个字节。这时候就要使用outBlock来接收信息。
     * 并把其中一部分拷贝给out
     * @param out
     * @param outPos
     * @param len
     * @param call
     * @return
     */
    private int tryRead(byte[] out, int outPos, int len, ReadTask call) {
        byte[] realOut = out;
        int realOutPos = outPos;
        int outCap = Math.min(out.length, len) - outPos;
        boolean toOutBlock = false;
        if (outCap < BIN_SIZE) {
            toOutBlock = true;
            outBlockLen = BIN_SIZE - out.length + outPos;
            out = new byte[BIN_SIZE];
            outPos = 0;
            outBlock = out;
        }
        int realRead = call.read(out,outPos);//readBlock( in, inPos,out, outPos);
        int read = Math.min(realRead, outCap);
        outBlockLen = BIN_SIZE - read;
        if (toOutBlock) {
            System.arraycopy(out, 0, realOut, realOutPos, read);
        }
        return read;
    }
    private static interface ReadTask{
        int read(byte[] out,int outPos);
    }

    /**
     * 从一整段中读取内容
     * @param out 二进制流写入块
     * @param outPos 二进制流开始写入位置
     * @param in 文本读取块
     * @param inPos 文本开始读取位置
     * @return 成功读取数量
     */
    protected abstract int readBlock( byte[] in, int inPos,byte[] out, int outPos);

    /**
     * 从结尾处读取内容，结尾可能有PAD或者不完整。
     * 结尾 内容在inBlock里存着
     * @param out 二进制流写入块
     * @param outPos 二进制流开始写入位置
     * @return 成功读取数量
     */
    protected abstract int readTail(byte[] out, int outPos);
}