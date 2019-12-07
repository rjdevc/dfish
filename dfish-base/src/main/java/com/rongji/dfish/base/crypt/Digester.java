package com.rongji.dfish.base.crypt;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Digester  extends  AbstractCryptor{
    public static class DigesterBuilder extends AbstractCryptBuilder<DigesterBuilder>{
        private DigesterBuilder(){};
        public static DigesterBuilder create(String algorithm){
            DigesterBuilder cb=new DigesterBuilder();
            cb.algorithm=algorithm;
            return cb;
        }
        public Digester build(){
            return new Digester(this);
        }
    }

    public Digester(DigesterBuilder db){
        super(db);
    }
    public String digest(String src){
        return super.encode(src);
    }
    public byte[] digest(InputStream is){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        super.encode(is, baos);
        return baos.toByteArray();
    }
}
