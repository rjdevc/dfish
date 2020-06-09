package com.rongji.dfish.ui.widget;


import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.MultiContainer;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.auxiliary.TimelineItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间轴
 *
 * @author lamontYu
 * @since DFish5.0 因项目需要,不方便升级框架,由5.0搬过来
 */
public class Timeline extends AbstractWidget<Timeline> implements MultiContainer<Timeline, TimelineItem>, Alignable<Timeline>, Scrollable<Timeline> {

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
    private TimelineItem pub;
    private List<TimelineItem> nodes = new ArrayList<>();

    /**
     * 构造函数
     *
     * @param id String
     */
    public Timeline(String id) {
        super();
        setId(id);
    }

    /**
     * 构造函数
     */
    public Timeline() {
        super();
    }

    /**
     * 展示效果。可选值: {@link #FACE_NORMAL}, {@link #FACE_BUBBLE}
     *
     * @return String
     */
    public String getFace() {
        return face;
    }

    /**
     * 展示效果。可选值: {@link #FACE_NORMAL}, {@link #FACE_BUBBLE}
     *
     * @param face String
     * @return 本身，这样可以继续设置其他属性
     */
    public Timeline setFace(String face) {
        this.face = face;
        return this;
    }

    /**
     * 排列方向。可选值: {@link #DIR_VERTICAL}, {@link #DIR_HORIZONTAL}
     *
     * @return String
     */
    public String getDir() {
        return dir;
    }

    /**
     * 排列方向。可选值: {@link #DIR_VERTICAL}, {@link #DIR_HORIZONTAL}
     *
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
    public Timeline setScrollClass(String scrollClass) {
        return null;
    }

    @Override
    public String getScrollClass() {
        return null;
    }

    @Override
    public Timeline setScroll(Boolean scroll) {
        this.scroll = scroll;
        return this;
    }

    @Override
    public List getNodes() {
        return nodes;
    }

    @Override
    public List findNodes() {
        return nodes;
    }

    /**
     * 添加子面板
     *
     * @param w N
     * @return 本身，这样可以继续设置其他属性
     */
    public Timeline add(TimelineItem w) {
        if (w == null) {
            return this;
        }
        nodes.add(w);
        return this;
    }

    /**
     * 在指定的位置添加子面板
     *
     * @param index 位置
     * @param w     N
     * @return 本身，这样可以继续设置其他属性
     */
    public Timeline add(int index, TimelineItem w) {
        if (w == null) {
            return this;
        }
        if (index < 0) {
            nodes.add(w);
        } else {
            nodes.add(index, w);
        }
        return this;
    }

    @Override
    public String getType() {
        return "Timeline";
    }
}
