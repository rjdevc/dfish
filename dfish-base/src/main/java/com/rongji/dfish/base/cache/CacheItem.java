package com.rongji.dfish.base.cache;

/**
 * 在cache中放置的对象实体。
 * 除了逻辑对象本身还会记录下这条数据产生时间。
 * 以便对象做更高级的的操作，如过期判断等。
 * @param <V> 数据类型
 */
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
