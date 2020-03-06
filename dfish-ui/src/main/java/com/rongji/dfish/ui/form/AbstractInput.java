package com.rongji.dfish.ui.form;

/**
 * 可以键盘输入的样式，一般都有一个边框。
 *
 * @param <T> 当前对象类型
 * @param <V> value对象类型
 * @author DFish Team
 * @date 2018-08-03 before
 * @since 1.0
 */
public abstract class AbstractInput<T extends AbstractInput<T, V>, V> extends AbstractFormElement<T, V> {

    private static final long serialVersionUID = 4291307643196526303L;

    protected Boolean transparent;
    protected String placeholder;

    public AbstractInput(String name, String label, Object value) {
        this.setName(name);
        this.setLabel(label);
        this.setValue(value);
    }
    public AbstractInput(String name, Label label, Object value) {
        this.setName(name);
        this.setLabel(label);
        this.setValue(value);
    }

    /**
     * 设置为true，表单将成为无边框无背景的状态。
     *
     * @return transparent
     */
    public Boolean getTransparent() {
        return transparent;
    }

    /**
     * 设置为true，表单将成为无边框无背景的状态。
     *
     * @param transparent Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setTransparent(Boolean transparent) {
        this.transparent = transparent;
        return (T) this;
    }

    /**
     * 占位符。当表单没有值时显示的提示文本。
     *
     * @return placeholder
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * 占位符。当表单没有值时显示的提示文本。
     *
     * @param placeholder String
     * @return 本身，这样可以继续设置其他属性
     */
    @SuppressWarnings("unchecked")
    public T setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return (T) this;
    }
}
