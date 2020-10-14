package com.rongji.dfish.base.util;

/**
 * byte[]处理类
 */
public class ByteArrayUtil {
    /**
     * 特征byte[] 在src中的位置
     * @param src byte[]
     * @param target byte[]
     * @return int
     * @see java.lang.String#indexOf(int)
     */
    public static int indexOf( byte[] src, byte[] target) {
        return indexOf(src,0,src.length,target);
    }
    /**
     * 特征byte[] 在src中的位置
     * <p>可参考java.lang.String.indexOf(char[], int, int, String, int)方法</p>
     * @param src byte[]
     * @param target byte[]
     * @param off 从第几个字节算起
     * @param len 最多检查到多少个字节
     * @return int 所处位置
     */
    public static int indexOf( byte[] src, int off, int len,byte[] target) {
        byte first = target[0];
        int max = off + len - target.length;
        for (int i = off ; i <= max; i++) {
            if (src[i] != first) {
                while (++i <= max && src[i] != first);
            }
            if (i <= max) {
                int j = i + 1;
                int end = j + target.length - 1;
                for (int k = 1; j < end && src[j] == target[k]; j++, k++);
                if (j == end) {
                    return i - off;
                }
            }
        }
        return -1;
    }

    /**
     * 是否以 feature 开头
     * @param content 内容
     * @param feature  特征
     * @return boolean
     * @see java.lang.String#startsWith(String)
     */
    public static boolean startsWith(byte[] content, byte[] feature) {
        return startsWith(content,0,feature);
    }
    /**
     * 在第offset字节后，是否以 feature 开头
     * @param content 内容
     * @param feature  特征
     * @param  offset 偏移量
     * @return boolean
     * @see java.lang.String#startsWith(String, int)
     */
    public static boolean startsWith(byte[] content,int offset, byte[] feature) {
        int len = Math.min(content.length-offset, feature.length);
        for (int i = 0; i < len; i++) {
            if (content[i+offset] != feature[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 读取一个short 2 字节。高位数在后(正常CPU格式)
     * @param src byte[]
     * @param pos 从第几个字节开始读取。
     * @return
     */
    public static int readShortBigEndian(byte[] src, int pos) {
        return ((src[pos]&0xFF)<<8)|(src[pos+1]&0xFF);
    }

    /**
     * 读取一个int 4 字节。高位数在后(正常CPU格式)
     * @param src byte[]
     * @param pos 从第几个字节开始读取。
     * @return
     */
    public static int readIntBigEndian(byte[] src, int pos) {
        return ((src[pos]&0xFF)<<24)|((src[pos+1]&0xFF)<<16)|((src[pos+2]&0xFF)<<8)|(src[pos+3]&0xFF);
    }
    /**
     * 读取一个short 2 字节。低位数在后(摩托罗拉格式)
     * @param src byte[]
     * @param pos 从第几个字节开始读取。
     * @return
     */
    public static int readShortLittleEndian(byte[] src, int pos) {
        return (src[pos]&0xFF)|((src[pos+1]&0xFF)<<8);
    }
    /**
     * 读取一个int 4 字节。低位数在后(摩托罗拉格式)
     * @param src byte[]
     * @param pos 从第几个字节开始读取。
     * @return
     */
    public static int readIntLittleEndian(byte[] src, int pos) {
        return (src[pos]&0xFF)|((src[pos+1]&0xFF)<<8)|((src[pos+2]&0xFF)<<16)|((src[pos+3]&0xFF)<<24);
    }

    /**
     * 读取一个byte 1 字节。低位数在后(摩托罗拉格式)
     * @param src byte[]
     * @param pos 从第几个字节开始读取。
     * @return
     */
    public static int readByte(byte[] src, int pos) {
        return src[pos]&0xFF;
    }

    /**
     * 转成十六进制格式
     * @param src byte[]
     * @return String
     */
    public static String toHexString(byte[] src){
        if(src==null){
            return "";
        }
        StringBuilder sb=new StringBuilder(src.length<<1);
        for(byte b:src){
            sb.append(HEX_CHARS[(b&0xF0)>>4]);
            sb.append(HEX_CHARS[b&0xF]);
        }
        return sb.toString();
    }

    /**
     * 转成16进制格式。
     * @param src
     * @param begin
     * @param end
     * @return
     */
    public static String toHexString(byte[] src,int begin,int end){
        if(src==null|| src.length<begin||src.length<end){
            return "";
        }
        StringBuilder sb=new StringBuilder(src.length<<1);
        for(int i=begin;i<end;i++){
            byte b=src[i];
            sb.append(HEX_CHARS[(b&0xF0)>>4]);
            sb.append(HEX_CHARS[b&0xF]);
        }
        return sb.toString();
    }
    private static final char[] HEX_CHARS={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
}
