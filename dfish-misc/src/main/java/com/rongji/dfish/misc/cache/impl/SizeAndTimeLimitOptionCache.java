package com.rongji.dfish.misc.cache.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.misc.cache.OptionCache;

/**
 * 选项缓存,带有顺序的缓存
 * @author		YuLM
 * @version		1.0
 * @deprecated 该缓存方法不推荐使用,建议使用base包的相关缓存方法
 */
public class SizeAndTimeLimitOptionCache<K, V> extends SizeAndTimeLimitCache<K, V> implements OptionCache<K, V> {
	public SizeAndTimeLimitOptionCache() {
	    super();
    }

	public SizeAndTimeLimitOptionCache(int maxSize, long alive) {
	    super(maxSize, alive);
    }

	/**
	 * 上次加载时间
	 */
	private long lastReload;
	
	/**
	 * 返回缓存的值
	 * @param key
	 * @return V
	 */
	@Override
	public V get(K key) {
		Item<V> item=getCore().get(key);
		if (item == null) {
			return null;
		}
		if(System.currentTimeMillis()-item.born>getAlive()){
			// 这里为了能保持选项数据不会丢失,不做删除动作,仅返回结果为空
//			getCore().remove(key);
			return null;
		}
		return item.value;
	}
	
	/**
	 * 删除缓存值,这里为保持选项数据并且排序一致,不进行个别数据的删除,数据删除可通过{@link #clear()}来清空数据
	 * @param key
	 * @return V
	 */
	@Override
	@Deprecated
    public V remove(K key) {
		// 这里为了能保持选项数据不会丢失,不做删除动作,仅返回结果为空
	    return null;
    }

	@Override
    public LinkedHashMap<K, V> getData() {
		LinkedHashMap<K, V> result = null;
	    Map<K,Item<V>> core = getCore();
	    if ((System.currentTimeMillis() - lastReload) <= getAlive() && Utils.notEmpty(core)) {
	    	result = new LinkedHashMap<K, V>();
	    	for (Map.Entry<K, Item<V>> entry : core.entrySet()) {
	    		Item<V> item = entry.getValue();
	    		V value = null;
	    		if (item != null) {
	    			value = item.value;
	    		}
	    		result.put(entry.getKey(), value);
	    	}
	    } else {
	    	result = new LinkedHashMap<K, V>(0);
	    }
	    return result;
    }
	
	@Override
    public void setData(Map<? extends K, ? extends V> data) {
		Map<K,Item<V>> core = getCore();
		// 数据清理
		core.clear();
	    if (data != null) { // 数据不为空则重新设置缓存,否则不进行设置
	    	for (Entry<? extends K, ? extends V> entry : data.entrySet()) {
	    		put(entry.getKey(), entry.getValue());
	    	}
	    	lastReload = System.currentTimeMillis();
	    }
    }
	
}
