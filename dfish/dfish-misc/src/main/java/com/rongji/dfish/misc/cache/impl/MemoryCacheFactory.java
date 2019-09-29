package com.rongji.dfish.misc.cache.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.rongji.dfish.misc.cache.Cache;
import com.rongji.dfish.misc.cache.CacheFactory;

/**
 * 内存缓存工厂,将缓存数据存放在内存中,通过缓存名称来获取或删除对应的缓存
 * @author lamontYu
 *
 * @param <K> 键
 * @param <V> 值
 * @deprecated 该缓存方法不推荐使用,建议使用base包的相关缓存方法
 */
public class MemoryCacheFactory<K extends Serializable, V> implements CacheFactory<K, V> {
	
	private int maxSize;
	private long alive;
	
	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public long getAlive() {
		return alive;
	}

	public void setAlive(long alive) {
		this.alive = alive;
	}

	Map<String, Cache<K, V>> caches = Collections.synchronizedMap(new HashMap<String, Cache<K, V>>());

	@Override
    public Cache<K, V> getCache(String cacheName) {
		Cache<K, V> cache = caches.get(cacheName);
		if (cache == null) {
			cache = new SizeAndTimeLimitCache<K, V>(getMaxSize(), getAlive());
			caches.put(cacheName, cache);
		}
		return cache;
    }

	@Override
    public void removeCache(String cacheName) {
	    Cache<K, V> cache = caches.remove(cacheName);
	    if (cache != null) {
	    	cache.clear();
	    }
    }

    public Set<String> cacheNames() {
	    return caches.keySet();
    }

}
