package com.rongji.dfish.ui.form;

import java.util.List;

import com.rongji.dfish.ui.LazyLoad;



/**
 * Xbox 为带图标的下拉框标签，支持多个checked
 * @author DFish Team
 * @version 1.2
 * @since DFish1.0
 */
public class DropBox extends AbstractOptionContainer<DropBox> implements LazyLoad<DropBox> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -953562902873398616L;
	private String src;
	private String success;
	private Boolean sync;
	private String error;
	private String complete;
	private String filter;
	private Boolean transparent;
	private String placeholder;
	private Boolean multiple;
	private Boolean cancelable;
	private String format;

	/**
	 * 构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 值
	 * @param options 候选项
	 */
	public DropBox(String name, String label, Object value, List<?> options) {
		super(name, label, value, options);
	}
	/**
	 * 构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 值
	 * @param options 候选项
	 */
	public DropBox(String name, Label label, Object value, List<?> options) {
		super(name, label, value, options);
	}

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
	public DropBox setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
		return this;
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
	public DropBox setTransparent(Boolean transparent) {
		this.transparent = transparent;
		return this;
	}

	/**
	 * 是否支持多值
	 * @return Boolean
	 * @author lamontYu
	 */
	public Boolean getMultiple() {
		return multiple;
	}

	/**
	 * 是否支持多值
	 * @param multiple 多值
	 * @return 本身，这样可以继续设置其他属性
	 */
	public DropBox setMultiple(Boolean multiple) {
		this.multiple = multiple;
		return this;
	}

	@Override
	public String getSrc() {
		return src;
	}

	@Override
	public DropBox setSrc(String src) {
		this.src=src;
		return this;
	}

	/**
	 * 如果设置为true 可取消当前选中的选项，并且不会默认选中第一项。该参数仅在单选模式下有效，默认值为false
	 * @return Boolean
	 */
	public Boolean getCancelable() {
		return cancelable;
	}

	/**
	 *  如果设置为true 可取消当前选中的选项，并且不会默认选中第一项。该参数仅在单选模式下有效，默认值为false
	 * @param cancelable Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public DropBox setCancelable(Boolean cancelable) {
		this.cancelable=cancelable;
		return this;
	}
	@Override
    public String getSuccess() {
		return success;
	}

	@Override
    public DropBox setSuccess(String success) {
		this.success = success;
		return this;
	}
	@Override
    public String getError() {
		return error;
	}

	@Override
    public DropBox setError(String error) {
		this.error = error;
		return this;
	}
	@Override
    public String getComplete() {
		return complete;
	}

	@Override
    public DropBox setComplete(String complete) {
		this.complete = complete;
		return this;
	}
	@Override
    public String getFilter() {
		return filter;
	}

	@Override
    public DropBox setFilter(String filter) {
		this.filter = filter;
		return this;
	}

	@Override
    public Boolean getSync() {
		return sync;
	}

	@Override
    public DropBox setSync(Boolean sync) {
		this.sync = sync;
		return this;
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
	public DropBox setFormat(String format) {
		this.format = format;
		return this;
	}
}
