package com.rongji.dfish.ui.form;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 * <p>CheckboxTag 这里支持多个checkbox或者称mutibox</p>
 * <p>多个的时候，值是由一个List传进来的。List里的数据格式详见OptionsFrag类</p>
 * <p>单个的时候，直接输入，期望值和显示字样即可</p>
 * @author DFish Team
 * @version 1.2
 * @since XMLTMPL 1.0
 */
public class Checkboxgroup extends AbstractBoxgroup<Checkboxgroup,Checkbox,Object> {
	private static final long serialVersionUID = -7269251020373915061L;
	/**
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 选中的项
	 * @param options 候选项
	 */
	public Checkboxgroup(String name, String label, Object value,
			List<?> options) {
		super(name, label, value, options);
	}
	@Override
	protected Checkbox buildOption(Option o) {
		return new Checkbox(null,null,o.getChecked(),o.getValue()==null?null:o.getValue().toString(),o.getText());
	}
	@Override
    public String getType() {
		return "checkboxgroup";
	}

	@Override
	public Checkboxgroup setValue(Object value) {
		this.value= value;
		Object checkedValue=value;
		Set<String> theValue=null;
		if(checkedValue==null){
			theValue=new HashSet<String>();
		}else if(checkedValue instanceof int[]){
			int[] cast=(int[])checkedValue;
			theValue=new HashSet<String>();
			for(int o:cast){
				theValue.add(String.valueOf(o));
			}
		}else if(checkedValue instanceof char[]){
			char[] cast=(char[])checkedValue;
			theValue=new HashSet<String>();
			for(char o:cast){
				theValue.add(String.valueOf(o));
			}
		}else if(checkedValue instanceof long[]){
			long[] cast=(long[])checkedValue;
			theValue=new HashSet<String>();
			for(long o:cast){
				theValue.add(String.valueOf(o));
			}
		}else if(checkedValue.getClass().isArray()){
			Object[] cast=(Object[])checkedValue;
			theValue=new HashSet<String>();
			for(Object o:cast){
				theValue.add(o==null?null:o.toString());
			}
		}else if(checkedValue instanceof Collection){
			Collection<?> cast=(Collection<?>)checkedValue;
			theValue=new HashSet<String>();
			for(Object o:cast){
				theValue.add(o==null?null:o.toString());
			}
		}else{
			theValue=new HashSet<String>();
			theValue.add(checkedValue==null?null:checkedValue.toString());
		}
		if(theValue.size()==0){
			theValue.add(null);
			theValue.add("");
		}
		if (nodes != null) {
			for (Checkbox r : nodes) {
				boolean checked =theValue.contains(r.getValue());
				r.setChecked(checked?checked:null);
			}
		}
		return this;
	}
	@Override
    public Checkbox getPub() {
		if (pub == null) {
			pub = new Checkbox(null, null, null, null, null);
		}
	    return pub;
    }
	
}
