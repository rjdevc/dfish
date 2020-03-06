package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.*;

import java.util.Arrays;
import java.util.List;

/**
 * 指定范围的表单组合，实际上就是一个容器，里面可以放置begin和end两个表单
 *
 * @author DFish Team
 * @version 1.0
 * @since Period
 */
public class Range extends AbstractWidget<Range> implements LabelRow<Range>, NodeContainer {

    private static final long serialVersionUID = -4525721180514710555L;

    private Object label;
    private FormElement<?, ?> begin;
    private FormElement<?, ?> end;
    private Boolean noLabel;
    private Object to;

    /**
     * 构造函数
     *
     * @param label String 标签名
     * @param begin FormElement 范围的开始表单
     * @param end   FormElement 范围的结束表单
     */
    public Range(String label, FormElement<?, ?> begin, FormElement<?, ?> end) {
        this.setLabel(label);
        this.begin = begin;
        this.end = end;
    }
    /**
     * 构造函数
     *
     * @param label String 标签名
     * @param begin FormElement 范围的开始表单
     * @param end   FormElement 范围的结束表单
     */
    public Range(Label label, FormElement<?, ?> begin, FormElement<?, ?> end) {
        this.setLabel(label);
        this.begin = begin;
        this.end = end;
    }

    /**
     * 范围的开始表单
     *
     * @return FormElement
     */
    public FormElement<?, ?> getBegin() {
        return begin;
    }

    /**
     * 设置范围的开始表单
     *
     * @param begin FormElement
     * @return this
     */
    public Range setBegin(FormElement<?, ?> begin) {
        this.begin = begin;
        return this;
    }

    /**
     * 范围的结束表单
     *
     * @return FormElement
     */
    public FormElement<?, ?> getEnd() {
        return end;
    }

    /**
     * 设置范围的结束表单
     *
     * @param end FormElement
     * @return this
     */
    public Range setEnd(FormElement<?, ?> end) {
        this.end = end;
        return this;
    }

    @Override
    public Range setLabel(String label) {
        this.label = label;
        return this;
    }

    @Override
    public Range setLabel(Label label) {
        this.label = label;
        return this;
    }

    @Deprecated
    public Range setTitle(String label) {
        return setLabel(label);
    }

    @Override
    public Object getLabel() {
        return label;
    }

    @Override
    public Range setNoLabel(Boolean noLabel) {
        this.noLabel = noLabel;
        if (noLabel != null && label instanceof Label) {
            ((Label) label).setWidth(noLabel ? "0" : null);
        }
        return this;
    }

    @Override
    public Boolean getNoLabel() {
        return noLabel;
    }


    /**
     * 表单组合中间文本"至"
     *
     * @return the to
     */
    public Object getTo() {
        return to;
    }

    /**
     * 表单组合中间文本"至"
     *
     * @param to the to to set
     * @return this
     */
    public Range setTo(Object to) {
        this.to = to;
        return this;
    }
    protected NodeContainerDecorator getNodeContainerDecorator(){
        return new NodeContainerDecorator() {
            @Override
            protected  List<Node> nodes() {
                return Arrays.asList(begin,end) ;
            }

            @Override
            protected void setNode(int i, Node node) {
                switch (i){
                    case 0:
                        begin=(FormElement) node;
                        break;
                    case 1:
                        end=(FormElement) node;
                        break;
                    default:
                        throw new IllegalArgumentException("expect 0-begin 1-end, but get "+i);
                }
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
}
