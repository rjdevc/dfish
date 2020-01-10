package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.Widget;

/**
 * LabelRow 表示表单里面带有标题的行。
 * <p>和FormElement 不同，FormElement一定有Name而不一定有label.
 * LabelRow，可以被插入到FormPanel中，但不一定可以取出。因为时是根据name来取得的。</p>
 * <p>Label 是一个LabelRow 但不是一个标准的FormElement。它的原型是一个HTML</p>
 * <p>按HTML规范 Hidden 是一个FormElement 但不是一个LabelRow。</p>
 * @author DFish Team
 * @version 1.0
 * @param <T>  当前对象类型
 * @since dfish 2.0
 * @see FormElement
 */
public interface LabelRow<T extends LabelRow<T>> extends Widget<T> {
    /**
     * 设置标题
     * <p>v2.3以后改方法返回自身以便可以继续对这个表单操作</p>
     * @param label String
     * @return 本身，这样可以继续设置其他属性
     */
    T setLabel(String label);
    /**
     * 设置标题
     * <p>v2.3以后改方法返回自身以便可以继续对这个表单操作</p>
     * @param label String
     * @return 本身，这样可以继续设置其他属性
     * @since 3.2.0
     */
    T setLabel(Label label);
    /**
     * 取得标题
     * @return label
     * @since 3.2.0
     */
    Label getLabel();

    /**
     * 这个元素，是否以藏标题。如果是，则不显示标题；否则显示标题。
     * <p>v2.3以后改方法返回自身以便可以继续对这个表单操作</p>
     * @param hideLabel boolean
     * @return 本身，这样可以继续设置其他属性
     */
    T setHideLabel(Boolean hideLabel);
    
    /**
     * 这个元素，是否显示标题。如果是，则不显示标题；否则显示标题。
     * @return showLabel
     */
    Boolean getHideLabel();
    
    

    

    /**
     * 这个元素，是否必填，这里非空不是dfish2.4以后的真实判断，而是在界面显示一个非空标记
     * 通常是红色字体的半角星号(*)
     * setStar和setRequired不同，setStar仅设置必填的视觉效果，并不会增加校验。
     * @param star boolean
     * @return 本身，这样可以继续设置其他属性
     * @since 3.0
     */
    T setStar(Boolean star);
    
    /**
     * 这个元素，是否非空，这里非空不是dfish2.4以后的真实判断，而是在界面显示一个非空标记
     * 通常是红色字体的半角星号(*)
     * @return Boolean
     * @since 3.0
     */
    Boolean getStar();
    
    
//    
//    /**
//     * 设置这个元素是否不显示。
//     * <p>v2.3以后改方法返回自身以便可以继续对这个表单操作</p>
//     * @param hidden Boolean
//     * @return 本身，这样可以继续设置其他属性
//     * FIXME 这个属性在该版本中比较尴尬。隐藏的表单元素可能还会占用一行
//     */
//    T setHidden(Boolean hidden);
//    
//    /**
//     * 判断这个元素是否隐藏
//     * @return Boolean 是否隐藏
//     * FIXME 这个属性在该版本中比较尴尬。隐藏的表单元素可能还会占用一行
//     */
//    Boolean getHidden();
    
    

}
