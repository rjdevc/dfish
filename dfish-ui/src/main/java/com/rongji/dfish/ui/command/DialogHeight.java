package com.rongji.dfish.ui.command;

/**
 * 表示该对象可以设置弹出窗口高度
 *
 * @param <T> 当前对象
 * @author DFish Team
 * @version 2.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 */
public interface DialogHeight<T extends DialogHeight<T>> {

    /**
     * 小窗口高度
     */
    String HEIGHT_SMALL = "250";
    /**
     * 中等窗口高度
     */
    String HEIGHT_MEDIUM = "400";
    /**
     * 大窗口高度
     */
    String HEIGHT_LARGE = "560";
    /**
     * 最大窗口高度，会根据当前分辨率自动算出一个高度。
     */
    String HEIGHT_MAX = "*";

    /**
     * 设置高度
     *
     * @param height String
     * @return 本身，这样可以继续设置其他属性
     */
    T setHeight(int height);

    /**
     * 设置高度
     *
     * @param height String
     * @return 本身，这样可以继续设置其他属性
     */
    T setHeight(String height);

    /**
     * 高度
     *
     * @return String
     */
    String getHeight();

    /**
     * 最大高度
     *
     * @return String
     */
    String getMaxHeight();

    /**
     * 最大高度
     *
     * @param minHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMaxHeight(int minHeight);

    /**
     * 最大高度
     *
     * @param minHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMaxHeight(String minHeight);

    /**
     * 最小高度
     *
     * @return String
     */
    String getMinHeight();

    /**
     * 最小高度
     *
     * @param minHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMinHeight(int minHeight);

    /**
     * 最小高度
     *
     * @param minHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMinHeight(String minHeight);

}
