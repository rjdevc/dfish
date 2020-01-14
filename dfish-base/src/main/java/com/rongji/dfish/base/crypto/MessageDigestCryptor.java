package com.rongji.dfish.base.crypto;

import com.rongji.dfish.base.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

public class MessageDigestCryptor extends  AbstractCryptor{
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
    @Override
    public void decrypt(InputStream is, OutputStream os){
        throw new UnsupportedOperationException("can not decrypt message digest");
    }
    @Override
    public String decrypt(String src){
        throw new UnsupportedOperationException("can not decrypt message digest");
    }


    public MessageDigestCryptor(CryptorBuilder db){
        super(db);
    }
}
