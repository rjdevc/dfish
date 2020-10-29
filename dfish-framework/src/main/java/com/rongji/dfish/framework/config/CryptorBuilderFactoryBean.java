package com.rongji.dfish.framework.config;

import com.rongji.dfish.base.crypto.CryptorBuilder;
import com.rongji.dfish.base.crypto.stream.HexInputStream;
import com.rongji.dfish.base.util.CryptoUtil;
import org.springframework.beans.factory.FactoryBean;

import java.io.UnsupportedEncodingException;

/**
 * 加密工具工厂类,实现Spring的FactoryBean接口
 * 通过不同配置快速得到不同的实例
 * @since DFish5.0
 * @author LinLW
 */
public class CryptorBuilderFactoryBean implements FactoryBean<CryptorBuilder> {

    /**
     * 默认算法
     */
    public static final String ALGORITHM_DEFAULT = CryptoUtil.ALGORITHM_BLOWFISH;
    /**
     * 默认秘钥(部分算法不需要秘钥)
     */
    public static final String SECRET_KEY_DEFAULT = "DFish@RJ002474";

    @Override
    public CryptorBuilder getObject() throws Exception {
        String realAlgorithm=parse(0,algorithm);
        String realPresent=parse(1,present);
        String realEncoding=parse(2,encoding);
        byte[] realSecretKey=parseRealSecretKey(secretKey,realAlgorithm);
        CryptorBuilder builder=CryptoUtil.prepareCryptor(realAlgorithm,realSecretKey);
        if(realEncoding!=null){
            builder.encoding(realEncoding);
        }
        if(realPresent!=null){
            builder.present(Integer.parseInt(realPresent));
        }
        if(gzip!=null){
            builder.gzip(gzip);
        }
        return builder;
    }

    private byte[] parseRealSecretKey(String secretKey, String realAlgorithm) {
        int KEY_LENGTH;
        switch (realAlgorithm){
            case "SM3":
            case "MD5":
            case "SHA-1":
            case "SHA-256":
            case "SHA-512":
                return null;
            case "DES":
            case "DESede":
                KEY_LENGTH=8;
                break;
            case "SM4":
                KEY_LENGTH=32;
                break;
            default: KEY_LENGTH=16;
        };
        byte[] key;
        if(HexInputStream.isHex(secretKey)){
            key=HexInputStream.parseHex(secretKey);
        }else{
            try {
                key=secretKey.getBytes(CryptoUtil.ENCODING_UTF8);
            } catch (UnsupportedEncodingException e) {
                key=secretKey.getBytes();
            }
        }
        //如果key 大于32字节，截取，否则循环补齐
        if(key.length>KEY_LENGTH){
            byte[] realKey=new byte[KEY_LENGTH];
            System.arraycopy(key,0,realKey,0,KEY_LENGTH);
            return realKey;
        }
        if(key.length<KEY_LENGTH){
            byte[] realKey=new byte[KEY_LENGTH];
            for(int i=0;i<KEY_LENGTH;i++){
                realKey[i]=key[i%key.length];
            }
            return realKey;
        }
        return key;
    }

    private static final String[][][] PATTENS={
            {
                    {"Blowfish","BLOWFISH"},
                    {"DES"},{"AES"},{"SM4"},{"SM3"},{"MD5"},
                    {"DESede","DESEDE","3DES","TRIPLEDES","TRIPLE_DES"},
                    {"SHA-1","SHA1"},
                    {"SHA-256","SHA256"},
                    {"SHA-512","SHA512"},
            },
            {
                    {"0","RAW"},
                    {"100","HEX","1"},
                    {"200","BASE64","2"},
                    {"201","BASE64_URLSAFE","3"},
                    {"300","BASE32","4"}
            },
            {
                    {"UTF-8","UTF8","UTF"},
                    {"GBK","GB2312","GB18030"},
            }
    };
    private String parse(int i, String key) {
        if(key==null){
            return null;
        }
        String src=key.toUpperCase();
        String[][] PATTEN=PATTENS[i];
        for(String[]row:PATTEN){
            for(String p:row){
                if(p.equals(src)){
                    return row[0];
                }
            }
        }
        return null;
    }


    @Override
    public Class getObjectType() {
        return CryptorBuilder.class;
    }

    private String algorithm = ALGORITHM_DEFAULT;
    private String encoding;
    private String present;
    private String secretKey = SECRET_KEY_DEFAULT;
    private Boolean gzip;


    /**
     * 是否压缩
     * @return Boolean
     */
    public Boolean getGzip() {
        return gzip;
    }

    /**
     * 是否压缩
     * @param gzip Boolean
     */
    public void setGzip(Boolean gzip) {
        this.gzip = gzip;
    }


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
    public String getPresent() {
        return present;
    }

    /**
     * 密文呈现方式
     * @param present int
     */
    public void setPresent(String present) {
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
