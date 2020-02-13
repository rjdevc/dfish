package com.rongji.dfish.ui;

/**
 * 定义了该组件有target指向的组件及相关方法
 * @param <T> 组件本身
 * @since 5.0
 * @date 2020-02-12
 * @author lamontYu
 */
public interface TargetHolder<T> {

    /**
     * 获取目标组件
     * @return Widget 目标组件
     */
    Widget getTarget();

    /**
     * 设置目标组件
     * @param target Widget 目标组件
     * @return 本身，这样可以继续设置属性
     */
    T setTarget(Widget target);

}
