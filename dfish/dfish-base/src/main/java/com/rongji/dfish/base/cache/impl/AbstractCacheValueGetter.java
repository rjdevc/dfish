package com.rongji.dfish.base.cache.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.cache.CacheValueGetter;

public abstract class AbstractCacheValueGetter<K, V> implements CacheValueGetter<K, V> {

	public static final int BATCH_SIZE = 512;
	
	@Override
	public Map<K, V> gets(Set<K> keys) {
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
	
	protected abstract Map<K, V> batchGets(List<K> keys);

}
