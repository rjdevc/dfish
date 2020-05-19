package com.rongji.dfish.base.cache.impl;

import com.rongji.dfish.base.batch.BatchFunction;

/**
 * 内存缓存，如果数量达到了上限或者里面的内容已经操作期限那么返回空。
 *
 * @author LinLW
 * @version 1.0
 */
public class MemoryCache<K, V> extends BaseCache<K, V> {
    /**
     * 构造函数
     */
    public MemoryCache() {
        super(null);
    }

    /**
     * 构造函数
     * @param valueGetter 值获取
     * @param maxSize 最大缓存数
     * @param alive 存活时间
     */
    public MemoryCache(BatchFunction<K, V> valueGetter, int maxSize, long alive) {
        super(valueGetter, maxSize, alive);
    }


}
