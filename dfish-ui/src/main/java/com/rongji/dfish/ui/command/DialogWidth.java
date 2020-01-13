package com.rongji.dfish.ui.command;

/**
 * 表示当前对象可以设置弹出窗口宽度
 *
 * @param <T> 当前对象
 * @author DFish Team
 * @version 2.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 2.0
 */
public interface DialogWidth<T extends DialogWidth<T>> {
    /**
     * 小窗口宽度
     */
    String WIDTH_SMALL = "440";
    /**
     * 中等窗口宽度
     */
    String WIDTH_MEDIUM = "620";
    /**
     * 大窗口宽度
     */
    String WIDTH_LARGE = "980";
    /**
     * 最大窗口宽度，会根据当前分辨率自动算出一个宽度。
     */
    String WIDTH_MAX = "*";

    /**
     * 设置宽度
     *
     * @param width Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setWidth(int width);

    /**
     * 设置宽度
     *
     * @param width Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setWidth(String width);

    /**
     * 宽度
     *
     * @return String
     */
    String getWidth();

    /**
     * 最大宽度
     *
     * @return String
     */
    String getMaxWidth();

    /**
     * 最大宽度
     *
     * @param maxWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMaxWidth(String maxWidth);

    /**
     * 最大宽度
     *
     * @param maxWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMaxWidth(int maxWidth);

    /**
     * 最小宽度
     *
     * @return Integer
     */
    String getMinWidth();

    /**
     * 最小宽度
     *
     * @param minWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMinWidth(int minWidth);

    /**
     * 最小宽度
     *
     * @param minWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMinWidth(String minWidth);
}
