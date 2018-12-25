package com.rongji.dfish.uitemp;

import com.alibaba.fastjson.JSONObject;

public class IncludeTemplate extends AbstractTemplate{

	private static final long serialVersionUID = -2158779873290551754L;
	public IncludeTemplate(String template){
		this.json=new JSONObject();
		JSONObject cast=(JSONObject)this.json;
		cast.put("@w-include", template);
	}
	
	
}
