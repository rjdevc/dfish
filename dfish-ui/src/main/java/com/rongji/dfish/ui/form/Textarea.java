package com.rongji.dfish.ui.form;


/**
 * Textarea 为文本区
 * @author DFish Team
 * @version 1.1
 * @since XMLTMPL 1.0
 */
public class Textarea extends AbstractInput<Textarea,String> {

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
//	/**
//	 *  构造函数
//	 * 如果你正在使用DFish3.2新增的template功能。该构造函数可以让代码更加简约,
//	 * 你可以使用
//	 * <p>new Textarea("userName","姓名",()-&gt;"$data.userName");</p>
//	 * 表达该Hidden是动态取值，如果没有Java8 support 通常你需要使用
//	 * <p>new Textarea("userName","姓名",null).at("value","$data.userName");</p>
//	 * @param name 表单元素名
//	 * @param label 标题
//	 * @param value 值
//	 */
//	public Textarea(String name, String label, AtExpression value) {
//	    this.setName(name);
//	    this.setLabel(label);
//	    at("vallue",value.expr());
//	}


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
    
    

	@Override
    public String getType() {
		return "textarea";
	}
	@Override
	public Textarea setValue(Object value) {
		this.value=toString(value);
		return this;
	}
}
