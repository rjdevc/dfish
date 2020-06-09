package com.rongji.dfish.ui.widget;


import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.auxiliary.TimelineItem;

/**
 * 时间轴
 * @author lamontYu
 * @since DFish5.0
 */
public class Timeline extends AbstractPubNodeContainer<Timeline, TimelineItem, TimelineItem> implements Alignable<Timeline>, Scrollable<Timeline> {

    /**
     * 展示效果-正常
     */
    public static final String FACE_NORMAL = "normal";
    /**
     * 展示效果-气泡
     */
    public static final String FACE_BUBBLE = "bubble";

    /**
     * 排列方向-垂直
     */
    public static final String DIR_VERTICAL = "v";
    /**
     * 排列方向-水平
     */
    public static final String DIR_HORIZONTAL = "h";

    private String face;
    private String dir;
    private String align;
    private Boolean scroll;

    /**
     * 构造函数
     *
     * @param id String
     */
    public Timeline(String id) {
        super(id);
    }

    /**
     * 构造函数
     */
    public Timeline() {
        super(null);
    }

    @Override
    protected TimelineItem newPub() {
        return new TimelineItem(null);
    }

    /**
     * 展示效果。可选值: {@link #FACE_NORMAL}, {@link #FACE_BUBBLE}
     * @return String
     */
    public String getFace() {
        return face;
    }

    /**
     * 展示效果。可选值: {@link #FACE_NORMAL}, {@link #FACE_BUBBLE}
     * @param face String
     * @return 本身，这样可以继续设置其他属性
     */
    public Timeline setFace(String face) {
        this.face = face;
        return this;
    }

    /**
     * 排列方向。可选值: {@link #DIR_VERTICAL}, {@link #DIR_HORIZONTAL}
     * @return String
     */
    public String getDir() {
        return dir;
    }

    /**
     * 排列方向。可选值: {@link #DIR_VERTICAL}, {@link #DIR_HORIZONTAL}
     * @param dir String
     * @return 本身，这样可以继续设置其他属性
     */
    public Timeline setDir(String dir) {
        this.dir = dir;
        return this;
    }

    @Override
    public String getAlign() {
        return align;
    }

    @Override
    public Timeline setAlign(String align) {
        this.align = align;
        return this;
    }

    @Override
    public Boolean getScroll() {
        return scroll;
    }

    @Override
    public Timeline setScroll(Boolean scroll) {
        this.scroll = scroll;
        return this;
    }

}
