package com.rongji.dfish.ui.command;

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
    private String validateGroup;
    private String validateEffect;
    private String validateRange;
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
     * 需要提交的面板的ID，多个用逗号(半角)隔开，如果为空则表示整个view里面的所有表单。值得注意的是Grid等面板里面也可以有隐藏值。提交的时候，要考虑非FormPanel的情况
     *
     * @param range String 设置需要提交的面板的ID
     * @return 本身，这样可以继续设置其他属性
     */
    public Submit setRange(String range) {
        this.range = range;
        return this;
    }

    /**
     * 需要提交的面板的ID，多个用逗号(半角)隔开，如果为空则表示整个view里面的所有表单。值得注意的是Grid等面板里面也可以有隐藏值。提交的时候，要考虑非FormPanel的情况
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
     * @return String
     * @see #getValidateGroup()
     */
    @Deprecated
    public String getValidate() {
        return getValidateGroup();
    }

    /**
     * 使用哪一种策略进行校验
     *
     * @param validate String
     * @return 本身，这样可以继续设置其他属性
     * @see #setValidateGroup(String)
     */
    @Deprecated
    public Submit setValidate(String validate) {
        return setValidateGroup(validate);
    }

    /**
     * 指定校验组
     *
     * @return String
     */
    public String getValidateGroup() {
        return validateGroup;
    }

    /**
     * 校验组
     *
     * @param validateGroup String
     * @return 本身，这样可以继续设置其他属性
     */
    public Submit setValidateGroup(String validateGroup) {
        this.validateGroup = validateGroup;
        return this;
    }

    @Override
    public String getType() {
        return "submit";
    }

    /**
     * 验证效果
     *
     * @return validateeffect
     * @see #VALIDATE_EFFECT_ALERT
     * @see #VALIDATE_EFFECT_RED
     */
    public String getValidateEffect() {
        return validateEffect;
    }

    /**
     * 验证效果。
     *
     * @param validateEffect String
     * @return 本身，这样可以继续设置其他属性
     * @see #VALIDATE_EFFECT_ALERT
     * @see #VALIDATE_EFFECT_RED
     */
    public Submit setValidateEffect(String validateEffect) {
        this.validateEffect = validateEffect;
        return this;
    }

    /**
     * 范围，指定一个 widget id，只校验这个 widget 内的表单。
     * 多个 id 用逗号隔开。如果以感叹号 ! 开头，则表示不校验 widget 内的表单。
     *
     * @return String
     */
    public String getValidateRange() {
        return validateRange;
    }

    /**
     * 范围，指定一个 widget id，只校验这个 widget 内的表单。
     * 多个 id 用逗号隔开。如果以感叹号 ! 开头，则表示不校验 widget 内的表单。
     *
     * @param validateRange String
     * @return this
     */
    public Submit setValidateRange(String validateRange) {
        this.validateRange = validateRange;
        return this;
    }

}
