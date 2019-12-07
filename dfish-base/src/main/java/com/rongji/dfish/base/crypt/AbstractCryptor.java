package com.rongji.dfish.base.crypt;

import java.io.*;
import java.util.Base64;

public abstract class AbstractCryptor {
    public static class AbstractCryptBuilder<T extends AbstractCryptBuilder> {
        protected String algorithm;
        protected String encoding;
        protected int present;
        protected boolean gzip=false;
        public String getAlgorithm() {
            return algorithm;
        }
        public String getEncoding() {
            return encoding;
        }
        public int getPresent() {
            return present;
        }
        public T encoding(String encoding){
            this.encoding=encoding;
            return (T)this;
        }
        public T present(int present){
            this.present=present;
            return (T)this;
        }
    }
    /**
     * 该方法不计入encoding.
     * 1 先过 zip 的流
     * 2 转接 加密流
     * 3 转接 present的流
     * @param is
     * @param os
     */
    protected void encode(InputStream is, OutputStream os){

//        doInit();
//        //
//        doUpdate();
//
//        doFinal();
    }
    protected  abstract  void doInit(int mode);

    protected abstract byte[] doUpdate(byte[] bytes);

    protected  abstract  byte[] doFinal();

    protected void decode(InputStream is, OutputStream os){

    }

    protected String encode(String src){
        ByteArrayInputStream bais= null;
        try {
            bais = new ByteArrayInputStream(src.getBytes(builder.encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        this.encode(bais,baos);
        return new String(baos.toByteArray());
    }
    protected String decode(String src){
        ByteArrayInputStream bais=  new ByteArrayInputStream(src.getBytes());
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        this.decode(bais,baos);
        try {
                return new String(baos.toByteArray(), builder.encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractCryptor(AbstractCryptBuilder builder){
        this.builder=builder;
    }
    protected AbstractCryptBuilder builder;
}
