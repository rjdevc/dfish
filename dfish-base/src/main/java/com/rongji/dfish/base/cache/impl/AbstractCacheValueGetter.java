package com.rongji.dfish.base.cache.impl;

import com.rongji.dfish.base.batch.BatchAction;
import com.rongji.dfish.base.Utils;

import java.util.*;

// 原则上要使用桥接模式。另外因为功能是自动分组。名字应该是分组的获取器
@Deprecated
public abstract class AbstractCacheValueGetter<K, V> implements BatchAction<K, V> {

	public static final int BATCH_SIZE = 512;
	
	@Override
	public Map<K, V> act(Set<K> keys) {
		if (Utils.isEmpty(keys)) {
			return Collections.emptyMap();
		}
		List<K> keyList = new ArrayList<K>(keys);
		Map<K, V> map = null;
		int keySize = keyList.size();
		int fromIndex,toIndex = 0;
		while (toIndex < keySize) {
			fromIndex = toIndex;
			toIndex += BATCH_SIZE;
			toIndex = toIndex > keySize ? keySize : toIndex;
			List<K> subList = keyList.subList(fromIndex, toIndex);
			if (Utils.notEmpty(subList)) {
				Map<K, V> tempMap = batchGets(subList);
				if (tempMap != null) {
					if (map == null) {
						map = tempMap;
					} else {
						map.putAll(tempMap);
					}
				}
			}
		}
		return map;
	}

	/**
	 * 批量获取数据
	 * @param keys
	 * @return
	 */
	protected abstract Map<K, V> batchGets(List<K> keys);

}
