package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.TargetHolder;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.widget.AbstractButton;

/**
 * 折叠按钮
 */
public class CollapseButton extends AbstractButton<CollapseButton> implements TargetHolder<CollapseButton> {

    private Widget target;
    private String collapsedIcon;
    private String expandedIcon;

    /**
     * 构造函数
     *
     * @param text String 标题
     */
    public CollapseButton(String text) {
        super(text,null,null);
    }

    /**
     * 构造函数
     *
     * @param text String 标题
     * @param target Widget 目标组件
     */
    public CollapseButton(String text, Widget target) {
        super(text,null,null);
        this.setTarget(target);
    }

    /**
     * 折叠按钮对应的内容widget
     * @return Widget
     */
    @Override
    public Widget getTarget() {
        return target;
    }

    /**
     * 折叠按钮对应的内容widget
     * @param target Widget
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public CollapseButton setTarget(Widget target) {
        this.target = target;
        return this;
    }

    public String getCollapsedIcon() {
        return collapsedIcon;
    }

    public CollapseButton setCollapsedIcon(String collapsedIcon) {
        this.collapsedIcon = collapsedIcon;
        return this;
    }

    public String getExpandedIcon() {
        return expandedIcon;
    }

    public CollapseButton setExpandedIcon(String expandedIcon) {
        this.expandedIcon = expandedIcon;
        return this;
    }
}
