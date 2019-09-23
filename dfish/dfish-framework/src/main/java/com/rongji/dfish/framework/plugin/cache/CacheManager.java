package com.rongji.dfish.framework.plugin.cache;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.cache.Cache;
import com.rongji.dfish.base.cache.CacheDefine;
import com.rongji.dfish.base.cache.CacheValueGetter;
import com.rongji.dfish.base.cache.impl.AbstractCache;
import com.rongji.dfish.base.cache.impl.MemoryCache;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.misc.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 缓存管理类
 * @author YuLM
 */
@Component
@Lazy(false)
public class CacheManager {
	/**
     * 缓存值获取接口
	 */
	@Autowired(required = false)
	private List<CacheValueGetter<String, ?>> valueGetters;
	/**
	 * 缓存定义
	 */
	@Autowired(required = false)
	private List<CacheDefine<?>> cacheDefines;
	private Map<String, CacheValueGetter<String, ?>> getterMap = Collections.synchronizedMap(new HashMap<>());
	/**
	 * 默认缓存定义
	 */
	private CacheDefine<?> defaultCacheDefine;
	private Map<String, Cache<String, ?>> cacheMap = Collections.synchronizedMap(new HashMap<>());

	@PostConstruct
	private void init() {
		// 注册缓存值获取接口
		if (valueGetters != null) {
			for (CacheValueGetter<String, ?> valueGetter : valueGetters) {
				registerValueGetter(valueGetter);
			}
		}
		// 注册缓存定义
		if (cacheDefines != null) {
			for (CacheDefine<?> define : cacheDefines) {
				registerDefine(define);
			}
		}
	}

	/**
	 * 注册缓存值获取接口,用于当缓存值获取不到或过期时重新获取最新值
	 * @param valueGetter 缓存值获取器
	 */
	private void registerValueGetter(CacheValueGetter<String, ?> valueGetter) {
		if (valueGetter == null) {
			return;
		}
		if (Utils.isEmpty(valueGetter.cacheName())) {
			LogUtil.warn("The name of CacheValueGetter is empty @" + valueGetter.getClass().getName());
			return;
		}
		CacheValueGetter<String, ?> oldGetter = getterMap.put(valueGetter.cacheName(), valueGetter);
		if (oldGetter != null) {
			LogUtil.warn(oldGetter.getClass().getName() + " is replaced by " + valueGetter.getClass().getName() + ", because of the same name.");
		}
	}

	/**
     * 注册缓存定义
	 * @param define 缓存定义
	 */
	private void registerDefine(CacheDefine<?> define) {
		if (define == null) {
			return;
		}
		if (define.isDefaultCache()) {
			if (defaultCacheDefine != null) {
				LogUtil.warn("仅支持单个缓存定义为默认,当前默认缓存定义为[" + JsonUtil.toJson(defaultCacheDefine) + "],冲突缓存定义为[" + JsonUtil.toJson(define) + "]");
				return;
			}
			defaultCacheDefine = define;
		}
		if (Utils.isEmpty(define.getSupportNames())) {
			return;
		}
		for (String name : define.getSupportNames()) {
			Cache<?, ?> oldCache = cacheMap.get(name);
			if (oldCache != null) {
				LogUtil.warn("同个缓存的缓存定义冲突[" + name + "]");
			} else {
				Cache<String, ?> cache = createCache(define, name);
				if (cache != null) {
					cacheMap.put(name, cache);
				}
			}
		}
	}

	/**
     * 根据缓存定义和名称创建缓存组
	 * @param define 缓存定义
	 * @param cacheName 缓存名称
	 * @param <T> 缓存值泛型
     * @return 生成的对应缓存组
	 */
	private <T> Cache<String, T> createCache(CacheDefine<T> define, String cacheName) {
		Cache<String, T> cache = null;
		try {
			Class<Cache<String, T>> defineClazz = define.getCacheClazz();
			if (defineClazz == null) {
				return null;
			}
			cache = defineClazz.newInstance();
			cache.setMaxSize(define.getMaxSize());
			cache.setAlive(define.getAlive());
			if (cache instanceof AbstractCache) {
				@SuppressWarnings("unchecked")
                CacheValueGetter<String, T> valueGetter = (CacheValueGetter<String, T>) getterMap.get(cacheName);
				((AbstractCache<String, T>) cache).setValueGetter(valueGetter);
			}
		} catch (Exception e) {
			LogUtil.error("根据缓存定义创建缓存异常[" + JsonUtil.toJson(define) + "]", e);
		}
		return cache;
	}

	/**
     * 根据婚车名称获取缓存组
	 * @param cacheName 缓存名称
	 * @param <T> 缓存值泛型
     * @return 获取的缓存组
	 */
	@SuppressWarnings("unchecked")
	public <T> Cache<String, T> getCache(String cacheName) {
		Cache<String, T> cache = (Cache<String, T>) cacheMap.get(cacheName);
		if (cache == null) {
			cache = (Cache<String, T>) createCache(defaultCacheDefine, cacheName);
			if (cache == null) {
				// 理论上cache为空的情况不该发生,需给异常提示
				LogUtil.warn("缓存获取异常,默认创建内存缓存[" + cacheName + "]");
				cache = new MemoryCache<>();
			}
			cacheMap.put(cacheName, cache);
		}
		return cache;
	}

	/**
     * 获取系统所有缓存名称
	 * @return
     */
	public Collection<String> cacheNames() {
		return Collections.unmodifiableSet(cacheMap.keySet());
	}

	/**
     * 用于清理过期的缓存,一般是用来清理超过2倍存活时间的缓存,目前未实现
	 * 调用这个方法不需要过于频繁,影响系统性能,一般情况资源没有过于紧张情况下在非繁忙期清理一次即可
	 */
	public void clearExpiredCaches() {
		// FIXME 这里是清理超过2倍存活时间的缓存
	}

	/**
     * 根据缓存名称移除缓存组
	 * @param cacheName 缓存名称
	 * @param <T> 缓存值泛型
     * @return 移除的缓存组
	 */
	@SuppressWarnings("unchecked")
	public <T> Cache<String, T> removeCache(String cacheName) {
		return (Cache<String, T>) cacheMap.remove(cacheName);
	}

}
