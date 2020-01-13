package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.*;

/**
 * 提示信息
 *
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 3.0
 */
public class Tip extends AbstractDialog<Tip> implements Command<Tip>, Snapable<Tip>, HasText<Tip> {

    private static final long serialVersionUID = -3534531697064109684L;

    private String text;
    private Boolean hoverDrop;

    /**
     * 构造函数
     *
     * @param text 提示文本
     */
    public Tip(String text) {
        this.text = text;
    }

    @Override
    public String getType() {
        return "Tip";
    }

    /**
     * 内容
     *
     * @return String
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * 设置内容
     *
     * @param text 内容
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Tip setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 鼠标移开时tip自动关闭
     *
     * @return Boolean
     */
    public Boolean getHoverDrop() {
        return hoverDrop;
    }

    /**
     * 设置鼠标移开时tip自动关闭
     *
     * @param hoverDrop Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Tip setHoverDrop(Boolean hoverDrop) {
        this.hoverDrop = hoverDrop;
        return this;
    }

}
