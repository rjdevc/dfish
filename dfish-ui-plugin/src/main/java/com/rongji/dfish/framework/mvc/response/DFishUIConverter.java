package com.rongji.dfish.framework.mvc.response;

import com.rongji.dfish.ui.JsonObject;

/**
 * 用于转化JSON 并输出
 * @author LinLW
 *
 */
public class DFishUIConverter extends ObjectToJsonConverter {

	@Override
	protected boolean supports(Class<?> clz) {
		return JsonObject.class.isAssignableFrom(clz);
	}
	
	@Override
	protected String getObjectJson(Object obj) {
		if (obj == null) {
			return "";
		}
		return ((JsonObject) obj).asJson();
	}

}
