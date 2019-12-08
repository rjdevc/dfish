package com.rongji.dfish.base.crypt;

import com.rongji.dfish.base.util.LogUtil;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CyclicBarrier;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

public class Cryptor extends  AbstractCryptor{
    public static class CryptorBuilder extends AbstractCryptBuilder<CryptorBuilder> {
        public static final String ALGORITHM_NONE=null;

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
        }catch (Exception ex){
            LogUtil.error(null,ex);
        }
    }

    private Cipher initCipher(int encryptMode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(builder.getAlgorithm());
        CryptorBuilder cb=(CryptorBuilder) builder;
        byte[] key=(byte[]) cb.key;
        SecretKeySpec keySpec = new SecretKeySpec(key, builder.algorithm);
        cipher.init(encryptMode, keySpec);//FIXME CBC 需要设置iv
        return cipher;
    }


    @Override
    public void decode(InputStream is, OutputStream os){
        //只是把protected改为 public
        super.decode(is,os);
    }

    protected void run(InputStream is, OutputStream os) {
        byte[] b = new byte[1024 * 8];
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
