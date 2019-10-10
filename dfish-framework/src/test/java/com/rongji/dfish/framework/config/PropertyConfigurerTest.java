package com.rongji.dfish.framework.config;

import org.junit.Test;

public class PropertyConfigurerTest {

	@Test
	public void test() {
		System.out.println(new DefaultPropertyCryptor().encrypt("dfish"));
	}
	
}
