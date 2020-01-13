package com.rongji.dfish.ui.form;

/**
 * Spinner 为可微调的数字输入框
 *
 * @author DFish Team
 * @version 1.1
 * @since XMLTMPL 1.0
 */
public class Spinner extends AbstractInput<Spinner, Number> {

    private static final long serialVersionUID = 4220692825414852306L;
    private Number step;
    private Boolean showButton;
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
    public Spinner(String name, String label, Number value) {
        super.setName(name);
        super.setLabel(label);
        setValue(value);
    }

    /**
     * 数字微调按钮
     *
     * @param name     表单元素名
     * @param label    标题
     * @param value    值
     * @param minValue 允许填写的最小值
     * @param maxValue 允许填写的最大值
     * @param step     步长，每点一次加/减，变动的数字
     */
    public Spinner(String name, String label, Number value, Number minValue,
                   Number maxValue, Number step) {
        super.setName(name);
        super.setLabel(label);
        setValue(value);
        this.setMinValue(minValue);
        this.setMaxValue(maxValue);
        this.setStep(step);
    }

    /**
     * 设置最大值
     *
     * @param maxValue Number
     * @return 本身，这样可以继续设置其他属性
     */
    public Spinner setMaxValue(Number maxValue) {
        this.addValidate(Validate.maxvalue(maxValue == null ? null : String.valueOf(maxValue)));
//        this.maxValue = maxValue;
        return this;
    }

    /**
     * 设置最小值
     *
     * @param minValue Number
     * @return 本身，这样可以继续设置其他属性
     */
    public Spinner setMinValue(Number minValue) {
        this.addValidate(Validate.minvalue(minValue == null ? null : String.valueOf(minValue)));
        return this;
    }

    /**
     * 设置步长
     *
     * @param step Number
     * @return 本身，这样可以继续设置其他属性
     */
    public Spinner setStep(Number step) {
        this.step = step;
        return this;
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
    public Boolean getShowButton() {
        return showButton;
    }

    /**
     * 是否显示尾部按钮
     *
     * @param showButton Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Spinner setShowButton(Boolean showButton) {
        this.showButton = showButton;
        return this;
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
    public Spinner setDecimal(Integer decimal) {
        this.decimal = decimal;
        return this;
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
    public Spinner setFormat(SpinnerFormat format) {
        this.format = format;
        return this;
    }

    /**
     * 设置 输入值应该匹配的模式
     *
     * @param match String
     * @return 本身，这样可以继续设置其他属性
     * @deprecated 使用addPatternValidate 代替
     */
    @Deprecated
    public Spinner setMatch(String match) {
        this.addValidate(Validate.pattern(match));
        return this;
    }

    @Override
    public String getType() {
        return "Spinner";
    }

    @Override
    public Spinner setValue(Object value) {
        this.value = toNumber(value);
        return this;
    }
}
