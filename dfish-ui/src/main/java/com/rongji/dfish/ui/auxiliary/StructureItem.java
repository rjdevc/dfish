package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.command.Tip;

import java.util.List;
import java.util.function.Predicate;

/**
 * 结构树的节点。
 *
 * @author lamontYu
 * @since 5.0
 */
public class StructureItem extends AbstractWidget<StructureItem> implements MultiNodeContainer<StructureItem, StructureItem>,
        Alignable<StructureItem>, VAlignable<StructureItem> {

    private String text;
    private Boolean escape;
    private Object tip;
    private Boolean br;
    private String format;
    private String align;
    private String vAlign;
    private List<StructureItem> nodes;

    /**
     * 构造函数
     *
     * @param text 显示文本
     */
    public StructureItem(String text) {
        this.text = text;
    }

    /**
     * 显示文本
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * 显示文本
     * @param text String
     * @return 本身，这样可以继续设置其他属性
     */
    public StructureItem setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 是否对html内容转义
     * @return Boolean
     */
    public Boolean getEscape() {
        return escape;
    }

    /**
     * 是否对html内容转义
     * @param escape Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public StructureItem setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    /**
     * 鼠标悬停的时候的提示文本
     * @return 鼠标悬停的时候的提示文本
     */
    public Object getTip() {
        return tip;
    }

    /**
     * 鼠标悬停的时候的提示文本
     * @param tip Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public StructureItem setTip(Boolean tip) {
        this.tip = tip;
        return this;
    }

    /**
     * 鼠标悬停的时候的提示文本
     * @param tip String
     * @return 本身，这样可以继续设置其他属性
     */
    public StructureItem setTip(String tip) {
        this.tip = tip;
        return this;
    }

    /**
     * 鼠标悬停的时候的提示文本
     * @param tip Tip
     * @return 本身，这样可以继续设置其他属性
     */
    public StructureItem setTip(Tip tip) {
        this.tip = tip;
        return this;
    }

    /**
     * 文本是否换行
     * @return Boolean
     */
    public Boolean getBr() {
        return br;
    }

    /**
     * 文本是否换行
     * @param br Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public StructureItem setBr(Boolean br) {
        this.br = br;
        return this;
    }

    /**
     * 格式化内容。支持"javascript:"开头的JS语句(需return返回值)。
     * @return String
     */
    public String getFormat() {
        return format;
    }

    /**
     * 格式化内容。支持"javascript:"开头的JS语句(需return返回值)。
     * @param format String
     * @return 本身，这样可以继续设置其他属性
     */
    public StructureItem setFormat(String format) {
        this.format = format;
        return this;
    }

    @Override
    public String getAlign() {
        return align;
    }

    @Override
    public StructureItem setAlign(String align) {
        this.align = align;
        return this;
    }

    @Override
    public String getVAlign() {
        return vAlign;
    }

    @Override
    public StructureItem setVAlign(String vAlign) {
        this.vAlign = vAlign;
        return this;
    }

    @Override
    public List<StructureItem> getNodes() {
        return nodes;
    }

    @Override
    public StructureItem setNodes(List<StructureItem> nodes) {
        this.nodes = nodes;
        return this;
    }

    /**
     * 获得装饰器
     *
     * @return NodeContainerDecorator
     * @see com.rongji.dfish.ui.NodeContainerDecorator
     */
    protected NodeContainerDecorator getNodeContainerDecorator() {
        return new NodeContainerDecorator() {
            @Override
            protected List<Node> nodes() {
                return (List) nodes;
            }

            @Override
            protected void setNode(int i, Node node) {
                if (node == null) {
                    nodes.remove(i);
                } else {
                    nodes.set(i, (StructureItem) node);
                }
            }
        };
    }

    @Override
    public Node findNode(Predicate<Node> filter) {
        return getNodeContainerDecorator().findNode(filter);
    }

    @Override
    public List<Node> findAllNodes(Predicate<Node> filter) {
        return getNodeContainerDecorator().findAllNodes(filter);
    }

    @Override
    public Node replaceNode(Predicate<Node> filter, Node node) {
        return getNodeContainerDecorator().replaceNode(filter, node);
    }

    @Override
    public int replaceAllNodes(Predicate<Node> filter, Node node) {
        return getNodeContainerDecorator().replaceAllNodes(filter, node);
    }

}
