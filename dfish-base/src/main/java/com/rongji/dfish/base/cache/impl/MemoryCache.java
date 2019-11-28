package com.rongji.dfish.base.cache.impl;

import com.rongji.dfish.base.batch.BatchAction;

/**
 * 内存缓存，如果数量达到了上限或者里面的内容已经操作期限那么返回空。
 *
 * @author LinLW
 * @version 1.0
 */
public class MemoryCache<K, V> extends BaseCache<K, V> {

    public MemoryCache() {
        super(null);
    }

    public MemoryCache(BatchAction<K, V> valueGetter, int maxSize, long alive) {
        super(valueGetter, maxSize, alive);
    }


}
