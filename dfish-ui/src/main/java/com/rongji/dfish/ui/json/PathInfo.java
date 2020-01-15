package com.rongji.dfish.ui.json;

import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.form.FormElement;

import java.util.Collection;

/**
 * 用于记录构建JSON过程中的路径
 * @author Dfish team
 *
 */
public class PathInfo {
	/**
	 * 完整构造函数
	 * @param propName String
	 * @param propValue Object
	 */
	public PathInfo(String propName,Object propValue){
		this.propName = propName;
		this.propValue=propValue;
	}
	private String propName;
	private Object propValue;
	/**
	 * 属性名
	 * @return String
	 */
	public String getPropName() {
		return propName;
	}
	/**
	 * 属性名
	 * @param propName 属性名
	 */
	public void setPropName(String propName) {
		this.propName = propName;
	}
	/**
	 * 属性值
	 * @return Object
	 */
	public Object getPropValue() {
		return propValue;
	}
	/**
	 * 属性值
	 * @param propValue Object
	 */
	public void setPropValue(Object propValue) {
		this.propValue = propValue;
	}
	@Override
    public String toString(){
		StringBuilder sb=new StringBuilder();

		sb.append("{\"");
		if(propName!=null){
			sb.append("path\":\"").append(propName).append("\", \"");
		}
		sb.append("obj\":");
		if(propValue==null){
			sb.append("null");
		}else if(propValue instanceof Collection){
			sb.append("\"Arr[size=");
			sb.append(((Collection<?>) propValue).size());
			sb.append("]\"");
		}else if(propValue instanceof Object[]){
			sb.append("\"Arr[leng=");
			sb.append(((Object[]) propValue).length);
			sb.append("]\"");
		}else if(propValue instanceof Node){
			sb.append("\"Widget[type=");
			sb.append(((Node) propValue).getType());
			boolean showed=false;
			if(!showed){
				if(propValue instanceof Node){
					String id=((Node<?>) propValue).getId();
					if(id!=null&&!"".equals(id)){
						showed=true;
						sb.append(",id=");
						sb.append(id);
					}
				}
			}
			if(!showed){
				if(propValue instanceof FormElement){
					String name=((FormElement<?, ?>) propValue).getName();
					if(name!=null&&!"".equals(name)){
						showed=true;
						sb.append(",name=");
						sb.append(name);
					}
				}
			}
			if(!showed){
				if(propValue instanceof HasText){
					String text=((HasText<?>) propValue).getText();
					if(text!=null&&!"".equals(text)){
						showed=true;
						sb.append(",text=");
						sb.append(text);
					}
				}
			}
			sb.append("]\"");
		}
		sb.append('}');
		return sb.toString();
	}
}
