package com.rongji.dfish.base.crypto;


import com.rongji.dfish.base.crypto.stream.*;
import com.rongji.dfish.base.util.Utils;

import java.io.*;

/**
 * 抽象加解密工具，提供基础方法，用于计划加解密工作开发
 */
public abstract class AbstractCryptor implements Cryptor{

    @Override
    public void encrypt(InputStream is, OutputStream os) {

        /*
        该方法不计入encoding.
     * 1 先过 zip 的流
     * 2 转接 加密流
     * 3 转接 present的流
         */
        is=decorate(is);
        os=decorate(os);
        OutputStream pos;
        switch (builder.present) {
            case CryptorBuilder.PRESENT_HEX:
                pos = new HexOutputStream(os);
                break;
            case CryptorBuilder.PRESENT_BASE32:
                pos = new Base32OutputStream(os);
                break;
            case CryptorBuilder.PRESENT_BASE64:
                pos = new Base64OutputStream(os);
                break;
            case CryptorBuilder.PRESENT_BASE64_URLSAFE:
                pos = new Base64UrlsafeOutputStream(os);
                break;
            default:
                pos = os;
        }
        doEncrypt(is, pos);

    }

    private InputStream decorate(InputStream is) {
        if(is instanceof ByteArrayInputStream ||is instanceof BufferedInputStream){
            return is;
        }
        return new BufferedInputStream(is);
    }
    private OutputStream decorate(OutputStream out) {
        if(out instanceof ByteArrayOutputStream ||out instanceof BufferedOutputStream){
            return out;
        }
        return new BufferedOutputStream(out);
    }

    /**
     * 去除了 压缩， 字符集，以及 转化成BASE64 等操作后，内核的加解密动作
     * @param is InputStream
     * @param os OutputStream
     */
    protected abstract void doEncrypt(InputStream is, OutputStream os);

    /**
     * 去除了 解压缩， 字符集，以及 转化成BASE64 等操作后，内核的加解密动作
     * @param is InputStream
     * @param os OutputStream
     */
    protected abstract void doDecrypt(InputStream is, OutputStream os);

    @Override
    public void decrypt(InputStream is, OutputStream os) {
        is=decorate(is);
        os=decorate(os);
        InputStream pis;
        switch (builder.present) {
            case CryptorBuilder.PRESENT_HEX:
                pis = new HexInputStream(is);
                break;
            case CryptorBuilder.PRESENT_BASE32:
                pis = new Base32InputStream(is);
                break;
            case CryptorBuilder.PRESENT_BASE64:
            case CryptorBuilder.PRESENT_BASE64_URLSAFE:
                pis = new Base64InputStream(is);
                break;
            default:
                pis = is;
        }
        doDecrypt(pis, os);
    }

    @Override
    public String encrypt(String src) {
        if (Utils.isEmpty(src)) {
            return null;
        }
        ByteArrayInputStream bais;
        try {
            bais = new ByteArrayInputStream(src.getBytes(builder.encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.encrypt(bais, baos);
        return new String(baos.toByteArray());
    }

    @Override
    public String decrypt(String src) {
        if (Utils.isEmpty(src)) {
            return null;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(src.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.decrypt(bais, baos);
        try {
            return new String(baos.toByteArray(), builder.encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构造函数
     * @param builder
     */
    public AbstractCryptor(CryptorBuilder builder) {
        this.builder = builder;
    }

    /**
     * 取得builder句柄。
     */
    protected CryptorBuilder builder;

    /**
     * 将文本配置的秘钥转化成当前算法要求的 byte[]
     * @param key 文本秘钥
     * @param minLen 最小宽度
     * @param maxLen 最大宽度
     * @param algorithm 算法
     * @return byte[]
     */
    protected static byte[] getKeyBytes(String key,int minLen,int maxLen,String algorithm){
        if(HexInputStream.isHex(key)&& key.length()/2>=minLen&&key.length()/2>=maxLen){
            return HexInputStream.parseHex(key);
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



    /**
     * 运行加密或解密的流动作
     * @param is  InputStream
     * @param os OutputStream
     */
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
