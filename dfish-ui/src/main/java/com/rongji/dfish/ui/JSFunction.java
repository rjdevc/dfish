package com.rongji.dfish.ui;

import com.rongji.dfish.ui.json.J;
import com.rongji.dfish.ui.layout.grid.GridColumn;
/**
 * JSFunction 用于表示java script中的function。
 * @author DFish Team
 * @see GridColumn#setFormat(JSFunction)
 * @since DFish 3.0
 *
 */
public class JSFunction {
	private String params;
	private String funtionText;
	/**
	 * 默认构造函数
	 */
	public JSFunction(){}
	/**
	 * 构造函数
	 * 如：
	 * new JSFunction("","var v=this.x.data; return v['id'];")
	 * @param params 参数
	 * @param funtionText 内容
	 */
	public JSFunction(String params,String funtionText){
		this.params=params;
		this.funtionText=funtionText;
	}
	/**
	 * 参数
	 * @return String
	 */
	public String getParams() {
		return params;
	}
	/**
	 * 参数
	 * @param params 参数
	 * @return 本身，这样可以继续设置其他属性
	 */
	public JSFunction setParams(String params) {
		this.params = params;
		return this;
	}
	/**
	 * 函数体
	 * @return String
	 */
	public String getFuntionText() {
		return funtionText;
	}
	/**
	 * 函数体
	 * @param funtionText 函数体
	 * @return 本身，这样可以继续设置其他属性
	 */
	public JSFunction setFuntionText(String funtionText) {
		this.funtionText = funtionText;
		return this;
	}
	public String toString(){
		return J.toJson(this);
	}
}
