package com.rongji.dfish.ui.form;



/**
 * 单行文本输入框。
 * @author DFish Team
 *
 */
public class Text extends AbstractInput<Text,String>{

	private static final long serialVersionUID = 1873658828853845701L;

	/**
	 * 构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 值
	 * @param maxLength 最大长度
	 */
	public Text(String name, String label, Object value, Integer maxLength){
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
	public Text(String name, String label, String value){
		this.setName(name);
		this.setValue(value);
		this.setLabel(label);
	}

    /**
     * 设置表单的元素的值
     * <p>在HTML协议中提交表单元素的时候，以键值对的方式提交。
     * 一般情况下。其中值就是value. 某些多选项的元素可能这些键值对会以复数形式出现。
     * 比如select 多行形式时。</p>
     * <p>v2.3以后改方法返回自身以便可以继续对这个表单操作</p>
     * @param value Object
     * @return 本身，这样可以继续设置其他属性
     */
	@Override
    public Text setValue(Object value) {
		this.value=toString(value);
		return this;
	}

}
