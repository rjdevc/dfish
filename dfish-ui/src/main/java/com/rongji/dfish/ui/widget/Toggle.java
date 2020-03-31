package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.HtmlContentHolder;

/**
 * 展开收拢的工具条
 *
 * @author DFish Team
 */
public class Toggle extends AbstractWidget<Toggle> implements HasText<Toggle>, HtmlContentHolder<Toggle> {

    private static final long serialVersionUID = 4964103955404250558L;

    /**
     * 构造函数
     */
    public Toggle() {
    }

    /**
     * 构造函数
     *
     * @param text 文本
     */
    public Toggle(String text) {
        this.setText(text);
    }

    private Boolean hr;
    private Boolean expanded;
    private String target;
    private String text;
    private String collapsedIcon;
    private String expandedIcon;
    private Boolean escape;
    private String format;
    private Object tip;

    /**
     * 是否显示一条水平线
     *
     * @return hr
     */
    public Boolean getHr() {
        return hr;
    }

    /**
     * 是否显示一条水平线
     *
     * @param hr Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Toggle setHr(Boolean hr) {
        this.hr = hr;
        return this;
    }

    /**
     * 设置初始状态为展开或收拢。如果设置了此参数，会产生一个toggle图标
     *
     * @return Boolean
     */
    public Boolean getExpanded() {
        return expanded;
    }

    /**
     * 设置初始状态为展开或收拢。如果设置了此参数，会产生一个toggle图标
     *
     * @param expanded 设置初始状态是否展开
     * @return 本身，这样可以继续设置其他属性
     */
    public Toggle setExpanded(Boolean expanded) {
        this.expanded = expanded;
        return this;
    }

    /**
     * 绑定要展开收拢的 widget ID。多个用逗号隔开。
     *
     * @return target
     */
    public String getTarget() {
        return target;
    }

    /**
     * 绑定要展开收拢的 widget ID。多个用逗号隔开。
     *
     * @param target 绑定要展开收拢的 widget ID
     * @return 本身，这样可以继续设置其他属性
     */
    public Toggle setTarget(String target) {
        this.target = target;
        return this;
    }

    /**
     * 显示文本
     *
     * @return texts
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * 显示文本
     *
     * @param text 显示文本
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Toggle setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 收缩图标
     *
     * @return String
     */
    public String getCollapsedIcon() {
        return collapsedIcon;
    }

    /**
     * 收缩图标
     *
     * @param collapsedIcon String icon
     * @return this
     */
    public Toggle setCollapsedIcon(String collapsedIcon) {
        this.collapsedIcon = collapsedIcon;
        return this;
    }

    /**
     * 展开图标
     *
     * @return String
     */
    public String getExpandedIcon() {
        return expandedIcon;
    }

    /**
     * 展开图标
     *
     * @param expandedIcon String
     * @return this
     */
    public Toggle setExpandedIcon(String expandedIcon) {
        this.expandedIcon = expandedIcon;
        return this;
    }

    @Override
    public Boolean getEscape() {
        return escape;
    }

    @Override
    public Toggle setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public Toggle setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * 提示信息。设为true，提示信息将使用 text 参数的值。
     *
     * @return Object
     */
    public Object getTip() {
        return tip;
    }

    /**
     * 提示信息。设为true，提示信息将使用 text 参数的值。
     *
     * @param tip 提示信息
     * @return 本身，这样可以继续设置其他属性
     */
    public Toggle setTip(String tip) {
        this.tip = tip;
        return this;
    }

    /**
     * 提示信息。设为true，提示信息将使用 text 参数的值。
     *
     * @param tip 设为true，提示信息将使用 text 参数的值。
     * @return 本身，这样可以继续设置其他属性
     */
    public Toggle setTip(Boolean tip) {
        this.tip = tip;
        return this;
    }
}
