package com.rongji.dfish.ui;


/**
 * Scrollable 标记当前部件允许出现滚动条，以便内容能够完整呈现
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
public interface Scrollable<T extends Scrollable<T>> extends Widget<T>{
	/**
	 * 显示系统默认的滚动条
	 */
	public static final String SCROLL_SCROLL="scroll";
	/**
	 * 不显示滚动条
	 */
	public static final String SCROLL_HIDDEN="hidden";
	/**
	 * 自动确定要不要显示滚动条，当窗口不够显示内容时显示
	 */
	public static final String SCROLL_AUTO="auto";
	/**
	 * 显示一个窄边的滚动条。并且鼠标不在范围内的时候不显示。
	 */
	public static final String SCROLL_MINI="miniscroll";
	
	/**
	 * 设置这个widget是否需要滚动条
	 * @param scroll Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setScroll(Boolean scroll);
	/**
	 * 取得这个widget是否需要滚动条
	 * @return Boolean
	 */
	Boolean getScroll();
	/**
     * 设置这个widget滚动条的样式
     * @param scrollClass String
	 * @return 本身，这样可以继续设置其他属性
     */
    T setScrollClass(String scrollClass);
    /**
     * 取得这个widget滚动条的样式
     * @return String
     */
    String getScrollClass();

}
