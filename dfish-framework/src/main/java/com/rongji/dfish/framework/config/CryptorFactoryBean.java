package com.rongji.dfish.framework.config;

import com.rongji.dfish.base.crypt.Cryptor;
import com.rongji.dfish.base.util.CryptUtil;
import org.springframework.beans.factory.FactoryBean;

public class CryptorFactoryBean implements FactoryBean<Cryptor> {

    public static final String ALGORITHM_DEFAULT = CryptUtil.ALGORITHM_BLOWFISH;
    public static final String ENCODING_DEFAULT = CryptUtil.ENCODING_UTF8;
    public static final int PRESENT_DEFAULT = CryptUtil.PRESENT_BASE32;
    public static final String SECRET_KEY_DEFAULT = "DFish@RJ002474";

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

    private String algorithm = ALGORITHM_DEFAULT;
    private String encoding = ENCODING_DEFAULT;
    private int present = PRESENT_DEFAULT;
    private String secretKey = SECRET_KEY_DEFAULT;

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
