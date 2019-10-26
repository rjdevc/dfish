package com.rongji.dfish.ui.json;

import java.util.Stack;

/**
 * BaseJsonBuilder 为基础类型的JSON转化器
 * 如Boolean boolean Integer int Long long等 
 * @author DFish Team
 *
 */
public class BaseJsonBuilder extends AbstractJsonBuilder{

	@Override
    public void buildJson(Object o, StringBuilder sb, Stack<PathInfo> path) {
		sb.append(o);
	}

}
