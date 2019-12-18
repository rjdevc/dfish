package com.rongji.dfish.framework.plugin.file.dto;

/**
 * 预览响应
 * @author lamontYu
 * @date 2019-12-11
 * @since 5.0
 */
public class PreviewResponse {

    public static final int WAY_DOWNLOAD = 0;
    public static final int WAY_INLINE = 1;
    public static final int WAY_PREVIEW_IMAGE = 2;
    public static final int WAY_PREVIEW_FILE = 3;

    private int way = WAY_DOWNLOAD;
    private String url;

    /**
     * 预览方式,目前
     * @return
     */
    public int getWay() {
        return way;
    }

    public PreviewResponse setWay(int way) {
        this.way = way;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PreviewResponse setUrl(String url) {
        this.url = url;
        return this;
    }
}
