package com.rongji.dfish.ui.form;

import java.util.List;

/**
 * 上传图片组件
 *
 * @author DFish Team
 * @version 1.1 将Alignable, Valignable的接口下移到抽象类 lamontYu 2019-12-05
 */
public class ImageUpload extends AbstractUpload<ImageUpload> {

    private static final long serialVersionUID = -2689106571559435242L;

    private String thumbnail;
    private Boolean transparent;


    /**
     * 构造函数
     *
     * @param name  名称
     * @param label 标签
     * @param value 附件上传项集合
     */
    public ImageUpload(String name, String label, List<UploadItem> value) {
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
    public ImageUpload(String name, Label label, List<UploadItem> value) {
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
    public ImageUpload setThumbnail(String thumbnail) {
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
    public ImageUpload setTransparent(Boolean transparent) {
        this.transparent = transparent;
        return this;
    }
}
