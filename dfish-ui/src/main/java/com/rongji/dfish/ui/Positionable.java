package com.rongji.dfish.ui;

/**
 * 该对象可以设置位置，一般是一个弹出窗口
 * 对话框弹出位置，可选值: 0(默认) 1 2 3 4 5 6 7 8 9。其中 0 为页面中心点，1-9是页面八个角落方位。
 * @author DFish Team
 *
 * @param <T>  当前对象类型
 */
public interface Positionable<T extends Positionable<T>> {
	/**
	 * 对话框弹出位置，可选值: 0(默认) 1 2 3 4 5 6 7 8 9。其中 0 为页面中心点，1-9是页面八个角落方位。
	 * @param position 位置
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setPosition(String position);
	/**
	 * 对话框弹出位置，可选值: 0(默认) 1 2 3 4 5 6 7 8 9。其中 0 为页面中心点，1-9是页面八个角落方位。
	 * @return 本身，这样可以继续设置其他属性
	 */
	String getPosition();
	/**
	 * 如果设为 true, 页面和对话框之间将覆盖一层半透明蒙版。
	 * @return 本身，这样可以继续设置其他属性
	 */
	Boolean getCover();
	/**
	 * 如果设为 true, 页面和对话框之间将覆盖一层半透明蒙版。
	 * @param cover Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setCover(Boolean cover);
	/**
	 * 定时关闭，单位:毫秒。-1则不关闭
	 * @return 本身，这样可以继续设置其他属性
	 */
	Long getTimeout();
	/**
	 * 定时关闭，单位:毫秒。-1则不关闭
	 * @param timeout Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setTimeout(Long timeout);
	
	/**
	 * 位置正中
	 */
	public static String POSITION_CENTER="c";
	/**
	 * 位置正中
	 */
	@Deprecated
	public static String POSITION_MIDDLE=POSITION_CENTER;
	/**
	 * 位置右上(东北)
	 */
	public static String POSITION_TOP_RIGHT="tr";
	/**
	 * 位置右上(东北)
	 */
	@Deprecated
	public static String POSITION_TOPRIGHT=POSITION_TOP_RIGHT;

	/**
	 * 位置右上(东北)
	 */
	public static String POSITION_RIGHT_TOP="rt";

	/**
	 * 位置左上(西北)
	 */
	public static String POSITION_TOPLEFT="tl";
	/**
	 * 位置左上(西北)
	 */
	public static String POSITION_LEFT_TOP="lt";

	/**
	 * 位置右下(东南)
	 */
	public static String POSITION_BOTTOM_RIGHT="br";
	/**
	 * 位置左下(西南)
	 */
	@Deprecated
	public static String POSITION_BOTTOMRIGHT=POSITION_BOTTOM_RIGHT;
	/**
	 * 位置左下(西南)
	 */
	@Deprecated
	public static String POSITION_SOUTHWEST=POSITION_BOTTOMRIGHT;
	/**
	 * 位置右下(东南)
	 */
	public static String POSITION_RIGHT_BOTTOM="rb";
	/**
	 * 位置左下(西南)
	 */
	public static String POSITION_BOTTOM_LEFT="bl";

	/**
	 * 位置左下(西南)
	 */
	public static String POSITION_LEFT_BOTTOM="lb";

	/**
	 * 位置左
	 */
	public static String POSITION_LEFT="l";
	/**
	 * 位置右
	 */
	public static String POSITION_RIGHT="r";
	/**
	 * 位置上
	 */
	public static String POSITION_TOP="t";
	/**
	 * 位置下
	 */
	public static String POSITION_BOTTOM="b";


}
