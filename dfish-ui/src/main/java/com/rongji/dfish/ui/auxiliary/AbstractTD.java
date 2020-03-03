package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.*;

import java.util.Arrays;
import java.util.List;

/**
 * Td 表示一个Table的单元格
 * <p>在一些复杂布局结构中，它可以占不止一行或不止一列</p>
 * <p>TableCell 有两种工作模式，他内部可以包含一个Widget或简单的包含一个文本，如果包含了widget文本模式将失效</p>
 * <p>虽然TableCell也是一个Widget，但其很可能并不会专门设置ID。虽然它是一个Layout，但它最多包含1个子节点。即其内容。</p>
 *
 * @param <T> 本身类型
 * @author DFish Team
 * @see TD
 */
public abstract class AbstractTD<T extends AbstractTD<T>> extends AbstractWidget<T>
        implements SingleNodeContainer<T, Widget>, Alignable<T>, VAlignable<T> {

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

    @Override
    public String getType() {
        return null;
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
//            if (colSpan != null) {
//                if (colSpan < 1) {
//                    throw new IllegalArgumentException("colspan must greater than 1");
//                }
//                if (colSpan == 1) {
//                    colSpan = null;
//                }
//            }
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
//            if (rowSpan != null) {
//                if (rowSpan < 1) {
//                    throw new IllegalArgumentException("rowspan must greater than 1");
//                }
//                if (rowSpan == 1) {
//                    rowSpan = null;
//                }
//            }
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

    protected NodeContainerDecorator getNodeContainerDecorator(){
        return new NodeContainerDecorator() {
            @Override
            protected List<Node> nodes() {
                return Arrays.asList(AbstractTD.this.node) ;
            }

            @Override
            protected void setNode(int i, Node node) {
                assert(i==0);
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
        return getNodeContainerDecorator().replaceNode(filter,node);
    }

    @Override
    public int replaceAllNodes(Filter filter, Node node) {
        return getNodeContainerDecorator().replaceAllNodes(filter,node);
    }
    @Override
    public T setNode(Widget node){
        this.node=(Widget)node;
        return (T)this;
    }
    @Override
    public Widget getNode(){
        return node;
    }
}
