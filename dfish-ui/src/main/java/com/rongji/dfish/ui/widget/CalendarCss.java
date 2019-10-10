//package com.rongji.dfish.ui.widget;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import com.rongji.dfish.ui.AbstractJsonObject;
//import com.rongji.dfish.ui.json.JsonWrapper;
//
///**
// * 设置一组日期样式。
// * 范例: 按顺序对应value中值为Y的日期设置绿色字体，值为N的日期设置红色字体
// * <table border="1"><tr><td>
// * var opt = { type: 'canlendar/date', css: { value: 'NNNNNNNNYYNNNNNNNNYYYYYYYNNNNNN', N: 'color:red;', Y : 'color:green;' } }
// * </td></tr></table>
// * @author DFish Team
// *
// */
//public class CalendarCss extends AbstractJsonObject implements JsonWrapper<Map<String,String>>{
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 3105363801396968376L;
//	Map<String, String> prototype=new LinkedHashMap<String, String>();
//	
//	public String getType() {
//		return null;
//	}
//
//	@Override
//	public Map<String, String> getPrototype() {
//		return prototype;
//	}
//	
//	/**
//	 * 设置css的值
//	 * 比如设置一个月30天的格子
//	 * <pre>
//	 * value="NNYNN"+"YNYYYNN"+"YNNNNNN"+"YNNNYNN"+"YNYY"
//	 * Y="color:red"
//	 * N="color:gray"
//	 * </pre>
//	 * 这里设置这个value
//	 * @param value String
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public CalendarCss setValue(String value){
//		prototype.put("value", value);
//		return this;
//	}
//	/**
//	 * 设置css的值
//	 * 比如设置一个月30天的格子
//	 * <pre>
//	 * value="NNYNN"+"YNYYYNN"+"YNNNNNN"+"YNNNYNN"+"YNYY"
//	 * Y="color:red"
//	 * N="color:gray"
//	 * </pre>
//	 * 这里设置这个Y或N的详细属性
//	 * @param key char
//	 * @param value value
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public CalendarCss set(char key,String value){
//		prototype.put(new String(new char[]{key}), value);
//		return this;
//	}
//	/**
//	 * 移除设置css的值
//	 * @param key char
//	 * @return value
//	 */
//	public CalendarCss remove(char key){
//		prototype.remove(new String(new char[]{key}));
//		return this;
//	}
//	
//}
