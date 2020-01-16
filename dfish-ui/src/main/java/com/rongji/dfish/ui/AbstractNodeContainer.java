package com.rongji.dfish.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.form.FormElement;
import com.rongji.dfish.ui.form.Hidden;

/**
 * 抽象布局类，默认的布局同时还是一个Widget
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
public abstract class AbstractNodeContainer<T extends AbstractNodeContainer<T>> extends AbstractWidget<T> implements NodeContainer<T> {

    private static final long serialVersionUID = 6322077434879898040L;

    /**
     * 构造函数
     *
     * @param id String
     */
    public AbstractNodeContainer(String id) {
        this.id = id;
    }

    protected List<Node> nodes = new ArrayList<>();

    /**
     * 添加子面板
     *
     * @param w N
     * @return 本身，这样可以继续设置其他属性
     */
    public T add(Node w) {
        if (w == null) {
            return (T) this;
        }
        if (w == this) {
            throw new IllegalArgumentException("can not add widget itself as a sub widget");
        }
        nodes.add(w);
        return (T) this;
    }

    /**
     * 拷贝属性
     *
     * @param to   AbstractLayout
     * @param from AbstractLayout
     */
    protected void copyProperties(AbstractNodeContainer<?> to, AbstractNodeContainer<?> from) {
        super.copyProperties(to, from);
        to.nodes = from.nodes;
    }

    @Override
    public List<Node> findNodes() {
        return nodes;
    }

    @Override
    public boolean onReplace(Node oldNode, Node newNode) {
        Widget oldWidget = (Widget) oldNode;
        Widget newWidget = (Widget) newNode;
        if (!Utils.isEmpty(oldWidget.getWidth()) && Utils.isEmpty(newWidget.getWidth())) {
            newWidget.setWidth(oldWidget.getWidth());
        }
        if (!Utils.isEmpty(oldWidget.getHeight()) && Utils.isEmpty(newWidget.getHeight())) {
            newWidget.setHeight(oldWidget.getHeight());
        }
        return true;
    }

    /**
     * 根据表单提交名取得表单元素。可能学多个。
     *
     * @param name 提交名
     * @return 本身，这样可以继续设置其他属性
     */
    public List<FormElement<?, ?>> findFormElementsByName(String name) {
        List<FormElement<?, ?>> result = new ArrayList<>();
        findFormElementsByName(name, result);
        return result;
    }

    protected void findFormElementsByName(String name, List<FormElement<?, ?>> result) {
        if (name == null || "".equals(name)) {
            return;
        }
        if (this instanceof HiddenContainer<?>) {
            HiddenContainer<?> cast = (HiddenContainer<?>) this;
            if (cast.getHiddens() != null) {
                for (Hidden hidden : cast.getHiddens()) {
                    if (name.equals(hidden.getName())) {
                        result.add(hidden);
                    }
                }
            }
        }
        List nodes = findNodes();
        if (nodes != null) {
            for (Object item : nodes) {
                if (item == null || !(item instanceof Widget)) {
                    continue;
                }
                if (item instanceof FormElement<?, ?>) {
                    FormElement<?, ?> cast = (FormElement<?, ?>) item;
                    if (name.equals(cast.getName())) {
                        result.add(cast);
                    }
                } else if (item instanceof AbstractNodeContainer) {
                    ((AbstractNodeContainer) item).findFormElementsByName(name, result);
                }
            }
        }
    }

}
