package com.rongji.dfish.base.crypto;

import com.rongji.dfish.base.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;

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

    private static int T(int j) {
        if(j>=64){
            throw new RuntimeException("data invalid");
        }
        if(j>=16){
            return TJ_63;
        }
        if(j>=0){
            return TJ_15;
        }
        throw new RuntimeException("data invalid");
    }

    private static int FF(int x, int y, int z, int j) {
        if(j>=64){
            throw new RuntimeException("data invalid");
        }
        if(j>=16){
            return (x & y) | (x & z) | (y & z);
        }
        if(j>=0){
            return x ^ y ^ z;
        }
        throw new RuntimeException("data invalid");
    }

    private static int GG(int x, int y, int z, int j) {
        if(j>=64){
            throw new RuntimeException("data invalid");
        }
        if(j>=16){
            return (x & y) | (~x & z) ;
        }
        if(j>=0){
            return x ^ y ^ z;
        }
        throw new RuntimeException("data invalid");
    }

    private static int P0(int x) {
        return x ^ Integer.rotateLeft(x, 9) ^ Integer.rotateLeft(x, 17);
    }

    private static int P1(int x) {
        return x ^ Integer.rotateLeft(x, 15) ^ Integer.rotateLeft(x, 23);
    }

    byte[] value=null;
    protected byte[] buff;
    protected int buffLen;
    protected long totalByte;
    private void reset(){
        value=IV;
        buffLen=0;
        buff=new byte[64];
    }
    private void update(byte[] source,int offset,int len){
        totalByte+=len;
        int consum=0;
        if (buffLen > 0) {
            consum = 64 - buffLen;
            if(len<consum){
                System.arraycopy(source, 0, buff, buffLen, len);
                buffLen +=len;
                return;
            }
            System.arraycopy(source, 0, buff, buffLen, consum);
            value = CF(value, source, offset);
            buffLen =0;//使用了。
        }
        offset += consum;

        while(offset<=len-64) {
            value = CF(value, source, offset);
            offset+=64;
        }
        if (offset > len - 64) {
            buffLen = len - offset;
            if(buffLen >0) {
                System.arraycopy(source, offset, buff, 0, buffLen);
            }
        }
    }
    private byte[] digest(){
//        totalByte*8;
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
            value = CF(value, buff, 0);
            Arrays.fill(buff,0,56,ZERO_PADDING);
        }
        byte[]lenthPadding=toByteArray(totalByte*8);
        System.arraycopy(lenthPadding,0,buff,56,8);
        value = CF(value, buff, 0);

        return value;
    }



//    @Deprecated
//    private static byte[] padding(byte[] source) throws IOException {
//        if (source.length >= 0x2000000000000000l) {
//            throw new RuntimeException("src data invalid.");
//        }
//        long l = source.length * 8;
//        long k = 448 - (l + 1) % 512;
//        if (k < 0) {
//            k = k + 512;
//        }
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        baos.write(source);
//        baos.write(FIRST_PADDING);
//        long i = k - 7;
//        while (i > 0) {
//            baos.write(ZERO_PADDING);
//            i -= 8;
//        }
//        baos.write(toByteArray(l));
//        return baos.toByteArray();
//    }
//
//
//
//    //主入口
//    public static byte[] hash(byte[] source) throws IOException {
//        byte[] m1 = padding(source);
//        int n = m1.length / 64;
//        byte[] b;
//        byte[] vi = IV;
//        byte[] vi1 = null;
//        for (int i = 0; i < n; i++) {
//            b = Arrays.copyOfRange(m1, i * 64, (i + 1) * 64);
//            vi1 = CF(vi, b);
//            vi = vi1;
//        }
//        return vi1;
//    }


    private static byte[] CF(byte[] vi, byte[] bi,int offset) {
        int a, b, c, d, e, f, g, h;
        a = toInteger(vi, 0);
        b = toInteger(vi, 1);
        c = toInteger(vi, 2);
        d = toInteger(vi, 3);
        e = toInteger(vi, 4);
        f = toInteger(vi, 5);
        g = toInteger(vi, 6);
        h = toInteger(vi, 7);

        int[] w = new int[68];
        int[] w1 = new int[64];
        for (int i = 0; i < 16; i++) {
            w[i] = toInteger(bi, i, offset);
        }
        for (int j = 16; j < 68; j++) {
            w[j] = P1(w[j - 16] ^ w[j - 9] ^ Integer.rotateLeft(w[j - 3], 15)) ^ Integer.rotateLeft(w[j - 13], 7)
                    ^ w[j - 6];
        }
        for (int j = 0; j < 64; j++) {
            w1[j] = w[j] ^ w[j + 4];
        }
        int ss1, ss2, tt1, tt2;
        for (int j = 0; j < 64; j++) {
            ss1 = Integer.rotateLeft(Integer.rotateLeft(a, 12) + e + Integer.rotateLeft(T(j), j), 7);
            ss2 = ss1 ^ Integer.rotateLeft(a, 12);
            tt1 = FF(a, b, c, j) + d + ss2 + w1[j];
            tt2 = GG(e, f, g, j) + h + ss1 + w[j];
            d = c;
            c = Integer.rotateLeft(b, 9);
            b = a;
            a = tt1;
            h = g;
            g = Integer.rotateLeft(f, 19);
            f = e;
            e = P0(tt2);
        }
        byte[] v = toByteArray(a, b, c, d, e, f, g, h);
        for (int i = 0; i < v.length; i++) {
            v[i] = (byte) (v[i] ^ vi[i]);
        }
        return v;
    }
    private static int toInteger(byte[] source, int index,int offset) {
        return (source[index*4+0+offset]&0xff)<<24|
                (source[index*4+1+offset]&0xff)<<16|
                (source[index*4+2+offset]&0xff)<<8|
                (source[index*4+3+offset]&0xff);
    }

    private static int toInteger(byte[] source, int index) {
        return (source[index*4+0]&0xff)<<24|
                (source[index*4+1]&0xff)<<16|
                (source[index*4+2]&0xff)<<8|
                (source[index*4+3]&0xff);
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

    public static byte[] toByteArray(int i) {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte) (i >>> 24);
        byteArray[1] = (byte) ((i & 0xFFFFFF) >>> 16);
        byteArray[2] = (byte) ((i & 0xFFFF) >>> 8);
        byteArray[3] = (byte) (i & 0xFF);
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
