package com.rongji.dfish.ui;

import java.util.Map;

/**
 * TemplateSupport 是指当前对象支持DFish3.2的模板功能。
 * <p>假设有个对象是{"type":"text","name":"xxx","value":"xxx" } 我们做模板的时候希望他的值应该根据具体情况变化的。
 * 我们将会定义成{"type":"text","name":"xxx","@value":"$data.xxx" } 这个模板结合后台返回的数据，则可显示出最终的结果。</p>
 * <p>为了开发方便，我们不会要求所有的绑定都要转成动态模板，所以对大部分DFish的JAVA对象进行了简单的封装。
 * 是的很多对象都可以设置 @prop 为一个表达式。已提供最基本的功能。</p>
 * <p>完整的模板prop 的结果不一定是一个表达式，也可能是一个具体的Widget或@include 考虑到，尽可能兼容旧版，
 * 这里，仅支持表达式。而没有引入复杂的uitemp类。如果有需要，则可把对象转化成动态模板后，再设置它的at</p>
 *
 * @param <T> 当前对象类型
 */
public interface TemplateSupport<T extends TemplateSupport<T>> {
	/**
	 * 设置某个属性的值是动态的，并且他的结果是表达式的结果
	 * @param prop 属性名，不需要带@
	 * @param expr 表达式，经常会用到$
	 * @return this
	 */
	T at(String prop,String expr);

	/**
	 * 当这个组件在模板中使用的时候，设置这个组件是批量出现的。这里设置数据取值的表达式
	 * @param expr 数据取值的表达式
	 * @return
	 */
	T setFor(String expr);

	/**
	 * 当这个组件在模板中使用的时候，设置这个组件是批量出现的。
	 * @param expr 数据取值的表达式
	 * @param itemName 默认为item
	 * @param indexName 默认为空
	 * @return
	 */
	T setFor(String expr,String itemName,String indexName);
	
	/**
	 * 取得所有的at设置，以便转化成json
	 * @return Map
	 */
	Map<String,String> ats();
	
	/**
	 * 设置所有的at的值。主要是在封装类或者其他有需要拷贝对象的场景使用。
	 * @param ats
	 */
	void ats(Map<String,String> ats);
}
