package com.rongji.dfish.misc.rjd;

import com.rongji.dfish.base.util.LogUtil;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

/**
 * 读取RJD格式的回调。可以取得文件路径，和文件的内容，并当做文本处理
 */
@FunctionalInterface
public interface RJDStringCallback extends RJDCallback {
    String ENCODING="UTF-8";
    default void execute(String path, InputStream is){
        try {
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            int read=0;
            byte[] buff=new byte[8192];
            while((read=is.read(buff))>=0){
                baos.write(buff,0,read);
            }
            //IS 不能关闭
            try {
                GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(baos.toByteArray()));
                baos.reset();
                while((read=zis.read(buff))>=0){
                    baos.write(buff,0,read);
                }
                execute(path,new String(baos.toByteArray(),ENCODING));
                zis.close();
            }catch (ZipException ex){
                throw new RuntimeException("SM4 decrypt fail");
            }
        } catch (IOException e) {
            LogUtil.error(null,e);
        }
    }

    /**
     * 执行动作
     * @param path 文件的路径
     * @param str 文件的内容
     */
    void execute(String path,String str);

}
