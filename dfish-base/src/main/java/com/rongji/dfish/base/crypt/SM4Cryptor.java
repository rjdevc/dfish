package com.rongji.dfish.base.crypt;

import com.rongji.dfish.base.crypt.stream.SM4ECBInputStream;
import com.rongji.dfish.base.crypt.stream.SM4ECBOutputStream;
import com.rongji.dfish.base.util.LogUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SM4Cryptor extends AbstractCryptor {
    public <T extends CryptorBuilder> SM4Cryptor(CryptorBuilder builder) {
        super(builder);
    }


    @Override
    protected void doEncrypt(InputStream is, OutputStream os) {
        try {
            OutputStream cos = new SM4ECBOutputStream(os, getKey());
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
            InputStream cis =new SM4ECBInputStream(is, getKey());
            if (builder.gzip) {
                cis = new GZIPInputStream(cis);
            }
            run(cis, os);
        }catch (RuntimeException ex){
            throw ex;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
    private byte[] getKey(){
        Object key=builder.getKey();
        if(key.getClass().isArray()){
            if( key.getClass().getName().equals("[B")) {
                return (byte[])key;
            }
        }else if(key instanceof String){
            return getKeyBytes((String)builder.getKey(),16,16,CryptorBuilder.ALGORITHM_SM4);
        }
        return null;
    }
}
