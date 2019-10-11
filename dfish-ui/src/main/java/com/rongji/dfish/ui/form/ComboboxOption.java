package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;

/**
 * combobox 的已选项。它由 combobox 自动生成，没有显式定义。可以通过 combobox 的 pub 属性来设置它的参数。
 * @author DFish Team
 *
 */
public class ComboboxOption extends AbstractWidget<ComboboxOption> implements HasText<ComboboxOption>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8723811310007986652L;
	private String text;
	private Boolean escape;
	private String value;
	@Override
    public String getText() {
		return text;
	}
	@Override
    public ComboboxOption setText(String text) {
		this.text = text;
		return this;
	}
	@Override
    public ComboboxOption setEscape(Boolean escape){
		this.escape=escape;
		return this;
	}
	@Override
    public Boolean getEscape(){
		return escape;
	}
	/**
	 * 值
	 * @return  value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 值
	 * @param value 值
	 * @return 本身，这样可以继续设置其他属性
	 */
	public ComboboxOption setValue(String value) {
		this.value = value;
		return this;
	}
	@Override
	public String getType() {
		return "combobox/option";
	}

}
