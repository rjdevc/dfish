package com.rongji.dfish.base.cache.impl;

/**
 * 内存缓存，如果数量达到了上限或者里面的内容已经操作期限那么返回空。
 * 
 * @author LinLW
 * @version 1.0
 *
 */
public class MemoryCache<K, V> extends AbstractCache<K, V> {

	public MemoryCache() {
		super();
	}

	public MemoryCache(int maxSize, long alive) {
		super(maxSize, alive);
	}
	
	
}
