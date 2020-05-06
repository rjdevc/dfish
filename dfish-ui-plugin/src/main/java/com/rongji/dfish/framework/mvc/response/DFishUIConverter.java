package com.rongji.dfish.framework.mvc.response;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.json.JsonFormat;

/**
 * 用于转化JSON 并输出
 * @author LinLW
 *
 */
public class DFishUIConverter extends ObjectToJsonConverter {

	@Override
	protected boolean supports(Class<?> clz) {
		return Node.class.isAssignableFrom(clz);
	}
	
	@Override
	protected String getObjectJson(Object obj) {
		if (obj == null) {
			return "";
		}
		String json=((Node) obj).asJson();
		LogUtil.lazyDebug(DFishUIConverter.class,()-> JsonFormat.formatJson(json));
		return json;
	}

}
