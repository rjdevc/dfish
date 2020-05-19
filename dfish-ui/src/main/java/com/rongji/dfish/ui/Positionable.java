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
	 * 位置正中
	 */
	String POSITION_CENTER="c";
	/**
	 * 位置正中
	 */
	@Deprecated
	String POSITION_MIDDLE=POSITION_CENTER;
	/**
	 * 位置右上(东北)
	 */
	String POSITION_TOP_RIGHT="tr";
	/**
	 * 位置右上(东北)
	 */
	@Deprecated
	String POSITION_TOPRIGHT=POSITION_TOP_RIGHT;

	/**
	 * 位置右上(东北)
	 */
	String POSITION_RIGHT_TOP="rt";

	/**
	 * 位置左上(西北)
	 */
	String POSITION_TOPLEFT="tl";
	/**
	 * 位置左上(西北)
	 */
	String POSITION_LEFT_TOP="lt";

	/**
	 * 位置右下(东南)
	 */
	String POSITION_BOTTOM_RIGHT="br";
	/**
	 * 位置左下(西南)
	 */
	@Deprecated
	String POSITION_BOTTOMRIGHT=POSITION_BOTTOM_RIGHT;
	/**
	 * 位置左下(西南)
	 */
	@Deprecated
	String POSITION_SOUTHWEST=POSITION_BOTTOMRIGHT;
	/**
	 * 位置右下(东南)
	 */
	String POSITION_RIGHT_BOTTOM="rb";
	/**
	 * 位置左下(西南)
	 */
	String POSITION_BOTTOM_LEFT="bl";

	/**
	 * 位置左下(西南)
	 */
	String POSITION_LEFT_BOTTOM="lb";

	/**
	 * 位置左
	 */
	String POSITION_LEFT="l";
	/**
	 * 位置右
	 */
	String POSITION_RIGHT="r";
	/**
	 * 位置上
	 */
	String POSITION_TOP="t";
	/**
	 * 位置下
	 */
	String POSITION_BOTTOM="b";


}
