package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.auxiliary.OverflowButton;

/**
 * button 的父类。 Alignable
 */
public class ButtonBar extends AbstractPubNodeContainer<ButtonBar, Widget, Button> implements Directional<ButtonBar>,
        Alignable<ButtonBar>, VAlignable<ButtonBar> {

    private static final long serialVersionUID = 5193505708325695202L;

    private String dir;
    private Boolean focusMultiple;
    private Integer space;
    private String align;
    private String vAlign;
    private Split split;
    private Boolean br;
    private Boolean scroll;
    private OverflowButton overflow;

    /**
     * 构造函数
     *
     * @param id String
     */
    public ButtonBar(String id) {
        super(id);
    }

    /**
     * 构造函数
     */
    public ButtonBar() {
        super(null);
    }

    @Override
    protected Button newPub() {
        return new Button(null);
    }

    /**
     * 当按钮过多，放不下的时候的效果
     *
     * @return Overflow
     */
    public OverflowButton getOverflow() {
        return overflow;
    }

    /**
     * 当按钮过多，放不下的时候，的效果
     *
     * @param overflow 设置当内容太多的时候不换行
     * @return this
     */
    public ButtonBar setOverflow(OverflowButton overflow) {
        this.overflow = overflow;
        return this;
    }

    /**
     * 当内容太多的时候不换行
     *
     * @return Boolean
     */
    public Boolean getBr() {
        return br;
    }

    /**
     * 设置
     *
     * @param br 设置当内容太多的时候不换行
     * @return 本身，这样可以继续设置其他属性
     */
    public ButtonBar setBr(Boolean br) {
        this.br = br;
        return this;
    }

    /**
     * 按钮排列方向。可用值: h(横向,默认),v(纵向)
     *
     * @return dir
     */
    @Override
    public String getDir() {
        return dir;
    }

    /**
     * 按钮排列方向。可用值: h(横向,默认),v(纵向)
     *
     * @param dir h/v
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public ButtonBar setDir(String dir) {
        this.dir = dir;
        return this;
    }

    /**
     * 是否有多个按钮可同时设为焦点状态。
     *
     * @return focusMultiple
     */
    public Boolean getFocusMultiple() {
        return focusMultiple;
    }

    /**
     * 是否有多个按钮可同时设为焦点状态。
     *
     * @param focusMultiple Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public ButtonBar setFocusMultiple(Boolean focusMultiple) {
        this.focusMultiple = focusMultiple;
        return this;
    }

    /**
     * 按钮之间的间隔。
     *
     * @return space
     */
    public Integer getSpace() {
        return space;
    }

    /**
     * 按钮之间的间隔。
     *
     * @param space 间距 (像素)
     * @return 本身，这样可以继续设置其他属性
     */
    public ButtonBar setSpace(Integer space) {
        this.space = space;
        return this;
    }


    /**
     * 添加分隔符，默认添加1像素的分隔符
     *
     * @return 本身，这样可以继续设置其他属性
     * @deprecated 使用 {@link #add(Node)}
     */
    @Deprecated
    public ButtonBar addSplit() {
        nodes.add(new Split().setWidth(1));
        return this;
    }

    @Override
    public String getVAlign() {
        return vAlign;
    }

    @Override
    public ButtonBar setVAlign(String vAlign) {
        this.vAlign = vAlign;
        return this;
    }

    @Override
    public String getAlign() {
        return align;
    }

    @Override
    public ButtonBar setAlign(String align) {
        this.align = align;
        return this;
    }

    /**
     * 在按钮之间默认插入的split
     *
     * @return Split
     */
    public Split getSplit() {
        return split;
    }

    /**
     * 在按钮之间默认插入的split;
     * 设置了该属性,无需调用{@link #add(Node)}来添加split,按钮间自动添加1个split
     *
     * @param split Split
     * @return this
     */
    public ButtonBar setSplit(Split split) {
        this.split = split;
        return this;
    }

    /**
     * <p>描述:是否滚动</p>
     *
     * @return Boolean
     */
    public Boolean getScroll() {
        return scroll;
    }

    /**
     * <p>描述:设置是否滚动</p>
     *
     * @param scroll Boolean
     * @return 本身，以便继续设置属性
     */
    public ButtonBar setScroll(Boolean scroll) {
        this.scroll = scroll;
        return this;
    }
}
