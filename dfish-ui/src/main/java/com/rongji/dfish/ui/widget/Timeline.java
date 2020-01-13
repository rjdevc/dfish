package com.rongji.dfish.ui.widget;


import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.PubHolder;
import com.rongji.dfish.ui.layout.AbstractLayout;

/**
 * 时间轴
 * @author lamontYu
 * @date 2019-11-25 11:52
 */
public class Timeline extends AbstractLayout<Timeline> implements Alignable<Timeline>, PubHolder<Timeline, TimelineItem> {

    private String align;
    private TimelineItem pub;

    /**
     * 构造函数
     *
     * @param id String
     */
    public Timeline(String id) {
        super(id);
    }

    @Override
    public String getType() {
        return "timeline";
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

    private TimelineItem pub() {
        if (this.pub == null) {
            this.pub = new TimelineItem();
        }
        return this.pub;
    }

    @Override
    public TimelineItem getPub() {
        return pub();
    }

    @Override
    public Timeline setPub(TimelineItem pub) {
        this.pub = pub;
        return this;
    }
}
