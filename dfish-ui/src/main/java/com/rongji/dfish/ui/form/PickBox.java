package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.command.Dialog;


/**
 * 选择框。
 * @author DFish Team
 *
 */
public class PickBox extends AbstractInput<PickBox,String> implements HasText<PickBox>{

	private static final long serialVersionUID = 7493588393931087665L;

	private String text;
	private String format;
	private Boolean escape;
	private Dialog drop;
	/**
	 * 显示文本
	 * @return String
	 */
	@Override
    public String getText() {
		return text;
	}
	/**
	 * 显示文本
	 * @param text 显示文本
	 * @return this
	 */
	@Override
    public PickBox setText(String text) {
		this.text = text;
		return this;
	}
	@Override
	public String getFormat() {
		return format;
	}

	@Override
	public PickBox setFormat(String format) {
		this.format = format;
		return this;
	}
	/**
	 * 构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 值
	 * @param maxLength 最大长度
	 */
	public PickBox(String name, String label, String value, Integer maxLength){
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
	public PickBox(String name, String label, String value){
		this.setName(name);
		this.setValue(value);
		this.setLabel(label);
	}

	private Dialog picker;

	/**
	 * dialog 参数。其中 dialog 的 src 支持 $value 和 $text 变量。
	 * @return picker
	 */
	public Dialog getPicker() {
		return picker;
	}
	/**
	 * dialog 参数。其中 dialog 的 src 支持 $value 和 $text 变量。
	 * @param picker dialog 参数
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PickBox setPicker(Dialog picker) {
		this.picker = picker;
		return this;
	}

	@Override
	public PickBox setValue(Object value) {
		this.value=toString(value);
		return this;
	}

	/**
	 * 获取下拉对话框,如果不存在则新建
	 * @return DialogCommand
	 */
	protected Dialog drop() {
		if (this.drop == null) {
			this.drop = new Dialog();
		}
		return this.drop;
	}

	/**
	 * 显示所有选项的下拉对话框。
	 * @return DialogCommand
	 */
	public Dialog getDrop() {
		return drop;
	}

	/**
	 * 显示所有选项的下拉对话框。
	 * @param drop DialogCommand
	 * @return 本身，这样可以继续设置其他属性
	 */
	public PickBox setDrop(Dialog drop) {
		this.drop = drop;
		return this;
	}
	@Override
    public PickBox setEscape(Boolean escape){
		this.escape=escape;
		return this;
	}
	@Override
    public Boolean getEscape(){
		return escape;
	}
}
