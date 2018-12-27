package com.rongji.dfish.base.crypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.Test;

import com.rongji.dfish.base.crypt.sm.SM2Cryptor;
import com.rongji.dfish.base.crypt.sm.SM2KeyPair;

public class SM2Test {
	@Test
	public void test() throws Exception{
		FileInputStream fis = new FileInputStream(new File("C:\\Users\\chenchao\\Desktop\\111\\111.zip"));
		ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);

		byte[] fileData = null;
		byte[] b = new byte[2048];
		int n;
		while ((n = fis.read(b)) != -1) {
			bos.write(b, 0, n);
		}
		fis.close();
		bos.close();
		fileData = bos.toByteArray();
//		SM2Cryptor sm2 = new SM2Cryptor();
//		ByteArrayInputStream bins = new ByteArrayInputStream(fileData);
//		ByteArrayOutputStream bous = new ByteArrayOutputStream();
//		byte buffer[] = new byte[8192];  
//		SM2KeyPair keyPair = sm2.generateKeyPair();
//		int l = -1;
//		while (   (l= bins.read(buffer)) != -1) {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			baos.write(buffer,0,l);
//			byte []temp = sm2.encrypt(baos.toByteArray(),keyPair.getPublicKey());
//			bous.write(temp);
//		}
//		bins.close();
//		bous.close();
//		byte[] data = bous.toByteArray();
////		System.out.println(fileData.length);
//		byte[] data1 = sm2.decrypt(data, keyPair.getPrivateKey());
//		System.out.println(data1.length);
//		FileOutputStream fos = new FileOutputStream("C:\\Users\\chenchao\\Desktop\\111\\deresult.zip");
//		fos.write(data1);
//		fos.close();
		if(fileData!=null){
//			System.out.println("fileData is:"+Arrays.toString(fileData));
			SM2Cryptor sm2 = new SM2Cryptor(CryptFactory.UTF8,StringCryptor.HEX_STRING,null);
			SM2KeyPair keyPair = sm2.generateKeyPair();
			byte[] data = sm2.encrypt(fileData,keyPair.getPublicKey());
			byte[] data1 = sm2.decrypt(data, keyPair.getPrivateKey());
			
			FileOutputStream fos = new FileOutputStream("C:\\Users\\chenchao\\Desktop\\111\\deresult.zip");
			fos.write(data1);
			fos.close();
			
			
//			System.out.println("data1 is:"+Arrays.toString(data1));
		}  
		
	}
	

}
