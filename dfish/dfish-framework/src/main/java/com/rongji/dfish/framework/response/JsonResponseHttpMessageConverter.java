package com.rongji.dfish.framework.response;

import com.rongji.dfish.ui.json.J;

public class JsonResponseHttpMessageConverter extends JsonHttpMessageConverter {

	@Override
	protected String getObjectJson(Object obj) {
		if (obj == null) {
			return "";
		}
		return J.toJson(obj);
	}

	@Override
	protected boolean supports(Class<?> clz) {
		return JsonResponse.class.isAssignableFrom(clz);
	}

}
