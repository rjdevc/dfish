package com.rongji.dfish.ui;

import java.io.Serializable;
import java.util.Map;


/**
 * JsonObject 是指在dfish-ui提醒中可以和前端交互的内容，这些内容被转化成json对象输出的内容。
 * <p>有别与默认的JAVA bean。为了能让前端识别json的作用，这些对象，必须包含一个type属性。
 *
 * @author DFish Team
 * @version 1.1 DFish team
 * @since DFish3.0
 */
public interface Node<T extends Node<T>> extends Serializable {

    /**
     * 获取这个对象的种类名
     * <p>种类名称一般标明这个对象的功能。JS引擎会根据这个对象的种类为其赋予一定的功能.
     * 这个种类名称一般是该类名的全称或缩写。如TableLayout的种类名称是grid</p>
     * <p>少数对象只能以某些对象的子元素出现，这时候type值可能为空</p>
     *
     * @return String
     */
    default String getType() {
        return getClass().getSimpleName();
    }

    /**
     * asJson就是转成JSON字符串。
     *
     * @return String
     */
    default String asJson() {
        return toString();
    }

    ;

    /**
     * JsonObject的toString就是转成JSON字符串。
     *
     * @return String
     */
    @Override
    String toString();

    /**
     * 设置属性
     *
     * @param key   数值属性的关键字
     * @param value 数值属性的值
     * @return 本身，这样可以继续设置其他属性
     */
    T putData(String key, Object value);

    /**
     * 取得设置的所有属性
     *
     * @return Map
     */
    Map<String, Object> getData();

    /**
     * 获取某个属性的值
     *
     * @param key 数值属性的关键字
     * @return Object
     */
    Object getData(String key);

    /**
     * 移除某个属性的值
     *
     * @param key 数值属性的关键字
     * @return Object
     */
    Object removeData(String key);

    /**
     * 设置ID
     * 自定义的ID。前端可通过 view.find( id ) 方法来获取 widget。
     * 后端一般也有对应的findNodeById 等方法。一般建议，需要控制的面板都要设置ID
     *
     * @param id String
     * @return 本身，这样可以继续设置其他属性
     */
    T setId(String id);

    /**
     * 取得ID
     * 自定义的ID。前端可通过 view.find( id ) 方法来获取 widget。
     * 后端一般也有对应的findNodeById 等方法。一般建议，需要控制的面板都要设置ID
     *
     * @return String
     */
    String getId();

}
