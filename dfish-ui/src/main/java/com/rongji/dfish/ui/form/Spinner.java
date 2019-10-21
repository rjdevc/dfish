package com.rongji.dfish.ui.form;

/**
 * Spinner 为可微调的数字输入框
 * @author DFish Team
 * @version 1.1
 * @since XMLTMPL 1.0
 */
public class Spinner extends AbstractInput<Spinner,Number> {
    //陈明远(陈明远 ) 11:51:50
//<i tp="number" n="size" t="分段尺寸" v="100" ntn="1" rdn="0" ds="0" mnv="30" mxv="200" step="0.5" showbtn="1" match="/^\d+\.?\d{0,2}$/" mk="(KB)" mkw="30"/>
//
//说明：
//mnv: 最小值，可选
//mxv: 最大值，可选
//step: 步长，每次点击按钮时增加/减少的量，可选
//showbtn: 是否显示点击按钮，可选
//  明远(陈明远 ) 11:57:42
//还有几个属性忘了：
//w: 宽度 (width)

//陈明远(陈明远 ) 11:57:57
//都可选

/**
	 * 
	 */
	private static final long serialVersionUID = 4220692825414852306L;
	//    private Number minValue;
//    private Number maxValue;
    private Number step;
    private Integer decimal;
//    private String match;
   
    /**
     * 数字微调按钮
     * @param name 表单元素名
     * @param label 标题
     * @param value 值
     * @param minValue 允许填写的最小值
     * @param maxValue 允许填写的最大值
     * @param step 步长，每点一次加/减，变动的数字
     */
    public Spinner(String name, String label, Number value, Number minValue,
            Number maxValue, Number step) {
		 super.setName(name);
		 super.setLabel(label);
		 setValue(value);
		 this.setMinValue(minValue);
		 this.setMaxValue(maxValue);
		 this.setStep(step);
	}

    /**
     * 设置最大值
     * @param maxValue Number
     * @return 本身，这样可以继续设置其他属性
     */
    public Spinner setMaxValue(Number maxValue) {
    	this.addValidate(Validate.maxvalue(maxValue==null?null:String.valueOf(maxValue)));
//        this.maxValue = maxValue;
    	return this;
    }

    /**
     * 设置最小值
     * @param minValue Number
     * @return 本身，这样可以继续设置其他属性
     */
    public Spinner setMinValue(Number minValue) {
    	this.addValidate(Validate.minvalue(minValue==null?null:String.valueOf(minValue)));
    	return this;
    }

    /**
     * 设置步长
     * @param step Number
     * @return 本身，这样可以继续设置其他属性
     */
    public Spinner setStep(Number step) {
        this.step = step;
        return this;
    }

    /**
     * 设置 输入值应该匹配的模式
     * @param match String
     * @return 本身，这样可以继续设置其他属性
     * @deprecated 使用addPatternValidate 代替
     */
	@Deprecated
    public Spinner setMatch(String match) {
       	this.addValidate(Validate.pattern(match));
       	return this;
    }

	/**
	 *  步长
	 * @return  步长
	 */
	public Number getStep() {
		return step;
	}

	@Override
    public String getType() {
		return "spinner";
	}

	@Override
	public Spinner setValue(Object value) {
		this.value=toNumber(value);
		return this;
	}

	/**
	 * 小数精度
	 * 设为0时，只允许输入整数。设为正整数，则限制小数的最大位数。设为负数，则不限整数和小数。默认值为0
	 * @return Integer
	 */
	public Integer getDecimal() {
		return this.decimal;
	}

	/**
	 * 小数精度
	 * 设为0时，只允许输入整数。设为正整数，则限制小数的最大位数。设为负数，则不限整数和小数。默认值为0
	 * @param decimal Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Spinner setDecimal(Integer decimal) {
		this.decimal = decimal;
		return this;
	}

}
