package com.rongji.dfish.framework.config;

import com.rongji.dfish.base.crypt.CryptFactory;
import com.rongji.dfish.base.crypt.CryptProvider;
import org.junit.Test;

public class CryptorTest {

    @Test
    public void encrypt() {
        String[] strArray = new String[]{"test", "RJtest"};

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

    @Test
    public void decrypt() {
        CryptProvider cryptProvider = new CryptProvider();

        String enStr = "5F73FHNBVVQWDZPA8AACVZEYVW";
        System.out.println(enStr + "->" + cryptProvider.getCryptor().decrypt(enStr));
    }

}
