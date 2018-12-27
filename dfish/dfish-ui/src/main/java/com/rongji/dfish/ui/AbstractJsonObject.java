package com.rongji.dfish.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rongji.dfish.ui.json.J;
import com.rongji.dfish.ui.json.JsonWrapper;

/**
 * AbstractJsonObject 为抽象JsonObject 为方便构建JsonObject而创立
 * @author DFish Team
 *
 */
public abstract class AbstractJsonObject<T extends AbstractJsonObject<T>> implements JsonObject,TemplateSupport<T>{

	private static final long serialVersionUID = -2456281685328829918L;
	//	protected Boolean escape;
	
	protected static final Log LOG=LogFactory.getLog(JsonObject.class);
	
	/**
	 * 默认构造函数，尝试去读取默认值，默认值放置在com.rongji.dfish.ui.default_prop.properties中
	 */
	public AbstractJsonObject(){
		super();
		// 如果是封装类,绑定属性需要原型类构成后方可调用此方法
		if (!(this instanceof JsonWrapper)) {
			// FIXME 目前还不清楚这种做法是否合理,所有JsonWrapper必须等原型赋值后再进行调用该方法
			bundleProperties();
		}
	}
	
	/**
	 * 绑定默认属性
	 * 
	 * @author DFish Team - YuLM
	 */
	protected void bundleProperties() {
		ObjectTemplate.get(getClass()).bundleProperties(this);
	}
	
	private Map<String,String> atProps;
	public T at(String prop,String expr){
		if(atProps==null){
			atProps=new LinkedHashMap<String,String>();
		}
		atProps.put(prop,expr);
		return(T)this;
	}
	public Map<String,String> ats(){
		return atProps;
	}
	public void ats(Map<String,String> ats){
		this.atProps=ats;
//		return atProps;
	}

	public String asJson() {
		return toString();
	}

	@Override
	public String toString(){
		Object o=this;
		while(o instanceof JsonWrapper<?>){
			Object prototype=((JsonWrapper<?>)o).getPrototype();
			if(prototype==o){
				break;
			}
			o=prototype;
		}
		return J.toJson(o);
	}
	
    /**
     *  转化成带换行和缩进的JSON 格式
     * @return String
     */
    public String formatString() {
    	return J.formatJson(this.toString());
    }
    
    /**
	 * HTML编码字符
	 * @param src String
	 * @return String
	 */
	public static String toHtml(String src) {
		StringBuilder sb = new StringBuilder();
		if (src != null) {
			char[] c = src.toCharArray();
			for (int i = 0; i < c.length; i++) {
				switch (c[i]) {
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '\"':
					sb.append("&quot;");
					break;
				case '\'':
					sb.append("&#39;");
					break;
				case '\r': { //把换行替换成<br/>
					sb.append("<br/>");
					if (i + 1 < c.length && c[i + 1] == '\n') {
						i++; //如果紧跟着的那个是\n那么忽略掉.这样对macos linux window 都支持
					}
					break;
				}
				case ' ': {
					if (i + 1 < c.length && c[i + 1] == ' ') {
						sb.append("&nbsp;"); //根据HTML规范如果两个以上的空格,除了最后一个是空格外,其他要打&nbsp;
					} else {
						sb.append(' ');
					}
					break;
				}
				case '\n': //把换行替换成<br/>
					sb.append("<br/>");
					break;
				default:
					if (c[i] < 0||c[i] > 31) {
						sb.append(c[i]);
					}
				}
			}
		}
		return sb.toString();
	}
}
