package com.rongji.dfish.misc.batch;

import java.util.List;
import java.util.Map;
/**
 * 批量的获取数据 批量获取数据。
 * @author LinLW
 *
 * @param <K>
 * @param <V>
 */
public interface BatchFetcher<K,V> {
	Map<K, V> fetch(List<K> keys) throws Exception;
}
