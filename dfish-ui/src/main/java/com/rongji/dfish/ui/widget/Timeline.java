package com.rongji.dfish.ui.widget;


import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.auxiliary.TimelineItem;

/**
 * 时间轴
 * @author lamontYu
 * @since DFish5.0
 */
public class Timeline extends AbstractPubNodeContainer<Timeline, TimelineItem, TimelineItem> implements Alignable<Timeline>, Scrollable<Timeline> {

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
