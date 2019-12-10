package com.rongji.dfish.framework.plugin.file.dto;

import java.io.Serializable;

/**
 * @author lamontYu
 * @create 2019-12-09
 */
public class UploadItem implements Serializable {

    private static final long serialVersionUID = -1888955113018055589L;
    private String id;
    private String name;
    private Long size;
    private String url;
    private String thumbnail;
    private Boolean error;
    private String text;
    private Boolean escape;
    private String width;
    private String height;

    public String getId() {
        return id;
    }

    public UploadItem setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UploadItem setName(String name) {
        this.name = name;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public UploadItem setSize(Long size) {
        this.size = size;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public UploadItem setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public UploadItem setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public Boolean getError() {
        return error;
    }

    public UploadItem setError(Boolean error) {
        this.error = error;
        return this;
    }

    public String getText() {
        return text;
    }

    public UploadItem setText(String text) {
        this.text = text;
        return this;
    }

    public Boolean getEscape() {
        return escape;
    }

    public UploadItem setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    public String getWidth() {
        return width;
    }

    public UploadItem setWidth(String width) {
        this.width = width;
        return this;
    }

    public String getHeight() {
        return height;
    }

    public UploadItem setHeight(String height) {
        this.height = height;
        return this;
    }
}
