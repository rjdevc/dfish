package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.command.DialogCommand;


/**
 * 选择框。
 * @author DFish Team
 *
 */
public class Pickbox extends AbstractInput<Pickbox,String> implements HasText<Pickbox>{
	private static final long serialVersionUID = 7493588393931087665L;
	private String text;
	private DialogCommand drop;
	/**
	 * 显示文本
	 * @return String
	 */
	public String getText() {
		return text;
	}
	/**
	 * 显示文本
	 * @param text 显示文本
	 * @return this
	 */
	public Pickbox setText(String text) {
		this.text = text;
		return this;
	}
	/**
	 * 构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 值
	 * @param maxLength 最大长度
	 */
	public Pickbox(String name, String label, String value, Integer maxLength){
		this.setName(name);
		this.setValue(value);
		this.setLabel(label);
		this.addValidate(Validate.maxlength(maxLength));
	}
	/**
	 * 构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 值
	 * @since DFish 3.0
	 */
	public Pickbox(String name, String label, String value){
		this.setName(name);
		this.setValue(value);
		this.setLabel(label);
	}

	private DialogCommand picker;
	
	public String getType() {
		return "pickbox";
	}


	/**
	 * dialog 参数。其中 dialog 的 src 支持 $value 和 $text 变量。
	 * @return picker
	 */
	public DialogCommand getPicker() {
		return picker;
	}
	/**
	 * dialog 参数。其中 dialog 的 src 支持 $value 和 $text 变量。
	 * @param picker dialog 参数
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Pickbox setPicker(DialogCommand picker) {
		this.picker = picker;
		return this;
	}

	@Override
	public Pickbox setValue(Object value) {
		this.value=toString(value);
		return this;
	}

	/**
	 * 获取下拉对话框,如果不存在则新建
	 * @return DialogCommand
	 */
	protected DialogCommand drop() {
		if (this.drop == null) {
			this.drop = new DialogCommand();
		}
		return this.drop;
	}

	/**
	 * 显示所有选项的下拉对话框。
	 * @return DialogCommand
	 */
	public DialogCommand getDrop() {
		return drop;
	}

	/**
	 * 显示所有选项的下拉对话框。
	 * @param drop DialogCommand
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Pickbox setDrop(DialogCommand drop) {
		this.drop = drop;
		return this;
	}

}
