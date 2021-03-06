package com.rongji.dfish.base.cache.impl;

import com.rongji.dfish.base.batch.BatchFunction;
import com.rongji.dfish.base.cache.Cache;
import com.rongji.dfish.base.cache.CacheItem;
import com.rongji.dfish.base.util.Utils;

import java.util.*;

/**
 * 缓存抽象类,该类实现缓存获取等通用的方法
 *
 * @param <K> 缓存Key
 * @param <V> 缓存Value
 * @author lamontYu
 */
public class BaseCache<K, V> extends CachedBatchFunction<K, V> implements Cache<K, V> {

    @Override
    public String getName() {
        return name;
    }

    /**
     * 缓存名称
     * @param name 缓存名称
     */
    public void setName(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public long getAlive() {
        return alive;
    }

    @Override
    public void setAlive(long alive) {
        this.alive = alive;
    }

    /**
     * 注入一个BatchAction 这个batchAction 的作用是批量从原始途经获取数值的。
     * BaseCache里面是策略会在何时的时机回调该valueGetter
     * @return BatchAction
     */
    public BatchFunction<K, V> getValueGetter() {
        return function;
    }

    /**
     * 注入一个BatchAction 这个batchAction 的作用是批量从原始途经获取数值的。
     * BaseCache里面是策略会在何时的时机回调该valueGetter
     * @param valueGetter BatchAction
     */
    public void setValueGetter(BatchFunction<K, V> valueGetter) {
        this.function = valueGetter;
    }

    /**
     * 构造函数
     */
    public BaseCache() {
        super();
    }

    /**
     * 构造函数
     * @param valueGetter
     */
    public BaseCache(BatchFunction<K, V> valueGetter) {
        super(valueGetter);
    }

    /**
     * 构造函数
     *
     * @param maxSize 最大缓存数量
     * @param alive   最大生存时间 -毫秒
     */
    public BaseCache(BatchFunction<K, V> valueGetter, int maxSize, long alive) {
        super(valueGetter, maxSize, alive);
    }

    /**
     * 设置这个key的值，如果这个key原来就有对应的值，则返回这个值，否则返回空
     *
     * @param key   K
     * @param value V
     * @return V
     */
    @Override
    public V put(K key, V value) {
        CacheItem<V> item = core.put(key, new CacheItem<>(value));
        if (item != null) {
            return item.getValue();
        }
        return null;
    }




    /**
     * 删除
     *
     * @param key 键
     * @return V 缓存值
     */
    @Override
    public V remove(K key) {
        CacheItem<V> item = core.remove(key);
        if (item != null) {
            return item.getValue();
        }
        return null;
    }

    /**
     * 清除
     */
    @Override
    public void clear() {
        core.clear();
    }



    @Override
    public V get(K key) {
        return super.apply(key);
    }

    @Override
    public Map<K, V> gets(K... keys) {
        if (keys == null) {
            return Collections.emptyMap();
        }

        return apply(new HashSet<>(Arrays.asList(keys)));
    }

    @Override
    public Map<K, V> gets(Collection<K> keys) {
        if (keys == null) {
            return Collections.emptyMap();
        }
        return apply(new HashSet<>(keys));
    }

    @Override
    public boolean containsKey(K key) {
        return core.containsKey(key);
    }

    /**
     * 判断是否包含value值
     * @param value 缓存值
     * @return 是否包含
     */
    @Override
    public boolean containsValue(V value) {
        Collection<CacheItem<V>> col = core.values();
        for (CacheItem<V> item : col) {
            if (value == null) {
                if (item.getValue() == null) {
                    return true;
                }
            } else {
                if (value.equals(item.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int size() {
        return core.size();
    }

    @Override
    public Set<K> keySet() {
        return core.keySet();
    }

    /**
     * value集合
     * @return value集合
     */
    @Override
    public Collection<V> values() {
//        Collection<CacheItem<V>> col = core.values();
//        ArrayList<V> result = new ArrayList<>();
//        if (col != null) {
//            for (CacheItem<V> item : col) {
//                result.add(item.getValue());
//            }
//        }
        return core.values().stream().collect(ArrayList::new,
                (list,item)->list.add(item.getValue()),
                List::addAll);
    }

    @Override
    public Map<K, CacheItem<V>> getItems() {
        return Collections.unmodifiableMap(core);
    }

    @Override
    public CacheItem<V> getItem(K key) {
        CacheItem<V> item = core.get(key);
        if (item == null) {
            return null;
        }
        CacheItem<V> result = new CacheItem<>(item.getValue());
        Utils.copyPropertiesExact(result, item);
        return result;
    }

}
