package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;

/**
 * 徽标数
 * @author lamontYu
 * @create 2019-11-25 12:36
 */
public class Badge extends AbstractWidget<Badge> {

    private String text;

    /**
     * 显示文本,不设置文本时显示圆点
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * 显示文本,不设置文本时显示圆点
     * @param text 显示文本
     * @return 本身，这样可以继续设置其他属性
     */
    public Badge setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String getType() {
        return "badge";
    }
}
