package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.SingleContainer;
import com.rongji.dfish.ui.Widget;

import java.util.Arrays;
import java.util.List;

/**
 * 标签按钮
 */
public class Tab extends AbstractWidget<Tab>  {
    private String text;
    private Widget<?> target;
    private Boolean focus;
    private String status;

    /**
     * 状态-正常
     */
    public static final String STATUS_NORMAL = "normal";
    /**
     * 状态-禁用
     */
    public static final String STATUS_DISABLED = "disabled";

    public Tab(String text, Widget<?>target){
        this.text=text;
        this.target=target;
    }


    @Override
    public String getType() {
        return "tab";
    }

    /**
     * 标签页对应的面板或元素
     * @param target Widget
     * @return this
     */
    public Tab setTarget(Widget<?> target) {
        this.target=target;
        return this;
    }

    /**
     * 标签页对应的面板或元素
     * @return Widget
     */
    public Widget<?> getTarget() {
        return target;
    }

    /**
     * 标签页文本
     * @param text String
     * @return this
     */
    public Tab setText(String text) {
        this.text=text;
        return this;
    }

    /**
     * 标签页文本
     * @return String
     */
    public String getText() {
        return text;
    }

    @Override
    public List<Widget<?>> findNodes() {
        return Arrays.asList(target);
    }

    /**
     * 设置按钮状态
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     * 按钮状态
     * @param status String
     * @return 本身，这样可以继续设置其他属性
     */
    public Tab setStatus(String status) {
        this.status = status;
        return this;
    }
    /**
     * 是否焦点模式
     *
     * @return 是否焦点模式
     */
    public Boolean getFocus() {
        return focus;
    }

    /**
     * 是否焦点模式
     *
     * @param focus Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Tab setFocus(Boolean focus) {
        this.focus = focus;
        return (Tab) this;
    }
}
