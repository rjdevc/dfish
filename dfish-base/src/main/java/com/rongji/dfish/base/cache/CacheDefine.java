//package com.rongji.dfish.base.cache;
//
//import java.util.List;
//
///**
// * Description: 缓存定义,用于定义系统中的缓存
// * Copyright:   Copyright © 2018
// * Company:     rongji
// * @author		YuLM
// * @version		1.0
// *
// * Modification History:
// * Date						Author			Version			Description
// * ------------------------------------------------------------------
// * 2018年5月16日 上午10:39:17		YuLM			1.0				1.0 Version
// */
//public class CacheDefine<T> {
//
//	/**
//	 * 是否默认缓存定义
//	 */
//	private boolean defaultCache;
//	/**
//	 * 支持的缓存名称
//	 */
//	private List<String> supportNames;
//	/**
//	 * 最大缓存数量,当设置为-1时,则表示不限制数量
//	 */
//	private int maxSize;
//	/**
//	 * 缓存有效时间
//	 */
//	private long alive;
//
//	private Class<Cache<String, T>> cacheClazz;
//
//	public boolean isDefaultCache() {
//		return defaultCache;
//	}
//
//	public void setDefaultCache(boolean defaultCache) {
//		this.defaultCache = defaultCache;
//	}
//
//	public List<String> getSupportNames() {
//		return supportNames;
//	}
//
//	public void setSupportNames(List<String> supportNames) {
//		this.supportNames = supportNames;
//	}
//
//	public int getMaxSize() {
//		return maxSize;
//	}
//
//	public void setMaxSize(int maxSize) {
//		this.maxSize = maxSize;
//	}
//
//	public long getAlive() {
//		return alive;
//	}
//
//	public void setAlive(long alive) {
//		this.alive = alive;
//	}
//
//	public Class<Cache<String, T>> getCacheClazz() {
//		return cacheClazz;
//	}
//
//	public void setCacheClazz(Class<Cache<String, T>> cacheClazz) {
//		this.cacheClazz = cacheClazz;
//	}
//
//	@Override
//	public String toString() {
//		return "CacheDefine [defaultCache=" + defaultCache + ", supportNames=" + supportNames + ", maxSize=" + maxSize
//				+ ", alive=" + alive + ", cacheClazz=" + cacheClazz + "]";
//	}
//
//}
