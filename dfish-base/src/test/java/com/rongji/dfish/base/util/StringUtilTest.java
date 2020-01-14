package com.rongji.dfish.base.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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

	@Test
	public void testXss() throws UnsupportedEncodingException{
		String src="<script>alert();</script>";
		String s=StringUtil.replace(src,StringUtil.XSS_REMOVE);
		System.out.println(s);
		s=StringUtil.replace(src,StringUtil.XSS_TO_BLANK);
		System.out.println(s);
		s=StringUtil.replace(src,StringUtil.XSS_TO_SBC);
		System.out.println(s);
		Map<String,String> map=new HashMap(StringUtil.XSS_REMOVE);
		map.put("<script>","");
		map.put("</script>","");
		map.put("<SCRIPT>","");
		map.put("</SCRIPT>","");
		s=StringUtil.replace(src,map);
		System.out.println(s);
	}
	@Test
	public void testRead(){
		ByteArrayOutputStream baos=new ByteArrayOutputStream();

	}
}
