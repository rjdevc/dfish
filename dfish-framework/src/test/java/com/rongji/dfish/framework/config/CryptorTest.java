package com.rongji.dfish.framework.config;

import com.rongji.dfish.base.crypt.Cryptor;
import com.rongji.dfish.base.util.CryptUtil;
import org.junit.Test;

public class CryptorTest {

    @Test
    public void encrypt() {
        String[] strArray = new String[]{"test", "RJtest"};

        String[] algorithmArray = new String[]{
                CryptUtil.ALGORITHM_BLOWFISH,
                CryptUtil.ALGORITHM_SHA1,
                CryptUtil.ALGORITHM_SHA256,
                CryptUtil.ALGORITHM_SHA512
        };
        for (String algorithm : algorithmArray) {
            Cryptor cryptor = CryptUtil.prepareCryptor(algorithm, "DFish@RJ002474").build();
            for (String str : strArray) {
                System.out.println("[" + algorithm + "]" + str + "->(" + cryptor.encrypt(str) + ")");
            }
        }
    }

    @Test
    public void decrypt() {
        Cryptor cryptor = CryptUtil.prepareCryptor(CryptUtil.ALGORITHM_BLOWFISH, "DFish@RJ002474").build();

        String enStr = "5F73FHNBVVQWDZPA8AACVZEYVW";
        System.out.println(enStr + "->" + cryptor.decrypt(enStr));
    }

}
