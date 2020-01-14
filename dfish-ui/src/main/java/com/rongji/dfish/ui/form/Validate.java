package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.ui.AbstractNode;

import java.util.Map;

/**
 * 校验
 * @author DFish Team
 *
 */
public class Validate extends AbstractNode<Validate> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6934337282036061111L;
	/**
	 * 大于
	 */
	public static final String COMPARE_MODE_GREATER_THAN=">";
	/**
	 * 小于
	 */
	public static final String COMPARE_MODE_LESS_THAN="<";
	/**
	 * 小于或等于
	 */
	public static final String COMPARE_MODE_NOT_GREATER_THAN="<=";
	/**
	 * 大于或等于
	 */
	public static final String COMPARE_MODE_NOT_LESS_THAN=">=";
	/**
	 * 等于
	 */
	public static final String COMPARE_MODE_EQUALS="==";
	/**
	 * 必填
	 */
	private Boolean required;
	/**
	 * 必填提示文本  ${t}不能为空
	 */
	private String requiredText;
	/**
	 * 正则表达式
	 */
	private String pattern;
	/**
	 * 正则提示文本
	 */
	private String patternText;
	/**
	 * 另一个表单的name。用于简单的比较
	 */
	private String compare;
	/**
	 * 比较符号，可选值: > >= < <= ==。
	 */
	private String compareMode;
	/**
	 * 比较提示文本。
	 */
	private String compareText;
	/**
	 * 最小字节数
	 */
	private Integer minLength;
	/**
	 * 最小字节数提示文本
	 */
	private String minLengthText;
	/**
	 * 最大字节数。用于 text textarea password
	 */
	private Integer maxLength;
	/**
	 * 最大字节数提示文本
	 */
	private String maxLengthText;
	/**
	 * 最少选择几项。用于 checkbox
	 */
	private Integer minSize;
	/**
	 * 最少选择几项提示文本
	 */
	private String minSizeText;
	/**
	 * 最多选择几项。用于 checkbox
	 */
	private Integer maxSize;
	/**
	 * 最多选择几项提示文本
	 */
	private String maxSizeText;
	/**
	 * 最小值。用于 spinner date
	 */
	private String minValue;
	/**
	 * 最小值提示文本。 默认值为  ${t}不能小于于${maxvalue}
	 */
	private String minValueText;
	/**
	 * 最大值。用于 spinner date
	 */
	private String maxValue;
	/**
	 * 最大值提示文本。默认值为  ${t}不能大于${maxvalue}
	 */
	private String maxValueText;
	/**
	 * 不能大于当前时间。用于 date
	 */
	private Boolean beforeNow;
	/**
	 * 必填提示文本  ${t}不能为空
	 */
	private String beforeNowText;
	/**
	 * 不能小于当前时间的显示文本。
	 */
	private Boolean afterNow;
	/**
	 * 必填提示文本  ${t}不能为空
	 */
	private String afterNowText;
	/**
	 * JS语句。如果验证不通过，执行语句应当 return 一个字符串作为说明。如果验证通过则无需返回或返回空。
	 */
	private String method;

	@Override
	public String getType() {
		return null;
	}
	
	
	/**
	 * 校验不能为空
	 * @return required
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * 校验不能为空,必填
	 * @param required Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setRequired(Boolean required) {
		this.required = required;
		return this;
	}


	/**
	 * 必填提示文本。
	 * @return requiredtext
	 */
	public String getRequiredText() {
		return requiredText;
	}


	/**
	 * 必填提示文本。
	 * @param requiredText 必填提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setRequiredText(String requiredText) {
		this.requiredText = requiredText;
		return this;
	}


	/**
	 * 正则表达式
	 * @return pattern
	 */
	public String getPattern() {
		return pattern;
	}


	/**
	 * 模式(正则表达式)
	 * @param pattern 模式
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setPattern(String pattern) {
		this.pattern = pattern;
		return this;
	}


	/**
	 * 正则提示文本
	 * @return  patterntext
	 */
	public String getPatternText() {
		return patternText;
	}


	/**
	 * 模式(正则表达式)
	 * @param patternText 模式
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setPatternText(String patternText) {
		this.patternText = patternText;
		return this;
	}


	/**
	 * 另一个表单的name。用于简单的比较。
	 * @return compare
	 */
	public String getCompare() {
		return compare;
	}


	/**
	 * 另一个表单的name。用于简单的比较。
	 * @param compare 另一个表单的name
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setCompare(String compare) {
		this.compare = compare;
		return this;
	}


	/**
	 * 比较符号，可选值: &gt; / &gt;= / &lt; / &lt;= / ==。
	 * @return comparemode String
	 */
	public String getCompareMode() {
		return compareMode;
	}


	/**
	 * 比较符号，可选值: &gt; / &gt;= / &lt; / &lt;= / ==。
	 * @param compareMode String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setCompareMode(String compareMode) {
		this.compareMode = compareMode;
		return this;
	}


	/**
	 * 比较提示文本
	 * @return comparetext
	 */
	public String getCompareText() {
		return compareText;
	}


	/**
	 * 比较结果，如果不吻合，提示文本
	 * @param compareText 比较结果，如果不吻合，提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setCompareText(String compareText) {
		this.compareText = compareText;
		return this;
	}


	/**
	 * 最小字节数。
	 * @return  minlength
	 */
	public Integer getMinLength() {
		return minLength;
	}


	/**
	 * 最小字节数。
	 * @param minLength 最小字节数。
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMinLength(Integer minLength) {
		if (minLength != null && minLength <= 0) {
			minLength = null;
		}
		this.minLength = minLength;
		return this;
	}


	/**
	 * 最小字节数提示文本。
	 * @return  minlengthtext
	 */
	public String getMinLengthText() {
		return minLengthText;
	}


	/**
	 * 最小字节数，如果不吻合，提示文本。
	 * @param minLengthText 提示文本。
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMinLengthText(String minLengthText) {
		this.minLengthText = minLengthText;
		return this;
	}


	/**
	 * 最大字节数。
	 * @return maxlength
	 */
	public Integer getMaxLength() {
		return maxLength;
	}


	/**
	 * 最大字节数
	 * @param maxLength 最大字节数
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMaxLength(Integer maxLength) {
		if (maxLength != null && maxLength <= 0) {
			maxLength = null;
		}
		this.maxLength = maxLength;
		return this;
	}


	/**
	 * 最大字节数提示文本。
	 * @return maxlengthtext
	 */
	public String getMaxLengthText() {
		return maxLengthText;
	}


	/**
	 * 最大字节数，如果不吻合，提示文本。
	 * @param maxLengthText 提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMaxLengthText(String maxLengthText) {
		this.maxLengthText = maxLengthText;
		return this;
	}

	/**
	 * 最少选择几项。用于 checkbox
	 * @return minsize
	 */
	public Integer getMinSize() {
		return minSize;
	}


	/**
	 * 最少选择几项。用于 checkbox
	 * @param minSize 最少选择几项
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMinSize(Integer minSize) {
		if (minSize != null && minSize <= 0) {
			minSize = null;
		}
		this.minSize = minSize;
		return this;
	}


	/**
	 * 最少选择几项提示文本。
	 * @return minsizetext
	 */
	public String getMinSizeText() {
		return minSizeText;
	}


	/**
	 * 最少选择几项，如果不吻合，提示文本。
	 * @param minSizeText 提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMinSizeText(String minSizeText) {
		this.minSizeText = minSizeText;
		return this;
	}


	/**
	 * 最多选择几项。用于 checkbox
	 * @return maxsize
	 */
	public Integer getMaxSize() {
		return maxSize;
	}


	/**
	 * 最多选择几项。用于 checkbox
	 * @param maxSize 最多选择几项
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMaxSize(Integer maxSize) {
		if (maxSize != null && maxSize <= 0) {
			maxSize = null;
		}
		this.maxSize = maxSize;
		return this;
	}


	/**
	 * 最多选择几项提示文本
	 * @return  maxsizetext
	 */
	public String getMaxSizeText() {
		return maxSizeText;
	}


	/**
	 * 最多选择几项，如果不吻合，提示文本
	 * @param maxSizeText 提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMaxSizeText(String maxSizeText) {
		this.maxSizeText = maxSizeText;
		return this;
	}


	/**
	 * 最小值。用于 spinner date
	 * @return  minvalue
	 */
	public String getMinValue() {
		return minValue;
	}


	/**
	 * 最小值。用于 spinner date
	 * @param minValue 最小值
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMinValue(String minValue) {
		this.minValue = minValue;
		return this;
	}


	/**
	 * 最小值提示文本。
	 * @return minvaluetext
	 */
	public String getMinValueText() {
		return minValueText;
	}


	/**
	 * 最小值，如果不吻合，提示文本。
	 * @param minValueText 提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMinValueText(String minValueText) {
		this.minValueText = minValueText;
		return this;
	}


	/**
	 * 最大值。用于 spinner date
	 * @return  maxvalue 最大值
	 */
	public String getMaxValue() {
		return maxValue;
	}


	/**
	 * 最大值。用于 spinner date
	 * @param maxValue 最大值
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMaxValue(String maxValue) {
		this.maxValue = maxValue;
		return this;
	}


	/**
	 * 最大值提示文本。
	 * @return maxvaluetext
	 */
	public String getMaxValueText() {
		return maxValueText;
	}


	/**
	 * 最大值，如果不吻合，提示文本。
	 * @param maxValueText 提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMaxValueText(String maxValueText) {
		this.maxValueText = maxValueText;
		return this;
	}

	/**
	 *JS语句。如果验证不通过，执行语句应当 return 一个字符串作为说明。如果验证通过则无需返回或返回空。
	 * @return method
	 */
	public String getMethod() {
		return method;
	}


	/**
	 * JS语句。如果验证不通过，执行语句应当 return 一个字符串作为说明。如果验证通过则无需返回或返回空。
	 * @param method String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMethod(String method) {
		this.method = method;
		return this;
	}
	/**
	 * 不能大于当前时间。用于 date
	 * @return Boolean
	 */
	public Boolean getBeforeNow() {
		return beforeNow;
	}

	/**
	 * 不能大于当前时间。用于 date
	 * @param beforeNow Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setBeforeNow(Boolean beforeNow) {
		this.beforeNow = beforeNow;
		return this;
	}

	/**
	 * 不能大于当前时间的显示文本。
	 * @return String
	 */
	public String getBeforeNowText() {
		return beforeNowText;
	}

	/**
	 * 不能大于当前时间的提示文本。
	 * @param beforeNowText 提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setBeforeNowText(String beforeNowText) {
		this.beforeNowText = beforeNowText;
		return this;
	}

	/**
	 * 不能小于当前时间。用于 date
	 * @return Boolean
	 */
	public Boolean getAfterNow() {
		return afterNow;
	}

	/**
	 * 不能小于当前时间。用于 date
	 * @param afterNow Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setAfterNow(Boolean afterNow) {
		this.afterNow = afterNow;
		return this;
	}

	/**
	 * 不能小于当前时间的显示文本。
	 * @return String
	 */
	public String getAfterNowText() {
		return afterNowText;
	}

	/**
	 * 不能小于当前时间的显示文本。
	 * @param afterNowText String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setAfterNowText(String afterNowText) {
		this.afterNowText = afterNowText;
		return this;
	}
	
	/**
	 * 便捷构建一个必填的校验
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate required(){
		return new Validate().setRequired(true);
	}
	/**
	 * 便捷构建一个模式的校验
	 * @param pattern 模式(正则表达式) 
	 * @return 本身，这样可以继续设置其他属性
	 * 
	 */
	public static Validate pattern(String pattern){
		return new Validate().setPattern(pattern);
	}
	
	/**
	 * 便捷构建一个对比的校验
	 * @param comparemode 对比模式
	 * @param compare 对比另外一个表单的名字
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate compare(String comparemode,String compare){
		return new Validate().setCompareMode(comparemode).setCompare(compare);
	}
	/**
	 * 便捷构建一个最大长度的校验
	 * @param maxlength  最大长度
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate maxlength(Integer maxlength){
		return new Validate().setMaxLength(maxlength);
	}
	/**
	 * 便捷构建一个最多选择项的校验
	 * @param maxsize  最多选择项
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate maxsize(Integer maxsize){
		return new Validate().setMaxSize(maxsize);
	}
	/**
	 * 便捷构建一个最大值的校验
	 * @param maxvalue   最大值
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate maxvalue(String maxvalue){
		return new Validate().setMaxValue(maxvalue);
	}
	
	/**
	 * 便捷构建一个最小长度的校验
	 * @param minlength  最小长度
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate minlength(Integer minlength){
		return new Validate().setMinLength(minlength);
	}
	/**
	 * 便捷构建一个最小选择项的校验
	 * @param minsize 最小选择项
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate minsize(Integer minsize){
		return new Validate().setMinSize(minsize);
	}
	/**
	 * 便捷构建一个最小值的校验
	 * @param minvalue 最小值
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate minvalue(String minvalue){
		return new Validate().setMinValue(minvalue);
	}
	
	/**
	 * 便捷构建一个早于当前时间的校验
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate beforenow() {
		return new Validate().setBeforeNow(true);
	}
	
	/**
	 * 便捷构建一个晚于当前时间的校验
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate afternow() {
		return new Validate().setAfterNow(true);
	}
	/**
	 * 合并另一个条件，以便输出
	 * @param other 另一个条件
	 */
	public void combine(Validate other){
		if (other == null) {
			return;
		}
		try {
			Map<String,Object>props=BeanUtil.getPropMap(other);
			props.remove("class");
			props.remove("type");
			for(Map.Entry<String, Object> entry:props.entrySet()){
				String name=entry.getKey();
				Object value=entry.getValue();
				if(name.endsWith("text")){
					continue;
				}
				BeanUtil.invokeMethod(this, "set" +(char)(name.charAt(0)-32) +name.substring(1), new Object[]{value});
				if(!"comparemode".equals(name)){//comparemode 和compare共用一个comparetext
					try{
						BeanUtil.invokeMethod(this, "set" +(char)(name.charAt(0)-32) +name.substring(1)+"text", new Object[]{props.get(name+"text")});
					}catch(NoSuchMethodException ex){
						//do nothing
						LogUtil.error(getClass(),null,ex);
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error(getClass(),null,e);
		}
		
//		Method[] methods = this.getClass().getDeclaredMethods();
//		for (Method method : methods) { // 通过类反射去绑定
//			if (method.getName().startsWith("get")) {
//				try {
//	                Object value = method.invoke(other);
//	                if (value != null) {
//	                	Class<?> paramType = method.getReturnType();
//	                	Method setter = getClass().getMethod("set" + method.getName().substring(3), paramType);
//	                	setter.invoke(this, value);
//	                }
//                } catch (Exception e) {
//	                e.printStackTrace();
//                }
//			}
//		}
		
//		if(other.getRequired()!=null){
//			this.setRequired(other.getRequired());
//			this.setRequiredtext(other.getRequiredtext());
//		}
//		if(other.getCompare()!=null){
//			this.setCompare(other.getCompare());
//			this.setComparemode(other.getComparemode());
//			this.setComparetext(other.getComparetext());
//		}
//		if(other.getMaxlength()!=null){
//			this.setMaxlength(other.getMaxlength());
//			this.setMaxlengthtext(other.getMaxlengthtext());
//		}
//		if(other.getMaxsize()!=null){
//			this.setMaxsize(other.getMaxsize());
//			this.setMaxsizetext(other.getMaxsizetext());
//		}
//		if(other.getMaxvalue()!=null){
//			this.setMaxvalue(other.getMaxvalue());
//			this.setMaxvaluetext(other.getMaxvaluetext());
//		}
//		if(other.getMinlength()!=null){
//			this.setMinlength(other.getMinlength());
//			this.setMinlengthtext(other.getMinlengthtext());
//		}
//		if(other.getMinsize()!=null){
//			this.setMinsize(other.getMinsize());
//			this.setMinsizetext(other.getMinsizetext());
//		}
//		if(other.getMinvalue()!=null){
//			this.setMinvalue(other.getMinvalue());
//			this.setMinvaluetext(other.getMinvaluetext());
//		}
//		if(other.getPattern()!=null){
//			this.setPattern(other.getPattern());
//			this.setPatterntext(other.getPatterntext());
//		}
//		if (other.getBeforenow() != null) {
//			this.setBeforenow(other.getBeforenow());
//			this.setBeforenowtext(other.getBeforenowtext());
//		}
//		if (other.getAfternow() != null) {
//			this.setAfternow(other.getAfternow());
//			this.setAfternowtext(other.getAfternowtext());
//		}
//		if(other.getMethod()!=null){
//			this.setMethod(other.getMethod());
//		}
	}
	/**
	 * 清楚校验条件
	 */
	public void clear(){
		BeanUtil.copyPropertiesExact(this, new Validate());
	}

}
