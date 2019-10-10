package com.rongji.dfish.base.crypt;

import com.rongji.dfish.base.crypt.sm.SM2Cryptor;
import com.rongji.dfish.base.crypt.sm.SM4_ECB_PKCS7_Cryptor;

/**
 * CryptFactory 加解方法的工厂类
 *
 * @author LinLW
 * @version 1.1
 */
public class CryptFactory {
    /**
     * AES算法，改算法由DES和RSA融合而成，曾在美国军方使用，现已退役，但对于民用安全性还行
     */
    public static final String ALGORITHMS_AES = "AES";
    /**
     * BLOWFISH加解密方法,由于相同源可以得到不同结果,而且破解就像当于得到加密密钥的过程所以有相当的安全性,效率还可以.
     */
    public static final String ALGORITHMS_BLOWFISH = "Blowfish";
    /**
     * RSA
     */
    public static final String ALGORITHMS_RSA = "RSA";
    /**
     * 一个比较早期的加解密方法。一个字节改变可以改变8个字节的密文。安全性不是是很高。
     * 后面一般用多次加密的方法。多重DES。如Triple DES
     *
     * @see #ALGORITHMS_TRIPLE_DES
     */
    public static final String ALGORITHMS_DES = "DES";
    /**
     * 国密算法SM4
     * 配合分组规则ECB 以及补位规则PKCS7
     */
    public static final String ALGORITHMS_SM4_ECB_PKCS7 = "SM4_ECB_PKCS7";

    /**
     * 国密算法SM2
     * 椭圆曲线公钥密码算法
     */
    public static final String ALGORITHMS_SM2 = "SM2";

    /**
     * 由于加解密内码为byte[]要通过一种方法转存成字符串
     * 这里用BASE64
     */
    public static final int PRESENT_STYLE_BASE64 = StringCryptor.PRESENT_STYLE_BASE64;
    /**
     * 先调用压缩算法，再转成BASE_64
     * 一般只适合大片的文本原文，密文的话，压缩算法的效率很低。
     */
    @Deprecated
    public static final int PRESENT_STYLE_GZIP_WITH_BASE64 = StringCryptor.PRESENT_STYLE_GZIP_WITH_BASE64;
    /**
     * 由于加解密内码为byte[]要通过一种方法转存成字符串
     * 这里用BASE32
     */
    public static final int PRESENT_STYLE_BASE32 = StringCryptor.PRESENT_STYLE_BASE32;
    /**
     * 由于加解密内码为byte[]要通过一种方法转存成字符串
     * 这里用十六进制表达
     */
    public static final int PRESENT_STYLE_HEX_STRING = StringCryptor.PRESENT_STYLE_HEX_STRING;
    /**
     * 内码转化成字符串所用的方法
     * 这里用BASE64算法转化然後把BASE64的+/=符號替換成網絡傳輸中不失真的字符-_.
     */
    public static final int PRESENT_STYLE_URL_SAFE_BASE64 = StringCryptor.PRESENT_STYLE_URL_SAFE_BASE64;

    /**
     * MD5数字摘要方法
     * 注意不可解密
     */
    public static final String ALGORITHMS_MD5 = "MD5";
    /**
     * 不加密。仅转化内码和字符串
     */
    public static final String ALGORITHMS_NONE = null;
    /**
     * SHA-1数字摘要方法，精度为160位。安全性比MD5高
     * 注意不可解密
     */
    public static final String ALGORITHMS_SHA1 = "SHA-1";
    /**
     * SHA数字摘要方法，精度为256位。
     * 注意不可解密
     */
    public static final String ALGORITHMS_SHA256 = "SHA-256";

    /**
     * SHA数字摘要方法，精度为512位。
     * 注意不可解密
     */
    public static final String ALGORITHMS_SHA512 = "SHA-512";
    /**
     * 简单的RSA，没有分块，所以不可做大文本的加解密。
     *
     * @deprecated 大文本的加解密时会越界
     */
    public static final String ALGORITHMS_SIMPLE_RSA = "RSA without blocking";
    /**
     * RSA
     * 早期自行分组的RSA。
     * 在I-TASK的证书中使用
     * 该算法，不具备有通用性
     */
    public static final String ALGORITHMS_RSA_OLD = "RSA for itask license";
    /**
     * 三重DES算法。
     *
     * @see #ALGORITHMS_DES
     */
    public static final String ALGORITHMS_TRIPLE_DES = "DESede";

    /**
     * 字符集GBK
     */
    public static final String ENCODING_GBK = StringCryptor.ENCODING_GBK;
    /**
     * 字符集UTF-8
     */
    public static final String ENCODING_UTF8 = StringCryptor.ENCODING_UTF8;
    /**
     * 字符集用UTF-8,并对产生的byte数组进行一次GZIP压缩
     */
    public static final String ENCODING_UTF8_GZIP = StringCryptor.ENCODING_UTF8_GZIP;
    /**
     * 字符集UNICODE
     */
    public static final String ENCODING_UNICODE = StringCryptor.ENCODING_UNICODE;

    /**
     * @param algorithms   算法
     * @param encoding     字符集
     * @param presentStyle 加密字符串样式
     * @return 加解密工具
     */
    public static StringCryptor getStringCryptor(String algorithms, String encoding, int presentStyle) {
        return getStringCryptor(algorithms, encoding, presentStyle, null);
    }

    /**
     * @param algorithms   算法
     * @param encoding     字符集
     * @param presentStyle 加密字符串样式
     * @param arg          算法参数,一般是密钥
     * @return 加解密工具
     */
    @SuppressWarnings("deprecation")
    public static StringCryptor getStringCryptor(String algorithms, String encoding, int presentStyle, Object arg) {
        if (algorithms == null) {
            return new NoneCryptor(encoding, presentStyle);
        }
        if (ALGORITHMS_MD5.equals(algorithms) || ALGORITHMS_SHA1.equals(algorithms)
                || ALGORITHMS_SHA512.equals(algorithms) || ALGORITHMS_SHA256.equals(algorithms)) {
            return new MessageDigestCryptor(algorithms, encoding, presentStyle);
        }
        if (algorithms.equals(ALGORITHMS_SIMPLE_RSA)) {
            return new SimpleRSACryptor(encoding, presentStyle, arg);
        }
        if (algorithms.equals(ALGORITHMS_RSA_OLD)) {
            return new RSACryptor(encoding, presentStyle, arg);
        }
        if (algorithms.equals(ALGORITHMS_RSA)) {
            return new RSACryptor4BC(encoding, presentStyle, arg);
        }
        if (algorithms.equals(ALGORITHMS_SM4_ECB_PKCS7)) {
            return new SM4_ECB_PKCS7_Cryptor(encoding, presentStyle, arg);
        }
        if (algorithms.equals(ALGORITHMS_SM2)) {
            return new SM2Cryptor(encoding, presentStyle, arg);
        }

        return new JCECryptor(algorithms, encoding, presentStyle, arg);
    }

    private CryptFactory() {
    }
}
