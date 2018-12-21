package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.json.JsonWrapper;

public class FormLabel extends AbstractFormLabel<FormLabel> implements JsonWrapper<Object>{

	private static final long serialVersionUID = -1384522916094820984L;
	public FormLabel(String text){
		this.setText(text);
	}
	@Override
	public Object getPrototype() {
		if(isComplex()){
			JsonFormLabel jo=new JsonFormLabel();
			copyProperties(jo, this);
			return jo;
		}
		return this.text;
	}
	private boolean isComplex(){
		return !"0".equals(getWidth())||
				getCls()!=null||getStyle()!=null ||//常用的属性排在前面
				getId()!=null||getHeight()!=null||
				getAlign()!=null||getEscape()!=null||
				getAftercontent()!=null||getBeforecontent()!=null||
				getGid()!=null||getHmin()!=null||
				getMaxheight()!=null||getMaxwidth()!=null||
				getMinheight()!=null||getMinwidth()!=null||
				(getOn()!=null&&getOn().size()>0)||
				getWmin()!=null;
    }
	
}
