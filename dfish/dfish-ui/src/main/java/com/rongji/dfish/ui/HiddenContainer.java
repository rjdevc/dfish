package com.rongji.dfish.ui;

import java.util.List;

import com.rongji.dfish.ui.form.Hidden;


/**
 * HiddenContainer 表示这个元素支持设置隐藏值。
 * <p>Widget 也是一个典型的HiddenContainer。
 * 所有设置在这个对象中的隐藏值可以被提交。但不保证这个对象一定可见。</p>
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
public interface HiddenContainer<T extends HiddenContainer<T>> {
	/**
	 * 设置隐藏值
	 * @param name String
	 * @param value String
	 * @return 本身，这样可以继续设置其他属性
	 */
	T addHidden(String name, String value);
	/**
	 * 取得所有隐藏值
	 * @return Map
	 */
	List<Hidden> getHiddens();
	/**
     * 获取某个隐藏值
     * @param name 隐藏值的关键字
     * @return List 隐藏值
     */
    List<String> getHiddenValue(String name);
    /**
     * 移除某个隐藏值
     * @param name 隐藏值的关键字
     * @return 本身，这样可以继续设置其他属性
     */
    T removeHidden(String name);
}
