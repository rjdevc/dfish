package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.UiNode;
import com.rongji.dfish.ui.MultiContainer;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.layout.AbstractContainer;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * 指定范围的表单组合，实际上就是一个容器，里面可以放置begin和end两个表单
 *
 * @author DFish Team
 * @version 1.0
 * @since Period
 */
public class Range extends AbstractContainer<Range> implements LabelRow<Range>, MultiContainer<Range, FormElement<?, ?>> {

    private static final long serialVersionUID = -4525721180514710555L;

    private Label label;
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
        super(null);
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
        this.label = new Label(label);
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
    public Label getLabel() {
        return label;
    }

    @Override
    public Range setNoLabel(Boolean noLabel) {
        this.noLabel = noLabel;
        if (noLabel != null && label != null) {
            label.setWidth(noLabel ? "0" : null);
        }
        return this;
    }

    @Override
    public Boolean getNoLabel() {
        return noLabel;
    }

    @Transient
    @Override
    public List<FormElement<?, ?>> getNodes() {
        return (List)nodes;
    }

    @Override
    @Deprecated
    public Range add(UiNode node) {
        throw new UnsupportedOperationException("use setBegin / setEnd instead");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UiNode<?>> findNodes() {
        ArrayList<UiNode<?>> ret = new ArrayList<>();
        if (begin != null) {
            ret.add(begin);
        }
        if (end != null) {
            ret.add(end);
        }
        return ret;
    }

    @Override
    public boolean replaceNodeById(Widget<?> w) {
        if (w != null && w instanceof FormElement) {
            if (w.getId() == null) {
                return false;
            }
            if (begin != null && w.getId().equals(begin.getId())) {
                begin = (FormElement<?, ?>) w;
                return true;
            }
            if (end != null && w.getId().equals(end.getId())) {
                end = (FormElement<?, ?>) w;
                return true;
            }
        }
        return false;
    }

    @Override
    public Range removeNodeById(String id) {
        if (id == null) {
            return this;
        }
        if (begin != null && id.equals(begin.getId())) {
            begin = null;
        }
        if (end != null && id.equals(end.getId())) {
            end = null;
        }
        return this;
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

}
