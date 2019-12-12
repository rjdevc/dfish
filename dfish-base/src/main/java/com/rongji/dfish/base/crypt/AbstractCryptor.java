package com.rongji.dfish.base.crypt;


import java.io.*;
import com.rongji.dfish.base.crypt.stream.*;

public abstract class AbstractCryptor {
    public static class AbstractCryptBuilder<T extends AbstractCryptBuilder> {
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


        protected String algorithm;
        protected String encoding;
        protected int present;

        protected boolean gzip = false;

        public String getAlgorithm() {
            return algorithm;
        }

        public String getEncoding() {
            return encoding;
        }

        public int getPresent() {
            return present;
        }

        public T encoding(String encoding) {
            this.encoding = encoding;
            return (T) this;
        }

        public T present(int present) {
            this.present = present;
            return (T) this;
        }
    }

    /**
     * 该方法不计入encoding.
     * 1 先过 zip 的流
     * 2 转接 加密流
     * 3 转接 present的流
     *
     * @param is
     * @param os
     */

    protected void encrypt(InputStream is, OutputStream os) {
        OutputStream pos;
        switch (builder.present) {
            case AbstractCryptBuilder.PRESENT_HEX:
                pos = new HexOutputStream(os);
                break;
            case AbstractCryptBuilder.PRESENT_BASE32:
                pos = new Base32OutputStream(os);
                break;
            case AbstractCryptBuilder.PRESENT_BASE64:
                pos = new Base64OutputStream(os);
                break;
            case AbstractCryptBuilder.PRESENT_BASE64_URLSAFE:
                pos = new Base64UrlsafeOutputStream(os);
                break;
            default:
                pos = os;
        }
//        InputStream zis;
//        if(builder.gzip){
//            zos=new ZipOutputStream(is);
//        }else{
//            zos=os;
//        }
        doEncrypt(is, pos);
//        ZipOutputStream(new CipherOutputStream(presentOS ,cipher));

    }

    protected abstract void doEncrypt(InputStream is, OutputStream os);

    protected abstract void doDecrypt(InputStream is, OutputStream os);

    protected void decrypt(InputStream is, OutputStream os) {
        InputStream pis;
        switch (builder.present) {
            case AbstractCryptBuilder.PRESENT_HEX:
                pis = new HexInputStream(is);
                break;
            case AbstractCryptBuilder.PRESENT_BASE32:
                pis = new Base32InputStream(is);
                break;
            case AbstractCryptBuilder.PRESENT_BASE64:
            case AbstractCryptBuilder.PRESENT_BASE64_URLSAFE:
                pis = new Base64InputStream(is);
                break;
            default:
                pis = is;
        }
        doDecrypt(pis, os);
    }

    protected String encrypt(String src) {
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

    protected String decrypt(String src) {
        ByteArrayInputStream bais = new ByteArrayInputStream(src.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.decrypt(bais, baos);
        try {
            return new String(baos.toByteArray(), builder.encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractCryptor(AbstractCryptBuilder builder) {
        this.builder = builder;
    }

    protected AbstractCryptBuilder builder;


}
