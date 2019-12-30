package com.rongji.dfish.base.crypt;


import java.io.*;
import com.rongji.dfish.base.crypt.stream.*;

public abstract class AbstractCryptor implements Cryptor{

    /**
     * 该方法不计入encoding.
     * 1 先过 zip 的流
     * 2 转接 加密流
     * 3 转接 present的流
     *
     * @param is
     * @param os
     */
    @Override
    public void encrypt(InputStream is, OutputStream os) {
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

    protected abstract void doEncrypt(InputStream is, OutputStream os);

    protected abstract void doDecrypt(InputStream is, OutputStream os);

    @Override
    public void decrypt(InputStream is, OutputStream os) {
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
        ByteArrayInputStream bais = null;
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
        ByteArrayInputStream bais = new ByteArrayInputStream(src.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.decrypt(bais, baos);
        try {
            return new String(baos.toByteArray(), builder.encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractCryptor(CryptorBuilder builder) {
        this.builder = builder;
    }

    protected CryptorBuilder builder;

}
