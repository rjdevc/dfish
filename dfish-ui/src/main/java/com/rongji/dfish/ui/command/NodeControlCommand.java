package com.rongji.dfish.ui.command;


import com.rongji.dfish.ui.AbstractNode;

/**
 * WidgetControlCommand 是 用于控制视图对象动作的命令
 * 一般分为视图对象的增加，删除，修改，
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 *
 * @since DFish3.0
 */
public abstract class NodeControlCommand<T extends NodeControlCommand<T>> extends AbstractNode<T> implements Command<T> {

    private static final long serialVersionUID = 999627992841928211L;

    protected String target;

    /**
     * widget ID。
     *
     * @return target
     */
    public String getTarget() {
        return target;
    }

    /**
     * widget ID。
     *
     * @param target String 目标
     * @return 本身，这样可以继续设置其他属性
     */
    public T setTarget(String target) {
        this.target = target;
        return (T) this;
    }

}
