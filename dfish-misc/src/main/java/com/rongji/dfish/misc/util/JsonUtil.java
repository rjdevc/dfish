package com.rongji.dfish.misc.util;

import com.rongji.dfish.misc.util.json.JsonBuilder;
import com.rongji.dfish.misc.util.json.impl.JsonBuilder4Fastjson;

import java.util.List;

/**
 * Description: Json解析工具,实现基于阿里的fastjson
 * FIXME 这个类可能可以删除,直接通过JsonBuilder接口进行调用
 * Copyright:   Copyright © 2018
 * Company:     rongji
 *
 * @author lamontYu
 * @version 1.0 Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018年4月20日 下午5:32:01		lamontYu			1.0				1.0 Version
 */
public class JsonUtil {

    private static JsonBuilder jsonBuilder;

    static {
        // 这个类的默认实现是fastjson
        jsonBuilder = new JsonBuilder4Fastjson();
    }

    /**
     * 转json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return jsonBuilder.toJson(object);
    }

    public static <T> T parseObject(String json, Class<T> objClass) {
        return jsonBuilder.parseObject(json, objClass);
    }

    public static <T> List<T> parseArray(String json, Class<T> objClass) {
        return jsonBuilder.parseArray(json, objClass);
    }

}
