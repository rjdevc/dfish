package com.rongji.dfish.base.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.Test;

public class CryptUtilTest {
	
	public void testJCECryptTool() throws Exception {
		
		CryptUtil.JCECryptTool tool=new CryptUtil.JCECryptTool(CryptUtil.BLOWFISH,"我就是密码1".getBytes("UTF-8"));
		FileInputStream fis1=new FileInputStream("D:/4_行政/培训/2018/加解密基础知识普及.pptx");
		FileOutputStream fos1=new FileOutputStream("D:/4_行政/培训/2018/加解密基础知识普及.myfile");
		tool.setIn(fis1);
		tool.setOut(fos1);
		tool.encrypt();
		fis1.close();
		fos1.close();
		
		
		FileInputStream fis2=new FileInputStream("D:/4_行政/培训/2018/加解密基础知识普及.myfile");
		FileOutputStream fos2=new FileOutputStream("D:/4_行政/培训/2018/加解密基础知识普及_解密版.pptx");
		tool.setIn(fis2);
		tool.setOut(fos2);
		tool.decrypt();
		fis2.close();
		fos2.close();
	}
	@Test
	public void testGzipAndCryptTool() throws Exception {
		
		CryptUtil.GzipAndCryptTool tool=new CryptUtil.GzipAndCryptTool(CryptUtil.BLOWFISH,"我就是密码1".getBytes("UTF-8"));
		FileInputStream fis1=new FileInputStream("D:/cvs/rjitask/rjitask/itask7beta/WebRoot/WEB-INF/applicationContext.xml");
		FileOutputStream fos1=new FileOutputStream("D:/cvs/rjitask/rjitask/itask7beta/WebRoot/WEB-INF/applicationContext.myfile");
		tool.setIn(fis1);
		tool.setOut(fos1);
		tool.encrypt();
		fis1.close();
		fos1.close();
		
		
		FileInputStream fis2=new FileInputStream("D:/cvs/rjitask/rjitask/itask7beta/WebRoot/WEB-INF/applicationContext.myfile");
		FileOutputStream fos2=new FileOutputStream("D:/cvs/rjitask/rjitask/itask7beta/WebRoot/WEB-INF/applicationContext(解密).xml");
		tool.setIn(fis2);
		tool.setOut(fos2);
		tool.decrypt();
		fis2.close();
		fos2.close();
	}
}
