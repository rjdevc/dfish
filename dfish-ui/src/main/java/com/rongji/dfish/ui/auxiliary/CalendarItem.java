package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.AbstractWidget;

/**
 * Calendar里面每个元素按钮的默认值
 *
 * @author DFish Team
 */
public class CalendarItem extends AbstractWidget<CalendarItem> {

    private static final long serialVersionUID = 6176298189403402501L;

    private String value;
    private String text;
    private Boolean focus;
    private Boolean focusable;

    /**
     * 构造函数
     * @param value 日期
     */
    public CalendarItem(String value) {
        this.setValue(value);
    }

    /**
     * 构造函数
     * @param value 日期
     * @param text 显示内容
     */
    public CalendarItem(String value, String text) {
        this.setValue(value);
        this.setText(text);
    }

    /**
     * 日期
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置日期
     * @param value
     * @return 本身，这样可以继续设置其他属性
     */
    public CalendarItem setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * 显示内容
     *
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * 显示内容
     *
     * @param text String
     * @return 本身，这样可以继续设置其他属性
     */
    public CalendarItem setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 是否焦点
     *
     * @return Boolean
     */
    public Boolean getFocus() {
        return focus;
    }

    /**
     * 是否聚焦
     *
     * @param focus Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public CalendarItem setFocus(Boolean focus) {
        this.focus = focus;
        return this;
    }

    /**
     * 是否可聚焦模式
     *
     * @return
     */
    public Boolean getFocusable() {
        return focusable;
    }

    /**
     * 是否可聚焦模式
     *
     * @param focusable Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public CalendarItem setFocusable(Boolean focusable) {
        this.focusable = focusable;
        return this;
    }

}
