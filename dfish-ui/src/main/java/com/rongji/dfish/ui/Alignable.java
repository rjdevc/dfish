package com.rongji.dfish.ui;

/**
 * Alignable 指定 可以设置水平对齐方式的对象
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
public interface Alignable<T extends Alignable<T>> {
	/**
	 * 左对齐
	 */
	public static final String ALIGN_LEFT="left"; 
	/**
	 * 居中对齐
	 */
	public static final String ALIGN_CENTER="center"; 
	/**
	 * 右对齐
	 */
	public static final String ALIGN_RIGHT="right"; 

	/**
	 * 部件内容的水平对齐方式
	 * @return String
	 */
	public String getAlign();
	/**
	 * 部件内容的水平对齐方式
	 * @param align String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setAlign(String align);

}
