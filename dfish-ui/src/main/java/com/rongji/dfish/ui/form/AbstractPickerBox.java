package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.command.Dialog;

/**
 * picker选择框组件,这里定义选择框组件该有特性
 * @param <T> 组件本身
 * @since 5.0
 * @date 2020-02-12
 * @author lamontYu
 */
public class AbstractPickerBox<T extends AbstractPickerBox<T>> extends AbstractInput<T, String> {

    private static final long serialVersionUID = 647561925546899578L;
    private Dialog drop;
    private Dialog picker;

    /**
     * 构造函数
     *
     * @param name  表单元素名
     * @param label 标题
     * @param value 值
     * @since DFish 3.0
     */
    public AbstractPickerBox(String name, String label, String value) {
        this.setName(name);
        this.setValue(value);
        this.setLabel(label);
    }

    @Override
    public T setValue(Object value) {
        return setValue(toString(value));
    }

    /**
     * 获取下拉对话框,如果不存在则新建
     *
     * @return DialogCommand
     */
    protected Dialog drop() {
        if (this.drop == null) {
            this.drop = new Dialog(null,null,null);
        }
        return this.drop;
    }

    /**
     * 显示所有选项的下拉对话框。
     *
     * @return DialogCommand
     */
    public Dialog getDrop() {
        return drop;
    }

    /**
     * 显示所有选项的下拉对话框。
     *
     * @param drop DialogCommand
     * @return 本身，这样可以继续设置其他属性
     */
    public T setDrop(Dialog drop) {
        this.drop = drop;
        return (T) this;
    }

    /**
     * "选择"按钮点击动作
     *
     * @return "选择"组件
     */
    public Dialog getPicker() {
        return picker;
    }

    /**
     * 组件最右侧显示的"选择"组件
     *
     * @param picker "选择"组件
     * @return 本身，这样可以继续设置其他属性
     */
    public T setPicker(Dialog picker) {
        this.picker = picker;
        return (T) this;
    }

}
