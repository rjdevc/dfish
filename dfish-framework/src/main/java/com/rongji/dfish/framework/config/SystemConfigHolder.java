package com.rongji.dfish.framework.config;

/**
 * 系统配置接口,主要用于获取/设置系统全局配置
 * @since 2.x
 */
public interface SystemConfigHolder {

	/**
	 * 获取系统配置
	 * @param key 配置key
	 * @return 配置值
	 */
	String getProperty(String key);

	/**
	 * 设置系统配置
	 * @param key 配置key
	 * @param value 配置值
	 */
	void setProperty(String key, String value);

	/**
	 * 重置系统配置
	 */
	void reset();

}
