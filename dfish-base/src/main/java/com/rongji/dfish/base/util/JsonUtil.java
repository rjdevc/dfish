package com.rongji.dfish.base.util;

import com.rongji.dfish.base.context.SystemContext;
import com.rongji.dfish.base.util.json.JsonBuilder;

import java.util.List;

/**
 * Json处理工具类
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class JsonUtil {

    private static JsonBuilder jsonBuilder;

    static {
        jsonBuilder = (JsonBuilder) SystemContext.getInstance().get("jsonBuilder");
        if (jsonBuilder == null) {
            String className = "com.rongji.dfish.misc.util.json.impl.JsonBuilder4Fastjson";
            if (BeanUtil.exists(className)) {
                jsonBuilder = (JsonBuilder) BeanUtil.newInstance(className);
            }
        }
    }

    /**
     * 对象转json方法
     *
     * @param obj 待解析对象
     * @return String
     */
    public static String toJson(Object obj) {
        String json = null;
        try {
            json = jsonBuilder.toJson(obj);
        } catch (Exception e) {
            LogUtil.error("Json转换异常", e);
        }
        return json;
    }

    /**
     * json字符转对象方法
     *
     * @param json     json字符
     * @param objClass 对象类
     * @param <T>      泛型类
     * @return T, 解析对象
     */
    public static <T> T parseObject(String json, Class<T> objClass) {
        T obj = null;
        try {
            obj = jsonBuilder.parseObject(json, objClass);
        } catch (Exception e) {
            LogUtil.error("Json转换异常", e);
        }
        return obj;
    }

    /**
     * json字符转对象集合方法
     *
     * @param json     json字符
     * @param objClass 对象类
     * @param <T>      泛型类
     * @return List&lt;T&gt;解析对象集合
     */
    public static <T> List<T> parseArray(String json, Class<T> objClass) {

        List<T> objList = null;
        try {
            objList = jsonBuilder.parseArray(json, objClass);
        } catch (Exception e) {
            LogUtil.error("Json转换异常", e);
        }
        return objList;
    }

}
