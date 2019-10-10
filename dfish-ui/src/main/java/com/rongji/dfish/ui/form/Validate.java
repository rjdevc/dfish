package com.rongji.dfish.ui.form;

import java.util.Map;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.ui.AbstractJsonObject;

/**
 * 校验
 * @author DFish Team
 *
 */
public class Validate extends AbstractJsonObject {
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
	private Boolean required;//必填
	private String requiredtext;//必填提示文本  ${t}不能为空
	private String pattern;//正则表达式
	private String patterntext;//正则提示文本
	private String compare;//另一个表单的name。用于简单的比较
	private String comparemode;//比较符号，可选值: > >= < <= ==。
	private String comparetext;//比较提示文本。
	private Integer minlength;//最小字节数
	private String minlengthtext;//最小字节数提示文本
	private Integer maxlength;//最大字节数。用于 text textarea password
	private String maxlengthtext;//最大字节数提示文本
	private Boolean maxlengthcheck; // 设置为true，键入文本时将会即时检测是否超出最大长度。设置为false则在提交时检测。
	private Integer minsize;//最少选择几项。用于 checkbox
	private String minsizetext;//最少选择几项提示文本
	private Integer maxsize;//最多选择几项。用于 checkbox
	private String maxsizetext;//最多选择几项提示文本 
	private String minvalue;//最小值。用于 spinner date 
	private String minvaluetext;//最小值提示文本。 默认值为  ${t}不能小于于${maxvalue}
	private String maxvalue;//最大值。用于 spinner date 
	private String maxvaluetext;//最大值提示文本。默认值为  ${t}不能大于${maxvalue}
	private Boolean beforenow;//不能大于当前时间。用于 date
	private String beforenowtext;//必填提示文本  ${t}不能为空
	private Boolean afternow;//不能小于当前时间的显示文本。
	private String afternowtext;//必填提示文本  ${t}不能为空
	private String method;//JS语句。如果验证不通过，执行语句应当 return 一个字符串作为说明。如果验证通过则无需返回或返回空。

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
	public String getRequiredtext() {
		return requiredtext;
	}


	/**
	 * 必填提示文本。
	 * @param requiredtext 必填提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setRequiredtext(String requiredtext) {
		this.requiredtext = requiredtext;
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
	public String getPatterntext() {
		return patterntext;
	}


	/**
	 * 模式(正则表达式)
	 * @param patterntext 模式
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setPatterntext(String patterntext) {
		this.patterntext = patterntext;
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
	public String getComparemode() {
		return comparemode;
	}


	/**
	 * 比较符号，可选值: &gt; / &gt;= / &lt; / &lt;= / ==。
	 * @param comparemode String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setComparemode(String comparemode) {
		this.comparemode = comparemode;
		return this;
	}


	/**
	 * 比较提示文本
	 * @return comparetext
	 */
	public String getComparetext() {
		return comparetext;
	}


	/**
	 * 比较结果，如果不吻合，提示文本
	 * @param comparetext 比较结果，如果不吻合，提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setComparetext(String comparetext) {
		this.comparetext = comparetext;
		return this;
	}


	/**
	 * 最小字节数。
	 * @return  minlength
	 */
	public Integer getMinlength() {
		return minlength;
	}


	/**
	 * 最小字节数。
	 * @param minlength 最小字节数。
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMinlength(Integer minlength) {
		if (minlength != null && minlength <= 0) {
			minlength = null;
		}
		this.minlength = minlength;
		return this;
	}


	/**
	 * 最小字节数提示文本。
	 * @return  minlengthtext
	 */
	public String getMinlengthtext() {
		return minlengthtext;
	}


	/**
	 * 最小字节数，如果不吻合，提示文本。
	 * @param minlengthtext 提示文本。
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMinlengthtext(String minlengthtext) {
		this.minlengthtext = minlengthtext;
		return this;
	}


	/**
	 * 最大字节数。
	 * @return maxlength
	 */
	public Integer getMaxlength() {
		return maxlength;
	}


	/**
	 * 最大字节数
	 * @param maxlength 最大字节数
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMaxlength(Integer maxlength) {
		if (maxlength != null && maxlength <= 0) {
			maxlength = null;
		}
		this.maxlength = maxlength;
		return this;
	}


	/**
	 * 最大字节数提示文本。
	 * @return maxlengthtext
	 */
	public String getMaxlengthtext() {
		return maxlengthtext;
	}


	/**
	 * 最大字节数，如果不吻合，提示文本。
	 * @param maxlengthtext 提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMaxlengthtext(String maxlengthtext) {
		this.maxlengthtext = maxlengthtext;
		return this;
	}

	/**
	 * 设置为true，键入文本时将会即时检测是否超出最大长度。设置为false则在提交时检测。
	 * @return Boolean
	 * @author YuLM
	 */
	public Boolean getMaxlengthcheck() {
		return maxlengthcheck;
	}

	/**
	 * 设置为true，键入文本时将会即时检测是否超出最大长度。设置为false则在提交时检测。
	 * @param maxlengthcheck Boolean
	 * @return 本身，这样可以继续设置其他属性
	 * @author YuLM
	 */
	public Validate setMaxlengthcheck(Boolean maxlengthcheck) {
		this.maxlengthcheck = maxlengthcheck;
		return this;
	}


	/**
	 * 最少选择几项。用于 checkbox
	 * @return minsize
	 */
	public Integer getMinsize() {
		return minsize;
	}


	/**
	 * 最少选择几项。用于 checkbox
	 * @param minsize 最少选择几项
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMinsize(Integer minsize) {
		if (minsize != null && minsize <= 0) {
			minsize = null;
		}
		this.minsize = minsize;
		return this;
	}


	/**
	 * 最少选择几项提示文本。
	 * @return minsizetext
	 */
	public String getMinsizetext() {
		return minsizetext;
	}


	/**
	 * 最少选择几项，如果不吻合，提示文本。
	 * @param minsizetext 提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMinsizetext(String minsizetext) {
		this.minsizetext = minsizetext;
		return this;
	}


	/**
	 * 最多选择几项。用于 checkbox
	 * @return maxsize
	 */
	public Integer getMaxsize() {
		return maxsize;
	}


	/**
	 * 最多选择几项。用于 checkbox
	 * @param maxsize 最多选择几项
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMaxsize(Integer maxsize) {
		if (maxsize != null && maxsize <= 0) {
			maxsize = null;
		}
		this.maxsize = maxsize;
		return this;
	}


	/**
	 * 最多选择几项提示文本
	 * @return  maxsizetext
	 */
	public String getMaxsizetext() {
		return maxsizetext;
	}


	/**
	 * 最多选择几项，如果不吻合，提示文本
	 * @param maxsizetext 提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMaxsizetext(String maxsizetext) {
		this.maxsizetext = maxsizetext;
		return this;
	}


	/**
	 * 最小值。用于 spinner date
	 * @return  minvalue
	 */
	public String getMinvalue() {
		return minvalue;
	}


	/**
	 * 最小值。用于 spinner date
	 * @param minvalue 最小值
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMinvalue(String minvalue) {
		this.minvalue = minvalue;
		return this;
	}


	/**
	 * 最小值提示文本。
	 * @return minvaluetext
	 */
	public String getMinvaluetext() {
		return minvaluetext;
	}


	/**
	 * 最小值，如果不吻合，提示文本。
	 * @param minvaluetext 提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMinvaluetext(String minvaluetext) {
		this.minvaluetext = minvaluetext;
		return this;
	}


	/**
	 * 最大值。用于 spinner date
	 * @return  maxvalue 最大值
	 */
	public String getMaxvalue() {
		return maxvalue;
	}


	/**
	 * 最大值。用于 spinner date
	 * @param maxvalue 最大值
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMaxvalue(String maxvalue) {
		this.maxvalue = maxvalue;
		return this;
	}


	/**
	 * 最大值提示文本。
	 * @return maxvaluetext
	 */
	public String getMaxvaluetext() {
		return maxvaluetext;
	}


	/**
	 * 最大值，如果不吻合，提示文本。
	 * @param maxvaluetext 提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setMaxvaluetext(String maxvaluetext) {
		this.maxvaluetext = maxvaluetext;
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
	public Boolean getBeforenow() {
		return beforenow;
	}

	/**
	 * 不能大于当前时间。用于 date
	 * @param beforenow Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setBeforenow(Boolean beforenow) {
		this.beforenow = beforenow;
		return this;
	}

	/**
	 * 不能大于当前时间的显示文本。
	 * @return String
	 */
	public String getBeforenowtext() {
		return beforenowtext;
	}

	/**
	 * 不能大于当前时间的提示文本。
	 * @param beforenowtext 提示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setBeforenowtext(String beforenowtext) {
		this.beforenowtext = beforenowtext;
		return this;
	}

	/**
	 * 不能小于当前时间。用于 date
	 * @return Boolean
	 */
	public Boolean getAfternow() {
		return afternow;
	}

	/**
	 * 不能小于当前时间。用于 date
	 * @param afternow Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setAfternow(Boolean afternow) {
		this.afternow = afternow;
		return this;
	}

	/**
	 * 不能小于当前时间的显示文本。
	 * @return String
	 */
	public String getAfternowtext() {
		return afternowtext;
	}

	/**
	 * 不能小于当前时间的显示文本。
	 * @param afternowtext String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Validate setAfternowtext(String afternowtext) {
		this.afternowtext = afternowtext;
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
		return new Validate().setComparemode(comparemode).setCompare(compare);
	}
	/**
	 * 便捷构建一个最大长度的校验
	 * @param maxlength  最大长度
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate maxlength(Integer maxlength){
		return new Validate().setMaxlength(maxlength);
	}
	/**
	 * 便捷构建一个最多选择项的校验
	 * @param maxsize  最多选择项
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate maxsize(Integer maxsize){
		return new Validate().setMaxsize(maxsize);
	}
	/**
	 * 便捷构建一个最大值的校验
	 * @param maxvalue   最大值
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate maxvalue(String maxvalue){
		return new Validate().setMaxvalue(maxvalue);
	}
	
	/**
	 * 便捷构建一个最小长度的校验
	 * @param minlength  最小长度
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate minlength(Integer minlength){
		return new Validate().setMinlength(minlength);
	}
	/**
	 * 便捷构建一个最小选择项的校验
	 * @param minsize 最小选择项
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate minsize(Integer minsize){
		return new Validate().setMinsize(minsize);
	}
	/**
	 * 便捷构建一个最小值的校验
	 * @param minvalue 最小值
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate minvalue(String minvalue){
		return new Validate().setMinvalue(minvalue);
	}
	
	/**
	 * 便捷构建一个早于当前时间的校验
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate beforenow() {
		return new Validate().setBeforenow(true);
	}
	
	/**
	 * 便捷构建一个晚于当前时间的校验
	 * @return 本身，这样可以继续设置其他属性
	 */
	public static Validate afternow() {
		return new Validate().setAfternow(true);
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
						LOG.debug(null,ex);
					}
				}
			}
		} catch (Exception e) {
			LOG.debug(null,e);
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
