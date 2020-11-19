package com.rongji.dfish.misc.rjd;

import com.rongji.dfish.base.crypto.stream.HexInputStream;
import com.rongji.dfish.base.crypto.stream.SM4ECBOutputStream;

import java.io.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * RJD格式写入器
 */
public class RJDWriter implements Closeable {
    private static final String ENCODING="UTF-8";
    private ZipOutputStream zos;
    private String password;

    /**
     * 构造函数
     * @param os 输出流
     * @param password 密码
     * @throws IOException
     */
    public RJDWriter(OutputStream os, String password)throws IOException {
        this.zos=new ZipOutputStream(os);
        zos.setLevel(ZipOutputStream.STORED);
        this.password=password;

    }

    /**
     * 写入一个文件
     * @param path 路径
     * @param is 文件内容流
     * @throws IOException
     */
    public void write(String path,InputStream is)throws IOException{
        //加密写入
        ZipEntry ze=new ZipEntry(path);
        zos.putNextEntry(ze);
        int read=0;
        byte[] buff=new byte[8192];
        SM4ECBOutputStream sm4os=new SM4ECBOutputStream(zos,getKey(password));
        while((read=is.read(buff))>=0){
            sm4os.write(buff,0,read);
        }
        sm4os.flush();
        // sm4os 不能关闭，否则会连带zos关闭
        is.close();
        zos.closeEntry();
    }

    protected static byte[] getKey(String password) {
        if(password==null){
            password="RJSOFT002474";
        }
        //如果password 是16进制的，先转成byte[]
        byte[] key;
        if(HexInputStream.isHex(password)){
            key=HexInputStream.parseHex(password);
        }else{
            try {
                key=password.getBytes(ENCODING);
            } catch (UnsupportedEncodingException e) {
                key=password.getBytes();
            }
        }
        //如果key 大于16字节，截取，否则循环补齐
        int KEY_LENGTH=16;
        if(key.length>KEY_LENGTH){
            byte[] realKey=new byte[KEY_LENGTH];
            System.arraycopy(key,0,realKey,0,KEY_LENGTH);
            return realKey;
        }
        if(key.length<KEY_LENGTH){
            byte[] realKey=new byte[KEY_LENGTH];
            for(int i=0;i<KEY_LENGTH;i++){
                realKey[i]=key[i%key.length];
            }
            return realKey;
        }
        return key;
    }



    /**
     * 写入一个文件
     * @param path 文件路径
     * @param str 文件文本
     * @throws IOException
     */
    public void write(String path,String str)throws IOException{
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        GZIPOutputStream gzos=new GZIPOutputStream(baos);
        gzos.write(str.getBytes(ENCODING));
        gzos.close();
        write(path, new ByteArrayInputStream(baos.toByteArray()));
    }

    /**
     * 写入结束后，关闭。
     * @throws IOException
     */
    @Override
    public void close() throws IOException{
        zos.close();
    }

}
