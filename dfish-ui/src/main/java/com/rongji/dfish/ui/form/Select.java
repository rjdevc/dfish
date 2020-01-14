package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.Option;

import java.util.List;



/**
 * Select 下拉选择表单。
 * @author DFish Team
 * @version 1.2
 * @since XMLTMPL 1.0
 */
public class Select extends AbstractOptionsHolder<Select, Option> {

	private static final long serialVersionUID = -4055773878898188252L;

	private Boolean transparent;

	/**
	 * 构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 值
	 * @param options 候选项
	 */
	public Select(String name, String label, Object value, List<?> options) {
		super(name, label, value, options);
	}

	@Override
	protected Option buildOption(Option o) {
		return o;
	}

	/**
	 * 设置为true，表单将成为无边框无背景的状态。
	 * @return  transparent
	 */
	public Boolean getTransparent() {
		return transparent;
	}
	/**
	 * 设置为true，表单将成为无边框无背景的状态。
	 * @param transparent  Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Select setTransparent(Boolean transparent) {
		this.transparent = transparent;
		return this;
	}
}
