package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.*;

/**
 * 表单标签。
 */
public class Label extends AbstractWidget<Label> implements HtmlContentHolder<Label>, Alignable<Label>, HasText<Label> {

    private static final long serialVersionUID = -1384522916094820984L;

    protected String align;
    protected String text;
    protected Boolean escape;
    protected String format;
    protected String suffix;

    public Label(String text){
        this.text=text;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Label setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String getAlign() {
        return align;
    }

    @Override
    public Label setAlign(String align) {
        this.align = align;
        return this;
    }

    /**
     * 用于显示文本是否需要转义,不设置默认是true
     *
     * @return Boolean
     */
    @Override
    public Boolean getEscape() {
        return this.escape;
    }

    /**
     * 用于显示文本是否需要转义,不设置默认是true
     *
     * @param escape Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Label setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    /**
     * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
     *
     * @return String 格式化内容
     */
    @Override
    public String getFormat() {
        return format;
    }

    /**
     * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
     *
     * @param format String 格式化内容
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Label setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * 后缀
     * @return String
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * 后缀
     * @param suffix String
     * @return 本身，这样可以继续设置其他属性
     */
    public Label setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

}
