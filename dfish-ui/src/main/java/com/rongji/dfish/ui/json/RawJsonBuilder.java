package com.rongji.dfish.ui.json;

import com.rongji.dfish.ui.RawJson;

import java.util.Stack;

public class RawJsonBuilder extends AbstractJsonBuilder {
	@Override
	public void buildJson(Object o, StringBuilder sb, Stack<PathInfo> path) {
		RawJson fun=(RawJson)o;
		sb.append(fun.getText());
	}
}
