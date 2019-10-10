package com.rongji.dfish.framework.config;

import com.rongji.dfish.base.crypt.CryptFactory;
import com.rongji.dfish.base.crypt.CryptProvider;

public class CryptorTest {

	public static void main(String[] args) {
		String[] strArray = new String[]{ "test", "RJtest" };

		CryptProvider cryptProvider = new CryptProvider();
		String[] algorithmsArray = new String[]{
				CryptFactory.ALGORITHMS_BLOWFISH,
				CryptFactory.ALGORITHMS_SHA1,
				CryptFactory.ALGORITHMS_SHA256,
				CryptFactory.ALGORITHMS_SHA512
		};
		for (String algorithms : algorithmsArray) {
			cryptProvider.setAlgorithms(algorithms);
			for (String str : strArray) {
				System.out.println("[" + algorithms + "]" + str + "->(" + cryptProvider.getCryptor().encrypt(str) + ")");
			}
		}

	}
	
}
