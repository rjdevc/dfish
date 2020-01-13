package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;

/**
 * combobox 的已选项。它由 combobox 自动生成，没有显式定义。可以通过 combobox 的 pub 属性来设置它的参数。
 *
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 3.0
 */
public class ComboBoxOption extends AbstractWidget<ComboBoxOption> implements HasText<ComboBoxOption> {

    private static final long serialVersionUID = -8723811310007986652L;

    private String text;
    private Boolean escape;
    private String value;

    @Override
    public String getType() {
        return "ComboBoxOption";
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public ComboBoxOption setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public ComboBoxOption setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    @Override
    public Boolean getEscape() {
        return escape;
    }

    /**
     * 值
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * 值
     *
     * @param value 值
     * @return 本身，这样可以继续设置其他属性
     */
    public ComboBoxOption setValue(String value) {
        this.value = value;
        return this;
    }

}
