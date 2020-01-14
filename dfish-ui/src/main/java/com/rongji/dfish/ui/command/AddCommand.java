package com.rongji.dfish.ui.command;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.ui.MultiContainer;
import com.rongji.dfish.ui.Widget;

/**
 * AddCommand为增加一个元素的命令的基础格式，一般增加元素分为
 * {@link Append},{@link Prepend},{@link After}和{@link Before}
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 * @since DFish 2.0 当时为InsertComman通过where属性控制增加在哪里。
 */
@SuppressWarnings("unchecked")
public abstract class AddCommand<T extends AddCommand<T>> extends NodeControlCommand<T> implements
        MultiContainer<T, Widget<? extends Widget<?>>> {

    private static final long serialVersionUID = -2417775749900268295L;

    protected List<Widget> nodes = new ArrayList<>();

    public AddCommand(String target, Widget<?>... nodes) {
        setTarget(target);
        if (nodes != null) {
            for (Widget<?> node : nodes) {
                add(node);
            }
        }
    }

    /**
     * 添加需要插入元素
     * 一般是增加Widget。也允许增加Command等其他部件
     *
     * @param w JsonObject
     * @return 本身，这样可以继续设置其他属性
     */
    public T add(Widget w) {
        if (w == null) {
            return (T) this;
        }
        if (w == this) {
            throw new IllegalArgumentException("can not add widget itself as a sub widget");
        }
        nodes.add(w);
        return (T) this;
    }

    @Override
    public List<Widget> findNodes() {
        return nodes;
    }


    @Override
    public void clearNodes(){
        this.nodes.clear();
    }
    @Override
    public List<Widget<?>> getNodes() {
        return (List)nodes;
    }

    @Override
    public Widget<?> findNodeById(String id) {
        return null;
    }

    @Override
    public T removeNodeById(String id) {
        return (T)this;
    }

    @Override
    public boolean replaceNodeById(Widget<?> w) {
        return false;
    }

}
