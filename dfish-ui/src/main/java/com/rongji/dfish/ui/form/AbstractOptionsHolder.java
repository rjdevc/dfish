package com.rongji.dfish.ui.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.rongji.dfish.base.Option;
import com.rongji.dfish.ui.HtmlContentHolder;

/**
 * AbstractOptionsHolder 抽象的含有options的部件。
 * dfish3中 不建议使用Select 并且RadioGroup和CheckboxGroup重新实现了以后，实际上该类也只提供给XBox使用。
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
@SuppressWarnings("unchecked")
public abstract class AbstractOptionsHolder<T extends AbstractOptionsHolder<T, P>, P> extends AbstractFormElement<T, P> implements HtmlContentHolder<T >{
	private static final long serialVersionUID = -1184424744014200791L;

	/**
	 * @param name 表单名
	 * @param label 标签
	 * @param value 选中的值
	 * @param options 候选项
	 */
	public AbstractOptionsHolder(String name, String label, Object value, List<?> options){
		this.setName(name);
		this.setLabel(label);
		this.setValue(value);
		this.setOptions(options);
//		escape=true;
	}
	
	protected Boolean escape;
	protected List<?> options;
//    protected Boolean br;
//    protected String align;
//	protected String valign;

	/**
     * 设置候选项
     * @param options List
	 * @return 本身，这样可以继续设置其他属性
     */
	public T setOptions(List<?> options) {
        this.options = options;
        return(T)this;
    }
//    /**
//     * 是否每个选项独立一行.
//     * @param br boolean
//     * @return 本身，这样可以继续设置其他属性
//     */
//    @SuppressWarnings("unchecked")
//	public T setBr(Boolean br) {
//        this.br = br;
//        return(T)this;
//    }
//	/**
//	 * 每个选项占一行
//	 * @return the br
//	 */
//	public Boolean getBr() {
//		return br;
//	}
	
	 /**
     * 设置 已选中的值(多项)
     * @param obj Object[]
	 * @return 本身，这样可以继续设置其他属性
     */
	public T setValue(Object[] obj) {
        this.value = (P) obj;
        return(T)this;
    }

    /**
     * 设置 已选中的值(单项)
     * @param obj Object[]
     */
	@Override
	public T setValue(Object obj) {
    	if(obj==null){
    		//do nothing
    	}else if(obj instanceof Object[]){
    		this.value = (P) obj;
    	}else if(obj instanceof String[]){
    		this.value = (P) obj;
    	}else if(obj instanceof Collection<?>){
    		Collection<?> cast=(Collection<?>)obj;
    		this.value = (P) cast.toArray();
    	}else{
    		value = (P) new Object[]{obj};
    	}
    	
        return (T)this;
    }
	
    /**
	 * @return the options
	 */
	public List<?> getOptions() {
		List<Option> result=new ArrayList<Option>(options==null?0:options.size());
		
		Set<Object> theValue=null;
		if(value==null){
			theValue=new HashSet<Object>(0);
		}else if(value.getClass().isArray()){
			Object[] cast=(Object[])value;
			theValue=new HashSet<Object>(Arrays.asList(cast));
		}else if(value instanceof Collection){
			Collection<?> cast=(Collection<?>)value;
			theValue=new HashSet<Object>(cast);
		}else{
			theValue=new HashSet<Object>(0);
			theValue.add(value);
		}
		if(options!=null){
		for(Object item:options){
			if(item ==null ){
				continue;
			}

			Option o = null;
			if (item instanceof Option) {
				o = (Option) item;
			} else {
				String label=null;
				Object value=null; 
				String ic=null;
				if (item instanceof Object[]|| item instanceof String[]) {
					Object[] castItem = (Object[]) item;
					value = castItem[0];
					if (castItem.length > 2) {
						ic= String.valueOf(castItem[2]);
					}else if (castItem.length > 1) {
						label = String.valueOf(castItem[1]);
					} else {
						label = String.valueOf(castItem[0]);
					}
					
				} else if (item instanceof String || item instanceof Number ||
						item instanceof java.util.Date) {
					value = item;
					label =  String.valueOf(item);
				} else {
					LOG.error("invalide options item " + item + " ,should be Object[] ");
					break;
				}
				o = new Option(value, label);
				o.setIcon(ic);
				o.setChecked(theValue.contains(value)?true:null);
			}
			result.add(o);
		}
		}
		return result;
	}
	
//	protected static final void removeOpitonsProperties(Class<?> clz){
////		removeValidationProperties(clz);
//		JsonBuilder builder=J.get(clz);
//		builder.removeProperty("value");
//	}
//	/**
//	 * 水平对齐方式。可选值: left, center, right
//	 * @return align
//	 */
//	public String getAlign() {
//		return align;
//	}
//	/**
//	 * 水平对齐方式。可选值: left, center, right
//	 * @param align
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	@SuppressWarnings("unchecked")
//	public T setAlign(String align) {
//		this.align = align;
//		return (T)this;
//	}
//	/**
//	 * 垂直对齐方式。可选值: top, middle, bottom
//	 * @return valign
//	 */
//	public String getValign() {
//		return valign;
//	}
//	/**
//	 * 垂直对齐方式。可选值: top, middle, bottom
//	 * @param valign
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	@SuppressWarnings("unchecked")
//	public T setValign(String valign) {
//		this.valign = valign;
//		return (T)this;
//	}
	@Override
	public Boolean getEscape() {
		return this.escape;
	}
	
	@Override
	public T setEscape(Boolean escape) {
		this.escape = escape;
		return (T)this;
	}
}
