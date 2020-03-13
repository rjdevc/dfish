package com.rongji.dfish.ui.auxiliary;

/**
 * 提交表单时的校验策略
 */
public class SubmitValidate {
    private String range;
    private String group;
    private String effect;

    /**
     * 获得被校验的表单widget id
     * @return widget id
     */
    public String getRange() {
        return range;
    }

    /**
     *指定一个 widget id，只校验这个 widget 内的表单。多个 id 用逗号隔开。如果以感叹号 ! 开头，则表示不校验 widget 内的表单。
     * @param range
     * @return 本身，这样可以继续设置其他属性
     */
    public SubmitValidate setRange(String range) {
        this.range = range;
        return this;
    }

    /**
     * 验证组名
     * @return String
     */
    public String getGroup() {
        return group;
    }

    /**
     * 验证组名
     * @param group
     * @return 本身，这样可以继续设置其他属性
     */
    public SubmitValidate setGroup(String group) {
        this.group = group;
        return this;
    }

    /**
     * 验证效果。可选值: alert, red, none
     * @return String
     */
    public String getEffect() {
        return effect;
    }

    /**
     * 验证效果。可选值: alert, red, none
     * @param effect
     * @return 本身，这样可以继续设置其他属性
     */
    public SubmitValidate setEffect(String effect) {
        this.effect = effect;
        return this;
    }
}
