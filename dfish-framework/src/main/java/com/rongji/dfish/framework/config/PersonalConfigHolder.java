package com.rongji.dfish.framework.config;

/**
 * 个人配置接口,主要用于获取/设置个人配置
 * @since 2.x
 */
public interface PersonalConfigHolder {

	/**
	 * 获取个人配置
	 * @param userId 用户编号
	 * @param key 配置key
	 * @return 配置值
	 */
	String getProperty(String userId, String key);

	/**
	 * 设置个人配置
	 * @param userId 用户编号
	 * @param key 配置key
	 * @param value 配置值
	 */
	void setProperty(String userId, String key, String value);

	/**
	 * 重置个人配置
	 */
	void reset();
}
