package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.Widget;

/**
 * LabelRow 表示表单里面带有标题的行。
 * <p>和FormElement 不同，FormElement一定有Name而不一定有label.
 * LabelRow，可以被插入到FormPanel中，但不一定可以取出。因为时是根据name来取得的。</p>
 * <p>Label 是一个LabelRow 但不是一个标准的FormElement。它的原型是一个HTML</p>
 * <p>按HTML规范 Hidden 是一个FormElement 但不是一个LabelRow。</p>
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 * @version 1.0
 * @see FormElement
 * @since DFish2.0
 */
public interface LabelRow<T extends LabelRow<T>> extends Widget<T> {
    /**
     * 设置标题
     * <p>v2.3以后改方法返回自身以便可以继续对这个表单操作</p>
     *
     * @param label String
     * @return 本身，这样可以继续设置其他属性
     */
    T setLabel(String label);

    /**
     * 设置标题
     * <p>v2.3以后改方法返回自身以便可以继续对这个表单操作</p>
     *
     * @param label String
     * @return 本身，这样可以继续设置其他属性
     * @since DFish3.2.0
     */
    T setLabel(Label label);

    /**
     * 取得标题
     *
     * @return label
     * @since DFish3.2.0
     */
    Object getLabel();

    /**
     * 这个元素，是否以藏标题。如果是，则不显示标题；否则显示标题。
     * <p>v2.3以后改方法返回自身以便可以继续对这个表单操作</p>
     *
     * @param noLabel boolean
     * @return 本身，这样可以继续设置其他属性
     */
    @Deprecated
    T setNoLabel(Boolean noLabel);

    /**
     * 这个元素，是否显示标题。如果是，则不显示标题；否则显示标题。
     *
     * @return showLabel
     */
    @Deprecated
    Boolean getNoLabel();

}
