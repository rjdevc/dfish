package com.rongji.dfish.framework.plugin.file.dto;

/**
 * 预览响应
 * @author lamontYu
 * @date 2019-12-11
 * @since 5.0
 */
public class PreviewResponse {
    /**
     * 预览方式-文件下载
     */
    public static final int WAY_DOWNLOAD = 0;
    /**
     * 预览方式-内联方式
     */
    public static final int WAY_INLINE = 1;
    /**
     * 预览方式-图片预览
     */
    public static final int WAY_PREVIEW_IMAGE = 2;
    /**
     * 预览方式-文档预览
     */
    public static final int WAY_PREVIEW_DOCUMENT = 3;

    private int way = WAY_DOWNLOAD;
    private String url;

    /**
     * 预览方式,目前
     * @return
     */
    public int getWay() {
        return way;
    }

    /**
     * 预览方式
     * @param way
     * @return 本身，这样可以继续设置其他属性
     */
    public PreviewResponse setWay(int way) {
        this.way = way;
        return this;
    }

    /**
     * url地址
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * url地址
     * @param url
     * @return 本身，这样可以继续设置其他属性
     */
    public PreviewResponse setUrl(String url) {
        this.url = url;
        return this;
    }
}
