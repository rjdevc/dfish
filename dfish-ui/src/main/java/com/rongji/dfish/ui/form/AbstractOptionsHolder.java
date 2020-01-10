package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.Option;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.ui.HtmlContentHolder;

import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * AbstractOptionsHolder 抽象的含有options的部件。
 * dfish3中 不建议使用Select 并且RadioGroup和CheckboxGroup重新实现了以后，实际上该类也只提供给XBox使用。
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
@SuppressWarnings("unchecked")
public abstract class AbstractOptionsHolder<T extends AbstractOptionsHolder<T, N>, N> extends AbstractFormElement<T, Object> implements HtmlContentHolder<T >{
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
		this.doSetValue(value);
		this.doSetOptions(options);
        fixChecked();
	}
	
	protected Boolean escape;
	protected List<N> options;
//    protected Boolean br;
//    protected String align;
//	protected String valign;

	/**
     * 设置候选项
     * @param options List
	 * @return 本身，这样可以继续设置其他属性
     */
	public T setOptions(List<?> options) {
        doSetOptions(options);
        fixChecked();
        return(T)this;
    }

    protected void doSetOptions(List<?> options) {
        List<N> result=new ArrayList<N>(options==null?0:options.size());

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
                N node = null;
                if (item instanceof Option) {
                    node=buildOption((Option)item);
                }else if(item instanceof AbstractBox<?>){
                    ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
                    // 获取第一个类型参数的真实类型
                    Class nodeClass = (Class<T>) pt.getActualTypeArguments()[1];
                    if(nodeClass.isAssignableFrom(item.getClass())){
                        node=(N)item;
                    }
                } else {

                    String label = null;
                    Object value = null;
                    String ic = null;
                    if (item instanceof Object[] || item instanceof String[]) {
                        Object[] castItem = (Object[]) item;
                        value = castItem[0];
                        if (castItem.length > 2) {
                            ic = String.valueOf(castItem[2]);
                        } else if (castItem.length > 1) {
                            label = String.valueOf(castItem[1]);
                        } else {
                            label = String.valueOf(castItem[0]);
                        }
                    } else if (item instanceof String || item instanceof Number ||
                            item instanceof java.util.Date) {
                        value = item;
                        label = String.valueOf(item);
                    } else {
                        LogUtil.error(getClass(), "invalid options item " + item + " ,should be Object[] ", null);
                        break;
                    }
                    Option o = new Option(value, label);
                    o.setIcon(ic);
                    o.setChecked(theValue.contains(value) ? true : null);
                    node=buildOption(o);
                }
                if(node!=null) {
                    result.add(node);
                }
            }
        }
        this.options=result;
    }
    /**
     * 将统一的Option转化为当前对象专有的选项对象N，如RadioGroup中是Radio
     * @param o Option
     * @return N
     */
    protected abstract N buildOption(Option o);
    protected N buildOption(Object value,String text,Boolean checked){
        return buildOption(new Option(value,text).setChecked(checked));
    }



    protected void fixChecked(){
        if(this.value==null){
            //如果value没有值，则不进行判断以option自己的checked为准
            return;
        }
        Set<String> theValue = null;
        if (value == null) {
            theValue = new HashSet<String>();
        } else if (value instanceof int[]) {
            int[] cast = (int[]) value;
            theValue = new HashSet<String>();
            for (int o : cast) {
                theValue.add(String.valueOf(o));
            }
        } else if (value instanceof char[]) {
            char[] cast = (char[]) value;
            theValue = new HashSet<String>();
            for (char o : cast) {
                theValue.add(String.valueOf(o));
            }
        } else if (value instanceof long[]) {
            long[] cast = (long[]) value;
            theValue = new HashSet<String>();
            for (long o : cast) {
                theValue.add(String.valueOf(o));
            }
        } else if (value.getClass().isArray()) {
            Object[] cast = (Object[]) value;
            theValue = new HashSet<String>();
            for (Object o : cast) {
                theValue.add(o == null ? null : o.toString());
            }
        } else if (value instanceof Collection) {
            Collection<?> cast = (Collection<?>) value;
            theValue = new HashSet<String>();
            for (Object o : cast) {
                theValue.add(o == null ? null : o.toString());
            }
        } else {
            theValue = new HashSet<String>();
            theValue.add(value == null ? null : value.toString());
        }
        if (theValue.size() == 0) {
            theValue.add(null);
            theValue.add("");
        }
//		nodes.clear();
        if (options != null) {
            for (Object item : options) {
                if (item == null) {
                    continue;
                } else if (item instanceof Option) {
                    Option o = (Option) item;
                    boolean checked=theValue.contains(o.getValue() == null ? null : o.getValue().toString());
                    o.setChecked(checked?true:null);

                } else if(item instanceof AbstractBox<?>) {
                    AbstractBox<?> o = (AbstractBox<?>) item;
                    boolean checked=theValue.contains(o.getValue() == null ? null : o.getValue().toString());
                    o.setChecked(checked?true:null);
                }
            }
        }
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
//
//	 /**
//     * 设置 已选中的值(多项)
//     * @param obj Object[]
//	 * @return 本身，这样可以继续设置其他属性
//     */
//	public T setValue(Object[] obj) {
//        this.value = (P) obj;
//        return(T)this;
//    }

    /**
     * 设置 已选中的值(单项)
     * @param obj Object[]
     */
	@Override
	public T setValue(Object obj) {
    	doSetValue(obj);
        fixChecked();
        return (T)this;
    }
    protected void doSetValue(Object obj){
        if(obj==null){
            //do nothing
        }else if(obj instanceof Object[]||
                obj instanceof String[]){
            this.value =  obj;
        }else if(obj instanceof Collection<?>){
            Collection<?> cast=(Collection<?>)obj;
            this.value = cast.toArray();
        }else{
            value = new Object[]{obj};
        }
    }
	
    /**
	 * @return the options
	 */
	public List<?> getOptions() {
		return options;
	}

	@Override
    protected List<? extends Object> findNodes(){
	    return this.options;
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
