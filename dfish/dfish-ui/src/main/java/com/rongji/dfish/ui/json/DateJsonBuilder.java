package com.rongji.dfish.ui.json;

import java.text.SimpleDateFormat;
import java.util.Stack;

/**
 * 时间格式转化器
 * <p>这只是转成最简单的格式。按yyyy-MM-dd HH:mm:ss 转成字符串。</p>
 * @author DFish Team
 */
public class DateJsonBuilder extends  AbstractJsonBuilder {
	private static final SimpleDateFormat SDF=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void buildJson(Object o, StringBuilder sb,Stack<PathInfo> path) {
		sb.append('"');
		synchronized (SDF) {
			sb.append(SDF.format((java.util.Date)o));
		}
		sb.append('"');
	}
}
