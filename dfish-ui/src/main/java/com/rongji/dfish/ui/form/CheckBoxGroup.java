package com.rongji.dfish.ui.form;

import java.util.List;


/**
 * <p>CheckboxTag 这里支持多个checkbox或者称mutibox</p>
 * <p>多个的时候，值是由一个List传进来的。List里的数据格式详见OptionsFrag类</p>
 * <p>单个的时候，直接输入，期望值和显示字样即可</p>
 *
 * @author DFish Team
 * @version 2.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 1.0
 */
public class CheckBoxGroup extends AbstractBoxGroup<CheckBoxGroup, CheckBox> {

    private static final long serialVersionUID = -7269251020373915061L;

    /**
     * @param name    表单元素名
     * @param label   标题
     * @param value   选中的项
     * @param options 候选项
     */
    public CheckBoxGroup(String name, String label, Object value, List<?> options) {
        super(name, label, value, options);
    }

    @Override
    public CheckBox newPub() {
        return new CheckBox(null, null, null, null, null);
    }

}
