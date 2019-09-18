package com.rongji.dfish.base.crypt;

public class CryptProvider {

    private String algorithms = CryptFactory.ALGORITHMS_BLOWFISH;
    private String encoding = CryptFactory.ENCODING_UTF8;
    private int presentStyle = CryptFactory.PRESENT_STYLE_BASE32;
    private String secretKey = "DFish";

    private StringCryptor cryptor;

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
        if (cryptor == null) {
            cryptor = CryptFactory.getStringCryptor(algorithms, encoding, presentStyle, secretKey);
        }
        return cryptor;
    }

}
