package com.rongji.dfish.ui.form;

/**
 * PasswordTag 为密码框
 *
 * @author DFish Team
 * @version 1.1 2020-04-01 lamontYu 增加密码强度显示
 * @since 1.0
 */
public class Password extends AbstractInput<Password, String> {

    private static final long serialVersionUID = -5526694553323929504L;

    private Boolean autocomplete;
    private Boolean strength;

    /**
     * 构造函数
     *
     * @param name      String
     * @param label     String
     * @param value     Object
     * @param maxLength int
     */
    public Password(String name, String label, String value, int maxLength) {
        super(name, label, value);
        this.addValidate(Validate.maxLength(maxLength));
    }

    /**
     * 构造函数
     *
     * @param name      String
     * @param label     String
     * @param value     Object
     * @param maxLength int
     */
    public Password(String name, Label label, String value, int maxLength) {
        super(name, label, value);
        this.addValidate(Validate.maxLength(maxLength));
    }

    /**
     * 构造函数
     *
     * @param name  String
     * @param label String
     * @param value Object
     */
    public Password(String name, String label, String value) {
        super(name, label, value);
    }

    @Override
    public Password setValue(Object value) {
        this.value = toString(value);
        return this;
    }

    /**
     * 是否允许自动填充保存的密码。默认值为false
     *
     * @return Boolean
     */
    public Boolean getAutocomplete() {
        return autocomplete;
    }

    /**
     * 是否允许自动填充保存的密码。默认值为false
     *
     * @param autocomplete Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Password setAutocomplete(Boolean autocomplete) {
        this.autocomplete = autocomplete;
        return this;
    }

    /**
     * 是否显示密码强度样式
     *
     * @return Boolean
     */
    public Boolean getStrength() {
        return strength;
    }

    /**
     * 是否显示密码强度样式
     *
     * @param strength Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Password setStrength(Boolean strength) {
        this.strength = strength;
        return this;
    }
}
