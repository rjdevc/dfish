package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.widget.AbstractButton;

/**
 * 标签按钮
 */
public class Tab extends AbstractButton<Tab> {

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
     * @param icon String 图标
     * @param text String 标题
     */
    public Tab(String icon, String text) {
        this.setIcon(icon);
        this.setText(text);
    }

    @Override
    public String getType() {
        return "tab";
    }
}
