package com.rongji.dfish.ui;

/**
 * 定义了该组件有target指向的组件及相关方法
 * @param <T> 组件本身
 * @param <N> 目标组件
 * @since 5.0
 * @date 2020-02-12
 * @author lamontYu
 */
public interface TargetHolder<T, N> {

    /**
     * 获取目标组件
     * @return N 目标组件
     */
    N getTarget();

    /**
     * 设置目标组件
     * @param target N 目标组件
     * @return 本身，这样可以继续设置属性
     */
    T setTarget(N target);

}
