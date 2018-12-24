package com.rongji.dfish.ui.helper;

import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.LabelRow;
import com.rongji.dfish.ui.json.JsonWrapper;

class FlexGridAppendingMode implements JsonWrapper<Widget<?>> {
	private FlexGridAppendingMode(){}
	private String mode;
	private Widget<?> p;
	private Integer occupy;
	public static FlexGridAppendingMode widget(Widget<?> w,Integer occupy){
		FlexGridAppendingMode mode=new FlexGridAppendingMode();
		mode.mode=MODE_WIDGET;
		mode.p=w;
		mode.occupy=occupy;
		return mode;
	}
	public static FlexGridAppendingMode labelRow(LabelRow<?> w,Integer occupy){
		FlexGridAppendingMode mode=new FlexGridAppendingMode();
		mode.mode=MODE_LABEL_ROW;
		mode.p=w;
		mode.occupy=occupy;
		if("0".equals(w.getWidth())){
			w.setWidth(null);
		}
		return mode;
	}
	
	public static final String MODE_WIDGET="W";
	public static final String MODE_LABEL_ROW="L";
	public String getMode(){
		return mode;
	}
	@Override
	public Widget<?> getPrototype() {
		return p;
	}
	public void setPrototype(Widget<?> w) {
		p=w;
	}
	public Integer getOccupy() {
		return occupy;
	}
	public void setOccupy(Integer occupy) {
		this.occupy = occupy;
	}
}
