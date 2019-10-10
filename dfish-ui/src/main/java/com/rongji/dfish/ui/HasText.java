package com.rongji.dfish.ui;

/**
 * 很多对象含有text属性，用于文本显示。
 * @author DFish Team
 * @version 1.0
 * @param <T> 当前对象类型
 * @since XMLTMPL 2.0
 */
public interface HasText<T extends HasText<T>> extends HtmlContentHolder<T>{
    /**
     * 设置 显示文本text
     * @param text String
     * @return 本身，这样可以继续设置其他属性
     */
    T setText(String text);

    /**
     * 取得 显示文本text
     * @return String
     */
    String getText();
}
