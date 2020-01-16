package com.rongji.dfish.ui.form;


/**
 * Textarea 为文本区
 *
 * @author DFish Team
 * @version 1.1
 * @since XMLTMPL 1.0
 */
public class Textarea extends AbstractInput<Textarea, String> {

    private static final long serialVersionUID = -8362625037919958854L;

    /**
     * 构造函数
     *
     * @param name      表单元素名
     * @param label     标题
     * @param value     值
     * @param maxLength 最大长度
     */
    public Textarea(String name, String label, String value, Integer maxLength) {
        this.setName(name);
        this.setValue(value);
        this.setLabel(label);
        this.addValidate(Validate.maxLength(maxLength));
    }

    /**
     * 构造函数
     *
     * @param name  表单元素名
     * @param label 标题
     * @param value 值
     */
    public Textarea(String name, String label, String value) {
        this.setName(name);
        this.setValue(value);
        this.setLabel(label);
    }

    @Override
    public Textarea setValue(Object value) {
        this.value = toString(value);
        return this;
    }
}
