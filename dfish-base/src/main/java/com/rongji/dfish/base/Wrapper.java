package com.rongji.dfish.base;

/**
 * 基于原型构建的封装类接口
 * @author DFish Team
 * @param <P> 原型
 */
public interface Wrapper<P> {

	/**
	 * 获取封装类的原型
	 * @return P
	 */
	P getPrototype();
	
}
