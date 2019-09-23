package com.rongji.dfish.framework.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.base.Utils;

/**
 * 过滤参数
 * @author DFish Team
 *
 */
public class FilterParam {
	HashMap<String ,String[]> context=new HashMap<String ,String[]>();
	public FilterParam(){}
	public void registerKey(String key){
		context.put(key, null);
	}
	/**
	 * 绑定提交的参数。只会处理registerKey方法中注册的参数
	 * 注意如果在这之前有单独的设置参数可能被覆盖。
	 * @param request 请求
	 */
	public void bindRequest(HttpServletRequest request){
		Set<String>keys=context.keySet();
		for(String key:keys){
			String[] s=request.getParameterValues(key);
			if(s!=null&&s.length==1){
				String value=Utils.getParameter(request, key);//处理tomcat的BUG
				setValueAsString(key,value);
			}else{
				setValue(key,s);
			}
		}
	}
	public void setValueAsString(String key,String value){
		context.put(key, new String[]{value});
	}
	public void setValue(String key,String[] value){
		context.put(key, value);
	}
	public String getValueAsString(String key){
		if(context.get(key)!=null&&context.get(key).length>0){
			return context.get(key)[0];
		}
		return null;
	}
	public String[] getValue(String key){
		return context.get(key);
	}
	
	public String toString(){
		StringBuilder sb=new StringBuilder();

		for(Map.Entry<String,String[]>entry:context.entrySet()){
			String key=entry.getKey();
			if(entry.getValue()!=null){
				for(String str:entry.getValue()){
					if(str!=null&&!str.equals("")){
						sb.append('&');
						sb.append(key);
						sb.append('=');
						try {
							sb.append(java.net.URLEncoder.encode(str, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							sb.append(str);
						}
					}
				}
			}
		}
		return sb.toString();
	}
}
