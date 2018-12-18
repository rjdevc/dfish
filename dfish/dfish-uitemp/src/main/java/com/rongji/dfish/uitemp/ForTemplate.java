package com.rongji.dfish.uitemp;

import com.alibaba.fastjson.JSONObject;
import com.rongji.dfish.ui.widget.Leaf;

/**
 * <p>ForTemplate 是用于制作一个for循环的模板</p>
 * <pre>
 * WidgetTemplate leafTmp=WidgetTemplate.convert(new Leaf())
 * 		.setAtProp("text", "$item.name");
 * return new ForTemplate("nodes", "data").setNode(leafTmp);
 * </pre>
 * <p>可以通过制定 itemName和indexName进一步指定prop的表达式中的参数。</p>
 * Description: 
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		DFish team
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018年12月18日 下午3:39:25		LinLW			1.0				1.0 Version
 */
public class ForTemplate extends AbstractTemplate{
	private static final long serialVersionUID = 8078169966257514240L;
	protected ForTemplate(){}
	private String propName;
	private String itemName;
	private String indexName;
	private String dataName;
	
	public ForTemplate(String propName,String dataName,String itemName,String indexName){
		this.propName=propName;
		this.itemName=itemName;
		this.indexName=indexName;
		this.dataName=dataName;
		this.json=new JSONObject();
	}
	/**
	 * 相当于itemName ="item",indexName=null 来创建这个循环。
	 * 后续表达式中，则必须使用 $item来取值
	 * @param propName String
	 * @param dataName String
	 * @see ForTemplate#ForTemplate(String, String, String, String)
	 */
	public ForTemplate(String propName,String dataName){
		this(propName,dataName,"item","index");
	}
	
	protected String getControlKey(){
		if(indexName==null||indexName.equals("")){
			return "@"+propName+":w-for(($"+itemName+",$"+indexName+") in $"+dataName+")";
		}else{
			return "@"+propName+":w-for($"+itemName+" in $"+dataName+")";
		}
	}
	/**
	 * 设置循环内容
	 * @param temp DFishTemplate
	 * @return this
	 */
	public ForTemplate setNode(DFishTemplate temp){
		String key=getControlKey();
		((JSONObject)json).put(key, ((AbstractTemplate) temp).json);
		return this;
	}
	/**
	 * 删除内容

	 */
	public void removeNode(){
		String key=getControlKey();
		((JSONObject)json).remove(key);
	}
}
