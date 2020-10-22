package com.rongji.dfish.base.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * DFish 中用到的键值对缓存。
 * 注意缓存中的值不保证都不会丢失，可能会根据缓存的大小，
 * 数据的新旧部分数据会被清理，具体缓存的实现将会指定是清理最旧的数据还是清理不经常使用的数据，或是其他规范。
 * @author LinLW v1.0 lamontYu v1.1
 * @version 1.1
 */
public interface Cache<K, V> {
	/**
	 * 取得一个值
	 * @param key K
	 * @return V
	 */
	V get(K key);
	/**
	 * 取得多个值，一次性取得多个值，可以触发批量，提高性能
	 * @param keys key数组
	 * @return List&lt;V&gt;
	 */
	@SuppressWarnings("unchecked")
	Map<K, V> gets(K... keys);

	/**
	 * 取得多个值，一次性取得多个值，可以触发批量，提高性能
	 * @param keys key的集合
	 * @return List&lt;V&gt;
	 */
	Map<K, V> gets(Collection<K> keys);
	/**
	 * 设置一个值。
	 * 设置这个值的时候，<strong>有可能</strong>把旧的值置放出来。
	 * @param key K
	 * @param value V
	 * @return V
	 */
	V put(K key, V value);
	/**
	 * 批量设置值
	 * @param m
	 */
	default void putAll(Map<K, V> m) {
		if (m == null) {
			return;
		}
		m.forEach(this::put);
	}
	/**
	 * 删除一个值。
	 * 删除这个值的时候，<strong>有可能</strong>把旧的值置放出来。
	 * @param key K
	 * @return  V
	 */
	V remove(K key);
	/**
	 * 如果这个缓存支持size的话，应该返回这个缓存现在已经缓存了多少对键值对。
	 * 如果不支持的话，返回-1
	 * @return int
	 */
	int size();

	/**
	 * 缓存是否可被清理
	 *
	 * @return 是否可清理
	 */
	default boolean clearable() {
		return true;
	}

	/**
	 * 清空缓存
	 */
	void clear();

	/**
	 * 清理超期缓存数据(超过2倍存活时间)
	 */
	void clearExpiredData();
	/**
	 * 键的集合
	 * @return Set&lt;K&gt;
	 */
	Set<K> keySet();
	/**
	 * 判定是否包含本关键字
	 * @param key K
	 * @return boolean
	 */
	default boolean containsKey(K key) {
		return keySet().contains(key);
	}

	/**
	 * 值集合(只查询已存在的值)
	 * @return Collection&lt;V&gt;
	 */
	Collection<V> values();

	/**
	 * 判断是否包含本值(只查询已存在的值)
	 * @param value V
	 * @return boolean
	 */
	default boolean containsValue(V value) {
		return values().contains(value);
	}

	/**
	 * 缓存最大数量
	 * @return int
	 */
	int getMaxSize();
	
	/**
	 * 设置缓存最大数量,当值为-1时不限制
	 * @param maxSize int
	 */
	void setMaxSize(int maxSize);
	
	/**
	 * 缓存有效时间,单位:毫秒
	 * @return long
	 */
	long getAlive();
	
	/**
	 * 设置缓存有效时间,当值小于0时不限制
	 * @param alive long
	 */
	void setAlive(long alive);

	/**
	 * 获得CacheItem
	 * @param key 缓存键
	 * @return 缓存数据项
	 */
	CacheItem<V> getItem(K key);

	/**
	 * 批量获得CacheItem
	 * @return 缓存数据项集合
	 */
	Map<K, CacheItem<V>> getItems();


	/**
	 * 缓存名称
	 * @return {@link String}
	 */
	String getName();

}
