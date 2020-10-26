package com.rongji.dfish.misc.rjd;

import com.rongji.dfish.base.crypto.stream.SM4ECBInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * RJD格式的读取器
 * <p></p>
 */
public class RJDReader {
    private ZipInputStream zis;
    private String password;

    /**
     * 新建一个读取器
     * @param is 输入流
     * @param password 密码
     * @throws IOException
     */
    public RJDReader(InputStream is, String password)throws IOException {
        zis=new ZipInputStream(is);
        this.password=password;
    }

    /**
     * 读取内容，并当做String处理
     * @param callback RJDStringCallback
     * @throws IOException
     */
    public void readStringEntries(RJDStringCallback callback)throws IOException {
        readEntries(callback);
    }

    /**
     * 读取内容，
     * @param callback RJDCallback
     * @throws IOException
     */
    public void readEntries(RJDCallback callback)throws IOException {
        ZipEntry ze= null;
        while((ze=zis.getNextEntry())!=null) {
            String name = ze.getName();
            SM4ECBInputStream sm4is=new SM4ECBInputStream(zis,RJDWriter.getKey(password));
            callback.execute(name, sm4is);
            zis.closeEntry();
        }
        zis.close();
    }

}

