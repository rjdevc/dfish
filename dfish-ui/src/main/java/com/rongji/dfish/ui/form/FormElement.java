package com.rongji.dfish.ui.form;


import com.rongji.dfish.ui.Widget;

/**
 * FormElement 为 组成表单的元素
 * <p>DFISH中表单元素包含标准的HTML表单元素以及其他一些扩展的元素。
 * 提交表单的时候</p>
 * <p>从3.0.4585-20180201起该接口继承Widget</p>
 *
 * @param <T> 当前对象类型
 * @param <P> value对象类型
 * @author DFish Team
 * @version 1.0
 * @date 2018-08-03 before
 * @since 2.0
 */
public interface FormElement<T extends FormElement<T, P>, P> extends Widget<T> {
    /**
     * 设置表单的元素的值
     * <p>在HTML协议中提交表单元素的时候，以键值对的方式提交。
     * 一般情况下。其中值就是value. 某些多选项的元素可能这些键值对会以复数形式出现。
     * 比如select 多行形式时。</p>
     * <p>v2.3以后改方法返回自身以便可以继续对这个表单操作</p>
     *
     * @param value Object
     * @return 本身，这样可以继续设置其他属性
     */
    T setValue(Object value);

    /**
     * 取得表单的元素的值
     *
     * @return Object
     */
    P getValue();

    /**
     * 设置表单的元素名称
     * <p>在HTML协议中提交表单元素的时候，以键值对的方式提交。
     * 一般情况下。其中键就是name.  某些多选项的元素可能这些键值对会以复数形式出现。
     * 比如select 多行形式时。</p>
     * <p>v2.3以后改方法返回自身以便可以继续对这个表单操作</p>
     *
     * @param name String
     * @return 本身，这样可以继续设置其他属性
     */
    T setName(String name);

    /**
     * 任何一个表单的元素都有名称，可以取得
     *
     * @return String
     */
    String getName();

}
