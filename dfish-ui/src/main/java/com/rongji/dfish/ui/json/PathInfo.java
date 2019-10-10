package com.rongji.dfish.ui.json;

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
}
