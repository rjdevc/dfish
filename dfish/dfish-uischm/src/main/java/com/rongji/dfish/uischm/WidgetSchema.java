package com.rongji.dfish.uischm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.rongji.dfish.ui.JsonObject;

	/**
	 * WidgetTemplate 是为DFish的widget 封装的模板
	 * <pre>
	 * WidgetTemplate textTmp=new WidgetTemplate(new Text("userName","",null))
	 *	.at("value", "$data.name");
	 * </pre>
	 * 支持级联嵌套
	 * Description: 
	 * Copyright:   Copyright © 2018
	 * Company:     rongji
	 * @author		DFish team 
	 * @version		1.0
	 * Modification History:  
	 * Date						Author			Version			Description  
	 * ------------------------------------------------------------------  
	 * 2018年12月18日 下午1:04:18		LinLW			1.0				1.0 Version  
	 */
public class WidgetSchema extends AbstractSchema{
	
	private static final long serialVersionUID = 8524310739981005847L;

	public  WidgetSchema(){
		this(new JSONObject(true));
	}
	public  WidgetSchema(JsonObject jo){
		this(JSON.parseObject(jo.toString(),Feature.OrderedField));
	}
	public  WidgetSchema(String json){
		this(JSON.parseObject(json,Feature.OrderedField));
	}
	/**
	 * 指定JSONObject的实现
	 * @param json Object 一般是 JSONObject
	 */
	protected WidgetSchema(Object json){
		//一般来说现在这个json 必须是JSONObject类型
		this.json=json;
	}

	/**
	 * 根据ID 获得字节点的Template
	 * @param id String
	 * @return template
	 */
	public WidgetSchema findById(String id) {
		if(json==null){return null;}
		Object targetJson=findById(id, json);
		if(targetJson ==null){
			return null;
		}
		return new WidgetSchema(targetJson) ;
	}

	/**
	 * 直接新建一个子节点
	 * @param key 名字
	 * @return this
	 */
	public WidgetSchema createSubWidgetTemp(String key) {
		JSONObject subJson=new JSONObject();
		((JSONObject) json).put(key, subJson);
		return new WidgetSchema(subJson) ;
	}
	
	/**
	 * 删除子节点 
	 * @param key String
	 * @return this
	 */
	public void removeSubTemp(String key){
		((JSONObject)json).remove(key);
	}
	/**
	 * 增加子节点
	 * @param key 名字
	 * @param temp 子节点
	 * @return this
	 */
	public WidgetSchema addSubTemp(String key,DFishSchema temp) {
		((JSONObject)json).put(key,((AbstractSchema) temp).json);
		return this;
	}
	/**
	 * 取得子节点
	 * @param key 名字
	 * @return DFishTemplate 子节点
	 * @throws 如果子节点不是一个模板，而是 String / Integer / Double / Boolean时会抛出错误
	 */
	public DFishSchema getSubTemp(String key) {
		Object o=get(key);
		if(o instanceof DFishSchema){
			return (DFishSchema) o;
		}
		throw new RuntimeException("the "+key+" element is not a DFishTemplate, use get(String) instead.");
	}
	/**
	 * 取得子节点
	 * @param key 名字
	 * @return DFishTemplate 子节点
	 */
	public Object get(String key) {
		Object o=((JSONObject) json).get(key);
		if(o instanceof JSONArray){
			return new SchemaArray(o);
		}else if(o instanceof JSONObject){
			return new WidgetSchema(o);
		}
		return  o;
	}
	/**
	 * 高级设置属性
	 * 可以用点号[.]设置多重属性的名字。在各级点号前面的当做子节点处理。
	 * 如果节点名字为数字，则当做是数组节点去某个数据
	 * 如果某个一重属性不存在，则自动创建。
	 * 最后一层才当做属性处理
	 * @param keyPattern String 比如 nodes.0.tbody.@rows
	 * @param value 一般 为 String / Integer / Double / Boolean
	 * @return this 
	 * @see WidgetSchema#setProp(String, Object)
	 */
	public WidgetSchema setPropx(String keyPattern, Object value) {
		setJsonPropx(keyPattern,value);
		return this;
	}
	
	/**
	 * 设置属性 
	 * @param prop String
	 * @param value 一般 为 String / Integer / Double / Boolean
	 * @return this
	 */
	public WidgetSchema setProp(String prop, Object value) {
		((JSONObject)json).put(prop, value);
		return this;
	}
	
	/**
	 * 删除属性 
	 * @param prop String
	 * @return this
	 */
	public WidgetSchema removeProp(String key) {
		((JSONObject)json).remove(key);
		return this;
	}
	/**
	 * 设置@属性 带@的属性表示这个值是动态获取的。
	 * @param prop String
	 * @param expr JS表达
	 * @return this
	 */
	public WidgetSchema at(String prop, String expr) {
		return setProp("@"+prop,expr);
	}
	/**
	 * 设置@属性 带@的属性表示这个值是动态获取的。
	 * @param prop String
	 * @param temp 允许是 一个完整的对象，但一般常用的是include，否则完全可以在子节点中使用@
	 * @return this
	 */
	public WidgetSchema at(String prop, DFishSchema temp) {
		if(temp instanceof AbstractSchema ){
			Object  subProp=((AbstractSchema) temp).json;
			setProp("@"+prop,subProp);
		}
		return this;
	}
	
	@Deprecated
	public WidgetSchema setAtProp(String prop, String expr) {
		return setProp("@"+prop,expr);
	}
	
	/**
	 * 取得属性值
	 * @param prop String
	 * @return value 一般 为 String / Integer / Double / Boolean
	 */
	public Object getProp(String prop) {
		return ((JSONObject)json).get(prop);
	}
	
	public WidgetSchema addFor(String prop,String dataExpr,DFishSchema temp,String itemName,String indexName){
		String propFullName=null;
		if(indexName==null||indexName.equals("")){
			propFullName= "@"+prop+":w-for($"+itemName+" in ("+dataExpr+"))";
		}else{
			propFullName= "@"+prop+":w-for(($"+itemName+",$"+indexName+") in ("+dataExpr+"))";
		}
		if(temp instanceof AbstractSchema ){
			Object  subProp=((AbstractSchema) temp).json;
			this.setProp(propFullName, subProp);
		}
		return this;
	}
	/**
	 * 相当于itemName ="item",indexName=null 来创建这个循环。
	 * 后续表达式中，则必须使用 $item来取值
	 * @param prop String
	 * @param dataExpr String
	 * @see ForTemplate#ForTemplate(String, String, DFishSchema, String, String)
	 */
	public WidgetSchema addFor(String prop,String dataExpr,DFishSchema temp){
		return addFor(prop,dataExpr,temp,"item",null);
	}
	
}
