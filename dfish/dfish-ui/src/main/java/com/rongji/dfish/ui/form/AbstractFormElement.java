package com.rongji.dfish.ui.form;

import java.util.Map;
import java.util.TreeMap;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.FormElement;
import com.rongji.dfish.ui.Statusful;

/**
 * 抽象的FromElement 用于方便formElelemt的编写。
 * FormElement不包含Label， 而LabelRow不包含Hidden。
 * 这里抽象FormElement 这两个都不包含。
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 * @param <N> value对象类型
 */
@SuppressWarnings("unchecked")
public abstract class AbstractFormElement<T extends AbstractFormElement<T,N>,N> extends AbstractWidget<T> implements
        FormElement<T,N>, LabelRow<T>, Validatable<T>, Statusful<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6099805917622842572L;

	/**
	 * 名字
	 */
	protected String name;

	/**
	 * 标题
	 */
	protected String label;
	/**
	 * 值
	 */
	protected N value;
	/**
	 * 是否检查非空
	 */
	protected Boolean star;
//	/**
//	 * 是否只读
//	 */
//	protected Boolean readonly;
//	/**
//	 * 是否无效
//	 */
//	protected Boolean disabled;
	protected String status;
	
	// /**
	// * 是否占整行，已过时，现在替换成{@link #hideLabel}
	// * 这个不是必要属性，只是在helper里面才会使用它做辅助
	// */
	// @Deprecated
	// protected boolean fullLine;
	/**
	 * 是否隐藏标题 这个不是必要属性，只是在helper里面才会使用它做辅助
	 */
	protected Boolean hideLabel;

	protected Object tip;
	
	TreeMap<String,Validate> valids = new TreeMap<String,Validate>();// new
															   // HashMap<String,ValidationPartBean>();



//	public static final String[] DEFAULT_GROUP = new String[] { "default" };


	public T setName(String name) {
		this.name = name;
		return (T) this;
	}

	public abstract T setValue(Object value);
	
	@Override
	public Boolean getStar() {
		return this.star;
	}

	public String getName() {
		return name;
	}

	public N getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public T setLabel(String label) {
		this.label = label;
		return (T) this;
	}
	
	@Deprecated
	public T setTitle(String label) {
		this.label = label;
		return (T) this;
	}

	/**
	 * 是否只读。
	 * @return  Boolean
	 * @deprecated
	 */
	@Deprecated
	public Boolean getReadonly() {
		return STATUS_READONLY.equals(status);
	}
	
	/**
	 * 是否只读。
	 * @param readonly 只读
	 * @return 本身，这样可以继续设置其他属性
	 * @deprecated
	 */
	@Deprecated
	public T setReadonly(Boolean readonly) {
		if(readonly==null||!readonly){
			if(STATUS_READONLY.equals(status)){
				status=null;
			}
		}else{
			if(status==null||STATUS_NORMAL.equals(status)){
				status=STATUS_READONLY;
			}
		}
		return (T) this;
	}
	
	/**
	 * 状态。
	 * @return  String
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 *  设置状态
	 * @param status String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setStatus(String status) {
		this.status = status;
		return (T) this;
	}

	/**
	 * 是否禁用。
	 * @return  disabled
	 * @see #getStatus()
	 */
	@Deprecated
	public Boolean getDisabled() {
		return STATUS_DISABLED.equals(status);
	}

	/**
	 * 是否禁用。
	 * @param disabled 禁用
	 * @return 本身，这样可以继续设置其他属性
	 * @see #setStatus(String)
	 */
	@Deprecated
	public T setDisabled(Boolean disabled) {
		if(disabled==null||!disabled){
			if(STATUS_DISABLED.equals(status)){
				status=null;
			}
		}else{
			if(status==null||STATUS_NORMAL.equals(status)){
				status=STATUS_DISABLED;
			}
		}
		return (T) this;
	}


	 /**
     * 这个元素，是否非空，这里非空不是dfish2.4以后的真实判断，而是在界面显示一个非空标记
     * 通常是红色字体的半角星号(*)
     * @param required boolean
     * @return 本身，这样可以继续设置其他属性
     * @since 3.0
     * @deprecated notnull 属性已经被 required替代
     */
	@Deprecated
	public T setNotnull(Boolean required) {
		return setRequired(required);
	}
	/**
     * 这个元素，是否非空，
     * 这个方法同时在界面显示一个非空标记
     * 并增加一个非空判断
     * @param required boolean
     * @return 本身，这样可以继续设置其他属性
     * @since 3.0
     */
	public T setRequired(Boolean required) {
		this.star = required;
		if(required==null||!required){
			if(this.getValidate()!=null){
				this.getValidate().setRequired(null);
			}
		}else{
			if(this.getValidate()!=null){
				this.getValidate().setRequired(true);
			}else{
				this.addValidate(Validate.required());
			}
		}
		return (T) this;
	}
	public T setStar(Boolean star) {
		this.star = star;
		return (T) this;
	}

	/**
	 * 设置是隐藏标题
	 * @param hideLabel boolean
	 * @return 本身，这样可以继续设置其他属性
	 * @see #setHideLabel
	 * @deprecated move to setHideLabel
	 */
	@Deprecated
	public T setFullLine(Boolean hideLabel) {
		this.hideLabel = hideLabel;
		return (T) this;
	}

	public Boolean getHideLabel() {
		return hideLabel;
	}

	public T setHideLabel(Boolean hideLabel) {
		this.hideLabel = hideLabel;
		return (T) this;
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public Object getJsonType() {
	// Map<String,Object> jsonType=(Map<String,Object>)super.getJsonType();
	// jsonType.remove("mxl");
	// jsonType.remove("maxSize");
	// jsonType.remove("maxValue");
	// jsonType.remove("minSize");
	// jsonType.remove("minValue");
	// jsonType.remove("match");
	// jsonType.remove("patternMsg");
	// return jsonType;
	// }
	
	@Override
	public Validate getValidate() {
		return valids.get(Validatable.DEFAULT_VALIDATE_NAME);
	}

	public Validate getValidate(String name) {
		return valids.get(name);
	}
	@Override
	public Map<String, Validate> getValidategroup() {
		//除了 DEFAULT_VALIDATE_NAME 外所有
//		return valid.getValidategroup();
		Map<String, Validate> result=new TreeMap<String, Validate>(valids);
		result.remove(Validatable.DEFAULT_VALIDATE_NAME);
		return result;
	}
	
	public T addValidate(Validate validate){
		return addValidate(Validatable.DEFAULT_VALIDATE_NAME, validate);
	}
	public T addValidate(String name,Validate validate){
		if(validate==null){
			return (T)this;
		}
		if(valids.containsKey(name)){
			Validate old=valids.get(name);
			old.combine(validate);
		}else{
			valids.put(name,validate);
		}
		return (T)this;
	}
	public T setValidate(Validate validate){
		valids.put(Validatable.DEFAULT_VALIDATE_NAME, validate);
		return (T)this;
	}
	public T setValidate(String name,Validate validate){
		valids.put(name,validate);
		return (T)this;
	}
	public T removeValidate(String name){
		valids.remove(name);
		return (T)this;
	}

	/**
	 * 鼠标移上去的提示语
	 * @return Object
	 */
	public Object getTip() {
		return tip;
	}

	/**
	 * 设置鼠标移上去是否显示提示语
	 * @param tip Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setTip(Boolean tip) {
		this.tip = tip;
		return (T) this;
	}
	
	/**
	 * 设置鼠标移上去显示的提示语
	 * @param tip String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setTip(String tip) {
		this.tip = tip;
		return (T) this;
	}
	
}
