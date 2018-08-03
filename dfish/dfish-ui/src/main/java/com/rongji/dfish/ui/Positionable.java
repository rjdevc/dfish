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
	T setPosition(Integer position);
	/**
	 * 对话框弹出位置，可选值: 0(默认) 1 2 3 4 5 6 7 8 9。其中 0 为页面中心点，1-9是页面八个角落方位。
	 * @return 本身，这样可以继续设置其他属性
	 */
	Integer getPosition();
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
	 * 定时关闭，单位:秒。-1则不关闭
	 * @return 本身，这样可以继续设置其他属性
	 */
	Integer getTimeout();
	/**
	 * 定时关闭，单位:秒。-1则不关闭
	 * @param timeout Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setTimeout(Integer timeout);
	
	/**
	 * 位置正中
	 */
	public static int POSITION_MIDDLE=0;
	/**
	 * 位置右上(东北)
	 */
	public static int POSITION_NORTHEAST=2;
	/**
	 * 位置右上(东北)
	 */
	public static int POSITION_TOPRIGHT=2;
	/**
	 * 位置左上(西北)
	 */
	public static int POSITION_NORTHWEST=1;
	/**
	 * 位置左上(西北)
	 */
	public static int POSITION_TOPLEFT=1;
	/**
	 * 位置右下(东南)
	 */
	public static int POSITION_SOUTHEAST=5;
	/**
	 * 位置右下(东南)
	 */
	public static int POSITION_BOTTOMRIGHT=5;
	/**
	 * 位置左下(西南)
	 */
	public static int POSITION_SOUTHWEST=6;
	/**
	 * 位置左下(西南)
	 */
	public static int POSITION_BOTTOMLEFT=6;
	
	/**
	 * 位置右上(东北)，并且弹出窗口是从右上角上从右向左滑出
	 */
	public static int POSITION_NORTHEAST_POPFROM_EAST=3;
	/**
	 * 位置右上(东北)，并且弹出窗口是从右上角上从右向左滑出
	 */
	public static int POSITION_TOPRIGHT_POPFROM_RIGHT=3;
}
