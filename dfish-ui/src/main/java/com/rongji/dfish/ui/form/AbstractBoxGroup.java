package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.Option;
import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.AbstractNodeContainer;
import com.rongji.dfish.ui.layout.Layout;
import com.rongji.dfish.ui.widget.Html;

import java.util.*;

/**
 * @param <T> 当前对象类型
 * @param <N> 子节点对象类型
 * @author DFish Team
 * @date 2018-08-03 before
 * @since 1.0
 */
public abstract class AbstractBoxGroup<T extends AbstractBoxGroup<T, N>, N extends AbstractBox<N>> extends AbstractOptionsHolder<T, N> implements Layout<T>, HtmlContentHolder<T>, Directional<T> {

    private static final long serialVersionUID = 3733166777271763891L;

    protected N pub;
    protected Integer space;
    protected List<Widget<?>> targets = new ArrayList<>();
    protected List<N> nodes = new ArrayList<>();
    protected Boolean escape;
    protected String dir;
    private AbstractNodeContainer<?> bridgedObject=new AbstractNodeContainer(null){
        @Override
        public List<N> findNodes() {
            return AbstractBoxGroup.this.nodes;
        }
    };

    /**
     * 构造函数
     *
     * @param name         表单名
     * @param label        标题
     * @param checkedValue 已经选中的值
     * @param options      候选项
     */
    public AbstractBoxGroup(String name, String label, Object checkedValue, List<?> options) {
        super(name, label, checkedValue, options);
    }

    /**
     * 添加子面板
     *
     * @param w N
     * @return 本身，这样可以继续设置其他属性
     */
    public T add(N w) {
        return add(-1, w);
    }

    /**
     * 在指定的位置添加子面板
     *
     * @param index 位置
     * @param w     N
     * @return 本身，这样可以继续设置其他属性
     */
    public T add(int index, N w) {
        if (w == null) {
            return (T) this;
        }
        if (index < 0) {
            nodes.add(w);
        } else {
            nodes.add(index, w);
        }
        return (T) this;
    }

    /**
     * 增加选项元素
     *
     * @param w 元素option对象
     * @return add(w)s
     */
    public T addOption(N w) {
        return add(w);
    }

    private static Html EMPTY_TARGET = new Html(null);

    public T add(Option option, Widget<?> target) {
        option = option == null ? new Option(null, null) : option;
        N w = buildOption(option);
        while (targets.size() < nodes.size()) {
            targets.add(EMPTY_TARGET);
        }
        nodes.add(w);
        if (target == null) {
            target = EMPTY_TARGET;
        }
        targets.add(target);
        return (T) this;
    }

    /**
     * 增加选项，同时带一个target
     *
     * @param value  值
     * @param text   文本
     * @param target 附带的元素
     * @return
     */
    public T add(Object value, String text, Widget<?> target) {
        Option option = new Option(value, text);
        return add(option, target);
    }


    @Override
    public List<Widget<?>> findNodes() {
        List<Widget<?>> result = new ArrayList<Widget<?>>();
        if (nodes != null) {
            result.addAll(nodes);
        }
        if (targets != null) {
            result.addAll(targets);
        }
        return result;
    }


    @Override
    public T removeNodeById(String id) {
        List<Widget<?>> nodes = findNodes();
        if (id == null || nodes == null) {
            return (T) this;
        }
        for (int i = 0; i < nodes.size(); i++) {
            Widget<?> item = nodes.get(i);
            if (id.equals(item.getId())) {
                nodes.remove(i);
                if (targets.size() > i + 1) {
                    targets.remove(i);
                }
                i--;
            } else if (targets.size() > i && id.equals(targets.get(i).getId())) {
                nodes.remove(i);
                targets.remove(i);
                i--;
            } else if (item instanceof Layout) {
                Layout<?> cast = (Layout<?>) item;
                cast.removeNodeById(id);
            } else if (targets.size() > i && targets.get(i) instanceof Layout) {
                Layout<?> cast = (Layout<?>) targets.get(i);
                cast.removeNodeById(id);
            }
        }

        return (T) this;
    }

    @Override
    public boolean replaceNodeById(Node w) {
        List<Widget<?>> nodes = findNodes();
        if (w == null || w.getId() == null || nodes == null) {
            return false;
        }
        String id = w.getId();
        for (int i = 0; i < nodes.size(); i++) {
            Widget<?> item = nodes.get(i);
            Widget<?> t = targets.size() > i ? targets.get(i) : null;
            if (id.equals(item.getId())) {
                // 替换该元素
                if (onReplace(item, (Widget) w)) {
                    nodes.set(i, (Widget)w);
                    return true;
                } else {
                    return false;
                }
            } else if (t != null && id.equals(t.getId())) {
                // 替换该元素
                if (onReplace(t, (Widget)w)) {
                    targets.set(i, (Widget)w);
                    return true;
                } else {
                    return false;
                }
            } else if (t != null && t instanceof Layout) {
                Layout<?> cast = (Layout<?>) t;
                boolean replaced = cast.replaceNodeById(w);
                if (replaced) {
                    return true;
                }
            }
        }
        return false;
    }
    protected boolean onReplace(Widget<?> oldItem,Widget<?>newItem){
        return true;
    }

    @Override
    public void clearNodes() {
        nodes.clear();
        targets.clear();
        ;
    }

    protected abstract N newPub();

    /**
     * 公共参数
     *
     * @return N pub
     */
    public N getPub() {
        if (this.pub == null) {
            this.pub = newPub();
        }
        return this.pub;
    }

    /**
     * boxgroup的默认参数
     *
     * @param pub 默认元素对象
     * @return 本身，这样可以继续设置其他属性
     */
    public T setPub(N pub) {
        this.pub = pub;
        return (T) this;
    }

    /**
     * 当设置了 targets，再设置 space 可调整行间距。
     *
     * @return space
     */
    public Integer getSpace() {
        return space;
    }

    /**
     * 当设置了 targets，再设置 space 可调整行间距。
     *
     * @param space 间距
     * @return 本身，这样可以继续设置其他属性
     */
    public T setSpace(Integer space) {
        this.space = space;
        return (T) this;
    }

    /**
     * 和boxgroup 一一对应的节点数组。勾选复选框将改变 target 节点的 disabled 状态。
     *
     * @return targets
     */
    public List<Widget<?>> getTargets() {
        return targets;
    }

    /**
     * 和boxgroup 一一对应的节点数组。勾选复选框将改变 target 节点的 disabled 状态。
     *
     * @param w 对象
     * @return 本身，这样可以继续设置其他属性
     */
    public T addTarget(Widget<?> w) {
        targets.add(w);
        return (T) this;
    }

    @Override
    public Boolean getEscape() {
        return this.escape;
    }

    @Override
    public T setEscape(Boolean escape) {
        this.escape = escape;
        return (T) this;
    }

    @Override
    public String getDir() {
        return dir;
    }

    @Override
    public T setDir(String dir) {
        this.dir = dir;
        return (T) this;
    }

    @Override
    public Node<?> findNodeById(String id) {
        return bridgedObject.findNodeById(id);
    }



//    public List<FormElement<?, ?>> findFormElementsByName(String name) {
//        return bridgedObject.findFormElementsByName(name);
//    }

}