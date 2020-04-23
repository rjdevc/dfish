package com.rongji.dfish.ui.json;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.ui.JsonWrapper;
import com.rongji.dfish.ui.RawJson;
import com.rongji.dfish.ui.Widget;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
/**
 * <p>JSON构建器工具类入口</p>，该构建器不是通用的，它是为DFish3定制的。
 * 它功能不完整，但吻合DFish3的要求，构建出来的JSON效果较好(排序吻合DFish3的逻辑)
 * 性能更高(不做一些无必要的判断)
 * @author DFish Team
 *
 */
public class JsonFormat {
	private static Map<Class<?>,JsonBuilder> djbs=new HashMap<Class<?>,JsonBuilder>();
	/**
	 * 取得这个Class对应的Builder.
	 * 和Gson不同，在DFISH3.0中 不同class在初次装载的时候，将会构建一个类反射的Json构建器。
	 * 这个构建器会在初次进行属性的探测，排序，改名，忽略部分属性的功能。
	 * 因为有这个构建器的存在，在第二次装载的时候，将会跳过这个步骤，直接进入json构建。
	 * 从而达到加速的目的。
	 * <p>这个构建器是可以被改变的。比如说HtmlPanel控件中text属性可能是需要HTML转移——根据unescape属性的值
	 * ——这时候需要忽略控件本身的text属性。然后在buildJson中补充上这个属性的正确格式。
	 * 不需要在buildJson中重构全部属性。</p>
	 * <p>改变这个构建器的方法，请参看JsonBuilder#removeProperty JsonBuilder#replaceProperty 
	 * 以及JsonBuilder#buildJsonProps</p>
	 * 
	 * @param clz Class
	 * @return JsonBuilder
	 * @see ClassJsonBuilder
	 */
	public static JsonBuilder get(Class<?> clz){
		JsonBuilder jt=djbs.get(clz);
		if(jt==null){
			if(Map.class.isAssignableFrom(clz)){
				jt = MAP_JSON_BUILDER;
			}else if(Collection.class.isAssignableFrom(clz)){
				jt = COLLECTION_JSON_BUILDER;
			}else if(clz.isArray()){
				jt = AYYAY_JSON_BUILDER;
			}else if(java.util.Date.class.isAssignableFrom(clz)){
				jt = DATE_JSON_BUILDER;
			}else if(Number.class.isAssignableFrom(clz)){
				jt = BASE_JSON_BUILDER;
			}else if(Enum.class.isAssignableFrom(clz)){
				jt = ENUM_JSON_BUILDER;
			}else if(Widget.class.isAssignableFrom(clz)) {
				jt = new WidgetJsonBuilder(clz);
//			}else if(TemplateSupport.class.isAssignableFrom(clz)) {
//				jt = new TemplateJsonBuilder(clz);
			}else{
				jt = new ClassJsonBuilder(clz);
			}
			djbs.put(clz, jt);
			return jt;
		}
		return jt;
	}
//	private static final JsonBuilderParam DEFAULT_PARAM=new JsonBuilderParam();
	private static JsonBuilder MAP_JSON_BUILDER=new MapJsonBuilder();
	private static JsonBuilder COLLECTION_JSON_BUILDER=new CollectionJsonBuilder();
	private static JsonBuilder AYYAY_JSON_BUILDER=new ArrayJsonBuilder();
	private static JsonBuilder DATE_JSON_BUILDER=new DateJsonBuilder();
	private static JsonBuilder BASE_JSON_BUILDER=new BaseJsonBuilder();
	private static JsonBuilder ENUM_JSON_BUILDER=new EnumJsonBuilder();
	static {
		djbs.put(String.class, new StringJsonBuilder());
		// B	基本类型byte
		// C	char
		// D	double
		// F	float
		// I	int
		// J	long
		// S	short
		// Z	boolean
		// V void
		// L class
		djbs.put(Byte.class, BASE_JSON_BUILDER);
		djbs.put(Character.class, BASE_JSON_BUILDER);
		djbs.put(Double.class, BASE_JSON_BUILDER);
		djbs.put(Float.class, BASE_JSON_BUILDER);
		djbs.put(Integer.class, BASE_JSON_BUILDER);
		djbs.put(Long.class, BASE_JSON_BUILDER);
		djbs.put(Short.class, BASE_JSON_BUILDER);
		djbs.put(Boolean.class, BASE_JSON_BUILDER);
		djbs.put(Number.class, BASE_JSON_BUILDER);
		djbs.put(byte.class, BASE_JSON_BUILDER);
		djbs.put(char.class, BASE_JSON_BUILDER);
		djbs.put(double.class, BASE_JSON_BUILDER);
		djbs.put(float.class, BASE_JSON_BUILDER);
		djbs.put(int.class, BASE_JSON_BUILDER);
		djbs.put(long.class, BASE_JSON_BUILDER);
		djbs.put(short.class, BASE_JSON_BUILDER);
		djbs.put(boolean.class, BASE_JSON_BUILDER);
		djbs.put(byte[].class, new ArrayJsonBuilder.ByteArrayJsonBuilder());
		djbs.put(char[].class, new ArrayJsonBuilder.CharArrayJsonBuilder());
		djbs.put(double[].class, new ArrayJsonBuilder.DoubleArrayJsonBuilder());
		djbs.put(float[].class, new ArrayJsonBuilder.FloatArrayJsonBuilder());
		djbs.put(int[].class, new ArrayJsonBuilder.IntArrayJsonBuilder());
		djbs.put(long[].class, new ArrayJsonBuilder.LongArrayJsonBuilder());
		djbs.put(short[].class, new ArrayJsonBuilder.ShortArrayJsonBuilder());
		djbs.put(boolean[].class, new ArrayJsonBuilder.BooleanArrayJsonBuilder());
//		djbs.put(JSFunction.class, new FunctionJsonBuilder());
		djbs.put(RawJson.class, new RawJsonBuilder());
	}
	
	/**
	 * <p>构建元素的json字符串格式</p>
	 * <p>如果这个兑现是JsonObject 则使用其buildJson(sb);方法，否则调用默认的Json构建器来构建Json。
	 * 实际上JsonObject大部分也是调用此方法产生的JSON。</p>
	 * 
	 * @param o Object
	 * @param sb StringBuilder to append
	 * @param path 构建的时候附带构建过程中的路径。用于高级判定，比如判定type是否输出
	 * @see #getClass()
	 */
	public static void buildJson(Object o,StringBuilder sb,Stack<PathInfo> path){
		if(o==null){
			sb.append("{}");
			return;
		}
		
		while(o instanceof JsonWrapper){
			JsonWrapper<?> warp=(JsonWrapper<?>)o;
			o=warp.getPrototype();
			if(o==warp){break;}
		}
		if(o==null){
			sb.append("{}");
			return;
		}
		Class<?> clz=o.getClass();
		if(clz==Class.class){
			//不处理getClass属性。否则会死循环
			throw new java.lang.IllegalArgumentException(o.toString());
		}
		try{
			get(clz).buildJson(o, sb,path);
		}catch(Exception ex){
			LogUtil.error(JsonFormat.class, clz.getName(),ex);
		}
	}
	/**
	 * 转化成JSON
	 * @param o Object
	 * @return JS
	 */
	public static String toJson(Object o){
		StringBuilder sb=new StringBuilder();
		Stack<PathInfo> path =new Stack<>();
		path.push(new PathInfo(null,o));
		buildJson(o, sb, path);
		//help gc
		path.clear();
		return sb.toString();
	}
	
	  /**
	  * 转化成带换行和缩进的JSON 格式
	  * @param o Object
	  * @return String
	  * @since DFishdfish3.1
	  */
	public static String formatJson(Object o){
	    StringBuilder sb=new StringBuilder();
		Stack<PathInfo> path =new Stack<>();
		buildJson(o, sb, path);
		path.clear();
		return formatJson(sb.toString());
	 }
  /**
  * 转化成带换行和缩进的JSON 格式
  * V1.1版本折叠了属性
  * @param json String
  * @return String
  */
 public static String formatJson(String json){
 	StringBuilder sb=new StringBuilder();
 	int level=0;
 	char[] chars=json.toCharArray();
 	//双引号里面的逗号要忽略
 	boolean inComma=false;
 	int lineLen=0;
 	for(int i=0;i<chars.length;i++){
 		char c=chars[i];
 		if(inComma){
 			if(c=='"'){
 				inComma=false;
 			}else if(c=='\\'){
 				sb.append(c);
 				i++;
 				lineLen++;
 				c=chars[i];
 			}
 			sb.append(c);
 		}else if(c=='"'){
 			inComma=true;
 			sb.append(c);
 		}else if(c==','){
 			sb.append(c);
 			//换行 + N 个退格
 			if(lineLen>120){
 				sb.append('\r');
 				sb.append('\n');
 				for(int k=0;k<level;k++){
 					sb.append('\t');
 				}
 				lineLen=0;
 			}
 		}else if(c=='{'||c=='['){
 			sb.append(c);
 			//进入快编辑模式，如果网后直接扫到结束，则直接添加到结束符号后，而不触发换行
 			//否则继续往下走
 			boolean compress=false;
 			for(int m=i+1;m<chars.length;m++){
 				char c2=chars[m];
 				if(c2=='{'||c2=='['){
 					break;
 				}
 				if(c2==']'||c2=='}'){
 					//进入append模式
 					sb.append(chars,i+1,m-i);
 					lineLen+=m-i;
 					i=m;
 					compress=true;
 					break;
 				}
 			}
 			if(!compress){
	    			level++;
	    			//换行 + N 个退格
	    			sb.append('\r');
	    			sb.append('\n');
	    			lineLen=0;
	    			for(int k=0;k<level;k++){
	    				sb.append('\t');
	    			}
 			}
 		}else if(c=='}'||c==']'){
 			level--;
 			sb.append('\r');
 			sb.append('\n');
 			for(int k=0;k<level;k++){
 				sb.append('\t');
 			}
 			sb.append(c);
 		}else{
 			sb.append(c);
 			lineLen++;
 		}
 	}
 	return sb.toString();
 }
}
