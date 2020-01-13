package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.Option;

import java.util.List;


/**
 * Radio 为单选按钮标签
 *
 * @author DFish Team
 * @version 1.1
 * @since XMLTMPL 1.0
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

    @Override
    protected Radio buildOption(Option o) {
        return new Radio(null, null, o.getChecked(), o.getValue() == null ? null : o.getValue().toString(), o.getText());
    }

    @Override
    public String getType() {
        return "RadioGroup";
    }

    @Override
    public void doSetValue(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        if (value.getClass().isArray()) {
            return ((Object[]) value)[0];
        }
        return this.value;
    }

    @Override
    public Radio newPub() {
        return new Radio(null, null, null, null, null);
    }

}
