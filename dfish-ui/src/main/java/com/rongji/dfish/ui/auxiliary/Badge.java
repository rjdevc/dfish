package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HtmlContentHolder;

/**
 * 徽标数
 * @author lamontYu
 * @since DFish5.0
 */
public class Badge extends AbstractWidget<Badge> implements HtmlContentHolder<Badge> {

    private String text;
    private Boolean escape;
    private String format;

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
    public Boolean getEscape() {
        return escape;
    }

    @Override
    public Badge setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    /**
     * 格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
     * @return String
     */
    public String getFormat() {
        return format;
    }

    /**
     * 格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
     * @param format String
     * @return 本身，这样可以继续设置其他属性
     */
    public Badge setFormat(String format) {
        this.format = format;
        return this;
    }
}
