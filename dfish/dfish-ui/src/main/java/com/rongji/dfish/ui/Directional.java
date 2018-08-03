package com.rongji.dfish.ui;

/**
 * 该对象可以排列方向，一般是含有子节点的组件
 * 方向，可选值 v:垂直,h:水平
 * @author DFish Team
 *
 * @param <T>  当前对象类型
 */
public interface Directional<T extends Directional<T>> {

	/**
	 * 排列方向-水平
	 */
	String DIR_HORIZONTAL = "h";
	/**
	 * 排列方向-垂直
	 */
	String DIR_VERTICAL = "v";
	/**
	 * 获取排列方向
	 * @return String
	 */
	String getDir();
	/**
	 * 设置排列方向
	 * @param dir String
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setDir(String dir);
	
}
