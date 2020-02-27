package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.AbstractNode;

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
    private Boolean noButton;
    private Integer decimal;
    private Format format;
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
     * 设置最大值
     *
     * @param maxValue Number
     * @return 本身，这样可以继续设置其他属性
     */
    public Spinner setMaxValue(Number maxValue) {
        this.addValidate(Validate.maxValue(maxValue == null ? null : String.valueOf(maxValue)));
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
        this.addValidate(Validate.minValue(minValue == null ? null : String.valueOf(minValue)));
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
    public Boolean getNoButton() {
        return noButton;
    }

    /**
     * 是否显示尾部按钮
     *
     * @param noButton Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Spinner setNoButton(Boolean noButton) {
        this.noButton = noButton;
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
    public Format getFormat() {
        return format;
    }

    /**
     * 设置分隔格式
     *
     * @param format 分隔格式
     * @return 本身，这样可以继续设置其他属性
     */
    public Spinner setFormat(Format format) {
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
    public Spinner setValue(Object value) {
        this.value = toNumber(value);
        return this;
    }

    /**
     * 数字分隔格式,一般用于数字3位一组显示,手机号,银行号等
     */
    public static class Format extends AbstractNode {

        private Integer length;
        private String separator;
        private Boolean rightward;

        public Format(Integer length) {
            this.length = length;
        }

        /**
         * 构造函数
         * @param length 分隔长度
         * @param separator 分隔符
         * @param rightward 是否从右向左分隔
         */
        public Format(Integer length, String separator, Boolean rightward) {
            this.length = length;
            this.separator = separator;
            this.rightward = rightward;
        }

        /**
         * 分隔长度。默认值为 3
         * @return Integer
         */
        public Integer getLength() {
            return length;
        }

        /**
         * 分隔长度。默认值为 3
         * @param length Integer
         * @return 本身，这样可以继续设置其他属性
         */
        public Format setLength(Integer length) {
            this.length = length;
            return this;
        }

        /**
         * 分隔符。默认值为 ","
         * @return String
         */
        public String getSeparator() {
            return separator;
        }

        /**
         * 分隔符。默认值为 ","
         * @param separator String
         * @return 本身，这样可以继续设置其他属性
         */
        public Format setSeparator(String separator) {
            this.separator = separator;
            return this;
        }

        /**
         * 是否从右向左方向分隔。设置为true，从左向右的方向进行分隔。默认值为 false
         * @return Boolean
         */
        public Boolean getRightward() {
            return rightward;
        }

        /**
         * 是否从右向左方向分隔。设置为true，从左向右的方向进行分隔。默认值为 false
         * @param rightward Boolean
         * @return 本身，这样可以继续设置其他属性
         */
        public Format setRightward(Boolean rightward) {
            this.rightward = rightward;
            return this;
        }

        @Override
        public String getType() {
            return null;
        }
    }

}
