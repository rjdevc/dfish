package com.rongji.dfish.ui;

import java.util.Map;

/**
 * DataContainer 为可以设置数据属性的对象。
 * <p>一个widget 就是一个典型的这类对象。他们可以设置内置的数据属性。
 * 这些数据属性并不直接提交，他们往往用于编程控制。</p>
 * @author DFish Team
 * @param <T> 当前对象类型
 * @since V3.0
 */
public interface DataContainer<T extends DataContainer<T>> {
    /**
     * 设置属性
     * @param key 数值属性的关键字
     * @param value 数值属性的值
     * @return 本身，这样可以继续设置其他属性
     */
    T setData(String key,Object value);
    /**
     * 取得设置的所有属性
     * @return Map
     */
    Map<String,Object> getData();
    /**
     * 获取某个属性的值
     * @param key 数值属性的关键字
     * @return Object
     */
    Object getData(String key);
    /**
     * 移除某个属性的值
     * @param key 数值属性的关键字
     * @return Object
     */
    Object removeData(String key);
    
}