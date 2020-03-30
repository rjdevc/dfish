package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.auxiliary.SpinnerFormat;

/**
 * 数字输入框，数字输入框公共属性的基类
 *
 * @param <T> 泛型
 * @author lamontYu
 * @date 2020-03-30
 * @since 5.0
 */
public class AbstractNumberInput<T extends AbstractNumberInput<T>> extends AbstractInput<T, Number> {

    private static final long serialVersionUID = 4220692825414852306L;
    private Number step;
    private Boolean noButton;
    private Integer decimal;
    private SpinnerFormat format;
//    private String match;

    /**
     * 数字微调按钮
     *
     * @param name  表单元素名
     * @param label 标题
     * @param value 值
     */
    public AbstractNumberInput(String name, String label, Number value) {
        super(name, label, value);
    }

    /**
     * 数字微调按钮
     *
     * @param name  表单元素名
     * @param label 标题
     * @param value 值
     */
    public AbstractNumberInput(String name, Label label, Number value) {
        super(name, label, value);
    }


    /**
     * 设置最大值
     *
     * @param maxValue Number
     * @return 本身，这样可以继续设置其他属性
     */
    public T setMaxValue(Number maxValue) {
        this.addValidate(Validate.maxValue(maxValue == null ? null : String.valueOf(maxValue)));
//        this.maxValue = maxValue;
        return (T) this;
    }

    /**
     * 设置最小值
     *
     * @param minValue Number
     * @return 本身，这样可以继续设置其他属性
     */
    public T setMinValue(Number minValue) {
        this.addValidate(Validate.minValue(minValue == null ? null : String.valueOf(minValue)));
        return (T) this;
    }

    /**
     * 设置步长
     *
     * @param step Number
     * @return 本身，这样可以继续设置其他属性
     */
    public T setStep(Number step) {
        this.step = step;
        return (T) this;
    }

    /**
     * 步长
     *
     * @return 步长
     */
    public Number getStep() {
        return step;
    }

    /**
     * 是否显示尾部按钮
     *
     * @return Boolean
     */
    public Boolean getNoButton() {
        return noButton;
    }

    /**
     * 是否显示尾部按钮
     *
     * @param noButton Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setNoButton(Boolean noButton) {
        this.noButton = noButton;
        return (T) this;
    }

    /**
     * 小数精度
     * 设为0时，只允许输入整数。设为正整数，则限制小数的最大位数。设为负数，则不限整数和小数。默认值为0
     *
     * @return Integer
     */
    public Integer getDecimal() {
        return decimal;
    }

    /**
     * 小数精度
     * 设为0时，只允许输入整数。设为正整数，则限制小数的最大位数。设为负数，则不限整数和小数。默认值为0
     *
     * @param decimal Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public T setDecimal(Integer decimal) {
        this.decimal = decimal;
        return (T) this;
    }

    /**
     * 设置分隔格式
     *
     * @return 分隔格式
     */
    public SpinnerFormat getFormat() {
        return format;
    }

    /**
     * 设置分隔格式
     *
     * @param format 分隔格式
     * @return 本身，这样可以继续设置其他属性
     */
    public T setFormat(SpinnerFormat format) {
        this.format = format;
        return (T) this;
    }

    /**
     * 设置 输入值应该匹配的模式
     *
     * @param match String
     * @return 本身，这样可以继续设置其他属性
     * @deprecated 使用addPatternValidate 代替
     */
    @Deprecated
    public T setMatch(String match) {
        this.addValidate(Validate.pattern(match));
        return (T) this;
    }

    @Override
    public T setValue(Object value) {
        this.value = toNumber(value);
        return (T) this;
    }

}
