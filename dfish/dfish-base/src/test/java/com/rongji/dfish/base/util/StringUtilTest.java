package com.rongji.dfish.base.util;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {
	@Test
	public void testDetCharChinese() throws UnsupportedEncodingException{
			String s="ddd其实吧我也就是随便测试一下";
			String en=StringUtil.detCharset(s.getBytes("UTF-8"));
			Assert.assertTrue(StringUtil.ENCODING_UTF8.equals(en));
			en=StringUtil.detCharset(s.getBytes("GBK"));
			Assert.assertTrue(StringUtil.ENCODING_GBK.equals(en));
	}
	@Test
	public void testDetCharChineseWithPartByteArray() throws UnsupportedEncodingException{
			String s="ddd其实吧我也就是随便测试一下";
			byte[] ba=s.getBytes("UTF-8");
			byte[] bb=new byte[ba.length-1];
			System.arraycopy(ba, 0, bb, 0, bb.length);
			String en=StringUtil.detCharset(bb);
			Assert.assertTrue(StringUtil.ENCODING_UTF8.equals(en));
			
			ba=s.getBytes("GBK");
			bb=new byte[ba.length-1];
			System.arraycopy(ba, 0, bb, 0, bb.length);
			en=StringUtil.detCharset(bb);
			Assert.assertTrue(StringUtil.ENCODING_GBK.equals(en));
	}
	@Test
	public void testDetCharAscii() throws UnsupportedEncodingException{
		String s2="what the fuck";
		String en=StringUtil.detCharset(s2.getBytes("UTF-8"));
		Assert.assertTrue(StringUtil.ENCODING_UTF8.equals(en));
		en=StringUtil.detCharset(s2.getBytes("GBK"));
		Assert.assertTrue(StringUtil.ENCODING_UTF8.equals(en));
		
	}
	@Test
	public void testDetCharBin() throws UnsupportedEncodingException{
		byte[] b3=new byte[]{-1};
		try{
			System.out.println(StringUtil.detCharset(b3));
			Assert.assertTrue(false);//不应该执行到这里，应该抛异常
		}catch(UnsupportedEncodingException ex){}
	}
}
