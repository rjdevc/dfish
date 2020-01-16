package com.rongji.dfish.ui.form;

/**
 * PasswordTag 为密码框
 *
 * @author DFish Team
 * @version 1.1
 * @since 1.0
 */
public class Password extends AbstractInput<Password, String> {

    private static final long serialVersionUID = -5526694553323929504L;

    private Boolean autocomplete;

    /**
     * 构造函数
     *
     * @param name      String
     * @param label     String
     * @param value     Object
     * @param maxLength int
     */
    public Password(String name, String label, String value, int maxLength) {
        this.setName(name);
        this.setLabel(label);
        this.setValue(value);
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
        this.setName(name);
        this.setLabel(label);
        this.setValue(value);
    }

    /**
     * 构造函数
     *
     * @param name  String
     * @param label String
     */
    public Password(String name, String label) {
        this.setName(name);
        this.setLabel(label);
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
     * @return this
     */
    public Password setAutocomplete(Boolean autocomplete) {
        this.autocomplete = autocomplete;
        return this;
    }

}
