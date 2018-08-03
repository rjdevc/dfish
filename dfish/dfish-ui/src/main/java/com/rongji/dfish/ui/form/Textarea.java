package com.rongji.dfish.ui.form;


/**
 * Textarea 为文本区
 * @author DFish Team
 * @version 1.1
 * @since XMLTMPL 1.0
 */
public final class Textarea extends AbstractInput<Textarea,String> {

	private static final long serialVersionUID = -8362625037919958854L;
	/**
	 * 构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 值
	 * @param maxLength 最大长度
	 */
	public Textarea(String name, String label, String value, Integer maxLength) {
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
	 */
	public Textarea(String name, String label, String value) {
	    this.setName(name);
	    this.setValue(value);
	    this.setLabel(label);
	}

//    private int maxLength;
//    private Integer rows;
    

//    /**
//     * 设置行高
//     * @param rows int
//     * @return 自身
//     */
//    public Textarea setRows(Integer rows) {
//        this.rows = rows;
//        return this;
//    }
//
//	/**
//	 * 设置行高
//	 * @return the rows
//	 */
//	public Integer getRows() {
//		return rows;
//	}


    /**
     * 占位符。当表单没有值时显示的提示文本。
     * @param placeholder 占位符
     * @return 本身，这样可以继续设置其他属性
     */
    @Deprecated
    public Textarea setGrayTip(String placeholder) {
        this.setPlaceholder(placeholder);
        return this;
    }
    
    

	public String getType() {
		return "textarea";
	}
	@Override
	public Textarea setValue(Object value) {
		this.value=toString(value);
		return this;
	}
}
