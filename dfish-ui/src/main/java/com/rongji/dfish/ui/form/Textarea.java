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
        this.addValidate(Validate.maxlength(maxLength));
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

    /**
     * 占位符。当表单没有值时显示的提示文本。
     *
     * @param placeholder 占位符
     * @return 本身，这样可以继续设置其他属性
     */
    @Deprecated
    public Textarea setGrayTip(String placeholder) {
        this.setPlaceholder(placeholder);
        return this;
    }

    @Override
    public String getType() {
        return "Textarea";
    }

    @Override
    public Textarea setValue(Object value) {
        this.value = toString(value);
        return this;
    }
}
