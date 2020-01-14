package com.rongji.dfish.ui;

/**
 * Valignable 指定 可以设置垂直对齐方式的对象
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
public interface VAlignable<T extends VAlignable<T>> {
    /**
     * 上对齐
     */
    String V_ALIGN_TOP = "top";
    /**
     * 居中对齐
     */
    String V_ALIGN_MIDDLE = "middle";
    /**
     * 下对齐
     */
    String V_ALIGN_BOTTOM = "bottom";

	/**
	 * 上对齐
	 */
	String VALIGN_TOP = V_ALIGN_TOP;
	/**
	 * 居中对齐
	 */
	String VALIGN_MIDDLE = V_ALIGN_MIDDLE;
	/**
	 * 下对齐
	 */
	String VALIGN_BOTTOM = V_ALIGN_BOTTOM;

    /**
     * 部件内容的垂直对齐方式
     *
     * @return String
     */
    public String getVAlign();

    /**
     * 部件内容的垂直对齐方式
     *
     * @param vAlign String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setVAlign(String vAlign);
}
