package com.rongji.dfish.misc.cache.impl;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.misc.cache.Cache;

/**
 * 缓存，如果数量达到了上限或者里面的内容已经操作期限那么返回空。
 * 
 * @author LinLW
 * @version 1.0
 * @deprecated 该缓存方法不推荐使用,建议使用base包的相关缓存方法
 */
public class SizeAndTimeLimitCache<K,V> implements Cache<K, V>{
	Log LOG=LogFactory.getLog(SizeAndTimeLimitCache.class);
	private int maxSize;
	private long alive;
	private Map<K,Item<V>> core;
	
	public int getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	public long getAlive() {
		return alive;
	}
	public void setAlive(long alive) {
		this.alive = alive;
	}
	protected Map<K, Item<V>> getCore() {
		return core;
	}
	protected void setCore(Map<K, Item<V>> core) {
		this.core = core;
	}
	public SizeAndTimeLimitCache() {
		this(8192, 300000L);
	}
	/**
	 * 构造函数
	 * @param maxSize 最大缓存数量
	 * @param alive 最大生存时间 -毫秒
	 */
	public SizeAndTimeLimitCache(int maxSize, long alive) {
		this.maxSize = maxSize;
		this.alive = alive;
		core = Collections.synchronizedMap(new LinkedHashMap<K,Item<V>>(){
			/**
			 * 
			 */
            private static final long serialVersionUID = 1655485069249566518L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<K, Item<V>> entry) {
				int thisMaxSize = SizeAndTimeLimitCache.this.maxSize;
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
		Item<V> item = core.put(key, new Item<V>(value));
		if (item != null) {
			return item.value;
		}
		return null;
	}
	
	@Override
    public void putAll(Map<? extends K, ? extends V> m) {
	    if (m == null) {
	    	return;
	    }
	    for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
	    	put(entry.getKey(), entry.getValue());
	    }
    }
	/**
	 * 返回缓存的值
	 * @param key
	 * @return V
	 */
	@Override
	public V get(K key) {
		Item<V> item=core.get(key);
		if (item == null) {
			return null;
		}
		if(alive > 0 && System.currentTimeMillis()-item.born>alive){
			core.remove(key);
			return null;
		}
		return item.value;
	}
	
	/**
	 * 删除
	 * @param key
	 * @return V
	 */
	@Override
	public V remove(K key){
		Item<V> item = core.remove(key);
		if (item != null) {
			return item.value;
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

	protected static class Item<V>{
		public Item(V value) {
			this.value=value;
			this.born=System.currentTimeMillis();
		}
		long born;
		V value;
	}

	@SuppressWarnings("unchecked")
	@Override
    public Map<K, V> gets(K... key) {
	    Map<K, V> result = new HashMap<K, V>();
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

	@Override
	public boolean containsValue(V value) {
		Collection<Item<V>> col = core.values();
		if (value == null) {
			for (Item<V> item : col) {
				if (item.value == null) {
					return true;
				}
			}
		} else {
			for (Item<V> item : col) {
				if (value.equals(item.value)) {
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

	@Override
	public Collection<V> values() {
		Collection<Item<V>> col = core.values();
		Collection<V> result = new ArrayList<V>();
		if (col != null) {
			for (Item<V> item : col) {
				result.add(item.value);
			}
		}
	    return result;
    }
	//正在加载的cache 防止本部分内容，在过期时，好几个线程同时加载。
	private final Set<K> GETTING_KEYS=Collections.synchronizedSet(new HashSet<K>());
	//执行加载动作的加载器
	private Executor EXEC=Executors.newCachedThreadPool();
	//第一次加载的时候的锁。防止，第一次加载某个cache的饿时候，好几个线程同时加载。
	private Map<K,Object> LOCKS=Collections.synchronizedMap(new WeakHashMap<K,Object>());
	/**
	 * 使用该接口的时候，如果发现过期了，可以直接返回旧数据，但是将尝试用另一个线程从ValueGetter 获取数据，并装载缓存。等缓存装载好以后，再次调用将会是新的值。
	 * 专为高并发设计。如果并发很低的时候，当过期两倍的有效期才阻断并当场获取最新数据。
	 * 调法范例<pre>
	 * cache.get(codeType,new SizeAndTimeLimitCache.ValueGetter&lt;String, List&gt;() {
	 *     public List get(String key) {
	 *         return pubCommonDAO.getQueryList("FROM SysCode t WHERE t.code0100=? ORDER BY t.code0203 ASC", key);
	 *     }
	 * })
	 * </pre>
	 * JAVA 8+ 可以使用简化写法<pre>
	 * cache.get(codeType, (String key) -&gt; {return pubCommonDAO.getQueryList("FROM SysCode t WHERE t.code0100=? ORDER BY t.code0203 ASC", key);});
	 * </pre>
	 * @param key
	 * @param vg 
	 * @return
	 */
	public V get(final K key,final ValueGetter<K,V> vg) {
		Item<V> item=core.get(key);
		if (item == null) {
			item=getWithLock(key,vg);
		}
		if(alive > 0 && System.currentTimeMillis()-item.born>2*alive){
			item=getWithLock(key,vg);
		}else if(alive > 0 && System.currentTimeMillis()-item.born>alive){
			if(GETTING_KEYS.add(key)){
				EXEC.execute(new Runnable(){
					@Override
					public void run() {
						try{
							V v=vg.get(key);
							SizeAndTimeLimitCache.this.put(key, v);
						}catch(Throwable t){
							LogUtil.LOG.error(null,t);
						}
						GETTING_KEYS.remove(key);
					}
				});
			}
		}
		return item.value;
	}
	private Item<V> getWithLock(final K key,final ValueGetter<K,V> vg){
		Object lock=LOCKS.get(key);
		if(lock==null){
			LOCKS.put(key, lock=new Object());
		}
		synchronized (lock) {
			Item<V> item=core.get(key);
			if (item == null) {
				try{
					V v=vg.get(key);
					this.put(key, v);
					return core.get(key);
				}catch(Throwable t){
					LogUtil.LOG.error(null,t);
				}
			}else{
				return item;
			}
		}
		return null;
	}
	/**
	 * 通过 key取得实际VALUE的方法
	 * @author LinLW
	 *
	 * @param <K> key
	 * @param <V> value
	 */
	public static interface ValueGetter<K,V>{
		/**
		 * 通过 key取得实际VALUE的方法
		 * @param key K
		 * @return V
		 */
		public V get(K key);
	}
	
}
