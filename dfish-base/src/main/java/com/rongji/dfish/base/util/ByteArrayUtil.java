package com.rongji.dfish.base.util;

public class ByteArrayUtil {
    public static int indexOf( byte[] src, byte[] target) {
        return indexOf(src,0,src.length,target);
    }

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
    public static boolean startsWith(byte[] sample, byte[] feature) {
        return startsWith(sample,0,feature);
    }
    public static boolean startsWith(byte[] sample,int offset, byte[] feature) {
        int len = Math.min(sample.length-offset, feature.length);
        for (int i = 0; i < len; i++) {
            if (sample[i+offset] != feature[i]) {
                return false;
            }
        }
        return true;
    }

    public static int readShortBigEndian(byte[] src, int pos) {
        return ((src[pos]&0xFF)<<8)|(src[pos+1]&0xFF);
    }
    public static int readIntBigEndian(byte[] src, int pos) {
        return ((src[pos]&0xFF)<<24)|((src[pos+1]&0xFF)<<16)|((src[pos+2]&0xFF)<<8)|(src[pos+3]&0xFF);
    }
    public static int readShortLittleEndian(byte[] src, int pos) {
        return (src[pos]&0xFF)|((src[pos+1]&0xFF)<<8);
    }

    public static int readIntLittleEndian(byte[] src, int pos) {
        return (src[pos]&0xFF)|((src[pos+1]&0xFF)<<8)|((src[pos+2]&0xFF)<<16)|((src[pos+3]&0xFF)<<24);
    }
    public static int readByte(byte[] src, int pos) {
        return src[pos]&0xFF;
    }

    public static String toHexString(byte[] src){
        if(src==null){
            return "null";
        }
        StringBuilder sb=new StringBuilder(src.length<<1);
        sb.append('[');
        for(byte b:src){
            sb.append(HEX_CHARS[(b&0xF0)>>4]);
            sb.append(HEX_CHARS[b&0xF]);
            sb.append(' ');
        }
        sb.append(']');
        return sb.toString();
    }
    private static final char[] HEX_CHARS={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
}
