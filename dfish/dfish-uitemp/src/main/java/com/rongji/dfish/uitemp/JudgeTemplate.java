package com.rongji.dfish.uitemp;

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
 * Description: 
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

	public JudgeTemplate addIf(String expr,DFishTemplate temp){
		String key="@w-if("+expr+")";
		((JSONObject)json).put(key, ((AbstractTemplate) temp).json);
		return this;
	}
	
	public JudgeTemplate addElseif(String expr,DFishTemplate temp){
		String key="@w-elseif("+expr+")";
		((JSONObject)json).put(key, ((AbstractTemplate) temp).json);
		return this;
	}
	public JudgeTemplate addElse(DFishTemplate temp){
		String key="@w-else";
		((JSONObject)json).put(key, ((AbstractTemplate) temp).json);
		return this;
	}
	
	
}
