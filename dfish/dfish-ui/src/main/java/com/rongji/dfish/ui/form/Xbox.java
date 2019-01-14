package com.rongji.dfish.ui.form;

import java.util.List;

import com.rongji.dfish.ui.HasSrc;



/**
 * Xbox 为带图标的下拉框标签，支持多个checked
 * @author DFish Team
 * @version 1.2
 * @since XMLTMPL 1.0
 */
public class Xbox extends AbstractOptionsHolder<Xbox, Object> implements HasSrc<Xbox> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -953562902873398616L;
	private String src;
	private String schema;
	private Boolean transparent;
	private String placeholder;
	private Boolean multiple;
	private Boolean cancelable;
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

	@Override
	public String getSrc() {
		return src;
	}

	@Override
	public Xbox setSrc(String src) {
		this.src=src;
		return this;
	}

	@Override
	public String getSchema() {
		return schema;
	}

	@Override
	public Xbox setSchema(String schema) {
		this.schema=schema;
		return this;
	}
	
	/**
	 *如果设置为true 可取消当前选中的选项，并且不会默认选中第一项。该参数仅在单选模式下有效，默认值为false
	 * @return Boolean
	 */
	public Boolean getCancelable() {
		return cancelable;
	}

	/**
	 *  如果设置为true 可取消当前选中的选项，并且不会默认选中第一项。该参数仅在单选模式下有效，默认值为false
	 * @param defaultchecked Boolean
	 * @return this
	 */
	public Xbox setCancelable(Boolean cancelable) {
		this.cancelable=cancelable;
		return this;
	}
}
