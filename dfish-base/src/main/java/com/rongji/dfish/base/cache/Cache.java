package com.rongji.dfish.base.cache;

import java.util.Map;
import java.util.Set;

/**
 * DFish 中用到的键值对缓存。
 * 注意缓存中的值不保证都不会丢失，可能会根据缓存的大小，
 * 数据的新旧部分数据会被清理，具体缓存的实现将会指定是清理最旧的数据还是清理不经常使用的数据，或是其他规范。
 * @author LinLW v1.0 YuLM v1.1
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
	 * @param key K[]
	 * @return List&lt;V&gt;
	 */
	@SuppressWarnings("unchecked")
	Map<K, V> gets(K... key);
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
	void putAll(Map<K, V> m);
	/**
	 * 删除一个值。
	 * 删除这个值的时候，<strong>有可能</strong>把旧的值置放出来。
	 * @param key K
	 * @return  V
	 */
	V remove(K key);
	/**
	 * 判定是否包含本关键字
	 * @param key K
	 * @return boolean
	 */
	boolean containsKey(K key);
	
//	/**
//	 * 判断是否包含本值
//	 * @param value V
//	 * @return boolean
//	 */
//	boolean containsValue(V value);
	/**
	 * 如果这个缓存支持size的话，应该返回这个缓存现在已经缓存了多少对键值对。
	 * 如果不支持的话，返回-1
	 * @return int
	 */
	int size();
	/**
	 * 清空缓存
	 */
	void clear();
	/**
	 * 键的集合
	 * @return Set&lt;K&gt;
	 */
	Set<K> keySet();
//	/**
//	 * 值的集合
//	 * @return Collection&lt;V&gt;
//	 */
//	Collection<V> values();
	
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
	
	CacheItem<V> getItem(K key);
	
	Map<K, CacheItem<V>> getItems();

}
