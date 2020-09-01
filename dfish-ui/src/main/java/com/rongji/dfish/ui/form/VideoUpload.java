package com.rongji.dfish.ui.form;

import java.util.List;

/**
 * 上传视频组件
 *
 * @author DFish Team
 * @since 5.0
 */
public class VideoUpload extends AbstractUpload<VideoUpload> {

    private static final long serialVersionUID = 7749393240382257770L;
    private String thumbnail;
    private Boolean transparent;

    /**
     * 构造函数
     *
     * @param name  名称
     * @param label 标签
     * @param value 附件上传项集合
     */
    public VideoUpload(String name, String label, List<UploadItem> value) {
        this.name = name;
        setLabel(label);
        this.setValue(value);
    }

    /**
     * 构造函数
     *
     * @param name  名称
     * @param label 标签
     * @param value 附件上传项集合
     */
    public VideoUpload(String name, Label label, List<UploadItem> value) {
        this.name = name;
        setLabel(label);
        this.setValue(value);
    }

    /**
     * 缩略图地址
     *
     * @return String
     */
    public String getThumbnail() {
        return this.thumbnail;
    }

    /**
     * 缩略图地址
     *
     * @param thumbnail 缩略图地址
     * @return 本身，这样可以继续设置其他属性
     */
    public VideoUpload setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    /**
     * 设置为true，表单将成为无边框无背景的状态。
     *
     * @return Boolean
     */
    public Boolean getTransparent() {
        return transparent;
    }

    /**
     * 设置为true，表单将成为无边框无背景的状态。
     *
     * @param transparent Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public VideoUpload setTransparent(Boolean transparent) {
        this.transparent = transparent;
        return this;
    }
}
