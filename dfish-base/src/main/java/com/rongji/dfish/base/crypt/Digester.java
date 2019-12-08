package com.rongji.dfish.base.crypt;

import com.rongji.dfish.base.util.LogUtil;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.zip.GZIPOutputStream;

public class Digester  extends  AbstractCryptor{
    @Override
    protected void doEncrypt(InputStream is, OutputStream os) {
        try {

            MessageDigest md=MessageDigest.getInstance(builder.algorithm);
            md.reset();
            byte[] b = new byte[1024 * 8];
            int len = 0;
            try{
                while ((len = is.read(b)) != -1) {
                    md.update(b,0,len);
                }
                os.write(md.digest());
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
        }catch (Exception ex){
            LogUtil.error(null,ex);
        }
    }

    @Override
    protected void doDecrypt(InputStream is, OutputStream os) {
        throw new UnsupportedOperationException("can not decrypt message digest");
    }

    public static class DigesterBuilder extends AbstractCryptBuilder<DigesterBuilder>{
        private DigesterBuilder(){}
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
