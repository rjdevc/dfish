package com.rongji.dfish.ui.json;

import java.util.Stack;
/**
 * 取得属性名
 * @author DFish Team
 *
 */
public interface JsonPropAppender {
	/**
	 * 转化后是属性名，属性名和getter方法有可能在某些版本不完全一致，比如styleClass -  cls
	 * @return String
	 */
	String getPropName();
	/**
	 * 转化后是属性名，属性名和getter方法有可能在某些版本不完全一致，比如styleClass -  cls
	 * @param propName String
	 */
	void setPropName(String propName);
	
	/**
	 * 构建json
	 * @param o 宿主
	 * @param sb StringBuilder
	 * @param path 属性路径
	 * @param begin boolean
	 * @return 如果当前begin状态是true，并且过程中append了内容则返回false。如果begin为false则永远返回false. 
	 * 不是begin状态是追加下个属性则要先最追加一个逗号(,)
	 * @throws Exception 可能会抛出异常
	 */
	boolean appendProperty(Object o, StringBuilder sb,Stack<PathInfo> path,boolean begin) throws Exception;
}
