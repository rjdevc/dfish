package com.rongji.dfish.ui.json;

import java.util.Stack;

/**
 * 枚举变量转转化器
 * <p>DFish-ui 3.0中，不太建议试用enum，因为其扩展的时候灵活性不足</p>
 * @author DFish Team
 */
public class EnumJsonBuilder extends  AbstractJsonBuilder {
	public void buildJson(Object o, StringBuilder sb,Stack<PathInfo> path) {
		sb.append('"');
		sb.append(o);
		sb.append('"');
	}
}
