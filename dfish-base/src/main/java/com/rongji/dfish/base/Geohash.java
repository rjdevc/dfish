package com.rongji.dfish.base;

import java.util.HashMap;

/**
 * Geohash 是地理哈希。用于做附近搜索有较好的效果。
 * 每层都把地图分为8*4个小块，这样一个个城市大约在5-8个字符，就可以把地图上各个坐标分化到不同的小块中，越是附近的快，
 * geohash越接近，搜索前几位相同的结果，即可实现附近搜索。但注意，wx4sv  周边8个是 
 * wx4th wx4tj wx4tn wx4su  wx4sy wx4ss  wx4st wx4sw 而不是完全按字母顺序。
 * 可以使用expand方法进行计算。
 * 参考https://en.wikipedia.org/wiki/Geohash
 * 原来使用从谷歌获取的Geohash，但是这个方法从hash转化回经纬度存在BUG。故重做了，并增加expand方法以供实际项目使用
 * @since DFish3.2.0
 */
public class Geohash {
	private static final String BASECODE="0123456789bcdefghjkmnpqrstuvwxyz";
	private static final int TOP = 0;
	private static final int RIGHT = 1;
	private static final int BOTTOM = 2;
	private static final int LEFT = 3;

	private static final int EVEN = 0;
	private static final int ODD = 1;

	private static String[][] NEIGHBORS;
	private static String[][] BORDERS;

	static {
	    NEIGHBORS = new String[2][4];
	    BORDERS = new String[2][4];

	    BORDERS[ODD][TOP] = "bcfguvyz";
	    BORDERS[ODD][RIGHT] = "prxz";
	    BORDERS[ODD][BOTTOM] = "0145hjnp";
	    BORDERS[ODD][LEFT] = "028b";

	    BORDERS[EVEN][TOP] = BORDERS[ODD][RIGHT];
	    BORDERS[EVEN][RIGHT] = BORDERS[ODD][TOP];
	    BORDERS[EVEN][BOTTOM] = BORDERS[ODD][LEFT];
	    BORDERS[EVEN][LEFT] = BORDERS[ODD][BOTTOM];

	    NEIGHBORS[ODD][TOP] = "238967debc01fg45kmstqrwxuvhjyznp";
	    NEIGHBORS[ODD][RIGHT] = "14365h7k9dcfesgujnmqp0r2twvyx8zb";
	    NEIGHBORS[ODD][BOTTOM] = "bc01fg45238967deuvhjyznpkmstqrwx";
	    NEIGHBORS[ODD][LEFT] = "p0r21436x8zb9dcf5h7kjnmqesgutwvy";

	    NEIGHBORS[EVEN][TOP] = NEIGHBORS[ODD][RIGHT];
	    NEIGHBORS[EVEN][RIGHT] = NEIGHBORS[ODD][TOP];
	    NEIGHBORS[EVEN][BOTTOM] = NEIGHBORS[ODD][LEFT];
	    NEIGHBORS[EVEN][LEFT] = NEIGHBORS[ODD][BOTTOM];
	}
	/**
	* 求与当前geohash相邻的8个格子的geohash值。
	 * 
	 * @param geohash geohash值
	 * @return string 数组，周围格子的geohash值
	 */
	public static String[] expand(String geohash) {

	    String left = calculate(geohash, LEFT);
	    String right = calculate(geohash, RIGHT);
	    String top = calculate(geohash, TOP);
	    String bottom = calculate(geohash, BOTTOM);

	    String topLeft = calculate(top, LEFT);
	    String topRight = calculate(top, RIGHT);
	    String bottomLeft = calculate(bottom, LEFT);
	    String bottomRight = calculate(bottom, RIGHT);

	    return new String[] {topLeft, top, topRight, left, right, bottomLeft, bottom, bottomRight };
	}
	public Geohash(int bytes){
		this.bytes=bytes;
	}
	public Geohash(){
		this(5);
	}
	 private int bytes ;  
	 private final static char[] digits = BASECODE.toCharArray();
	      
	    final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();  
	    static {  
	        int i = 0;  
	        for (char c : digits)  {
	            lookup.put(c, i);  
	            lookup.put((char)(c-32), i++);  //支持大写字母的识别
	        }
	    }  
	    /**
	     * 把hash字符串转为纬度，经度
	     * @param geohash String
	     * @return double[]
	     */
	    public static double[] decode(String geohash) {  
	        StringBuilder buffer = new StringBuilder();  
	        for (char c : geohash.toCharArray()) {  
	            int i = lookup.get(c) + 32;  
	            buffer.append( Integer.toString(i, 2).substring(1) );  
	        }  
	          
	        int bitleng=geohash.length()*5;
	        boolean[] lonset =new boolean[(bitleng+1)/2];
	        boolean[] latset =new boolean[bitleng/2];
	          
	        //even bits  
	        int j =0;  
	        for (int i=0; i<bitleng ;i+=2) {  
	            lonset[j++]=buffer.charAt(i) == '1';  
	        }  
	          
	        //odd bits  
	        j=0;  
	        for (int i=1; i<bitleng;i+=2) {  
		            latset[j++]=buffer.charAt(i) == '1';  
	        }  
	          
	        double lon = decode(lonset, -180, 180);  
	        double lat = decode(latset, -90, 90);  
	          
	        return new double[] {lat, lon};       
	    }  
	      
	    private static double decode(boolean[] bs, double floor, double ceiling) {  
	        double mid = 0;  
	        for (int i=0; i<bs.length; i++) {  
	            mid = (floor + ceiling) / 2;  
	            if (bs[i]) {
                    floor = mid;
                } else {
                    ceiling = mid;
                }
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
	    	return Geohash.encode(lat, lon, bytes);
	    }  
	    public static  String encode(double lat, double lon,int bytes) {  
	    	int bitlen=bytes*5;
	    	boolean[] lonbits = getBits(lon, -180, 180,(bitlen+1)/2);  
	        boolean[] latbits = getBits(lat, -90, 90, bitlen/2);  
	        boolean[] buffer = new boolean[bitlen];  
	        for (int i = 0; i < bitlen; i++) {  
	        	if(i%2==0){
	        		buffer[i]=lonbits[i/2];  
	        	}else{
	        		buffer[i]=latbits[i/2];  
	        	}
	        }  
	        return base32(buffer);
//	        return base32(Long.parseLong(buffer.toString(), 2));  
	    }  
	    public static String base32(boolean[] buffer) {  
	    	StringBuilder sb=new StringBuilder();
	    	for(int i=0;i<buffer.length;i+=5){
	    		sb.append(toBase32char(buffer,i));
	    	}
	    	return sb.toString();
	    }  
	    
	    private static char toBase32char(boolean[] buffer, int i) {
			int b=(buffer[i]?16:0)|
					(buffer[i+1]?8:0)|
					(buffer[i+2]?4:0)|
					(buffer[i+3]?2:0)|
					(buffer[i+4]?1:0);
			return digits[b];
		}
	  
	    private static  boolean[] getBits(double lat, double floor, double ceiling ,int bitlen) {  
	    	 boolean[] buffer = new  boolean[bitlen];  
	        for (int i = 0; i < bitlen; i++) {  
	            double mid = (floor + ceiling) / 2;  
	            if (lat >= mid) {  
	                buffer[i]=true;  
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
	        if (!negative) {
                i = -i;
            }
	        while (i <= -32) {  
	            buf[charPos--] = digits[(int) (-(i % 32))];  
	            i /= 32;  
	        }  
	        buf[charPos] = digits[(int) (-i)];  
	  
	        if (negative) {
                buf[--charPos] = '-';
            }
	        return new String(buf, charPos, (65 - charPos));  
	    }  
	
	/**
	 * 递归计算当前区域特定方向的geohash值
	 * 
	 * @param geohash
	 * @param direction 偏移方向
	 * @return 周围区域的geohash值，超出边界则返回空字符串""
	 */
	private static String calculate(String geohash, int direction) {
	    if ("".equals(geohash))      //如果递归到第一个字符仍然处于边界，则不存在这一方向的相邻格子
        {
            return "";
        }
	    int length = geohash.length();
	    char lastChar = geohash.charAt(length - 1);
	    int charType = (geohash.length() % 2) == 1 ? ODD : EVEN;  //最后一位是奇数还是偶数
	    String base = geohash.substring(0, length - 1);
	    if (BORDERS[charType][direction].indexOf(lastChar) != -1) { //判断对后一位是否处在边界
	        base = calculate(base, direction);
	    }
        return base + NEIGHBORS[charType][direction].charAt(BASECODE.indexOf(lastChar));
	}
}
