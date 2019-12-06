package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.Valignable;

/**
 * 上传图片组件
 *
 * @author DFish Team
 * @create 2018-08-03 before
 * @version 1.1 去掉泛型设置,同时将Alignable, Valignable的接口下移到抽象类 lamontYu 2019-12-05
 */
public class UploadImage extends AbstractUpload<UploadImage> {
    /**
     *
     */
    private static final long serialVersionUID = -2689106571559435242L;
    private String thumbnailsrc;

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
    public String getThumbnailsrc() {
        return this.thumbnailsrc;
    }

    /**
     * 缩略图地址
     *
     * @param thumbnailsrc 缩略图地址
     * @return 本身，这样可以继续设置其他属性
     */
    public UploadImage setThumbnailsrc(String thumbnailsrc) {
        this.thumbnailsrc = thumbnailsrc;
        return this;
    }
}
