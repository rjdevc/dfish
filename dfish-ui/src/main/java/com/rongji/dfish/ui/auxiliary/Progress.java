package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;

/**
 * 进度项,用于展示进度情况
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class Progress extends AbstractWidget<Progress> implements HasText<Progress> {

    private Number percent;
    private String text;
    private Boolean escape;
    private String format;
    private String range;

    public Progress(String id) {
        setId(id);
    }

    public Progress(String id, Number percent) {
        setId(id);
        setPercent(percent);
    }

    /**
     * 进度值。范围从 0 到 100。
     *
     * @return Number
     */
    public Number getPercent() {
        return percent;
    }

    /**
     * 进度值。范围从 0 到 100。
     *
     * @param percent Number
     * @return 本身，这样可以继续设置其他属性
     */
    public Progress setPercent(Number percent) {
        this.percent = percent;
        return this;
    }

    /**
     * 显示文本。
     *
     * @return String
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * 显示文本。
     *
     * @param text String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Progress setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 是否对html内容转义。默认值为true。
     *
     * @return Boolean
     */
    @Override
    public Boolean getEscape() {
        return escape;
    }

    /**
     * 是否对html内容转义。默认值为true。
     *
     * @param escape Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Progress setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public Progress setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * 划分进度阶段的数值，用逗号隔开。每个数字都会生成该阶段的样式 "z-数值"，数值范围从 0 到 100。
     * 例如设置 range: "60,100"，那么进度在 (&gt;=60 &amp;&amp; &lt;100) 范围内会存在样式 "z-60"，进度在 100 时会存在样式 "z-100"。
     * @return String
     */
    public String getRange() {
        return range;
    }

    /**
     * 划分进度阶段的数值，用逗号隔开。每个数字都会生成该阶段的样式 "z-数值"，数值范围从 0 到 100。
     * 例如设置 range: "60,100"，那么进度在 (&gt;=60 &amp;&amp; &lt;100) 范围内会存在样式 "z-60"，进度在 100 时会存在样式 "z-100"。
     * @param range String
     * @return 本身，这样可以继续设置其他属性
     */
    public Progress setRange(String range) {
        this.range = range;
        return this;
    }
}
