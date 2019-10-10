package com.rongji.dfish.ui.form;
/**
 * 复选框 这个是老牌的HTML元素 
 * @author DFish Team
 * @since dfish 1.0
 */
public class Checkbox extends  AbstractBox<Checkbox> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7716080336465806097L;

	/**
	 * 构造函数
	 * @param name 表单名
	 * @param label 标题
	 * @param checked 选中
	 * @param value 如果选中的提交的值
	 * @param text 显示的内容标签
	 */
	public Checkbox(String name, String label, Boolean checked, Object value,
			String text) {
		super(name, label, checked, value, text);
	}

	@Override
	public String getType() {
		return "checkbox";
	}


//	/**
//	 * @param value theValue
//	 * @return this
//	 */
//	public Checkbox setValue(String value) {
//		
//		return this;
//	}

//	@Override
//	public Checkbox setValue(Object value) {
//		this.value=value;
//		return this;
//	}
	
}
