package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.widget.AbstractButton;

/**
 * 标签按钮
 */
public class Tab extends AbstractButton<Tab> {

    private Widget<?> target;
    /**
     * 构造函数
     *
     * @param text String 标题
     */
    public Tab(String text) {
        this.setText(text);
    }

    /**
     * 构造函数
     *
     * @param text String 标题
     * @param target Widget&lt;?&gt; 目标组件
     */
    public Tab(String text, Widget<?> target) {
        this.setText(text);
        this.setTarget(target);
    }

    /**
     * 标签对应的内容widget
     * @return Widget&lt;?&gt;
     */
    public Widget<?> getTarget() {
        return target;
    }

    /**
     * 标签对应的内容widget
     * @param target Widget&lt;?&gt;
     * @return 本身，这样可以继续设置其他属性
     */
    public Tab setTarget(Widget<?> target) {
        this.target = target;
        return this;
    }

    @Override
    public String getType() {
        return "tab";
    }
}
