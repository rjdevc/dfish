package com.rongji.dfish.ui.schema;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
	/**
	 * TemplateArray 为模板的数组或列表。
	 * Description: 
	 * Copyright:   Copyright © 2018
	 * Company:     rongji
	 * @author		LinLW 
	 * @version		1.0
	 * Modification History:  
	 * Date						Author			Version			Description  
	 * ------------------------------------------------------------------  
	 * 2018年12月18日 下午1:03:56		LinLW			1.0				1.0 Version  
	 */
	
public class SchemaArray extends AbstractSchema{
	

	private static final long serialVersionUID = -2754920648053888024L;
	/**
	 * 新建一个空数组
	 */
	public SchemaArray(){
		this.json=new JSONArray();
	}
	/**
	 * 指定JSONArray的实现
	 * @param json Object 一般是 JSONArray
	 */
	protected SchemaArray(Object json ){
		this.json=json;
	}
	/**
	 * 大小
	 * @return
	 * @author LinLW
	 */
	public int size(){
		return ((JSONArray) json).size();
	}
	/**
	 * 删除子节点 
	 * @param key String
	 * @return this
	 */
	public void removeSubTemp(int index){
		((JSONArray)json).remove(index);
	}
	/**
	 * 增加子节点
	 * @param temp 子节点
	 * @return this
	 */
	public SchemaArray addSubTemp(DFishSchema temp) {
		((JSONArray)json).add(((AbstractSchema) temp).json);
		return this;
	}
	/**
	 * 取得子节点
	 * @param index 位置
	 * @return 子节点
	 * @throws 如果子节点不是一个模板，而是 String / Integer / Double / Boolean时会抛出错误
	 */
	public DFishSchema getSubTemp(int index) {
		Object o=get(index);
		if(o instanceof DFishSchema){
			return (DFishSchema) o;
		}
		throw new RuntimeException("the "+index+"th element is not a DFishTemplate, use get(int) instead.");
	}
	/**
	 * 取得子节点
	 * @param index 位置
	 * @return 子节点
	 */
	public Object get(int index){
		Object o=((JSONArray) json).get(index);
		if(o instanceof JSONArray){
			return new SchemaArray(json);
		}else if(o instanceof JSONObject){
			return new WidgetSchema(json);
		}
		return  o;
	}
	
	public String toString(){
		if(json==null){
			return "[]";
		}
		return JSON.toJSONString(json, true);
	}
}
