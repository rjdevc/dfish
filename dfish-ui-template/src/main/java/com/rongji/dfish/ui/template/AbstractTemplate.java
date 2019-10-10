package com.rongji.dfish.ui.template;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rongji.dfish.ui.json.J;
	
	/**
	 * AbstractTemplate
	 * Description: 抽象的模板，该模板基于FastJson构建。
	 * Copyright:   Copyright © 2018
	 * Company:     rongji
	 * @author		DFish team 
	 * @version		1.0
	 * @since DFish 3.2
	 * Modification History:  
	 * Date						Author			Version			Description  
	 * ------------------------------------------------------------------  
	 * 2018年12月18日 下午12:25:10		LinLW			1.0				1.0 Version  
	 */
	
public abstract class AbstractTemplate implements DFishTemplate{

	private static final long serialVersionUID = -4474810267412968779L;
	protected Object json;
	public boolean equals(Object o){
		if (o==null) {return false;}
		if (o==this){return true;}
		if(!(o instanceof AbstractTemplate)){
			return false;
		}
		Object ojson=((AbstractTemplate)o).json;
		return ojson!=null&&json!=null&&json.equals(ojson);
	}
	public int hashCode(){
		if (json==null){
			return super.hashCode();
		}
		return json.hashCode();
	}
	public String toString(){
		if(json==null){
			return "{}";
		}
		String raw=JSON.toJSONString(json);
		return J.formatJson(raw);
	}
	
	/**
	 * 设置多级属性，级数用点号[.]隔开。
	 * @param keyPattern 多级属性名
	 * @param value 一般为 String / Integer / Double / Boolean
	 */
	protected void setJsonPropx(String keyPattern, Object value) {
		String[] keys=keyPattern.split("[.]");
		Object workingObject=json;
		int i=0;
		for(String key:keys){
			if(++i>=keys.length){
				JSONObject obj=(JSONObject)workingObject;
				obj.put(key, value);
			}else	if(isNumeric(key)){
				JSONArray arr=(JSONArray)workingObject;
				workingObject=arr.get(Integer.parseInt(key));
				//FIXME 如果arr为空 而key为0时
				//或者 arr不为空，但key=size 要支持。
			}else{
				JSONObject obj=(JSONObject)workingObject;
				workingObject=obj.get(key);
				if(workingObject==null){
					obj.put(key, workingObject=new JSONObject());
				}
			}
		}
	}
	
	private static Pattern pattern = Pattern.compile("[0-9]*");
	private static boolean isNumeric(String str){
        Matcher isNum = pattern.matcher(str);
        return isNum.matches() ;
	}
	
	/**
	 * 找到JSON里面id 为指定ID 的对象。通常是一个JSONObject
	 * @param id String
	 * @param json JSON对象可能是JSONObject或者是JSONArray
	 * @return Object
	 */
	protected static Object findById(String id, Object json) {
		if(json instanceof JSONObject){
			//先判定是不是自己
			String tid=((JSONObject) json).getString("id");
			if(id.equals(tid)){
				return json;
			}
			//遍历所有属性
			for(Object sub:((JSONObject) json).values()){
				Object tmp=findById(id,sub);
				if(tmp!=null){
					return tmp;
				}
			}
		}else if(json instanceof JSONArray){
			//遍历所有属性
			for(Object sub: (JSONArray)json){
				Object tmp=findById(id,sub);
				if(tmp!=null){
					return tmp;
				}
			}
		}
		return null;
	}
	@Override
	public String asJson() {
		return toString();
	}
	@Override
	public String getType() {
		return "DFish template with Ali fastjson implement.";
	}
	
}
