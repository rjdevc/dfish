package com.rongji.dfish.base.crypt;

import com.rongji.dfish.base.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

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
        /**
         * MD5数字摘要方法
         * 注意不可解密
         */
        public static final String ALGORITHM_MD5 = "MD5";
        /**
         * 不加密。仅转化内码和字符串
         */
        public static final String ALGORITHM_NONE = null;
        /**
         * SHA-1数字摘要方法，精度为160位。安全性比MD5高
         * 注意不可解密
         */
        public static final String ALGORITHM_SHA1 = "SHA-1";
        /**
         * SHA数字摘要方法，精度为256位。
         * 注意不可解密
         */
        public static final String ALGORITHM_SHA256 = "SHA-256";

        /**
         * SHA数字摘要方法，精度为512位。
         * 注意不可解密
         */
        public static final String ALGORITHM_SHA512 = "SHA-512";
        private DigesterBuilder(){}
        public static DigesterBuilder create(String algorithm){
            DigesterBuilder cb=new DigesterBuilder();
            cb.algorithm=algorithm;
            cb.encoding=ENCODING_UTF8;
            cb.present=PRESENT_HEX;
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
        return super.encrypt(src);
    }
    public byte[] digest(InputStream is){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        super.encrypt(is, baos);
        return baos.toByteArray();
    }
}
