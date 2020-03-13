package com.rongji.dfish.framework.config;

import com.rongji.dfish.base.crypto.Cryptor;
import com.rongji.dfish.base.util.CryptoUtil;
import org.junit.Test;

public class CryptorTest {

    @Test
    public void encrypt() {
        String[] strArray = new String[]{"test", "RJtest"};

        String[] algorithmArray = new String[]{
                CryptoUtil.ALGORITHM_BLOWFISH,
                CryptoUtil.ALGORITHM_SHA1,
                CryptoUtil.ALGORITHM_SHA256,
                CryptoUtil.ALGORITHM_SHA512
        };
        for (String algorithm : algorithmArray) {
            Cryptor cryptor = CryptoUtil.prepareCryptor(algorithm, "DFish@RJ002474").build();
            for (String str : strArray) {
                System.out.println("[" + algorithm + "]" + str + "->(" + cryptor.encrypt(str) + ")");
            }
        }
    }

    @Test
    public void decrypt() {
        Cryptor cryptor = CryptoUtil.prepareCryptor(CryptoUtil.ALGORITHM_BLOWFISH, "DFish@v5").build();

        String str = "DFishV587!";
        System.out.println(str + "->" + cryptor.encrypt(str));

        String enStr = "2ARS5BN2ZTEHTX7C0X5TXHAAB0";
        System.out.println(enStr + "->" + cryptor.decrypt(enStr));
    }

}
