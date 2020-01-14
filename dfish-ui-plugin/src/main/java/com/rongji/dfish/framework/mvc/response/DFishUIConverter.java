package com.rongji.dfish.framework.mvc.response;

import com.rongji.dfish.ui.UiNode;

/**
 * 用于转化JSON 并输出
 * @author LinLW
 *
 */
public class DFishUIConverter extends ObjectToJsonConverter {

	@Override
	protected boolean supports(Class<?> clz) {
		return UiNode.class.isAssignableFrom(clz);
	}
	
	@Override
	protected String getObjectJson(Object obj) {
		if (obj == null) {
			return "";
		}
		return ((UiNode) obj).asJson();
	}

}
