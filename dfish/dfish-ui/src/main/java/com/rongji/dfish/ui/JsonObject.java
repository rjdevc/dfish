package com.rongji.dfish.ui;

import java.io.Serializable;


/**
 * JsonObject 是指在dfish-ui提醒中可以和前端交互的内容，这些内容被转化成json对象输出的内容。
 * <p>有别与默认的JAVA bean。为了能让前端识别json的作用，这些对象，必须包含一个type属性。
 * @author DFish Team 
 * @version 1.1 DFish team
 * @since DFish 3.0
 */
public interface JsonObject extends Serializable {
	
	/**
	 * 获取这个对象的种类名
	 * <p>种类名称一般标明这个对象的功能。JS引擎会根据这个对象的种类为其赋予一定的功能.
	 * 这个种类名称一般是该类名的全称或缩写。如GridLayout的种类名称是grid</p>
	 * <p>少数对象只能以某些对象的子元素出现，这时候type值可能为空</p>
	 * @return String
	 */
	String getType();
	/**
	 * asJson就是转成JSON字符串。
	 * @return String
	 */
	String asJson();
	/**
	 * JsonObject的toString就是转成JSON字符串。
	 * @return String
	 */
	String toString();

}
