package com.rongji.dfish.ui.plugin.carousel;

import java.util.List;

import com.rongji.dfish.ui.AbstractWidget;


/**
 * 幻灯片插件
 *
 * @author lamontYu
 * @since DFish3.1
 */
public class Carousel extends AbstractWidget<Carousel> {

    private static final long serialVersionUID = -8435402044894707490L;

    /**
     * 构造方法
     */
    public Carousel() {
    }

    /**
     * 构造方法
     *
     * @param id 编号
     */
    public Carousel(String id) {
        super.setId(id);
    }

    private Integer thumbWidth;
    private Integer thumbHeight;
    private String bigWidth;
    private String bigHeight;
    private List<Option> value;

    /**
     * <p>描述:缩略图宽度</p>
     *
     * @return Integer 缩率宽度
     */
    public Integer getThumbWidth() {
        return thumbWidth;
    }

    /**
     * <p>描述:设置缩略图宽度</p>
     *
     * @param thumbWidth 缩略图宽度
     * @return 本身，这样可以继续设置其他属性
     */
    public Carousel setThumbWidth(Integer thumbWidth) {
        this.thumbWidth = thumbWidth;
        return this;
    }

    /**
     * <p>描述:缩略图高度</p>
     *
     * @return 缩略图高度
     */
    public Integer getThumbHeight() {
        return thumbHeight;
    }

    /**
     * <p>描述:设置缩略图高度</p>
     *
     * @param thumbHeight 缩略图高度
     * @return 本身，这样可以继续设置其他属性
     */
    public Carousel setThumbHeight(Integer thumbHeight) {
        this.thumbHeight = thumbHeight;
        return this;
    }

    /**
     * <p>描述:大图宽度</p>
     *
     * @return 大图宽度
     */
    public String getBigWidth() {
        return bigWidth;
    }

    /**
     * <p>描述:设置大图宽度</p>
     *
     * @param bigWidth 大图宽度
     * @return 本身，这样可以继续设置其他属性
     */
    public Carousel setBigWidth(Integer bigWidth) {
        this.bigWidth = String.valueOf(bigWidth);
        return this;
    }

    /**
     * <p>描述:大图高度</p>
     *
     * @return 大图高度
     */
    public String getBigHeight() {
        return bigHeight;
    }

    /**
     * <p>描述:设置大图高度</p>
     *
     * @param bigHeight 大图高度
     * @return 本身，这样可以继续设置其他属性
     */
    public Carousel setBigHeight(Integer bigHeight) {
        this.bigHeight = String.valueOf(bigHeight);
        return this;
    }

    /**
     * <p>描述:设置大图宽度</p>
     *
     * @param bigWidth 大图宽度
     * @return 本身，这样可以继续设置其他属性
     */
    public Carousel setBigWidth(String bigWidth) {
        this.bigWidth = bigWidth;
        return this;
    }


    /**
     * <p>描述:设置大图高度</p>
     *
     * @param bigHeight 大图高度
     * @return 本身，这样可以继续设置其他属性
     */
    public Carousel setBigHeight(String bigHeight) {
        this.bigHeight = bigHeight;
        return this;
    }

    /**
     * <p>描述:选项值</p>
     *
     * @return 选项列表
     */
    public List<Option> getValue() {
        return value;
    }

    /**
     * <p>描述:设置选项值</p>
     *
     * @param value 选项集合
     * @return 本身，这样可以继续设置其他属性
     */
    public Carousel setValue(List<Option> value) {
        this.value = value;
        return this;
    }

    /**
     * 幻灯片插件中的选项
     */
    public static class Option {

        private String url;
        private String thumbnail;
        private String text;
        private String href;

        /**
         * <p>描述:图片链接</p>
         *
         * @return String 图片链接地址
         */
        public String getUrl() {
            return url;
        }

        /**
         * <p>描述:设置图片链接</p>
         *
         * @param url 图片地址
         * @return 本身，这样可以继续设置其他属性
         */
        public Option setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * <p>描述:缩略图</p>
         *
         * @return String 缩略图
         */
        public String getThumbnail() {
            return thumbnail;
        }

        /**
         * 设置缩略图,默认图片链接
         * <p>描述:</p>
         *
         * @param thumbnail 缩略图
         * @return 本身，这样可以继续设置其他属性
         */
        public Option setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        /**
         * <p>描述:图片文本</p>
         *
         * @return String 图片文本
         */
        public String getText() {
            return text;
        }

        /**
         * <p>描述:设置图片文本</p>
         *
         * @param text 图片文本
         * @return 本身，这样可以继续设置其他属性
         */
        public Option setText(String text) {
            this.text = text;
            return this;
        }

        /**
         * <p>描述:图片链接动作</p>
         *
         * @return String 图片链接动作
         */
        public String getHref() {
            return href;
        }

        /**
         * <p>描述:图片链接动作</p>
         *
         * @param href 图片链接动作
         * @return 本身，这样可以继续设置其他属性
         */
        public Option setHref(String href) {
            this.href = href;
            return this;
        }

    }

}
