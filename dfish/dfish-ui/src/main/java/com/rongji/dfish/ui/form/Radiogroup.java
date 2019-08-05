package com.rongji.dfish.ui.form;

import java.util.List;



/**
 * Radio 为单选按钮标签
 * @author DFish Team
 * @version 1.1
 * @since XMLTMPL 1.0
 */
public class Radiogroup extends AbstractBoxgroup<Radiogroup,Radio,String> {

	private static final long serialVersionUID = 7676825772666910504L;
	/**
	 * 构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 值
	 * @param options 候选项
	 */
	public Radiogroup(String name, String label, Object value,
			List<?> options) {
		super(name, label, value, options);
	}
	@Override
	protected Radio buildOption(Option o) {
		return new Radio(null,null,o.getChecked(),o.getValue()==null?null:o.getValue().toString(),o.getText());
	}
	public String getType() {
		return "radiogroup";
	}

	@Override
	public Radiogroup setValue(Object value) {
		value=this.value=toString(value);
		if (nodes != null) {
			for (Radio r : nodes) {
				boolean checked =value != null&&value.equals(r.getValue());
				r.setChecked(checked?checked:null);
			}
		}
		return this;
	}
	@Override
    public Radio getPub() {
		if (pub == null) {
			pub = new Radio(null, null, null, null, null);
		}
	    return pub;
    }
}
