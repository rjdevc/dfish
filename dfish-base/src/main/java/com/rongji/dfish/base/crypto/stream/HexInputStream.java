package com.rongji.dfish.base.crypto.stream;

import java.io.IOException;
import java.io.InputStream;

/**
 *  把原文输出成16进制格式。
 */
public class HexInputStream extends AbstractPresentInputStream {
    private static final byte[] HEX_DE = { // 用于加速解密的cache
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 16
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 32
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, // 48
            0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, //64
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 80
            0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // 96

    /**
     * 构造函数
     * @param in 输入流
     */
    public HexInputStream(InputStream in) {
        super(in);
        inBuff = new byte[2];
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        //可尝试双倍读取in 的内容，做解压缩
        byte[] buff = new byte[2 * len];
        int rread = in.read(buff);
        if (rread < 0) {
            return rread;
        }
        // 剩余的字符应该加入到洗一次read中去
        int read = 0;
        for (int i = 0; i < rread; i++) {
            byte by = buff[i];
            if (by < '0') { //空格 \r \n \t
                continue;
            }
            if (inBuffLen++ == 0) {
                inBuff[0] = HEX_DE[by];
            } else {
                byte v = (byte) (inBuff[0] << 4 | HEX_DE[by]);
                b[off + read++] = v;
                inBuffLen = 0;
            }
        }
        return read;
    }


    @Override
    protected void doChunk(){}

    /**
     * 判断改字符串是否是HEX格式
     * @param key String
     * @return boolean
     */
    public static boolean isHex(String key) {
        if(key.length()%2==1){
            return false;
        }
        char[] chs=key.toCharArray();
        for(char c:chs){
//            if(c<'0'||(c>'9'&& c<'A')||(c>'F'&& c<'a')||c>'f'){
//                return false;
//            }
            if(c<'A'){
                if(c>'9'){
                    return false;
                }
                if(c<'0'){
                    return false;
                }
            }else if(c<'a'){
                if(c>'F'){
                    return false;
                }
            }else{
                if(c>'f'){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 将HEX格式的字符串转为 byte[]
     * @param key
     * @return
     */
    public static byte[] parseHex(String key){
        byte[] bytes=new byte[key.length()/2];
        char[] chs=key.toCharArray();
        for(int i=0;i<chs.length;i+=2){
            int hi=HEX_DE[chs[i]];
            int low=HEX_DE[chs[i+1]];
            bytes[i/2]=(byte)((hi<<4)|low);
        }
        return bytes;
    }

}