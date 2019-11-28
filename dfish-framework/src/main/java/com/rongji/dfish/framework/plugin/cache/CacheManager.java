package com.rongji.dfish.framework.plugin.cache;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.cache.Cache;
import com.rongji.dfish.base.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 缓存管理类
 *
 * @author lamontYu
 */
@Component
@Lazy(false)
public class CacheManager {
    /**
     * 缓存值获取接口
     */
//	@Autowired(required = false)
//	private List<BatchAction<String, ?>> valueGetters;

    @Autowired(required = false)
    private List<Cache<?, ?>> caches;
    /**
     * 缓存定义
     */
//	@Autowired(required = false)
//	private List<CacheDefine<?>> cacheDefines;
//	private Map<String, Cache<String, ?>> getterMap = Collections.synchronizedMap(new HashMap<>());
    /**
     * 默认缓存定义
     */
//	private CacheDefine<?> defaultCacheDefine;
    /**
     * 定义过的缓存
     */
    private Map<String, Cache<?, ?>> cacheMap = Collections.synchronizedMap(new HashMap<>());

    @PostConstruct
    private void init() {
        // 注册缓存值获取接口
        if (caches != null) {
            for (Cache<?, ?> cache : caches) {
                registerCache(cache);
            }
        }
        // 注册缓存定义
//		if (cacheDefines != null) {
//			for (CacheDefine<?> define : cacheDefines) {
//				registerDefine(define);
//			}
//		}
    }

    public void registerCache(Cache cache) {
        if (cache == null) {
            return;
        }
        if (Utils.isEmpty(cache.getName())) {
            LogUtil.warn("The name of Cache is empty.[" + cache.getClass().getName() + "]");
            return;
        }
        Cache oldGetter = cacheMap.put(cache.getName(), cache);
        if (oldGetter != null) {
            LogUtil.warn("[" + oldGetter.getClass().getName() + "] is replaced by [" + cache.getClass().getName() + "], because of the same name.");
        } else {
            LogUtil.info("The cache is registered.[" + cache.getClass().getName() + "]");
        }
    }

    /**
     * 根据婚车名称获取缓存组
     *
     * @param cacheName 缓存名称
     * @param <K,       V>       缓存值泛型
     * @return 获取的缓存组
     */
    public <K, V> Cache<K, V> getCache(String cacheName) {
        Cache<K, V> cache = (Cache<K, V>) cacheMap.get(cacheName);
        return cache;
    }

    /**
     * 获取系统所有缓存名称
     *
     * @return
     */
    public Collection<String> cacheNames() {
        return Collections.unmodifiableSet(cacheMap.keySet());
    }

    /**
     * 用于清理过期的缓存,一般是用来清理超过2倍存活时间的缓存
     * 调用这个方法不需要过于频繁,影响系统性能,一般情况资源没有过于紧张情况下在非繁忙期清理一次即可
     */
    public void clearExpiredData() {
        for (Map.Entry<String, Cache<?, ?>> entry : cacheMap.entrySet()) {
            Cache<?, ?> cache = entry.getValue();
            if (cache.clearable()) {
                cache.clearExpiredData();
            }
        }
    }

    /**
     * 根据缓存名称移除缓存组
     *
     * @param cacheName 缓存名称
     * @param <K, V>       缓存值泛型
     * @return 移除的缓存组
     */
    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> removeCache(String cacheName) {
        return (Cache<K, V>) cacheMap.remove(cacheName);
    }

}
