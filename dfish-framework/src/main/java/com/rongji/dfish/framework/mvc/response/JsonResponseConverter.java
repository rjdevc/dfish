package com.rongji.dfish.framework.mvc.response;

import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.misc.util.json.JsonBuilder;

public class JsonResponseConverter extends DFishUIConverter {

	@Override
	protected String getObjectJson(Object obj) {
		if (obj == null) {
			return "";
		}
		JsonBuilder jsonBuilder = FrameworkHelper.getBean(JsonBuilder.class);
		return jsonBuilder.toJson(obj);
	}

	@Override
	protected boolean supports(Class<?> clz) {
		return JsonResponse.class.isAssignableFrom(clz);
	}

}
