package com.rongji.dfish.base.crypt;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import javax.crypto.SecretKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Provider;

/**
 *
 * JCECryptor 是利用JCE提供的算法进行加解密.
 *
 * @author 小邱提供
 * @version 1.1
 */
public final class JCECryptor extends StringCryptor {
    private Cipher cipherE;
    private Cipher cipherD;

    private String algorithms;

    private SecretKey secretKey;
    /**
     * 构造函数
     * @param algorithms String
     * @param encoding String
     * @param presentStyle int
     */
    protected JCECryptor(String algorithms, String encoding, int presentStyle) {
        this(algorithms, encoding, presentStyle, null);
    }

    /**
     * 带密钥参数的构造函数
     * @param algorithms String
     * @param encoding String
     * @param presentStyle int
     * @param arg Object
     */
    protected JCECryptor(String algorithms, String encoding, int presentStyle,
                         Object arg) {
        super.encoding = encoding;
        super.presentStyle = presentStyle;
        this.algorithms = algorithms;
        String[] providers = {"com.sun.crypto.provider.SunJCE",
                             "com.ibm.crypto.provider.IBMJCE",
                             "com.ibm.crypto.hdwrCCA.provider.IBMJCE4758"};
        Provider p =null;
        for (int i = 0; i < providers.length; i++) {
            try {
                 //com.ibm.crypto.provider.IBMJCE
                 p= (Provider) Class.forName(providers[i]).
                            newInstance();
                     Security.addProvider(p);
                     break;
             } catch (ClassNotFoundException ex1) {
             } catch (IllegalAccessException ex1) {
             } catch (InstantiationException ex1) {
             }

        }
 //    Security.addProvider(new com.sun.crypto.provider.SunJCE());
        byte[] key = null;
        if (arg != null) {
            if (arg instanceof byte[]) {
                key = (byte[]) arg;
            } else if (arg instanceof String) {
                try {
                    key = ((String) arg).getBytes(UTF8_GZIP.equals(encoding)?UTF8:encoding);
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalArgumentException("The encoding setting ("
                            + encoding + ") is unavilable!");
                }
            }
        }
        if (key != null) {
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithms);
            secretKey = keySpec;
        }
        try {
            cipherE = Cipher.getInstance(algorithms);
            cipherD = Cipher.getInstance(algorithms);
            cipherE.init(Cipher.ENCRYPT_MODE, getKeyP());
            cipherD.init(Cipher.DECRYPT_MODE, getKeyP());

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("The algorithms (" + algorithms
                                               + ") is unavilable!");
        } catch (NoSuchPaddingException e) {
            throw new IllegalArgumentException("The algorithms (" + algorithms
                                               + ") is unavilable!");
        } catch (InvalidKeyException ex) {
            throw new IllegalArgumentException("The Key (" + arg
                                               + ") is unavilable!");
        }

    }

    /**
     * 取得密钥
     * @return byte[]
     */
    public byte[] getKey() {
        return getKeyP().getEncoded();
    }

    private SecretKey getKeyP() {
        if (secretKey == null) {
            KeyGenerator keygen;
            try {
                keygen = KeyGenerator.getInstance(algorithms);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalArgumentException("The algorithms ("
                        + algorithms + ") is unavilable!");
            }
            secretKey = keygen.generateKey();
        }
        return secretKey;
    }

    /**
     * 调用JCE 的加密方法加密
     * @param src byte[]
     * @return byte[]
     * @throws Exception
     */
    @Override
    protected synchronized byte[] encrypt(byte[] src) throws Exception {
        return cipherE.doFinal(src);
    }

    /**
     * 调用JCE的解密方法解密
     * @param src byte[]
     * @return byte[]
     * @throws Exception
     */
    @Override
    protected synchronized byte[] decrypt(byte[] src) throws Exception {
        return cipherD.doFinal(src);
    }

}
