package com.rongji.dfish.ui.json;

import java.util.Stack;

/**
 * JsonBuilder 是一个用于将实体类转化为JSON的工具。
 * <p>每个JsonBuilder 都用于将一定种类的Object转化成String。为了构建JSON能够高效，这里使用了模板方法模式。
 * 规定所有的JsonBuilder 都应该实现几组基本的操作，并且最重要的操作buildJson 一直使用同一个StringBuilder进行追加。</p>
 * @author DFish Team
 *
 */
public interface JsonBuilder {
	/**
	 * 把指定的对象转化为 json格式，并追加到指定的StringBuilder中。
	 * <p>因为一个大的对象构建的时候，它的每个属性很可能还是一个JsonObject还可以调用这个对象的转化JSON格式的方法。
	 * 如果我们能够重复利用同一个StringBuilder，将会大幅度提高性能。</p>
	 * @param o 指定的对象
	 * @param sb StringBuilder
	 * @param path Stack
	 * @since dfish 1.0 原来名字是buildXML
	 */
	void buildJson(Object o,StringBuilder sb,Stack<PathInfo> path);

	/**
	 * 通知这个构建器，忽略掉对象的某些属性。
	 * @param propName 属性名
	 */
	void removeProperty(String propName);
	/**
	 * 通知这个构建器，将对象的某些属性显示成其他名字。
	 * 这主要是因为一些关键字名称，在java端和JS端有着不同的名字。
	 * 比如js端class表示CSS的类。但在java端这个名称是Object里面的固有方法(getClass())而不能试用
	 * 所以在JAVA端它叫做styleClass
	 * 另外某些名称因为太长而影响交互效率时，也可能用替换成简称。
	 * @param propName 属性名
	 * @param jsonPropName 属性名
	 */
	void replaceProperty(String propName, String jsonPropName);
}
