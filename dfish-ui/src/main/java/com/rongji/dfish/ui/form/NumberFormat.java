package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.AbstractNode;

/**
 * 数字分隔格式,一般用于数字3位一组显示,手机号,银行号等
 */
public class NumberFormat extends AbstractNode {

    private Integer length;
    private String separator;
    private Boolean rightward;

    public NumberFormat() {
    }

    /**
     * 构造函数
     * @param length 分隔长度
     * @param separator 分隔符
     * @param rightward 是否从右向左分隔
     */
    public NumberFormat(Integer length, String separator, Boolean rightward) {
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
    public NumberFormat setLength(Integer length) {
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
    public NumberFormat setSeparator(String separator) {
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
    public NumberFormat setRightward(Boolean rightward) {
        this.rightward = rightward;
        return this;
    }

    @Override
    public String getType() {
        return null;
    }
}
