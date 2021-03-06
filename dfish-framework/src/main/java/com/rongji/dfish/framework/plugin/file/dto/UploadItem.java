package com.rongji.dfish.framework.plugin.file.dto;

import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;

import java.io.Serializable;

/**
 * 附件上传数据项，用于数据交互使用。
 * 这个类的属性和dfish-ui中同名类的属性基本一致，但因为包层不能继承，导致创建了2个相近的对象，同时Java开发模式数据还需二次转换
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class UploadItem implements Serializable {

    private static final long serialVersionUID = -1888955113018055589L;
    private String id;
    private String name;
    private String extension;
    private Long size;
    private String url;
    private String thumbnail;
    private Error error;
    private String text;
    private Boolean escape;
    private String width;
    private String height;
    private Integer duration;

    private PubFileRecord fileRecord;
    /**
     * 附件编号
     *
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * 附件编号
     *
     * @param id String
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadItem setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 附件名称
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * 附件名称
     *
     * @param name String
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadItem setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 附件大小
     *
     * @return Long
     */
    public Long getSize() {
        return size;
    }

    /**
     * 附件大小
     *
     * @param size Long
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadItem setSize(Long size) {
        this.size = size;
        return this;
    }

    /**
     * 附件地址
     *
     * @return String
     */
    public String getUrl() {
        return url;
    }

    /**
     * 附件地址
     *
     * @param url String
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadItem setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 附件缩略图(一般是图片才使用这个属性)
     *
     * @return String
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * 附件缩略图(一般是图片才使用这个属性)
     *
     * @param thumbnail String
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadItem setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    /**
     * 是否有错误
     *
     * @return Boolean
     */
    public Error getError() {
        return error;
    }

    /**
     * 是否有错误
     *
     * @param error Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadItem setError(Error error) {
        this.error = error;
        return this;
    }

    /**
     * 提示文本信息
     *
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * 提示文本信息
     *
     * @param text String
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadItem setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 是否转义
     *
     * @return Boolean
     */
    public Boolean getEscape() {
        return escape;
    }

    /**
     * 是否转义
     *
     * @param escape Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadItem setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    /**
     * 宽度
     *
     * @return String
     */
    public String getWidth() {
        return width;
    }

    /**
     * 宽度
     *
     * @param width String
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadItem setWidth(String width) {
        this.width = width;
        return this;
    }

    /**
     * 高度
     *
     * @return String
     */
    public String getHeight() {
        return height;
    }

    /**
     * 高度
     *
     * @param height String
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadItem setHeight(String height) {
        this.height = height;
        return this;
    }
    /**
     * 附件扩展名
     * @return String 附件扩展名
     */
    public String getExtension() {
        return extension;
    }

    /**
     * 附件扩展名
     * @param extension String
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadItem setExtension(String extension) {
        this.extension = extension;
        return this;
    }
    /**
     * 持续时间/播放时长(单位:秒)
     * @return Number 播放时长
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * 持续时间/播放时长(单位:秒)
     * @param duration Number 播放时长
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadItem setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    /**
     * 附件上传异常
     */
    public static class Error {
        private String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * 附件记录
     * @return PubFileRecord
     */
    public PubFileRecord getFileRecord() {
        return fileRecord;
    }

    /**
     * 附件记录
     * @param fileRecord PubFileRecord
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadItem setFileRecord(PubFileRecord fileRecord) {
        this.fileRecord = fileRecord;
        return this;
    }
}
