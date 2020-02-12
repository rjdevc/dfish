package com.rongji.dfish.ui.form;

/**
 * 定义该组件带有box(Radio/CheckBox/TripleBox)属性集相关方法
 * @param <T> 组件本身
 * @date 2020-02-12
 * @since 5.0
 * @author lamontYu
 */
public interface BoxHolder<T> {

    /**
     * 获取box
     * @return AbstractBox
     */
    AbstractBox getBox();

    /**
     * 设置box
     * @param box Abstract
     * @return 本身，这样可以继续设置属性
     */
    T setBox(AbstractBox box);

}
