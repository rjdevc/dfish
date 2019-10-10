package com.rongji.dfish.ui.json;

import java.util.Stack;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.JSFunction;

/**
 * 实现JS中的function
 * @author LinLW
 *
 */
public class FunctionJsonBuilder  extends AbstractJsonBuilder {

	@Override
	public void buildJson(Object o, StringBuilder sb, Stack<PathInfo> path) {
		JSFunction fun=(JSFunction)o;
		sb.append("function(");
		if(Utils.notEmpty(fun.getParams())){
			sb.append(fun.getParams());
		}
		sb.append("){");
		if(Utils.notEmpty(fun.getFuntionText())){
			sb.append(fun.getFuntionText());//没有转义！
		}
		sb.append('}');
	}

}
