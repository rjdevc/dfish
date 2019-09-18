package com.rongji.dfish.base.crypt;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

public class StringCryptorTest {
	@Test 
	public void compress10() throws UnsupportedEncodingException{
		String src="粉红墙上画凤凰，凤凰画在粉红墙。"
				+ "红凤凰、粉凤凰，红粉凤凰、花凤凰。"
				+ "红凤凰,黄凤凰,红粉凤凰,粉红凤凰,花粉花凤凰。";
		src=src+src+src+src+src+src+src+src+src+src;
		StringCryptor sc=CryptFactory.getStringCryptor(CryptFactory.ALGORITHMS_NONE, CryptFactory.ENCODING_UTF8_GZIP, CryptFactory.PRESENT_STYLE_BASE64);
		String en=sc.encrypt(src);
		String de=sc.decrypt(en);
		Assert.assertTrue(src.equals(de));
		System.out.println("compress10 原长度="+src.getBytes("UTF-8").length+" 压缩后长度="+en.length());
	}
	@Test
	public void compress() throws UnsupportedEncodingException{
		String src="粉红墙上画凤凰，凤凰画在粉红墙。"
				+ "红凤凰、粉凤凰，红粉凤凰、花凤凰。"
				+ "红凤凰,黄凤凰,红粉凤凰,粉红凤凰,花粉花凤凰。";
		StringCryptor sc=CryptFactory.getStringCryptor(CryptFactory.ALGORITHMS_NONE, CryptFactory.ENCODING_UTF8_GZIP, CryptFactory.PRESENT_STYLE_BASE64);
		String en=sc.encrypt(src);
		String de=sc.decrypt(en);
		Assert.assertTrue(src.equals(de));
		System.out.println("compress2原长度="+src.getBytes("UTF-8").length+" 压缩后长度="+en.length());
	}
}
