package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.*;

import java.util.Arrays;
import java.util.List;

abstract class AbstractTD<T extends AbstractTD<T>> extends AbstractWidget<T>
        implements SingleNodeContainer<T, Widget>, Alignable<T>, VAlignable<T> ,HtmlContentHolder<T> {

    private static final long serialVersionUID = -7870476532478876521L;


    protected Integer colSpan;
    protected Integer rowSpan;
    //	private String text;
    protected String align;
    protected String vAlign;
    protected Widget node;
    protected Boolean escape;
    protected String format;
    protected Integer labelWidth;

    private Boolean br;
    /**
     * 内容不换行。
     *
     * @return Boolean
     */
    public Boolean getBr() {
        return br;
    }

    /**
     * 内容过多的时候不会换行，而是隐藏不显示
     *
     * @param br Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setBr(Boolean br) {
        this.br = br;
        return (T)this;
    }


    /**
     * 这个这个单元格占几列。
     * 为空的时候相当于1
     *
     * @return Integer
     */
    public Integer getColSpan() {
        return colSpan;
    }

    /**
     * 这个这个单元格占几列。
     * 为空的时候相当于1
     *
     * @param colSpan Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public T setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
        return (T) this;
    }

    /**
     * 这个这个单元格占几行。
     * 为空的时候相当于1
     *
     * @return Integer
     */
    public Integer getRowSpan() {
        return rowSpan;
    }

    /**
     * 这个这个单元格占几行。
     * 为空的时候相当于1
     *
     * @param rowSpan Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public T setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
        return (T) this;
    }


    @Override
    public String getVAlign() {
        return vAlign;
    }

    @Override
    public T setVAlign(String vAlign) {
        this.vAlign = vAlign;
        return (T) this;
    }

    @Override
    public String getAlign() {
        return align;
    }

    @Override
    public T setAlign(String align) {
        this.align = align;
        return (T) this;
    }

    /**
     * 拷贝属性
     *
     * @param to   AbstractTd
     * @param from AbstractTd
     */
    protected void copyProperties(AbstractTD<?> to, AbstractTD<?> from) {
        super.copyProperties(to, from);
        to.node = from.node;
        to.align = from.align;
        to.colSpan = from.colSpan;
        to.rowSpan = from.rowSpan;
        to.vAlign = from.vAlign;
        to.labelWidth = from.labelWidth;
    }

    /**
     * 用于显示文本是否需要转义,不设置默认是true
     *
     * @return Boolean
     */
    public Boolean getEscape() {
        return escape;
    }

    /**
     * 用于显示文本是否需要转义,不设置默认是true
     *
     * @param escape Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setEscape(Boolean escape) {
        this.escape = escape;
        return (T) this;
    }

    /**
     * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
     *
     * @return String 格式化内容
     */
    public String getFormat() {
        return format;
    }

    /**
     * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
     *
     * @param format String 格式化内容
     * @return 本身，这样可以继续设置其他属性
     */
    public T setFormat(String format) {
        this.format = format;
        return (T) this;
    }

    /**
     * 表单标题宽度。
     *
     * @return Integer
     * @since 3.3
     */
    public Integer getLabelWidth() {
        return labelWidth;
    }

    /**
     * 表单标题宽度。
     *
     * @param labelWidth Integer
     * @return 本身，这样可以继续设置其他属性
     * @since 3.3
     */
    public T setLabelWidth(Integer labelWidth) {
        this.labelWidth = labelWidth;
        return (T) this;
    }

    /**
     * 获得装饰器
     * @see com.rongji.dfish.ui.NodeContainerDecorator
     * @return NodeContainerDecorator
     */
    protected NodeContainerDecorator getNodeContainerDecorator() {
        return new NodeContainerDecorator() {
            @Override
            protected List<Node> nodes() {
                return Arrays.asList(AbstractTD.this.node);
            }

            @Override
            protected void setNode(int i, Node node) {
                assert (i == 0);
                AbstractTD.this.setNode((Widget) node);
            }
        };
    }

    @Override
    public Node findNode(Filter filter) {
        return getNodeContainerDecorator().findNode(filter);
    }

    @Override
    public List<Node> findAllNodes(Filter filter) {
        return getNodeContainerDecorator().findAllNodes(filter);
    }

    @Override
    public Node replaceNode(Filter filter, Node node) {
        return getNodeContainerDecorator().replaceNode(filter, node);
    }

    @Override
    public int replaceAllNodes(Filter filter, Node node) {
        return getNodeContainerDecorator().replaceAllNodes(filter, node);
    }

    @Override
    public T setNode(Widget node) {
        this.node = node;
        return (T) this;
    }

    @Override
    public Widget getNode() {
        return node;
    }
}
