package com.rongji.dfish.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rongji.dfish.base.util.BeanUtil;

/**
 * 用于记录 创建初始值的模板。
 * 比如说 如果设置了 helper.GridPanel.escape=true 那么
 * 将会在GridPanel 这class对应的ObjectTemplate中记录  methods=[#setEscape] value=true
 * 那么以后在每次new GridPanel的时候，将会自动将escape属性设置为true
 * 支持helper.GridPanel.pub.tip=true
 * 那么ObjectTemplate中记录  methods=[#getPub,#setEscape] value=true
 * 
 * 注意多级属性中并不会在get第一级属性的时候进行空判断。然后自动创建。防止创建的时候产生其他风险。
 * 上例子，如果getPub为空，默认值设置将会失败。
 * 
 * 常见用法 ObjectTemplate.get(getClass()).bundleProperties(this);
 * @author DFishTeam
 *
 */
public class ObjectTemplate {
	private static class PropertyTemplate{
		List<Method> methods;//最后一个method是setter前面都是getter
		Object value;
	}
	private static final Class<?>[] NO_ARG=new Class<?>[0];
	private static final Object[] NO_PARAM=new Object[0];
	private static Map<Class<?>,ObjectTemplate> DEFAULT_INSTANCE=new HashMap<Class<?>,ObjectTemplate>();
	private static String PREFIX ;
	protected static final Log LOG=LogFactory.getLog(ObjectTemplate.class);
	protected static ResourceBundle RES_BUNDLE;
	static{
		try{
			String fullName=ObjectTemplate.class.getName();
			String simpleName=ObjectTemplate.class.getSimpleName();
			PREFIX=fullName.substring(0,fullName.indexOf(simpleName));
			RES_BUNDLE=ResourceBundle.getBundle(PREFIX+"default_prop");
			LOG.info("loading properties file : "+PREFIX+"default_prop");
		}catch(Exception ex){
			LOG.warn("can not find properties file : "+PREFIX+"default_prop",ex);
		}
	}
		
	private Class<?> clz;
	private List<PropertyTemplate> propTemps;
	
	/**
	 * 取得某个类的模板
	 * @param clz Class
	 * @return ObjectTemplate
	 */
	public static ObjectTemplate get(Class<?> clz){
		ObjectTemplate def=DEFAULT_INSTANCE.get(clz);
		if(def==null){
			//第一个对象一个个读取属性并保存
			//尝试读取所有属性
			def = new ObjectTemplate(clz);
			DEFAULT_INSTANCE.put(clz, def);
		}
		return def;
	}

	private ObjectTemplate(Class<?> itemCls){
		this.clz=itemCls;
		propTemps=new ArrayList<PropertyTemplate>();
		//读取

		String clzFullName = clz.getName();
		String relateClzName=clzFullName;
		if(clzFullName.startsWith(PREFIX)){
			relateClzName=clzFullName.substring(PREFIX.length());
		}
		java.util.Enumeration<String> keys=RES_BUNDLE.getKeys();
		outter:while(keys.hasMoreElements()){
			String key =keys.nextElement();
			if(!key.startsWith(relateClzName+".")){
				continue;
			}
			String propName=key.substring(relateClzName.length()+1);
			String propValue=RES_BUNDLE.getString(key);
			LOG.info(clz.getName()+"."+propName+" DEFAULT_VALUE= "+propValue);
			//如果 propName有多重，前面几重全部取getter。最后一重先从getter获取得返回值，再调用Setter
			String[] propTokens=propName.split("[.]");
			Class<?> curClz=clz;
			PropertyTemplate pt=new PropertyTemplate();
			pt.methods=new ArrayList<Method>();
			Class<?>returnType=null;
			for(int index=0;index<propTokens.length;index++){
				//取得getter
				String propToken=propTokens[index];
				String getterName="get"+((char)(propToken.charAt(0)-32))+propToken.substring(1);
				try {
					Method getter=curClz.getMethod(getterName, NO_ARG);
					returnType=BeanUtil.getRealReturnType(getter, curClz);
					if(index>=propTokens.length-1){
						//最后一重先从getter获取得返回值，再调用Setter
						String setterName="s"+getter.getName().substring(1);
						Method setter =null;
						if(Object.class==returnType){
							//如果返回值是tip等可变类型。有可能是String Boolean或者是专门的Tip类
							//那么我们根据数据决定这个类型。
							if("true".equals(propValue)||"false".equals(propValue)){
								try{
									setter = curClz.getMethod(setterName, new Class<?>[]{Boolean.class});
									returnType=Boolean.class;
								}catch (Exception e) {}
							}
							if(setter==null){
								try{
									Integer.parseInt(propValue);
									setter = curClz.getMethod(setterName, new Class<?>[]{Integer.class});
									returnType=Integer.class;
								}catch (Exception e) {}
							}
							if(setter==null){
								try{
									setter = curClz.getMethod(setterName, new Class<?>[]{String.class});
									returnType=String.class;
								}catch (Exception e) {}
							}
							if(setter==null){
								try{
									setter = curClz.getMethod(setterName, new Class<?>[]{Object.class});
								}catch (Exception e) {}
							}
						}else{
							setter = curClz.getMethod(setterName, new Class<?>[]{returnType});
						}
						pt.methods.add(setter);
					}else{
						pt.methods.add(getter);
						curClz=returnType;					
					}
				} catch (Exception e) {
					LOG.info("获取方法异常@" + clz.getName() + "." + propToken, e);
					continue outter;
				}
			}
			Object value=propValue;
			if(Integer.class==returnType){
				value=new Integer(propValue);
			}else if(Boolean.class==returnType){
				value=new Boolean(propValue);
			}
			pt.value=value;
			propTemps.add(pt);
		}
	}
	/**
	 * 绑定属性
	 * @param o 对象
	 */
	public void bundleProperties(Object o){
		for(PropertyTemplate prop: propTemps){
			try {
				Object cur=o;
				int i=0;
				for(Method m:prop.methods){
					if(++i<prop.methods.size()){
						cur=m.invoke(cur, NO_PARAM);
					}else{
						m.invoke(cur, new Object[]{prop.value});
					}
				}
			} catch (Exception e) {
				String methodName = "";
				for(int i=0;i<prop.methods.size();i++){
					methodName+=prop.methods.get(i).getName();
					if(i<prop.methods.size()-1){
						methodName+="().";
					}
				}
				Throwable cause = null;
				if (e instanceof InvocationTargetException) {
					cause = ((InvocationTargetException)e).getTargetException();
				} else {
					cause = e;
				}
				LOG.error("设置默认值异常@" + clz.getName() + "." + methodName + "(" + prop.value + ")", cause);
			}
		}
	}
	
}

