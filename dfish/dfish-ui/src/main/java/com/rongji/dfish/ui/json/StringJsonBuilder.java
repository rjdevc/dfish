package com.rongji.dfish.ui.json;

import java.util.Stack;


/**
 * 最基础的String
 * @author DFish Team
 *
 */
public class StringJsonBuilder extends AbstractJsonBuilder {

	public void buildJson(Object o, StringBuilder sb,Stack<PathInfo> path) {
		sb.append('"');
		escapeJson((String)o,sb);
		sb.append('"');
	}



}
