package com.rongji.dfish.misc.cache;

import java.util.Map;
/**
 * 选项缓存,用于缓存系统有使用到的选项缓存数据
 * 
 * @author lamontYu
 * @version 1.0
 *
 * @param <K> 键
 * @param <V> 值
 * @deprecated 该缓存方法不推荐使用,建议使用base包的相关缓存方法
 */
public interface OptionCache<K, V> extends Cache<K, V> {

	/**
	 * 获取缓存数据列表
	 * @return Map&lt;K,V&gt;
	 */
	Map<K, V> getData();
	/**
	 * 重置缓存数据列表
	 * @param data Map&lt;K,V&gt;
	 */
	void setData(Map<? extends K, ? extends V> data);
	
}
