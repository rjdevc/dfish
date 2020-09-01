package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;

/**
 * 视频播放组件
 * @author lamontYu
 * @since 5.0
 */
public class Video extends AbstractWidget<Video> {

    private String src;
    private String poster;

    /**
     * 构造函数
     * @param src 视频播放地址
     */
    public Video(String src) {
        this.src = src;
    }

    /**
     * 构造函数
     * @param src 视频播放地址
     * @param poster 视频截取封面地址
     */
    public Video(String src, String poster) {
        this.src = src;
        this.poster = poster;
    }

    /**
     * 视频播放地址
     * @return String
     */
    public String getSrc() {
        return src;
    }

    /**
     * 视频播放地址
     * @param src String
     * @return 本身，这样可以继续设置其他属性
     */
    public Video setSrc(String src) {
        this.src = src;
        return this;
    }

    /**
     * 视频截取封面地址
     * @return String
     */
    public String getPoster() {
        return poster;
    }

    /**
     * 视频截取封面地址
     * @param poster String
     * @return 本身，这样可以继续设置其他属性
     */
    public Video setPoster(String poster) {
        this.poster = poster;
        return this;
    }
}
