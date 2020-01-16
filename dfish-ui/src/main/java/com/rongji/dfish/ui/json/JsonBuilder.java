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

}
