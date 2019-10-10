package com.rongji.dfish.base.cache.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.cache.Cache;
import com.rongji.dfish.base.cache.CacheItem;
import com.rongji.dfish.base.cache.CacheValueGetter;

public class AbstractCache<K, V> implements Cache<K, V> {
	private int maxSize;
	private long alive;
	private Map<K,CacheItem<V>> core;
	
	private CacheValueGetter<K, V> valueGetter;
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
	protected Map<K, CacheItem<V>> getCore() {
		return core;
	}
	protected void setCore(Map<K, CacheItem<V>> core) {
		this.core = core;
	}
	public CacheValueGetter<K, V> getValueGetter() {
		return valueGetter;
	}
	public void setValueGetter(CacheValueGetter<K, V> valueGetter) {
		this.valueGetter = valueGetter;
	}
	public AbstractCache() {
		this(8192, 300000L);
	}
	/**
	 * 构造函数
	 * @param maxSize 最大缓存数量
	 * @param alive 最大生存时间 -毫秒
	 */
	public AbstractCache(int maxSize, long alive) {
		this.maxSize = maxSize;
		this.alive = alive;
		core = Collections.synchronizedMap(new LinkedHashMap<K,CacheItem<V>>(){
			/**
			 * 
			 */
            private static final long serialVersionUID = 1655485069249566518L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<K, CacheItem<V>> entry) {
				int thisMaxSize = AbstractCache.this.maxSize;
				return thisMaxSize > 0 && size() > thisMaxSize;
			}
		});
	}
	/**
	 * 设置这个key的值，如果这个key原来就有对应的值，则返回这个值，否则返回空
	 * @param key K
	 * @param value V
	 * @return V
	 */
	@Override
	public V put(K key, V value){
		CacheItem<V> item = core.put(key, new CacheItem<>(value));
		if (item != null) {
			return item.getValue();
		}
		return null;
	}
	
	@Override
    public void putAll(Map<K, V> m) {
	    if (m == null) {
	    	return;
	    }
	    for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
	    	put(entry.getKey(), entry.getValue());
	    }
    }
	//正在加载的cache 防止本部分内容，在过期时，好几个线程同时加载。
	private final Set<K> GETTING_KEYS = Collections.synchronizedSet(new HashSet<K>());
	//执行加载动作的加载器
	private Executor EXEC=Executors.newCachedThreadPool();
	//第一次加载的时候的锁。防止，第一次加载某个cache的饿时候，好几个线程同时加载。
	private Map<K,Object> LOCKS=Collections.synchronizedMap(new WeakHashMap<K,Object>());
	/**
	 * 返回缓存的值
	 * @param key
	 * @return V
	 */
	@Override
	public V get(K key) {
		CacheItem<V> item = core.get(key);
		if (item == null) {
			return getWithLock(key);
		}
		if (alive > 0) {
			if(System.currentTimeMillis()-item.getBorn()>2*alive){
				return getWithLock(key);
			} else if(System.currentTimeMillis()-item.getBorn()>alive){
				// 缓存失效的时候开始尝试获取新缓存
				if (valueGetter != null) { // 有值获取工具类,则尝试获取值,这里先返回旧值
					boolean add;
					synchronized (GETTING_KEYS) {
						add = GETTING_KEYS.add(key);
					}
					if (add) {
						EXEC.execute(new Runnable() {
							@Override
							public void run() {
								Set<K> keys = null;
								synchronized (GETTING_KEYS) {
									keys = new HashSet<>(GETTING_KEYS);
									GETTING_KEYS.clear();
								}
								if(keys != null) {
									getsWithLock(keys);
								}
							}
						});
					}
				} else {
					core.remove(key);
					return null;
				}
			}
		}
		return item.getValue();
	}
	
	private V getWithLock(K key) {
		Set<K> keys = new HashSet<>(1);
		keys.add(key);
		Map<K, V> valueMap = getsWithLock(keys);
		if (valueMap == null) {
			return null;
		}
		return valueMap.get(key);
	}
	
	private Map<K, V> getsWithLock(Set<K> keys) {
		if (keys == null || keys.isEmpty() || valueGetter == null) {
			return Collections.emptyMap();
		}
		Set<K> lockKeys = new HashSet<>(keys);
		for (K key : lockKeys) {
			Object lock = LOCKS.get(key);
			if(lock == null){
				LOCKS.put(key, new Object());
			}
		}
		// FIXME 有可能线程安全问题??
		Map<K, V> valueMap = valueGetter.gets(lockKeys);
		// 获取完成,将已获取的key移除
		for (K key : lockKeys) {
			LOCKS.remove(key);
		}
		this.putAll(valueMap);
		lockKeys.removeAll(valueMap.keySet());
		// 获取不到的数据默认补空值,否则将穿透缓存
		for (K key : lockKeys) {
			this.put(key, null);
		}
		return valueMap;
	}
	
	
	/**
	 * 删除
	 * @param key
	 * @return V
	 */
	@Override
	public V remove(K key){
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
	public void clear(){
		core.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
    public Map<K, V> gets(K... key) {
	    Map<K, V> result = new HashMap<>();
	    if (key != null) {
	    	for (K k : key) {
	    		result.put(k, get(k));
	    	}
	    }
	    return result;
    }

	@Override
	public boolean containsKey(K key) {
	    return core.containsKey(key);
    }

	public boolean containsValue(V value) {
		Collection<CacheItem<V>> col = core.values();
		if (value == null) {
			for (CacheItem<V> item : col) {
				if (item.getValue() == null) {
					return true;
				}
			}
		} else {
			for (CacheItem<V> item : col) {
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

	public Collection<V> values() {
		Collection<CacheItem<V>> col = core.values();
		Collection<V> result = new ArrayList<>();
		if (col != null) {
			for (CacheItem<V> item : col) {
				result.add(item.getValue());
			}
		}
	    return result;
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
