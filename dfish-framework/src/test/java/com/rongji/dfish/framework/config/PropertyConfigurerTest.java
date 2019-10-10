package com.rongji.dfish.framework.config;

import com.rongji.dfish.framework.config.impl.DefaultPropertyCryptor;
import org.junit.Test;

public class PropertyConfigurerTest {

	@Test
	public void test() {
		System.out.println(new DefaultPropertyCryptor().encrypt("dfish"));
	}
	
}
