package com.rongji.dfish.ui.json;

import com.rongji.dfish.ui.RawJson;

import java.util.Stack;

/**
 * 对象允许用RawJson的方式添加到 json对象的属性来。
 * RawJsonBuilder将会添加RawJson.getText作为内容。
 * 这样可以支持高级用法的注入。
 */
public class RawJsonBuilder extends AbstractJsonBuilder {
	@Override
	public void buildJson(Object o, StringBuilder sb, Stack<PathInfo> path) {
		RawJson fun=(RawJson)o;
		sb.append(fun.getText());
	}
}
