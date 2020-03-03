package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.TargetHolder;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.widget.AbstractButton;

/**
 * 标签按钮
 * @author LinLW
 * @date 2019-10-16
 * @since 5.0
 */
public class Tab extends AbstractButton<Tab> implements TargetHolder<Tab> {

    private Widget<?> target;
    /**
     * 构造函数
     *
     * @param text String 标题
     */
    public Tab(String text) {
        super(text,null,null);
    }

    /**
     * 构造函数
     *
     * @param text String 标题
     * @param target Widget 目标组件
     */
    public Tab(String text, Widget target) {
        super(text,null,null);
        this.setTarget(target);
    }

    /**
     * 标签对应的内容widget
     * @return Widget
     */
    @Override
    public Widget getTarget() {
        return target;
    }

    /**
     * 标签对应的内容widget
     * @param target Widget
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Tab setTarget(Widget target) {
        this.target = target;
        return this;
    }

}