package com.rongji.dfish.ui;
/**
 * Valignable 指定 可以设置垂直对齐方式的对象
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
public interface Valignable<T extends Valignable<T>> {
	/**
	 * 上对齐
	 */
	public static final String VALIGN_TOP="top"; 
	/**
	 * 居中对齐
	 */
	public static final String VALIGN_MIDDLE="middle"; 
	/**
	 * 下对齐
	 */
	public static final String VALIGN_BOTTOM="bottom"; 
	/**
	 * 部件内容的垂直对齐方式
	 * @return String
	 */
	public String getvAlign();
	/**
	 * 部件内容的垂直对齐方式
	 * @param vAlign String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setvAlign(String vAlign);
}
