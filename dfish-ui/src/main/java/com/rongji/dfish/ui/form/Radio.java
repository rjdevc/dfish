package com.rongji.dfish.ui.form;

/**
 * 单选项。
 * @author DFish Team
 *
 */
public class Radio extends AbstractBox<Radio> {
	
	private static final long serialVersionUID = -5801003592302816104L;

	/**
	 * 构造函数
	 * @param name 表单元素
	 * @param label 标题
	 * @param value 值
	 * @param text 显示的文本
	 */
	public Radio(String name, String label, Object value, String text) {
		super(name, label,  value, text);
	}
	/**
	 * 构造函数
	 * @param name 表单元素
	 * @param label 标题
	 * @param value 值
	 * @param text 显示的文本
	 */
	public Radio(String name, Label label, Object value, String text) {
		super(name, label,  value, text);
	}

}
