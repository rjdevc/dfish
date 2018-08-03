package com.rongji.dfish.misc.cache;

import java.io.Serializable;
import java.util.Set;

/**
 * 缓存工厂,存放系统中有使用到的缓存数据
 * @author LinLW v1.0 YuLM v1.1
 * @version 1.1
 *
 * @param <K> 键
 * @param <V> 值
 * @deprecated 该缓存方法不推荐使用,建议使用base包的相关缓存方法
 */
public interface CacheFactory<K extends Serializable, V> {
	/**
	 * 取得缓存cache
	 * @param cacheName String
	 * @return
	 */
	Cache<K, V> getCache(String cacheName);
	/**
	 * 删除缓存cache
	 * @param cacheName
	 */
	void removeCache(String cacheName);
	/**
	 * 缓存名的集合
	 * @return Set&lt;String&gt;
	 */
	Set<String> cacheNames();
}
