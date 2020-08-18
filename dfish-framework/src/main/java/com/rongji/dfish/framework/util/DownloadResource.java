package com.rongji.dfish.framework.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * 可下载的资源
 */
public class DownloadResource {

    /**
     * 可以打开资源的输入流的句柄
     */
    public static interface InputStreamProvider{
        InputStream open() throws IOException;
    }

    private String name;
    private String contentType;
    private long length;
    private long lastModified;
    private InputStreamProvider inputStreamProvider;

    /**
     * 构造函数
     * @param name String 文件名
     * @param length String 文件长度
     * @param lastModified long 最后修改时间，毫秒
     * @param provider long 输入流的句柄
     */
    public DownloadResource(String name, long length, long lastModified, InputStreamProvider provider){
        setName(name);
        setLength(length);
        setLastModified(lastModified);
        setInputStreamProvider(provider);
    }

    /**
     * 文件名
     * inline==false的时候 文件名则是附件下载时保存的名称
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * 文件名
     * inline==false的时候 文件名则是附件下载时保存的名称
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 浏览器识别的内容类别
     * inline=true的时候则使用该contentType
     * 如果该值为空，则会尝试从文件名的后缀名中自动获取contentType
     * 所有不能识别的类型，将作为下载(application/oct-stream)处理。
     * @return String
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * 浏览器识别的内容类别
     * inline=true的时候则使用该contentType
     * 如果该值为空，则会尝试从文件名的后缀名中自动获取contentType
     * 所有不能识别的类型，将作为下载(application/oct-stream)处理。
     * @param contentType String
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * 文件长度
     * @return long
     */
    public long getLength() {
        return length;
    }

    /**
     * 文件长度
     * @param length long
     */
    public void setLength(long length) {
        this.length = length;
    }

    /**
     * 最后修改时间，毫秒
     * @see java.util.Date#getTime()
     * @see java.io.File#lastModified()
     * @return long
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * 最后修改时间，毫秒
     * @see java.util.Date#getTime()
     * @see java.io.File#lastModified()
     * @param lastModified long
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * 输入流的句柄
     * 如果文件没有缓存的时候，将会打开输入流，并将内容转发到客户端。
     * @return InputStreamProvider
     */
    public InputStreamProvider getInputStreamProvider() {
        return inputStreamProvider;
    }

    /**
     * 输入流的句柄
     * 如果文件没有缓存的时候，将会打开输入流，并将内容转发到客户端。
     * @param inputStreamProvider InputStreamProvider
     */
    public void setInputStreamProvider(InputStreamProvider inputStreamProvider) {
        this.inputStreamProvider = inputStreamProvider;
    }





}
