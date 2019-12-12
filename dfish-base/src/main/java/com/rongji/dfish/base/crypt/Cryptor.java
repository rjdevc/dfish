package com.rongji.dfish.base.crypt;

import com.rongji.dfish.base.util.BeanUtil;
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
//        if(builder.algorithm.toUpperCase().equals("RSA")) {
//            checkSupportRSA();
//            KeyPair keyPair =null;
//            // 为空默认给一个秘钥();
//            if (cb.key ==null) {
//                BigInteger[] cast = new BigInteger[]{
//                        new BigInteger("65537"),
//                        new BigInteger("3975847456186545778910258323722382462226108968459932850835291748931926322431723811644249385364064316140709323155298729837697237790666969714582821721796300955220052752570615886418704964329385684186518046273152782074517389356344066757063850364102773170528468739571154868760309896337587189989866501283665899829"),
//                        new BigInteger("127155683514354915835005438861618010456325697090880831005595034330653840083638113565548151885659532139481404375489860318136054416572306159266703263342132400888831072667752725549589935295424145491341767982797872579143960638171751590304576930816961958556314358961250181659252196140481983213957121325735591765381")
//                };
//                RSAPrivateKey prikey = (RSAPrivateKey)BeanUtil.newInstance(
//                        "com.rongji.dfish.misc.crypt.SimpleRSAPrivateKey",cast[2], cast[1]);
//                RSAPublicKey pubkey = (RSAPublicKey)BeanUtil.newInstance(
//                        "com.rongji.dfish.misc.crypt.SimpleRSAPublicKey",cast[2], cast[0]);
//                keyPair = new KeyPair(pubkey, prikey);
//            }else if (cb.key instanceof KeyPair) {
//                keyPair = (KeyPair) cb.key;
//            } else if (cb.key != null && cb.key instanceof BigInteger[]) {
//                BigInteger[] cast = (BigInteger[]) cb.key;
//                RSAPrivateKey prikey = (RSAPrivateKey)BeanUtil.newInstance(
//                        "com.rongji.dfish.misc.crypt.SimpleRSAPrivateKey",cast[2], cast[1]);
//                RSAPublicKey pubkey = (RSAPublicKey)BeanUtil.newInstance(
//                        "com.rongji.dfish.misc.crypt.SimpleRSAPublicKey",cast[2], cast[0]);
//                keyPair = new KeyPair(pubkey, prikey);
//            }
//            if(encryptMode==Cipher.ENCRYPT_MODE){
//                cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
//            }else{
//                cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
//            }
//        }else {
        // bouncycastle 对chiper的流模式支持的也不是很好，如果要用它，要引包，要自己写PublicKey/PrivateKey
        // 以及可能需要进行ChiperOutputStream ChiperInputStream的改写。所以，暂时不做支持。 .
            byte[] key=(byte[]) cb.key;
            SecretKeySpec keySpec = new SecretKeySpec(key, builder.algorithm);
            cipher.init(encryptMode, keySpec);//FIXME CBC 需要设置iv
//        }
        return cipher;
    }
//    public static void main(String[]args){
//        KeyPairGenerator keyPairGen=null;
//        try {
//            keyPairGen = KeyPairGenerator.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        final int KEY_SIZE = 1024;// 没什么好说的了，这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
//        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
//        KeyPair keyPair = keyPairGen.genKeyPair();
//        System.out.println(keyPair);
//    }

//    private void checkSupportRSA() {
//        String[] classes={"org.bouncycastle.jce.provider.BouncyCastleProvider",
//        "com.rongji.dfish.misc.crypt.SimpleRSAPrivateKey"};
//        for(String clzName:classes){
//            if(!BeanUtil.exists(clzName)){
//                throw new UnsupportedOperationException("Can NOT support RSA. Please install bouncycastle-xxx.jar & dfish-misc-xxx.jar");
//            }
//        }
//    }


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
