package com.rongji.dfish.ui.form;

/**
 * 上传图片组件
 *
 * @author DFish Team
 * @date 2018-08-03 before
 * @version 1.1 将Alignable, Valignable的接口下移到抽象类 lamontYu 2019-12-05
 */
public class UploadImage extends AbstractUpload<UploadImage> {
    /**
     *
     */
    private static final long serialVersionUID = -2689106571559435242L;
    private String thumbnail;

    /**
     * @param name
     * @param label
     */
    public UploadImage(String name, String label) {
        this.name = name;
        setLabel(label);
    }

    @Override
    public String getType() {
        return "upload/image";
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
    public UploadImage setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    /**
     * 缩略图地址
     *
     * @return String
     * @see #getThumbnail()
     */
    @Deprecated
    public String getThumbnailsrc() {
        return getThumbnail();
    }

    /**
     * 缩略图地址
     *
     * @param thumbnailsrc 缩略图地址
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadImage setThumbnailsrc(String thumbnailsrc) {
        return setThumbnail(thumbnailsrc);
    }
}
