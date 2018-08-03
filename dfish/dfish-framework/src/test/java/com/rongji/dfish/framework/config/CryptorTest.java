package com.rongji.dfish.framework.config;

public class CryptorTest {

	public static void main(String[] args) {
		String[] strArray = new String[]{ "zhdjyf", "RJ123zhdjyf", "zhdj", "RJ123zhdj" };
		
		DefaultPropertyCryptor cryptor = new DefaultPropertyCryptor();
		for (String str : strArray) {
			System.out.println(str + ":[" + cryptor.encrypt(str) + "]");
		}
	}
	
}
