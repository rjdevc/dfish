package com.rongji.dfish.misc.cache.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.rongji.dfish.misc.cache.OptionCache;
import com.rongji.dfish.misc.cache.OptionCacheFactory;

/**
 * 内存缓存工厂,将缓存数据存放在内存中,通过缓存名称来获取或删除对应的缓存
 * @author YuLM
 *
 * @param <K> 键
 * @param <V> 值
 * @deprecated 该缓存方法不推荐使用,建议使用base包的相关缓存方法
 */
public class MemoryOptionCacheFactory<K, V> implements OptionCacheFactory<K, V> {
	
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

	Map<String, OptionCache<K, V>> caches = Collections.synchronizedMap(new HashMap<String, OptionCache<K, V>>());

	@Override
    public OptionCache<K, V> getCache(String cacheName) {
		OptionCache<K, V> cache = caches.get(cacheName);
		if (cache == null) {
			cache = new SizeAndTimeLimitOptionCache<K, V>(getMaxSize(), getAlive());
			caches.put(cacheName, cache);
		}
		return cache;
    }

	@Override
    public void removeCache(String cacheName) {
		OptionCache<K, V> cache = caches.remove(cacheName);
	    if (cache != null) {
	    	cache.clear();
	    }
    }

}
