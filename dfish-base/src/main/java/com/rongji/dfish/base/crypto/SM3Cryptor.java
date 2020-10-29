package com.rongji.dfish.base.crypto;

import com.rongji.dfish.base.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * 国密3 数字摘要算法。
 * https://gitee.com/snowlandltd/utilx4j/blob/master/src/main/java/ltd/snowland/security/SM3.java
 * 算法来源为以上开源地址。
 * 但以上地址，对JAVA的流模式处理比较弱，故在形式上做了较大改动。
 * 核心算法和 初始IV 轮转参数 以及 padding补码规则皆与之相同。
 */
public class SM3Cryptor extends  AbstractCryptor {
    @Override
    protected void doEncrypt(InputStream is, OutputStream os) {
        try {
            this.reset();
            byte[] b = new byte[1024 * 8];
            int len = 0;
            try{
                while ((len = is.read(b)) != -1) {
                    this.update(b,0,len);
                }
                os.write(this.digest());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception ex){
            LogUtil.error(null,ex);
        }
    }

    @Override
    protected void doDecrypt(InputStream is, OutputStream os) {
        throw new UnsupportedOperationException("can not decrypt SM3 message digest");
    }
    @Override
    public void decrypt(InputStream is, OutputStream os){
        throw new UnsupportedOperationException("can not decrypt SM3 message digest");
    }
    @Override
    public String decrypt(String src){
        throw new UnsupportedOperationException("can not decrypt SM3 message digest");
    }

    /**
     * 构造函数
     * @param db
     */
    public SM3Cryptor(CryptorBuilder db){
        super(db);
    }

    private static final byte[] IV = new BigInteger(
            "7380166f" +
                    "4914b2b9" +
                    "172442d7" +
                    "da8a0600" +
                    "a96f30bc" +
                    "163138aa" +
                    "e38dee4d" +
                    "b0fb0e4e", 16).toByteArray();
    private static final int TJ_15 = 0x79cc4519;
    private static final int TJ_63 = 0x7a879d8a;
    private static final byte FIRST_PADDING =  (byte) 0x80 ;
    private static final byte ZERO_PADDING =  (byte) 0x00 ;



    protected byte[] value=null;
    protected byte[] buff;
    protected int buffLen;
    protected long totalByte;

    /**
     * 重置、初始化
     * @see java.security.MessageDigest#reset()
     */
    protected void reset(){
        value=IV;
        buffLen=0;
        buff=new byte[64];
        this.buffLen=0;
        this.totalByte=0;
    }

    /**
     * 流模式的每段更新
     * @param source 内容
     * @param offset 偏移量
     * @param len 长度
     * @see java.security.MessageDigest#update(byte[], int, int)
     */
    protected void update(byte[] source,int offset,int len){
        totalByte+=len;
        int consume=0;
        if (buffLen > 0) {
            consume = 64 - buffLen;
            if(len<consume){
                System.arraycopy(source, 0, buff, buffLen, len);
                buffLen +=len;
                return;
            }
            System.arraycopy(source, 0, buff, buffLen, consume);
            value = cf(value, source, offset);
            buffLen =0;//使用了。
        }
        offset += consume;

        while(offset<=len-64) {
            value = cf(value, source, offset);
            offset+=64;
        }
        if (offset > len - 64) {
            buffLen = len - offset;
            if(buffLen >0) {
                System.arraycopy(source, offset, buff, 0, buffLen);
            }
        }
    }

    /**
     * 获取摘要结果
     * @return byte[]
     * @see java.security.MessageDigest#digest()
     */
    protected byte[] digest(){
        //增加一个FIRST_PADDING  和若干个字节的0 。最后8个字节为整个的bit长度
        //加入buffLen大于55 则 可能导致最后CF两次。
        buff[buffLen]=FIRST_PADDING;
        buffLen++;

        if(buffLen<=56){
            for(;buffLen<56;buffLen++){
                buff[buffLen]=ZERO_PADDING;
            }
        }else{
            for(;buffLen<64;buffLen++){
                buff[buffLen]=ZERO_PADDING;
            }
            value = cf(value, buff, 0);
            Arrays.fill(buff,0,56,ZERO_PADDING);
        }
        byte[]lenthPadding=toByteArray(totalByte*8);
        System.arraycopy(lenthPadding,0,buff,56,8);
        value = cf(value, buff, 0);

        return value;
    }

    /**
     * 单轮摘要(核心算法)
     * @param vi 已有结果
     * @param bi 原文内容
     * @param offset 本轮64byte的初始位置
     * @return
     */
    protected static byte[] cf(byte[] vi, byte[] bi, int offset) {
        int a, b, c, d, e, f, g, h;
        a = toInteger(vi, 0);
        b = toInteger(vi, 4);
        c = toInteger(vi, 8);
        d = toInteger(vi, 12);
        e = toInteger(vi, 16);
        f = toInteger(vi, 20);
        g = toInteger(vi, 24);
        h = toInteger(vi, 28);

        int[] w = new int[68];
        int[] w1 = new int[64];
        for (int i = 0; i < 16; i++) {
            w[i] = toInteger(bi, i*4+ offset);
        }
        for (int j = 16; j < 68; j++) {
            w[j] = p1(w[j - 16] ^ w[j - 9] ^ Integer.rotateLeft(w[j - 3], 15)) ^ Integer.rotateLeft(w[j - 13], 7)
                    ^ w[j - 6];
        }
        for (int j = 0; j < 64; j++) {
            w1[j] = w[j] ^ w[j + 4];
        }
        int ss1, ss2, tt1, tt2;
        for (int j = 0; j < 64; j++) {
            ss1 = Integer.rotateLeft(Integer.rotateLeft(a, 12) + e + Integer.rotateLeft(t(j), j), 7);
            ss2 = ss1 ^ Integer.rotateLeft(a, 12);
            tt1 = ff(a, b, c, j) + d + ss2 + w1[j];
            tt2 = gg(e, f, g, j) + h + ss1 + w[j];
            d = c;
            c = Integer.rotateLeft(b, 9);
            b = a;
            a = tt1;
            h = g;
            g = Integer.rotateLeft(f, 19);
            f = e;
            e = p0(tt2);
        }
        byte[] v = toByteArray(a, b, c, d, e, f, g, h);
        for (int i = 0; i < v.length; i++) {
            v[i] = (byte) (v[i] ^ vi[i]);
        }
        return v;
    }


    /**
     * 取得轮转参数
     * @param pos 位置。
     * @return int
     */
    private static int t(int pos) {
        if(pos>=64){
            throw new RuntimeException("data invalid");
        }
        if(pos>=16){
            return TJ_63;
        }
        if(pos>=0){
            return TJ_15;
        }
        throw new RuntimeException("data invalid");
    }

    private static int ff(int x, int y, int z, int pos) {
        if(pos>=64){
            throw new RuntimeException("data invalid");
        }
        if(pos>=16){
            return (x & y) | (x & z) | (y & z);
        }
        if(pos>=0){
            return x ^ y ^ z;
        }
        throw new RuntimeException("data invalid");
    }

    private static int gg(int x, int y, int z, int pos) {
        if(pos>=64){
            throw new RuntimeException("data invalid");
        }
        if(pos>=16){
            return (x & y) | (~x & z) ;
        }
        if(pos>=0){
            return x ^ y ^ z;
        }
        throw new RuntimeException("data invalid");
    }

    private static int p0(int x) {
        return x ^ Integer.rotateLeft(x, 9) ^ Integer.rotateLeft(x, 17);
    }

    private static int p1(int x) {
        return x ^ Integer.rotateLeft(x, 15) ^ Integer.rotateLeft(x, 23);
    }

    private static int toInteger(byte[] source, int offset) {
        return (source[offset]&0xff)<<24|
                (source[offset+1]&0xff)<<16|
                (source[offset+2]&0xff)<<8|
                (source[offset+3]&0xff);
    }

    private static byte[] toByteArray(int a, int b, int c, int d, int e, int f, int g, int h)  {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(32);
            baos.write(toByteArray(a));
            baos.write(toByteArray(b));
            baos.write(toByteArray(c));
            baos.write(toByteArray(d));
            baos.write(toByteArray(e));
            baos.write(toByteArray(f));
            baos.write(toByteArray(g));
            baos.write(toByteArray(h));
            return baos.toByteArray();
        }catch (Exception ex){
            //  不该发生
            return null;
        }
    }

    private static byte[] toByteArray(int i) {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte) (i >>> 24);
        byteArray[1] = (byte) (i >>> 16);
        byteArray[2] = (byte) (i >>> 8);
        byteArray[3] = (byte) i;
        return byteArray;
    }
    private static byte[] toByteArray(long l) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (l >>> ((7 - i) * 8));
        }
        return bytes;
    }
}
