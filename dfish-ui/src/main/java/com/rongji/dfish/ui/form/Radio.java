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
	 * @param checked 是否选中
	 * @param value 值
	 * @param text 显示的文本
	 */
	public Radio(String name, String label, Boolean checked, Object value,
			String text) {
		super(name, label, checked, value, text);
	}

	@Override
	public String getType() {
		return "radio";
	}


}
