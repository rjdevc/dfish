package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.AbstractWidget;

/**
 * 隐藏表单
 * @author DFish Team
 * @since XMLTMPL 1.0
 */
public class Hidden extends AbstractWidget<Hidden> implements FormElement<Hidden,String> {

	private static final long serialVersionUID = 7544920001509570208L;

	private String name;
	private String value;
	/**
	 * 构造函数
	 * @param name 表单提交名字
	 * @param value 值
	 */
	public Hidden(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
    public String getName() {
		return name;
	}

	@Override
    public Hidden setName(String name) {
		this.name = name;
		return this;
	}

	@Override
    public String getValue() {
		return value;
	}

	@Override
	public Hidden setValue(Object value) {
		this.value=toString(value);
		return this;
	}

}
