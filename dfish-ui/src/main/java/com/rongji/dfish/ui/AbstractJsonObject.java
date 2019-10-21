package com.rongji.dfish.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.ui.json.J;
import com.rongji.dfish.ui.json.JsonWrapper;

/**
 * AbstractJsonObject 为抽象JsonObject 为方便构建JsonObject而创立
 * @author DFish Team
 *
 */
public abstract class AbstractJsonObject implements JsonObject{
/**
	 * 
	 */
	private static final long serialVersionUID = -2456281685328829918L;
	//	protected Boolean escape;
	protected static final Log LOG=LogFactory.getLog(AbstractJsonObject.class);
	private static final String PREFIX="com.rongji.dfish.ui.";
	protected static ResourceBundle RES_BUNDLE;
	
	private static Map<Class<?>,Map<Method,Object>> DEFAULT_INSTANCE=new HashMap<Class<?>,Map<Method,Object>>();
	
	static{
		try{
			RES_BUNDLE=ResourceBundle.getBundle(PREFIX+"default_prop");
		}catch(Exception ex){
			LOG.warn("can not find properties file : "+PREFIX+"default_prop",ex);
		}
	}
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
		Map<Method,Object> def=DEFAULT_INSTANCE.get(getClass());
		if(def==null){
			//第一个对象一个个读取属性并保存
			//尝试读取所有属性
			def = getDef();
			DEFAULT_INSTANCE.put(getClass(), def);
		}
		for(Map.Entry<Method, Object> prop:def.entrySet()){
			try {
				prop.getKey().invoke(this, new Object[]{prop.getValue()});
			} catch (Exception e) {
				String methodName = "";
				if (prop.getKey() != null) {
					methodName = prop.getKey().getName();
				}
				Throwable cause = null;
				if (e instanceof InvocationTargetException) {
					cause = ((InvocationTargetException)e).getTargetException();
				} else {
					cause = e;
				}
				LOG.error("设置默认值异常@" + getClass().getName() + "." + methodName + "(" + prop.getValue() + ")", cause);
			}
		}
	}
	
	private Map<Method, Object> getDef() {
		//尝试找到当前对象的初始属性。
		//用getter方法来确定当前对象类型。如果支持多种(Object)。默认以String为准】
		Map<Method, Object> result=new HashMap<Method, Object>();
		Class<?> itemCls=getClass();
		String thisClazzName = this.getClass().getName();
		for(Method m:itemCls.getMethods()){
			if(!m.getName().startsWith("get")||
					m.getName().length()<=3||
					m.getParameterTypes().length>0||
					!Modifier.isPublic(m.getModifiers())||
					Modifier.isAbstract(m.getModifiers())||
					Modifier.isStatic(m.getModifiers()) ){
				continue;
			}
			String propName=m.getName().substring(3);
			char bc = propName.charAt(0);
			if(bc<'A'||bc>'Z'){
				continue;
			}
			propName = ((char) (bc + 32)) + propName.substring(1);
			//如果这个propName出现在RES_BUNDLE 中。则提取这个值，并放入RESULT中
			String fullName=thisClazzName+"."+propName;
			String name=fullName;
			if(fullName.startsWith(PREFIX)){
				name=fullName.substring(PREFIX.length());
			}
			String propValue=null;
			if(RES_BUNDLE!=null){
				try{
					propValue=RES_BUNDLE.getString(name);
				}catch(MissingResourceException ex){
					LOG.trace("missing resource: "+name);
				}
			}
			if(propValue!=null){
				String setterMethodName="s"+m.getName().substring(1);
				Class<?>realType=BeanUtil.getRealReturnType(m, getClass());
				try {
					Method setterMethod =null;
					if(Object.class==realType){
						//如果返回值是tip等可变类型。有可能是String Boolean或者是专门的Tip类
						//那么我们根据数据决定这个类型。
						if("true".equals(propValue)||"false".equals(propValue)){
							try{
								setterMethod = getClass().getMethod(setterMethodName, new Class<?>[]{Boolean.class});
								realType=Boolean.class;
							}catch (Exception e) {}
						}
						if(setterMethod==null){
							try{
								Integer.parseInt(propValue);
								setterMethod = getClass().getMethod(setterMethodName, new Class<?>[]{Integer.class});
								realType=Integer.class;
							}catch (Exception e) {}
						}
						if(setterMethod==null){
							try{
								setterMethod = getClass().getMethod(setterMethodName, new Class<?>[]{String.class});
								realType=String.class;
							}catch (Exception e) {}
						}
						if(setterMethod==null){
							try{
								setterMethod = getClass().getMethod(setterMethodName, new Class<?>[]{Object.class});
							}catch (Exception e) {}
						}
					}else{
						setterMethod = getClass().getMethod(setterMethodName, new Class<?>[]{realType});
					}
					Object value=propValue;
					if(Integer.class==realType){
						value=new Integer(propValue);
					}else if(Boolean.class==realType){
						value=new Boolean(propValue);
					}
					result.put(setterMethod, value);
					LOG.debug(getClass().getName()+"."+setterMethod.getName()+" DEFAULT_VALUE= "+value);
				} catch (Exception e) {
					LOG.info("获取方法异常@" + getClass().getName() + "." + setterMethodName, e);
				}
			}
			
//			result.put(key, value)
		}
		return result;
	}

	@Override
    public String asJson() {
		return toString();
	}

	@Override
	public String toString(){
//		StringBuilder sb=new StringBuilder();
		Object o=this;
		while(o instanceof JsonWrapper<?>){
			Object prototype=((JsonWrapper<?>)o).getPrototype();
			if(prototype==o){
				break;
			}
			o=prototype;
		}
//		Stack<PathInfo> path=new Stack<PathInfo>();
//		J.buildJson(o, sb,path);
//		return sb.toString();
		return J.toJson(o);
	}
	
//	/**
//	 * 是否属于调试模式，调试模式的时候输出的BUG信息比较多，
//	 * 非调试模式则注重性能
//	 */
//	public static boolean DEBUG=true;
//    protected void warning(String string) {
//		if(DEBUG){
//			throw new RuntimeException(string);
//		}
//	}
//    protected void error(String string) {
//		throw new RuntimeException(string);
//	}
    
//    /**
//     * 转化成带换行和缩进的JSON 格式
//     * @param json String
//     * @return String
//     */
//    public static String formatString(String json){
//    	StringBuilder sb=new StringBuilder();
//    	int level=0;
//    	char[] chars=json.toCharArray();
//    	//双引号里面的逗号要忽略
//    	boolean inComma=false;
//    	for(int i=0;i<chars.length;i++){
//    		char c=chars[i];
//    		if(inComma){
//    			if(c=='"'){
//    				if(chars[i-1]!='\\')
//    				inComma=false;
//    			}
//    			sb.append(c);
//    		}else if(c=='"'){
//    			inComma=true;
//    			sb.append(c);
//    		}else if(c==','){
//    			sb.append(c);
//    			//换行 + N 个退格
//    			sb.append('\r');
//    			sb.append('\n');
//    			for(int k=0;k<level;k++){
//    				sb.append('\t');
//    			}
//    		}else if(c=='{'||c=='['){
//    			sb.append(c);
//    			//进入快编辑模式，如果网后直接扫到结束，则直接添加到结束符号后，而不触发换行
//    			//否则继续往下走
//    			boolean compress=false;
//    			for(int m=i+1;m<chars.length;m++){
//    				char c2=chars[m];
//    				if(c2=='{'||c2=='['){
//    					break;
//    				}
//    				if(c2==']'||c2=='}'){
//    					//进入append模式
//    					sb.append(chars,i+1,m-i);
//    					i=m;
//    					compress=true;
//    					break;
//    				}
//    			}
//    			if(!compress){
//	    			level++;
//	    			//换行 + N 个退格
//	    			sb.append('\r');
//	    			sb.append('\n');
//	    			for(int k=0;k<level;k++){
//	    				sb.append('\t');
//	    			}
//    			}
//    		}else if(c=='}'||c==']'){
//    			level--;
//    			sb.append('\r');
//    			sb.append('\n');
//    			for(int k=0;k<level;k++){
//    				sb.append('\t');
//    			}
//    			sb.append(c);
//    		}else{
//    			sb.append(c);
//    		}
//    	}
//    	return sb.toString();
//    }
    
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

	

//	protected void escapeHtmlJson(String s,StringBuilder sb){
//		escapeHtmlJson(s,sb,unescape);
//	}
//	protected static void escapeHtmlJson(String s,StringBuilder sb,boolean unescape){
//		if(!unescape){
//			sb.append('"');
//			if(s!=null){
//				char[] c=s.toCharArray();
//				for (int i = 0; i < c.length; i++) {
//					switch (c[i]) {
//					case '\\':
//						sb.append("\\\\");
//						break;
//					case '\t':
//						sb.append("\\t");
//						break;
//					case '&':
//						sb.append("&amp;");
//						break;
//					case '<':
//						sb.append("&lt;");
//						break;
//					case '>':
//						sb.append("&gt;");
//						break;
//					case '\"':
//						sb.append("&quot;");
//						break;
//					case '\'':
//						sb.append("&#39;");
//						break;
//					case '\r': { //把换行替换成<br/>
//						sb.append("<br/>");
//						if (i + 1 < c.length && c[i + 1] == '\n') {
//							i++; //如果紧跟着的那个是\n那么忽略掉.这样对macos linux window 都支持
//						}
//						break;
//					}
//					case ' ': {
//						if (i + 1 < c.length && c[i + 1] == ' ') {
//							sb.append("&nbsp;"); //根据HTML规范如果两个以上的空格,除了最后一个是空格外,其他要打&nbsp;
//						} else {
//							sb.append(' ');
//						}
//						break;
//					}
//					case '\n': //把换行替换成<br/>
//						sb.append("<br/>");
//						break;
//					default:
//						if (c[i]<0||c[i]>31) {
//							sb.append(c[i]);
//						}
//					}
//				}
//			}
//			sb.append('"');
//		}else{
//			sb.append('"');
//			escapeJson(s,sb);
//			sb.append('"');
//		}
//	}
//	/**
//	 * 转义JSON。转义的结果追加到sb中
//	 * @param s String
//	 * @param sb StringBuilder
//	 */	
//	public static void escapeJson(String s,StringBuilder sb){
//		if(s==null)return;
//		for(char c:s.toCharArray()){
//			switch(c){
//			case '\\':sb.append("\\\\");break;
//			case '"':sb.append("\\\"");break;
//			case '\r':sb.append("\\r");break;
//			case '\n':sb.append("\\n");break;
//			case '\b':sb.append("\\b");break;
//			case '\f':sb.append("\\f");break;
//			case '\t':sb.append("\\t");break;
//			default :if(c<0||c>31)sb.append(c);
//			}
//		}
//	}

}
