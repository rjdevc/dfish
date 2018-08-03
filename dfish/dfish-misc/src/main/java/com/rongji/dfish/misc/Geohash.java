package com.rongji.dfish.misc;

import java.util.BitSet;
import java.util.HashMap;
/**
 * Geohash 是地理哈希。通过把经纬度分割哈希的做法，用一个较为简短的标记来表示一块区域。
 * 这样，相同或相近的区域拥有接近的值，以便进行快速的附近搜索。
 * 把地图划分为世界地图32*32块，每块用两个字符表示。然后这每一小块再划分成32*32块，再用两个字符表示。
 * 从左往右(东往西)，从上到下(北到南)分别为 0,1,2...9,b,c...z 因为我们只需要32个字符表达，所有没有A I L O
 * 比如说某块地域 GeoHash值为 wx4sv61q 表示在地球上被标识为WX的那块区域，里面划分为 4s的那个区域， 里面的v6 区域里面的 1q区域。
 * 大约是在(40.222012, 116.248283)或附近。
 * 一般来说到10位数字，到第5级基本上精确度就已经很高了。40000km/32/32/32/32/32 大约是1米多见方(南北只有半米)。
 * 同城搜索通常只需要前面6位或4位一样。
 * 参考https://en.wikipedia.org/wiki/Geohash
 * @author GOOGLE
 *
 */
public class Geohash {  
  
    private static int numbits = 5 * 5;  
    final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',  
            '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',  
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };  
      
    final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();  
    static {  
        int i = 0;  
        for (char c : digits)  
            lookup.put(c, i++);  
    }  
    /**
     * 把hash字符串转为纬度，经度
     * @param geohash String
     * @return double[]
     */
    public double[] decode(String geohash) {  
        StringBuilder buffer = new StringBuilder();  
        for (char c : geohash.toCharArray()) {  
  
            int i = lookup.get(c) + 32;  
            buffer.append( Integer.toString(i, 2).substring(1) );  
        }  
          
        BitSet lonset = new BitSet();  
        BitSet latset = new BitSet();  
          
        //even bits  
        int j =0;  
        for (int i=0; i< numbits*2;i+=2) {  
            boolean isSet = false;  
            if ( i < buffer.length() )  
              isSet = buffer.charAt(i) == '1';  
            lonset.set(j++, isSet);  
        }  
          
        //odd bits  
        j=0;  
        for (int i=1; i< numbits*2;i+=2) {  
            boolean isSet = false;  
            if ( i < buffer.length() )  
              isSet = buffer.charAt(i) == '1';  
            latset.set(j++, isSet);  
        }  
          
        double lon = decode(lonset, -180, 180);  
        double lat = decode(latset, -90, 90);  
          
        return new double[] {lat, lon};       
    }  
      
    private double decode(BitSet bs, double floor, double ceiling) {  
        double mid = 0;  
        for (int i=0; i<bs.length(); i++) {  
            mid = (floor + ceiling) / 2;  
            if (bs.get(i))  
                floor = mid;  
            else  
                ceiling = mid;  
        }  
        return mid;  
    }  
      
    /**
     * 把纬度 经度转为哈希值
     * @param lat 纬度
     * @param lon 经度
     * @return String
     */
    public String encode(double lat, double lon) {  
        BitSet latbits = getBits(lat, -90, 90);  
        BitSet lonbits = getBits(lon, -180, 180);  
        StringBuilder buffer = new StringBuilder();  
        for (int i = 0; i < numbits; i++) {  
            buffer.append( (lonbits.get(i))?'1':'0');  
            buffer.append( (latbits.get(i))?'1':'0');  
        }  
        return base32(Long.parseLong(buffer.toString(), 2));  
    }  
  
    private BitSet getBits(double lat, double floor, double ceiling) {  
        BitSet buffer = new BitSet(numbits);  
        for (int i = 0; i < numbits; i++) {  
            double mid = (floor + ceiling) / 2;  
            if (lat >= mid) {  
                buffer.set(i);  
                floor = mid;  
            } else {  
                ceiling = mid;  
            }  
        }  
        return buffer;  
    }  
  
    public static String base32(long i) {  
        char[] buf = new char[65];  
        int charPos = 64;  
        boolean negative = (i < 0);  
        if (!negative)  
            i = -i;  
        while (i <= -32) {  
            buf[charPos--] = digits[(int) (-(i % 32))];  
            i /= 32;  
        }  
        buf[charPos] = digits[(int) (-i)];  
  
        if (negative)  
            buf[--charPos] = '-';  
        return new String(buf, charPos, (65 - charPos));  
    }  
  
}