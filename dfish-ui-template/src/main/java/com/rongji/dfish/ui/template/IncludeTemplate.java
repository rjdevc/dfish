//package com.rongji.dfish.ui.template;
//
//import com.alibaba.fastjson.JSONObject;
//
///**
// * 表示当前节点是用另一个模板代替。这个在共用某个模块
// * 或者有递归调用的时候有不可代替的作用
// *
// */
//public class IncludeTemplate extends AbstractTemplate{
//
//	private static final long serialVersionUID = -2158779873290551754L;
//	/**
//	 * 构造函数
//	 * @param template String 编号
//	 */
//	public IncludeTemplate(String template){
//		this.json=new JSONObject();
//		JSONObject cast=(JSONObject)this.json;
//		cast.put("@w-include", template);
//	}
//
//
//}
