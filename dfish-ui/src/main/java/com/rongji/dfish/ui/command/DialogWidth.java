package com.rongji.dfish.ui.command;

/**
 * 表示当前对象可以设置弹出窗口宽度
 * @author DFish Team
 *
 * @param <T> 当前对象
 */
public interface DialogWidth<T extends DialogWidth<T>> {
//	/**
//	 * 小窗口宽度
//	 */
//	public static final int WIDTH_SMALL=440;
//	/**
//	 * 中等窗口宽度
//	 */
//	public static final int WIDTH_MEDIUM=620;
//	/**
//	 * 大窗口宽度
//	 */
//	public static final int WIDTH_LARGE=980;
//	/**
//	 * 最大窗口宽度，会根据当前分辨率自动算出一个宽度。
//	 */
//	public static final int WIDTH_MAX=-1;
	/**
	 * 小窗口宽度
	 */
	public static final String WIDTH_SMALL = "440";
	/**
	 * 中等窗口宽度
	 */
	public static final String WIDTH_MEDIUM = "620";
	/**
	 * 大窗口宽度
	 */
	public static final String WIDTH_LARGE = "980";
	/**
	 * 最大窗口宽度，会根据当前分辨率自动算出一个宽度。
	 */
	public static final String WIDTH_MAX = "*";

	 /**
     * 设置宽度
     * @param width Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setWidth(int width) ;
    /**
     * 设置宽度
     * @param width Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setWidth(String width) ;
    /**
     * 宽度
     * @return String
     */
    String getWidth();
    
    /**
	 * 最大宽度
	 * @return Integer
	 */
    String getMaxwidth();
	/**
	 * 最大宽度
	 * @param maxwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMaxwidth(String maxwidth);
	/**
	 * 最大宽度
	 * @param maxwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMaxwidth(int maxwidth);
	
    /**
	 * 最小宽度
	 * @return Integer
	 */
	String getMinwidth();
	/**
	 * 最小宽度
	 * @param minwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMinwidth(int minwidth);
	/**
	 * 最小宽度
	 * @param minwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMinwidth(String minwidth);
}
