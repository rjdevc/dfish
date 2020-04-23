package com.rongji.dfish.framework.config;

import com.rongji.dfish.base.crypto.Cryptor;
import com.rongji.dfish.base.util.CryptoUtil;
import org.springframework.beans.factory.FactoryBean;

/**
 * 加密工具工厂类,实现Spring的FactoryBean接口
 * 通过不同配置快速得到不同的实例
 * @since DFish5.0
 * @author LinLW
 */
public class CryptorFactoryBean implements FactoryBean<Cryptor> {

    /**
     * 默认算法
     */
    public static final String ALGORITHM_DEFAULT = CryptoUtil.ALGORITHM_BLOWFISH;
    /**
     * 默认编码
     */
    public static final String ENCODING_DEFAULT = CryptoUtil.ENCODING_UTF8;
    /**
     * 默认呈现方式
     */
    public static final int PRESENT_DEFAULT = CryptoUtil.PRESENT_BASE32;
    /**
     * 默认秘钥(部分算法不需要秘钥)
     */
    public static final String SECRET_KEY_DEFAULT = "DFish@RJ002474";

    @Override
    public Cryptor getObject() throws Exception {
        return CryptoUtil.prepareCryptor(algorithm,secretKey.getBytes("UTF-8"))
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

    /**
     * 加密算法
     * @return String
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * 加密算法
     * @param algorithm String
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * 加密编码
     * @return String
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * 加密编码
     * @param encoding String
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 密文呈现方式
     * @return int
     */
    public int getPresent() {
        return present;
    }

    /**
     * 密文呈现方式
     * @param present int
     */
    public void setPresent(int present) {
        this.present = present;
    }

    /**
     * 秘钥
     * @return String
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * 秘钥
     * @param secretKey String
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
