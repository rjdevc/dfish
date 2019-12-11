package com.rongji.dfish.base.crypt;

import com.rongji.dfish.base.util.LogUtil;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Cryptor extends  AbstractCryptor{

    public static class CryptorBuilder extends AbstractCryptBuilder<CryptorBuilder> {
        public static final String ALGORITHM_NONE=null;
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
         * 三重DES算法。
         * TRIPLE_DES 使用的秘钥 是8byte
         * @see #ALGORITHM_DES
         */
        public static final String ALGORITHM_TRIPLE_DES = "DESede";

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
        private CryptorBuilder() {
        }

        public static CryptorBuilder create(String algorithm, Object key) {
            CryptorBuilder cb = new CryptorBuilder();
            cb.algorithm = algorithm;
            cb.key = key;
            cb.encoding=ENCODING_UTF8;
            cb.present=PRESENT_BASE64;
            return cb;
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
            return new Cryptor(this);
        }
    }

    public Cryptor(CryptorBuilder cb){
        super(cb);
    }

    @Override
    public String encrypt(String src){
        //只是把protected改为 public
        return super.encrypt(src);
    }
    @Override
    public String decrypt(String src){
        //只是把protected改为 public
        return super.decrypt(src);
    }
    @Override
    public void encrypt(InputStream is, OutputStream os){
        //只是把protected改为 public
        super.encrypt(is,os);
    }

    @Override
    protected void doEncrypt(InputStream is, OutputStream os) {
        try {
            OutputStream cos;
            if(builder.getAlgorithm()!=null&&!builder.getAlgorithm().equals("")) {
                Cipher cipher = initCipher(Cipher.ENCRYPT_MODE);
                cos = new CipherOutputStream(os, cipher);
            }else{
                cos=os;
            }
            if (builder.gzip) {
                cos = new GZIPOutputStream(cos);
            }
            run(is, cos);
        }catch (Exception ex){
            LogUtil.error(null,ex);
        }
    }


    @Override
    protected void doDecrypt(InputStream is, OutputStream os) {
        try {
            InputStream cis ;
            if(builder.getAlgorithm()!=null&&!builder.getAlgorithm().equals("")) {
                Cipher cipher = initCipher(Cipher.DECRYPT_MODE);
                cis=new CipherInputStream(is, cipher);
            }else{
                cis=is;
            }
            if (builder.gzip) {
                cis = new GZIPInputStream(cis);
            }
            run(cis, os);
        }catch (Exception ex){
           throw new RuntimeException(ex);
        }
    }
    private static boolean inited=false;
    private void init(){
        if(inited){
            return;
        }
        String[] providers = {"com.sun.crypto.provider.SunJCE",
                "com.ibm.crypto.provider.IBMJCE",
                "com.ibm.crypto.hdwrCCA.provider.IBMJCE4758",
                "org.bouncycastle.jce.provider.BouncyCastleProvider"};
        Provider p =null;
        for (int i = 0; i < providers.length; i++) {
            try {
                p= (Provider) Class.forName(providers[i]).
                        newInstance();
                Security.addProvider(p);
                break;
            } catch (Exception ex) {
            }
        }
        inited=true;
    }

    private Cipher initCipher(int encryptMode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        init();
        Cipher cipher = Cipher.getInstance(builder.getAlgorithm());
        CryptorBuilder cb=(CryptorBuilder) builder;
        if(builder.algorithm.toUpperCase().equals("RSA")) {
            KeyPair keyPair =null;
            if (cb.key instanceof KeyPair) {
                keyPair = (KeyPair) cb.key;
            } else if (cb.key != null && cb.key instanceof BigInteger[]) {
                final BigInteger[] cast = (BigInteger[]) cb.key;
                RSAPrivateKey prikey = new RSACryptor4BC.DFishRSAPrivateKey(cast[2], cast[1]);
                RSAPublicKey pubkey = new RSACryptor4BC.DFishRSAPublicKey(cast[2], cast[0]);
                keyPair = new KeyPair(pubkey, prikey);
            }
            //FIXME 为空默认给一个秘钥();
            if(encryptMode==Cipher.ENCRYPT_MODE){
                cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
            }else{
                cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
            }
        }else {
            byte[] key=(byte[]) cb.key;
            //
            //FIXME 为空默认给一个秘钥();
            SecretKeySpec keySpec = new SecretKeySpec(key, builder.algorithm);
            cipher.init(encryptMode, keySpec);//FIXME CBC 需要设置iv
        }
        return cipher;
    }


    @Override
    public void decrypt(InputStream is, OutputStream os){
        //只是把protected改为 public
        super.decrypt(is,os);
    }

    protected void run(InputStream is, OutputStream os) {
        byte[] b = new byte[8192];
        int len = 0;
        try{
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
