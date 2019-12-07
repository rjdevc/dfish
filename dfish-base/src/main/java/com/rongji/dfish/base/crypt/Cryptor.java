package com.rongji.dfish.base.crypt;

import java.io.InputStream;
import java.io.OutputStream;

public class Cryptor extends  AbstractCryptor{
    public static class CryptorBuilder extends AbstractCryptBuilder<CryptorBuilder> {
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
    public String encode(String src){
        //只是把protected改为 public
        return super.encode(src);
    }
    @Override
    public String decode(String src){
        //只是把protected改为 public
        return super.decode(src);
    }
    @Override
    public void encode(InputStream is, OutputStream os){
        //只是把protected改为 public
        super.encode(is,os);
    }
    @Override
    public void decode(InputStream is, OutputStream os){
        //只是把protected改为 public
        super.decode(is,os);
    }
}
