package com.rongji.dfish.base.cache;

public class CacheItem<V> {
	
	private V value;
	private long born;

	public CacheItem(V value) {
		this.value = value;
		born = System.currentTimeMillis();
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public long getBorn() {
		return born;
	}

}
