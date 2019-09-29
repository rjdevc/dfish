package com.rongji.dfish.base.cache;

import java.util.Map;
import java.util.Set;

/**
 * Description: 缓存值获取器
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		YuLM
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018年5月16日 上午11:05:06		YuLM			1.0				1.0 Version
 */
public interface CacheValueGetter<K, V> {

	/**
	 * 缓存名称
	 * @return {@link String}
	 * @author lamontYu
	 */
	String cacheName();
	
	/**
	 * 根据Key批量获取对应的值
	 * @param keys 关键字
	 * @return {@link Map}&lt;K, V&gt;
	 * @author lamontYu
	 */
	Map<K, V> gets(Set<K> keys);
	
}
