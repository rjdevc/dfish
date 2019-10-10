package com.rongji.dfish.ui.layout;

/**
 * 对象查看器，主要就是平铺显示Album 或列表显示 GridLayout
 * @author DFish Team
 *
 * @param <T>  当前对象类型
 */
public interface ListView <T extends ListView<T>>{
//	/**
//	 * 是否可以被选中
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	Boolean getFocusable();
//
//	/**
//	 * 是否可以被选中
//	 * @param focusable
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	T setFocusable(Boolean focusable);
	/**
	 * 是否可以选中多项
	 * @return 本身，这样可以继续设置其他属性
	 */
	Boolean getFocusmultiple() ;

	/**
	 * 是否可以选中多项
	 * @param focusmultiple Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setFocusmultiple(Boolean focusmultiple) ;

	/**
	 * 是否有鼠标悬停效果
	 * @return Boolean
	 */
	Boolean getHoverable();

	/**
	 * 是否有鼠标悬停效果
	 * @param hoverable Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setHoverable(Boolean hoverable);
	
	/**
	 * 内容不换行
	 * @param nobr boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setNobr(Boolean nobr);
	
	/**
	 * 内容不换行
	 * @return Boolean
	 */
	Boolean getNobr();
}
