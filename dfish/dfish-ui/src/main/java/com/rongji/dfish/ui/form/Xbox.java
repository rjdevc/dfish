package com.rongji.dfish.ui.form;

import java.util.List;



/**
 * Xbox 为带图标的下拉框标签，支持多个checked
 * @author DFish Team
 * @version 1.2
 * @since XMLTMPL 1.0
 */
public class Xbox extends AbstractOptionsHolder<Xbox, Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -953562902873398616L;
	private Boolean transparent;
	private String placeholder;
	private Boolean multiple;
	/**
	 * 占位符。当表单没有值时显示的提示文本。
	 * @return String
	 */
	public String getPlaceholder() {
		return placeholder;
	}

	/**
	 * 占位符。当表单没有值时显示的提示文本。
	 * @param placeholder String
	 * @return XBox
	 */
	public Xbox setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
		return this;
	}


	/**
	 * 构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 值
	 * @param options 候选项
	 */
	public Xbox(String name, String label, Object value, List<?> options) {
		super(name, label, value, options);
	}


	public String getType() {
		return "xbox";
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
	 * @param transparent Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Xbox setTransparent(Boolean transparent) {
		this.transparent = transparent;
		return this;
	}

	/**
	 * 是否支持多值
	 * @return Boolean
	 * @author YuLM
	 */
	public Boolean getMultiple() {
		return multiple;
	}

	/**
	 * 是否支持多值
	 * @param multiple 多值
	 * @return 本身，这样可以继续设置其他属性
	 * @author YuLM
	 */
	public Xbox setMultiple(Boolean multiple) {
		this.multiple = multiple;
		return this;
	}
}
