package com.rongji.dfish.misc.util;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Description: Json解析工具,实现基于阿里的fastjson
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		YuLM
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018年4月20日 下午5:32:01		YuLM			1.0				1.0 Version
 */
public class JsonUtil {

	/**
	 * 转json字符串
	 * @param object
	 * @return
	 * @author YuLM
	 */
	public static String toJson(Object object) {
		return JSON.toJSONString(object);
	}
	
	public static <T> T parseObject(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}
	
	public static <T> List<T> parseArray(String json, Class<T> clazz) {
		return JSON.parseArray(json, clazz);
	}
	
}
