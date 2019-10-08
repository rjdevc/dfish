package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasFormat;

/**
 * 进度项,用于展示进度情况
 *
 * @author lamontYu
 * @create 2019-09-30 14:31
 * @since 3.3
 */
public class ProgressItem extends AbstractWidget<ProgressItem> implements HasFormat<ProgressItem> {

    private Number percent;
    private Boolean hidepercent;
    private String text;
    private Boolean escape;
    private String format;

    public ProgressItem(String id) {
        setId(id);
    }

    public ProgressItem(String id, Number percent) {
        setId(id);
        setPercent(percent);
    }

    @Override
    public String getType() {
        return "progress/item";
    }

    /**
     * 进度值。范围从 0 到 100。
     * @return Number
     */
    public Number getPercent() {
        return percent;
    }

    /**
     * 进度值。范围从 0 到 100。
     * @param percent Number
     * @return 本身，这样可以继续设置其他属性
     */
    public ProgressItem setPercent(Number percent) {
        this.percent = percent;
        return this;
    }

    /**
     * 是否隐藏进度数字。
     * @return Boolean
     */
    public Boolean getHidepercent() {
        return hidepercent;
    }

    /**
     * 是否隐藏进度数字。
     * @param hidepercent Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public ProgressItem setHidepercent(Boolean hidepercent) {
        this.hidepercent = hidepercent;
        return this;
    }

    /**
     * 显示文本。
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * 显示文本。
     * @param text String
     * @return 本身，这样可以继续设置其他属性
     */
    public ProgressItem setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 是否对html内容转义。默认值为true。
     * @return Boolean
     */
    public Boolean getEscape() {
        return escape;
    }

    /**
     * 是否对html内容转义。默认值为true。
     * @param escape Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public ProgressItem setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }
    public String getFormat() {
        return format;
    }

    public ProgressItem setFormat(String format) {
        this.format = format;
        return this;
    }
}
