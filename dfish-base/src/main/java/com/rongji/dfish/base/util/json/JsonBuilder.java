package com.rongji.dfish.base.util.json;

import java.util.List;

/**
 * Json对象构建器,主要提供对象与json字符之间的相互解析方法
 * @author lamontYu
 * @date 2019-9-29
 */
public interface JsonBuilder {

    /**
     * 日期对象的格式
     * @return String
     */
    default String getDateFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }

    /**
     * 对象转json方法
     * @param obj 待解析对象
     * @return String
     */
    String toJson(Object obj);

    /**
     * json字符转对象方法
     * @param json json字符
     * @param objClass 对象类
     * @param <T> 泛型类
     * @return T,解析对象
     */
    <T> T parseObject(String json, Class<T> objClass);

    /**
     * json字符转对象集合方法
     * @param json json字符
     * @param objClass 对象类
     * @param <T> 泛型类
     * @return List&lt;T&gt;解析对象集合
     */
    <T> List<T> parseArray(String json, Class<T> objClass);

}
