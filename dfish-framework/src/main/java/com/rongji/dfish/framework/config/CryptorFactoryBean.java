package com.rongji.dfish.framework.config;

import com.rongji.dfish.base.crypt.Cryptor;
import com.rongji.dfish.base.util.CryptUtil;
import org.springframework.beans.factory.FactoryBean;

public class CryptorFactoryBean implements FactoryBean<Cryptor> {
    @Override
    public Cryptor getObject() throws Exception {
        return CryptUtil.prepareCryptor(algorithm,secretKey.getBytes("UTF-8"))
                .encoding(encoding).present(present)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return Cryptor.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private String algorithm = CryptUtil.ALGORITHM_BLOWFISH;
    private String encoding = CryptUtil.ENCODING_UTF8;
    private int present = CryptUtil.PRESENT_BASE32;
    private String secretKey = "DFish@RJ002474";


    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
