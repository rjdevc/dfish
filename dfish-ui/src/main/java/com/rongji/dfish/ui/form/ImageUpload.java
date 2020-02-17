package com.rongji.dfish.ui.form;

import java.util.List;

/**
 * 上传图片组件
 *
 * @author DFish Team
 * @date 2018-08-03 before
 * @version 1.1 将Alignable, Valignable的接口下移到抽象类 lamontYu 2019-12-05
 */
public class ImageUpload extends AbstractUpload<ImageUpload> {

    private static final long serialVersionUID = -2689106571559435242L;

    private String thumbnail;

    /**
     * @param name
     * @param label
     */
    public ImageUpload(String name, String label) {
        this.name = name;
        setLabel(label);
    }

    /**
     * @param name
     * @param label
     * @param value
     */
    public ImageUpload(String name, String label, List<UploadItem> value) {
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

}