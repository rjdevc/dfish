package com.rongji.dfish.ui.command;

/**
 * 表示该对象可以设置弹出窗口高度
 * @author DFish Team
 *
 * @param <T> 当前对象
 */
public interface DialogHeight<T extends DialogHeight<T>> {

//	/**
//	 * 小窗口高度
//	 */
//	public static final int HEIGHT_SMALL=250;
//	/**
//	 * 中等窗口高度
//	 */
//	public static final int HEIGHT_MEDIUM=400;
//	/**
//	 * 大窗口高度
//	 */
//	public static final int HEIGHT_LARGE=560;
//	/**
//	 * 最大窗口高度，会根据当前分辨率自动算出一个高度。
//	 */
//	public static final int HEIGHT_MAX=-1;
	/**
	 * 小窗口高度
	 */
	public static final String HEIGHT_SMALL = "250";
	/**
	 * 中等窗口高度
	 */
	public static final String HEIGHT_MEDIUM = "400";
	/**
	 * 大窗口高度
	 */
	public static final String HEIGHT_LARGE = "560";
	/**
	 * 最大窗口高度，会根据当前分辨率自动算出一个高度。
	 */
	public static final String HEIGHT_MAX = "*";
	
	 /**
     * 设置高度
     * @param height String
     * @return 本身，这样可以继续设置其他属性
     */
    T setHeight(int height) ;
    /**
     * 设置高度
     * @param height String
     * @return 本身，这样可以继续设置其他属性
     */
    T setHeight(String height) ;
    /**
     * 高度
     * @return String
     */
    String getHeight();
    
    /**
	 * 最大高度
	 * @return Integer
	 */
    String getMaxheight();
	/**
	 * 最大高度
	 * @param minheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMaxheight(int minheight);
	/**
	 * 最大高度
	 * @param minheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMaxheight(String minheight);
	
    /**
	 * 最小高度
	 * @return Integer
	 */
	String getMinheight();
	/**
	 * 最小高度
	 * @param minheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMinheight(int minheight);
	/**
	 * 最小高度
	 * @param minheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMinheight(String minheight);

}
