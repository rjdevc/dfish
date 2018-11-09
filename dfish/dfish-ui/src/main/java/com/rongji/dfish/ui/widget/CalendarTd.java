package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;

/**
 * Calendar里面每个元素按钮的默认值
 * @author DFish Team
 *
 */
public class CalendarTd extends AbstractWidget<CalendarTd>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6176298189403402501L;

	public  CalendarTd(){}
	public  CalendarTd(String text){
		this.setText(text);
	}
	private String text;
	public String getText() {
		return text;
	}
	public CalendarTd setText(String text) {
		this.text = text;
		return this;
	}
	public String getType() {
		return null;
//		return "calendar/td";
	}
}
