package com.rongji.dfish.ui.auxiliary;

/**
 * 校验规则设置
 *
 * @param <E> 泛型类，一般是String或Boolean
 * @author lamontYu
 * @since DFish5.0
 */
public class ValidateRule<E> {
    private E value;
    private String text;
    private String target;
    private String mode;

    /**
     * 构造函数
     */
    public ValidateRule() {
    }

    /**
     * 构造函数
     *
     * @param value
     */
    public ValidateRule(E value) {
        this.value = value;
    }

    /**
     * 构造函数
     *
     * @param value
     * @param text
     */
    public ValidateRule(E value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 规则判断的值
     *
     * @return E 泛型类，一般使用String或Boolean
     */
    public E getValue() {
        return value;
    }

    /**
     * 规则判断的值
     *
     * @param value E 泛型类，一般使用String或Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public ValidateRule setValue(E value) {
        this.value = value;
        return this;
    }

    /**
     * 校验不通过显示的文本
     *
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * 校验不通过显示的文本
     *
     * @param text String
     * @return 本身，这样可以继续设置其他属性
     */
    public ValidateRule setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 规则校验目标，这个属性一般和mode配合使用，用于字段间比较
     *
     * @return String
     */
    public String getTarget() {
        return target;
    }

    /**
     * 规则校验目标，这个属性一般和mode配合使用，用于字段间比较
     *
     * @param target String
     * @return 本身，这样可以继续设置其他属性
     */
    public ValidateRule setTarget(String target) {
        this.target = target;
        return this;
    }

    /**
     * 规则校验模式，比如：&gt; &lt; =等运算符，这个属性一般和target配合使用，用于字段间比较
     *
     * @return String
     */
    public String getMode() {
        return mode;
    }

    /**
     * 规则校验模式，比如：&gt; &lt; =等运算符，这个属性一般和target配合使用，用于字段间比较
     *
     * @param mode String
     * @return 本身，这样可以继续设置其他属性
     */
    public ValidateRule setMode(String mode) {
        this.mode = mode;
        return this;
    }
}
