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
	private Boolean focus;
	private Boolean focusable;

	/**
	 * 显示内容
	 * @return String
	 */
	public String getText() {
		return text;
	}

	/**
	 * 显示内容
	 * @param text String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public CalendarTd setText(String text) {
		this.text = text;
		return this;
	}

	/**
	 * 是否焦点
	 * @return Boolean
	 */
	public Boolean getFocus() {
		return focus;
	}

	/**
	 * 是否聚焦
	 * @param focus Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public CalendarTd setFocus(Boolean focus) {
		this.focus = focus;
		return this;
	}

	/**
	 * 是否可聚焦模式
	 * @return
	 */
	public Boolean getFocusable() {
		return focusable;
	}

	/**
	 * 是否可聚焦模式
	 * @param focusable Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public CalendarTd setFocusable(Boolean focusable) {
		this.focusable = focusable;
		return this;
	}

	@Override
    public String getType() {
		return "calendar/td";
	}
}
