package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.auxiliary.SubmitValidate;

/**
 * SubmitCommand 提交命令，改命令用于提交页面上的表单
 *
 * @author DFish Team
 * @version 2.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 2.0
 */
public class Submit extends CommunicateCommand<Submit> {

    private static final long serialVersionUID = 7949712536303839674L;

    private String range;
    private SubmitValidate validate;
    /**
     * 如果没校验通过，弹出窗口提示
     */
    public static final String VALIDATE_EFFECT_ALERT = "alert";
    /**
     * 如果没校验通过，在输入框提示红线
     */
    public static final String VALIDATE_EFFECT_RED = "red";

    /**
     * 构造函数
     *
     * @param src 提交地址
     */
    public Submit(String src) {
        super(src);
    }

    /**
     * 需要提交的面板的ID，多个用逗号(半角)隔开，如果为空则表示整个view里面的所有表单。值得注意的是Table等面板里面也可以有隐藏值。提交的时候，要考虑非FormPanel的情况
     *
     * @param range String 设置需要提交的面板的ID
     * @return 本身，这样可以继续设置其他属性
     */
    public Submit setRange(String range) {
        this.range = range;
        return this;
    }

    /**
     * 需要提交的面板的ID，多个用逗号(半角)隔开，如果为空则表示整个view里面的所有表单。值得注意的是Table等面板里面也可以有隐藏值。提交的时候，要考虑非FormPanel的情况
     *
     * @return the range  String
     * @since XMLTMPL 2.1
     */
    public String getRange() {
        return range;
    }

    /**
     * 使用哪一种策略进行校验
     *
     * @return SubmitValidate
     */
    public SubmitValidate getValidate() {
        return validate;
    }

    /**
     * 使用哪一种策略进行校验
     *
     * @param validate SubmitValidate
     * @return 本身，这样可以继续设置其他属性
     */
    public Submit setValidate(SubmitValidate validate) {
        this.validate = validate;
        return this;
    }

    public SubmitValidate validate() {
        if (validate == null) {
            validate = new SubmitValidate();
        }
        return validate;
    }

    public Submit setValidateRange(String validateRange) {
        validate().setRange(validateRange);
        return this;
    }
    public Submit setValidateGroup(String validateGroup) {
        validate().setGroup(validateGroup);
        return this;
    }
    public Submit setValidateEffect(String validateEffect) {
        validate().setEffect(validateEffect);
        return this;
    }

}
