package com.rongji.dfish.base.crypto;

public class CryptorBuilder {
    public static final int PRESENT_RAW = 0;
    public static final int PRESENT_HEX = 1;
    public static final int PRESENT_BASE64 = 2;
    public static final int PRESENT_BASE32 = 3;
    /**
     * 注意和 RTF4648 的并不完全一致，该表示方式没有最后的PAD(=)
     * 加密的话和旧版本有所不同，后面不再带点号(.) 所以新版本加密的在旧版本中将无法解密
     * 但旧版本加密的新版本中可以解密。
     */
    public static final int PRESENT_BASE64_URLSAFE = 4;

    /**
     * 字符集用GBK
     */
    public static final String ENCODING_GBK = "GBK";
    /**
     * 字符集用UTF-8
     */
    public static final String ENCODING_UTF8 = "UTF-8";

    /**
     * BLOWFISH加解密方法,由于相同源可以得到不同结果,而且破解就像当于得到加密密钥的过程所以有相当的安全性,效率还可以.
     * BLOWFISH 使用的秘钥 可以是1-16byte
     */
    public static final String ALGORITHM_BLOWFISH = "Blowfish";
    /**
     * 一个比较早期的加解密方法。一个字节改变可以改变8个字节的密文。安全性不是是很高。 后面一般用多次加密的方法。多重DES。如Triple DES
     * DES 使用的秘钥 是8byte
     * @see #ALGORITHM_TRIPLE_DES
     */
    public static final String ALGORITHM_DES = "DES";
    /**
     * AES算法，改算法由DES和RSA融合而成，曾在美国军方使用，现已退役，但对于民用安全性还行
     * DES 使用的秘钥 是 8byte
     */
    public static final String ALGORITHM_AES = "AES";
    /**
     * AES算法，改算法由DES和RSA融合而成，曾在美国军方使用，现已退役，但对于民用安全性还行
     * DES 使用的秘钥 是 8byte
     */
    public static final String ALGORITHM_SM4 = "SM4";
    /**
     * 三重DES算法。
     * TRIPLE_DES 使用的秘钥 是8byte
     * @see #ALGORITHM_DES
     */
    public static final String ALGORITHM_TRIPLE_DES = "DESede";
    /**
     * MD5数字摘要方法
     * 注意不可解密
     */
    public static final String ALGORITHM_MD5 = "MD5";
    /**
     * 不加密。仅转化内码和字符串
     */
    public static final String ALGORITHM_NONE = null;
    /**
     * SHA-1数字摘要方法，精度为160位。安全性比MD5高
     * 注意不可解密
     */
    public static final String ALGORITHM_SHA1 = "SHA-1";
    /**
     * SHA数字摘要方法，精度为256位。
     * 注意不可解密
     */
    public static final String ALGORITHM_SHA256 = "SHA-256";

    /**
     * SHA数字摘要方法，精度为512位。
     * 注意不可解密
     */
    public static final String ALGORITHM_SHA512 = "SHA-512";

    protected String algorithm;
    protected String encoding;
    protected int present;
    protected boolean gzip = false;
    private Object key;
    private Object param;
    public Object getKey() {
        return key;
    }
    public Object getParam() {
        return param;
    }

    public boolean isGzip() {
        return gzip;
    }


    public String getAlgorithm() {
        return algorithm;
    }

    public String getEncoding() {
        return encoding;
    }

    public int getPresent() {
        return present;
    }

    public CryptorBuilder encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }
    public CryptorBuilder present(int present) {
        this.present = present;
        return this;
    }


    private CryptorBuilder() {
    }

    public static CryptorBuilder create(String algorithm, Object key) {
        CryptorBuilder cb = new CryptorBuilder();
        cb.algorithm = algorithm;
        cb.key = key;
        cb.encoding=ENCODING_UTF8;
        cb.present=isDigest(algorithm)?PRESENT_HEX:PRESENT_BASE64;
        return cb;
    }

    private static boolean isDigest(String algorithm){
        return ALGORITHM_MD5.equals(algorithm)||
                ALGORITHM_SHA1.equals(algorithm)||
                ALGORITHM_SHA256.equals(algorithm)||
                ALGORITHM_SHA512.equals(algorithm) ;
    }

    public CryptorBuilder param(Object param) {
        this.param = param;
        return this;
    }
    public CryptorBuilder gzip(boolean gzip) {
        this.gzip = gzip;
        return this;
    }

    public Cryptor build() {
        if(isDigest(algorithm)){
            return new MessageDigestCryptor(this);
        }
        if(ALGORITHM_SM4.equals(algorithm)){
            return new SM4Cryptor(this);
        }
        // MD5 SHA
        return new CipherCryptor(this);
    }
}