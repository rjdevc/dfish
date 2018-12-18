package com.rongji.dfish.uitemp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.rongji.dfish.ui.Widget;

	/**
	 * WidgetTemplate 是为DFish的widget 封装的模板
	 * <pre>
	 * WidgetTemplate textTmp=WidgetTemplate.convert(new Text("userName","",null))
	 *	.setAtProp("value", "$data.name");
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
public class WidgetTemplate extends AbstractTemplate{
	
	private static final long serialVersionUID = 8524310739981005847L;

	protected WidgetTemplate(){
		this.json=new JSONObject(true);
	}
	/**
	 * 指定JSONObject的实现
	 * @param json Object 一般是 JSONObject
	 */
	protected WidgetTemplate(Object json){
		//一般来说现在这个json 必须是JSONObject类型
		this.json=json;
	}
	/**
	 * 将一个widget 转化为template
	 * @param w Widget
	 * @return template
	 */
	public static WidgetTemplate convert(Widget<?> w){
		JSONObject json=JSON.parseObject(w.toString(),Feature.OrderedField);
		return new WidgetTemplate(json);
	}
	/**
	 * 根据ID 获得字节点的Template
	 * @param id String
	 * @return template
	 */
	public WidgetTemplate findById(String id) {
		if(json==null){return null;}
		Object targetJson=findById(id, json);
		if(targetJson ==null){
			return null;
		}
		return new WidgetTemplate(targetJson) ;
	}

	/**
	 * 直接新建一个子节点
	 * @param key 名字
	 * @return this
	 */
	public WidgetTemplate createSubWidgetTemp(String key) {
		JSONObject subJson=new JSONObject();
		((JSONObject) json).put(key, subJson);
		return new WidgetTemplate(subJson) ;
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
	public WidgetTemplate addSubTemp(String key,DFishTemplate temp) {
		((JSONObject)json).put(key,((AbstractTemplate) temp).json);
		return this;
	}
	/**
	 * 取得子节点
	 * @param key 名字
	 * @return DFishTemplate 子节点
	 * @throws 如果子节点不是一个模板，而是 String / Integer / Double / Boolean时会抛出错误
	 */
	public DFishTemplate getSubTemp(String key) {
		Object o=get(key);
		if(o instanceof DFishTemplate){
			return (DFishTemplate) o;
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
			return new TemplateArray(json);
		}else if(o instanceof JSONObject){
			return new WidgetTemplate(json);
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
	 * @see WidgetTemplate#setProp(String, Object)
	 */
	public WidgetTemplate setPropx(String keyPattern, Object value) {
		setJsonPropx(keyPattern,value);
		return this;
	}
	
	/**
	 * 设置属性 
	 * @param key String
	 * @param value 一般 为 String / Integer / Double / Boolean
	 * @return this
	 */
	public WidgetTemplate setProp(String key, Object value) {
		((JSONObject)json).put(key, value);
		return this;
	}
	
	/**
	 * 删除属性 
	 * @param key String
	 * @return this
	 */
	public WidgetTemplate removeProp(String key, Object value) {
		((JSONObject)json).remove(key);
		return this;
	}
	/**
	 * 设置@属性 带@的属性表示这个值是动态获取的。
	 * @param key String
	 * @param value 一般 为 String / Integer / Double / Boolean
	 * @return this
	 * @see WidgetTemplate#setProp(String, Object)
	 */
	public WidgetTemplate setAtProp(String key, Object value) {
		return setProp("@"+key,value);
	}
	
	/**
	 * 取得属性值
	 * @param key String
	 * @return value 一般 为 String / Integer / Double / Boolean
	 */
	public Object getProp(String key) {
		return ((JSONObject)json).get(key);
	}
}
