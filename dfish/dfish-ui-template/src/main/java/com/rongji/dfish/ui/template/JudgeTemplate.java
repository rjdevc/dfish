package com.rongji.dfish.ui.template;

import com.alibaba.fastjson.JSONObject;

/**
 *JudgeTemplate为 判断模板
 *<p>可以通过增加条件增加其分支，支持if  elseif  else</p>
 *<pre>
 *WidgetTemplate textTmp=WidgetTemplate.convert(new Text("userName","",null))
 *	.setAtProp("value", "$data.name");
 *WidgetTemplate roTmp=WidgetTemplate.convert(new Text("userName","",null).setReadonly(true))
 *	.setAtProp("value", "$data.name");
 *WidgetTemplate htmTmp=WidgetTemplate.convert(new Html(null))
 *	.setAtProp("text", "$data.name");
 *JudgeTemplate judge=new JudgeTemplate()
 *	.addIf("$data.readonly==0", textTmp)
 *	.addElseif("$data.readonly==1", roTmp)
 *	.addElse(htmTmp);
 *</pre>
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		DFish team
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018年12月18日 下午7:04:51		LinLW			1.0				1.0 Version
 */
public class JudgeTemplate extends AbstractTemplate {
	public JudgeTemplate(){
		json=new JSONObject(true);
	}
	private static final long serialVersionUID = 767478857627210712L;

	/**
	 * 增加if 条件，一般来说if条件应该在第一个
	 * @param expr 判断表达式
	 * @param temp 模板，可以是wiget模板，也可以是@include
	 * @return this
	 */
	public JudgeTemplate addIf(String expr,DFishTemplate temp){
		String key="@w-if("+expr+")";
		((JSONObject)json).put(key, ((AbstractTemplate) temp).json);
		return this;
	}
	
	/**
	 * 增加else-if 条件，一般来说不能放在第一位
	 * @param expr 判断表达式
	 * @param temp 模板，可以是wiget模板，也可以是@include
	 * @return this
	 */
	public JudgeTemplate addElseif(String expr,DFishTemplate temp){
		String key="@w-elseif("+expr+")";
		((JSONObject)json).put(key, ((AbstractTemplate) temp).json);
		return this;
	}
	/**
	 * 增加else条件，一般来说放在最后一位
	 * @param temp 模板，可以是wiget模板，也可以是@include
	 * @return this
	 */
	public JudgeTemplate addElse(DFishTemplate temp){
		String key="@w-else";
		((JSONObject)json).put(key, ((AbstractTemplate) temp).json);
		return this;
	}
	
	
}
