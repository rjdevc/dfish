package com.rongji.dfish.base.crypt;

import org.junit.Test;

public class RSACryptor4BCTest {
	
	@Test
	public void test() throws Exception{
		RSACryptor4BC r = new RSACryptor4BC("UTF-8", 1, null);
		
		byte []data = r.encrypt("231241241245151".getBytes());
		System.out.println(new String(r.decrypt(data)));
		
		
	}
	

}
