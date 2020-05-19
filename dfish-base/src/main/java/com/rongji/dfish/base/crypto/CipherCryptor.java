package com.rongji.dfish.base.crypto;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 基于JDK - JCE体系中Cipher 制作的加解密工具
 * 该工具支持 ALGORITHM_BLOWFISH  ALGORITHM_DES ALGORITHM_TRIPLE_DES 和 ALGORITHM_AES
 */
public class CipherCryptor extends  AbstractCryptor{

    public CipherCryptor(CryptorBuilder cb){
        super(cb);
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
        }catch (RuntimeException ex){
            throw ex;
        }catch (Exception ex){
            throw new RuntimeException(ex);
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
        }catch (RuntimeException ex){
            throw ex;
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

    private Cipher initCipher(int encryptMode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        init();
        Cipher cipher = Cipher.getInstance(builder.getAlgorithm());

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

            parseKey();
            if(parameter!=null){
                // CBC 需要设置iv
                cipher.init(encryptMode, secretkey,parameter);
            }else {
                cipher.init(encryptMode, secretkey);
            }

//        }
        return cipher;
    }
    private Key secretkey;
    private AlgorithmParameterSpec parameter;
    private Key parseKey() {
        if(secretkey != null){
            return secretkey;
        }
        CryptorBuilder cb=builder;
        Object key=cb.getKey();
        if(key == null){
            return null;
        }
        if(key instanceof Key){
            this.secretkey=(Key)key;
            return (Key) key;
        }
        if(key.getClass().isArray()){
            if( key.getClass().getName().equals("[B")) {
                SecretKeySpec keySpec = new SecretKeySpec((byte[]) key, builder.algorithm);
                this.secretkey = keySpec;
                return keySpec;
            }
        }else if(key instanceof String){
            String strKey=(String) key;
            if(strKey.trim().equals("")){
                return null;
            }
            // 如果包含逗号，第一段才是密码。
            String paramKey=null;
            if(strKey.indexOf(',')>0){
                String[] strs=strKey.split(",");
                strKey=strs[0];
                paramKey =strs[1];
            }
            //第二段当做IV处理
            SecretKeySpec keySpec = new SecretKeySpec(getKeyBytes(strKey,builder.algorithm), builder.algorithm);
            this.secretkey=keySpec;
            if(paramKey!=null) {
                this.parameter = new IvParameterSpec(getKeyBytes(paramKey,builder.algorithm));
            }
            return keySpec;
        }
        throw new IllegalArgumentException("can not parse key "+key+" ("+key.getClass()+")");
    }

    private static byte[] getKeyBytes(String key,String algorithm){
        key = key.trim();
        switch (algorithm) {
            case CryptorBuilder.ALGORITHM_BLOWFISH:
                return getKeyBytes(key,1,16,algorithm);
            case CryptorBuilder.ALGORITHM_DES:
            case CryptorBuilder.ALGORITHM_TRIPLE_DES:
                return getKeyBytes(key, 8, 8,algorithm);
            case CryptorBuilder.ALGORITHM_AES:
                return getKeyBytes(key, 16, 16,algorithm);
            default:
                throw new IllegalArgumentException("can not parse key "+key+" using algorithm "+algorithm);
        }
    }


}
