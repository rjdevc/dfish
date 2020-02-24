package com.rongji.dfish.ui.form;

import java.util.List;



/**
 * Select 下拉选择表单。
 * @author DFish Team
 * @version 1.2
 * @since XMLTMPL 1.0
 */
public class Select extends AbstractOptionContainer<Select> {

	private static final long serialVersionUID = -4055773878898188252L;

	private String format;
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

	/**
	 * 格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
	 * @return String
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
	 * @param format String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Select setFormat(String format) {
		this.format = format;
		return this;
	}

	/**
	 * 设置为true，表单将成为无边框无背景的状态。
	 * @return  Boolean
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
