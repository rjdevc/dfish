package com.rongji.dfish.base.crypt;

import com.rongji.dfish.base.util.LogUtil;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
            if(strKey.indexOf(",")>=0){
                String[] strs=strKey.split(",");
                strKey=strs[0];
                paramKey =strs[1];
            }
            //第二段当做IV处理
            SecretKeySpec keySpec = new SecretKeySpec(getKeyBytes(strKey,builder.algorithm), builder.algorithm);
            this.secretkey=keySpec;
            if(paramKey!=null) {
                IvParameterSpec parameter = new IvParameterSpec(getKeyBytes(paramKey,builder.algorithm));
                this.parameter = parameter;
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
    private static byte[] getKeyBytes(String key,int minLen,int maxLen,String algorithm){
        if(isHex(key)&& key.length()/2>=minLen&&key.length()/2>=maxLen){
            return parseHex(key);
        }else {
            try {
                byte[] toBytes=key.getBytes("UTF-8");
                if(toBytes.length>=minLen&&toBytes.length<=maxLen){
                    return toBytes;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            throw new IllegalArgumentException("key "+key+" is not suit for algorithm "+algorithm+" (maxLen="+maxLen+",minLen="+minLen+")");
        }
    }

    private static boolean isHex(String key) {
        if(key.length()%2==1){
            return false;
        }
        char[] chs=key.toCharArray();
        for(char c:chs){
            if(c<'0'||(c>'9'&& c<'A')||(c>'F'&& c<'a')||c>'f'){
                return false;
            }
        }
        return true;
    }
    private static byte[] parseHex(String key){
        byte[] bytes=new byte[key.length()/2];
        char[] chs=key.toCharArray();
        for(int i=0;i<chs.length;i+=2){
            int hi=HEX_DE[chs[i]];
            int low=HEX_DE[chs[i+1]];
            bytes[i/2]=(byte)((hi<<4)|low);
        }
        return bytes;
    }
    private static final byte[] HEX_DE = { // 用于加速解密的cache
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 16
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 32
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, // 48
            0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, //64
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 80
            0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // 96

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
