package com.rongji.dfish.ui.form;

import java.util.List;


/**
 * Radio 为单选按钮标签
 *
 * @author DFish Team
 * @version 1.1
 * @since DFish1.0
 */
public class RadioGroup extends AbstractBoxGroup<RadioGroup, Radio> {

    private static final long serialVersionUID = 7676825772666910504L;

    /**
     * 构造函数
     *
     * @param name    表单元素名
     * @param label   标题
     * @param value   值
     * @param options 候选项
     */
    public RadioGroup(String name, String label, Object value, List<?> options) {
        super(name, label, value, options);
    }
    /**
     * 构造函数
     *
     * @param name    表单元素名
     * @param label   标题
     * @param value   值
     * @param options 候选项
     */
    public RadioGroup(String name, Label label, Object value, List<?> options) {
        super(name, label, value, options);
    }

    @Override
    public Radio newPub() {
        return new Radio(null, (Label) null, null, null);
    }

}
