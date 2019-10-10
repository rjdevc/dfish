package com.rongji.dfish.base.util;

import com.rongji.dfish.base.util.CharUtil.CharRepacer;

public class CharUtilTest {
	public static void main(String[] args) {
		System.out.println(CharUtil.contains("B&Q", CharUtil.CHAR_TYPE_XSS));
		System.out.println(CharUtil.contains("我是好人", CharUtil.CHAR_TYPE_ASCII | CharUtil.CHAR_TYPE_SBC));
		System.out.println(CharUtil.contains("我是好人", CharUtil.CHAR_TYPE_CHINESE));
		System.out.println(CharUtil.contains("Ｗｏｎｄｅｒｆｕｌ", CharUtil.CHAR_TYPE_CHINESE));
		System.out.println(CharUtil.repace("Ｗｏｎｄｅｒｆｕｌ", CharUtil.CHAR_TYPE_SBC,new CharRepacer() {
			public String replaceTo(char c) {
				return new String(new char[]{CharUtil.sbc2dbc(c)});
			}
		}));
//		for(char c:"《》【】“”’‘——……".toCharArray()){
//		for(char c=0x3000;c<0x3080;c++){
			for(char c=CharUtil.SBC_CHAR_START;c<=CharUtil.SBC_CHAR_END;c++){
			
			System.out.println(c+"="+Integer.toString(c, 16));
		}
	}
}
