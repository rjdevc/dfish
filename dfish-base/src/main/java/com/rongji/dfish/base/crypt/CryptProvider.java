package com.rongji.dfish.base.crypt;

import java.util.HashMap;
import java.util.Map;

public class CryptProvider {

    private String algorithms = CryptFactory.ALGORITHMS_BLOWFISH;
    private String encoding = CryptFactory.ENCODING_UTF8;
    private int presentStyle = CryptFactory.PRESENT_STYLE_BASE32;
    private String secretKey = "DFish@RJ002474";

    private static final Map<String, StringCryptor> CRYPTORS = new HashMap<>();

    public String getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(String algorithms) {
        this.algorithms = algorithms;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getPresentStyle() {
        return presentStyle;
    }

    public void setPresentStyle(int presentStyle) {
        this.presentStyle = presentStyle;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public StringCryptor getCryptor() {
        String key = algorithms + "#" + encoding + "#" + presentStyle + "#" + secretKey;
        StringCryptor cryptor = CRYPTORS.get(key);
        if (cryptor == null) {
            cryptor = CryptFactory.getStringCryptor(algorithms, encoding, presentStyle, secretKey);
            CRYPTORS.put(key, cryptor);
        }
        return cryptor;
    }

}
