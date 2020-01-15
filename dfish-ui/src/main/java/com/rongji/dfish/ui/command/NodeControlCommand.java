package com.rongji.dfish.ui.command;


import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.Widget;

import java.util.List;

/**
 * WidgetControlCommand 是 用于控制视图对象动作的命令
 * 一般分为视图对象的增加，删除，修改，
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 3.0
 */
public abstract class NodeControlCommand<T extends NodeControlCommand<T>> extends AbstractCommand<T> {

    private static final long serialVersionUID = 999627992841928211L;

    protected String target;
    protected String section;

    /**
     * 目标类型-视图组件
     */
    public static final String SECTION_WIDGET = "widget";
    /**
     * 目标类型-命令
     */
    public static final String SECTION_COMMAND = "cmd";

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

    public String getSection() {
        return section;
    }

    public T setSection(String section) {
        this.section = section;
        return (T) this;
    }



}
