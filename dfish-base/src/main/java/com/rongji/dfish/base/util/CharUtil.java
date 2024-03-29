package com.rongji.dfish.base.util;

import java.util.Arrays;

/**
 * 字符的常用操作
 * @author DFish-team
 *
 */
public class CharUtil {
	/**
	 * ASCII表中可见字符从!开始，偏移位值为33(Decimal)
	 */
	static final char DBC_CHAR_START = '!'; // 半角!

	/**
	 * ASCII表中可见字符到~结束，偏移位值为126(Decimal)
	 */
	static final char DBC_CHAR_END = '~'; // 半角~

	/**
	 * 全角对应于ASCII表的可见字符从！开始，偏移值为65281
	 */
	static final char SBC_CHAR_START = 65281; // 全角！

	/**
	 * 全角对应于ASCII表的可见字符到～结束，偏移值为65374
	 */
	static final char SBC_CHAR_END = 65374; // 全角～

	/**
	 * ASCII表中除空格外的可见字符与对应的全角字符的相对偏移
	 */
	static final int CONVERT_STEP = SBC_CHAR_START-DBC_CHAR_START; // 全角半角转换间隔

	/**
	 * 全角空格的值，它没有遵从与ASCII的相对偏移，必须单独处理
	 */
	static final char SBC_SPACE = 12288; // 全角空格 12288

	/**
	 * 半角空格的值，在ASCII中为32(Decimal)
	 */
	static final char DBC_SPACE = ' '; // 半角空格
	/**
	 * 半角转换全角
	 * 
	 * @param src
	 * @return
	 */
	public static char dbc2sbc(char src) {
		char r = src;
		if (src == DBC_SPACE) { // 如果是半角空格，直接用全角空格替代
			src = SBC_SPACE;
		} else if ((src >= DBC_CHAR_START) && (src <= DBC_CHAR_END)) { // 字符是!到~之间的可见字符
			r = (char) (src + CONVERT_STEP);
		}
		return r;
	}
	
	/**
	 * 全角转换半角
	 * 
	 * @param src
	 * @return
	 */
	public static char sbc2dbc(char src) {
		char r = src;
		if (src >= SBC_CHAR_START && src <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内
			r = (char) (src - CONVERT_STEP);
		} else if (src == SBC_SPACE) { // 如果是全角空格
			r = DBC_SPACE;
		}
		return r;
	}


	/**
	 * 类型 -  仅数字
	 */
	public final static int CHAR_TYPE_NUMBER=1<<0;
	/**
	 * 类型 -  仅大写字母
	 */
	public final static int CHAR_TYPE_UPPER_CASE=1<<1;
	/**
	 * 类型 -  仅小写字母
	 */
	public final static int CHAR_TYPE_LOWER_CASE=1<<2;
	/**
	 * 类型 -  仅字母表大小写字符
	 */
	public final static int CHAR_TYPE_ALPHABET=
			CHAR_TYPE_LOWER_CASE |CHAR_TYPE_UPPER_CASE;
	/**
	 * 仅XSS符号
	 */
	public final static int CHAR_TYPE_XSS=1<<4;
	/**
	 * 所有ASCII 的可见符号 (不包含 内码为0-31的控制符号 如换行)
	 */
	public final static int CHAR_TYPE_ASCII_SYMBOL=1<<3|1<<4|1<<5|1<<6;
	/**
	 * 所有ASCII 可见字符  (不包含 内码为0-31的控制符号 如换行)
	 */
	public final static int CHAR_TYPE_ASCII=
			CHAR_TYPE_ASCII_SYMBOL | CHAR_TYPE_NUMBER | CHAR_TYPE_ALPHABET;
	/**
	 * 全角符号
	 */
	public final static int CHAR_TYPE_SBC=1<<7;
	/**
	 * 中文符号(同全角符号)
	 */
	public final static int CHAR_TYPE_CHINESE_SYMBOL=1<<7;
	/**
	 * 汉字
	 */
	public final static int CHAR_TYPE_CHINESE=1<<15;
	/*
	 * 第0段 数字
	 * 第1段 大写字母
	 * 第2段 小写字母
	 * 第3段 为正常的符号
	 * 第4段 为可能XSS的符号
	 * 第5段 预留
	 * 第6段 预留
	 * 第7段 全角
	 * 
	 */
	private static char[][] ALL_CHARS;
	static{
		char[] CHARS_NUMBER=buildCharArray("0123456789"); 
		char[] CHARS_UPPER_CASE=buildCharArray("ABCDEFGHIJKLMNOPQRSTUVWXYZ"); 
		char[] CHARS_LOWER_CASE=buildCharArray("abcdefghijklmnopqrstuvwxyz"); 
		char[] CHARS_COMMON_SYMBOL=buildCharArray(" !#$()*+,-.:;=?@[]^_`{}~"); 
		char[] CHARS_XSS=buildCharArray("&<>\"'/\\|%"); 
		char[] CHARS_SBC=new char[SBC_CHAR_END-SBC_CHAR_START+2];
		for(int i=0;i<=SBC_CHAR_END-SBC_CHAR_START;i++){
			CHARS_SBC[i]=(char)(SBC_CHAR_START+i);
		}
		CHARS_SBC[SBC_CHAR_END-SBC_CHAR_START+1]=SBC_SPACE;
		Arrays.sort(CHARS_SBC);
		char[] EMPTY=new char[0];
		ALL_CHARS=new char[][]{
			CHARS_NUMBER,CHARS_UPPER_CASE,CHARS_LOWER_CASE,
			CHARS_COMMON_SYMBOL, CHARS_XSS,
			EMPTY,EMPTY,
			CHARS_SBC
		};
	}
	
	private static char[] buildCharArray(String str) {
		char[] arr=str.toCharArray();
		Arrays.sort(arr);
		return arr;
	}

	/**
	 * 是否包含
	 * @param str  字符串
	 * @param type 类型
	 * @return 判断结果
	 */
	public static boolean contains(String str,int type){
		char[] cs=str.toCharArray();
		for(char c:cs){
			if(is(c,type)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 将字符串的某些内容替换成其他值。
	 * @param str 字符串
	 * @param type 类型
	 * @param cf 指定哪些内容需要替换
	 * @return
	 */
	public static String repace(String str,int type,CharReplacer cf){
		if(str==null){
			return null;
		}
		StringBuilder sb=new StringBuilder();
		char[] cs=str.toCharArray();
		for(char c:cs){
			if(is(c,type)){
				sb.append(cf.replaceTo(c));
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 替换器
	 */
	public interface CharReplacer {
		/**
		 * 如果这个字符属于指定类型，需要替换成其他字符
		 * @param c 字符
		 * @return String
		 */
		String replaceTo(char c);
	}

	/**
	 * 判断char是否是这个类型
	 * @param c char
	 * @param type 类型
	 * @return boolean
	 */
	public static boolean is(char c,int type){
		for(int typeIndex=0;typeIndex<ALL_CHARS.length;typeIndex++){
			char[] toCheck=ALL_CHARS[typeIndex];
			if((type>>typeIndex & 1)>0){
					if(Arrays.binarySearch(toCheck,c)>=0){
						return true; 
					}
			}
		}
//		if((type&CHAR_TYPE_CHINESE )>0){
//			if(c >=0x3000&& c<0xA000){
//				return true;
//			}
//		}
//		return false;
		return (type&CHAR_TYPE_CHINESE )>0&&c >=0x3000&& c<0xA000;
	}

}
