package com.rongji.dfish.ui.form;


/**
 * Slider 滑块。用户可以通过移动滑块在控件中显示对应的值
 * @author DFish Team
 *
 */
public class Slider extends AbstractFormElement<Slider,Number> {

	private static final long serialVersionUID = -2998281453210751005L;
	//	/**
//	 * 拖动滑块时显示的tip。
//	 */
//	private Object tip;
	/**
	 * 默认构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 值
	 */
	public Slider(String name, String label, Number value) {
		super.setName(name);
		super.setLabel(label);
		setValue(value);
	}
	/**
	 * 默认构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 值
	 * @param minValue 最小值
	 * @param maxValue 最大值
	 */
	public Slider(String name, String label, Number value, Number minValue,
	            Number maxValue) {
		 super.setName(name);
		 super.setLabel(label);
		 setValue(value);
		 this.setMinValue(minValue);
		 this.setMaxValue(maxValue);
	}
	  
	 /**
     * 设置最大值
     * @param maxValue Number
     * @return 本身，这样可以继续设置其他属性
     */
    public Slider setMaxValue(Number maxValue) {
    	this.addValidate(Validate.maxvalue(maxValue==null?null:String.valueOf(maxValue)));
    	return this;
    }

    /**
     * 设置最小值
     * @param minValue Number
     * @return 本身，这样可以继续设置其他属性
     */
    public Slider setMinValue(Number minValue) {
    	this.addValidate(Validate.minvalue(minValue==null?null:String.valueOf(minValue)));
    	return this;
    }

	@Override
	public String getType() {
		return "slider";
	}
	@Override
	public Slider setValue(Object value) {
		this.value=toNumber(value);
		return this;
	}
//	/**
//	 * 拖动滑块时显示的tip。
//	 * @return Object tip
//	 */
//	public Object getTip() {
//		return tip;
//	}
//	/**
//	 * 拖动滑块时显示的tip。
//	 * 默认为true，显示拖动的值，如果设置false，就不显示tip
//	 * @param tip
//	 * @return  本身，这样可以继续设置其他属性
//	 */
//	public Slider setTip(Boolean tip) {
//		this.tip = tip;
//		return this;
//	}
//	/**
//	 * 拖动滑块时显示的tip。支持${0}参数表示当前值。
//	 * @param tip
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public Slider setTip(String tip) {
//		this.tip = tip;
//		return this;
//	}
	
}