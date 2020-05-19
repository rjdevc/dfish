package com.rongji.dfish.base.cache.impl;


import com.rongji.dfish.base.batch.BatchFunction;
import com.rongji.dfish.base.cache.CacheItem;
import com.rongji.dfish.base.util.ThreadUtil;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * 带缓存的执行器。它可以通过缓存。减少执行的次数，或每次执行的输入大小。
 * CachedBatchFunction 套用的这个类即可获得缓存效果。
 * <pre>
 * CachedBatchFunction&lt;String,Object&gt; m=new CachedBatchFunction&lt;String,Object&gt;(
 * new BatchFunction&lt;String,Object&gt;(){
 * 	public Map&lt;String,Object&gt; apply(Set&lt;String&gt; input) {
 * 		return null; // 实际批量获取的代码。
 * 	}
 * });
 * Object o=m.apply("something");
 * Map&lt;String,Object&gt; map.=m.get( new HashSet&lt;String&gt;(Arrays.asList("str2","str3")));
 * </pre>
 *  调用的时候，即可生效。
 *  通常这个类是单例的。
 *
 * @param <T> 输入参数类型
 * @param <R> 输出参数类型
 */
public class CachedBatchFunction<T, R> implements BatchFunction<T, R> {
    protected int maxSize;
    protected long alive;
    protected Map<T, CacheItem<R>> core;
    protected BatchFunction<T, R> function;

    /**
     * 缓存的数据放在这个core中
     * @return Map
     */
    protected Map<T, CacheItem<R>> getCore() {
        return core;
    }

    /**
     * 缓存的数据放在这个Core当中
     * @param core Map
     */
    protected void setCore(Map<T, CacheItem<R>> core) {
        this.core = core;
    }

    /**
     * 构造函数
     */
    public CachedBatchFunction() {
        this(null);
    }

    /**
     * 缓存最大数量8192，最长时间300秒
     * @param function 批处理动作
     */
    public CachedBatchFunction(BatchFunction<T, R> function) {
        this( function,8192, 300000L);
    }

    /**
     * 构造函数
     *
     * @param function 批处理动作
     * @param maxSize 最大缓存数量
     * @param alive   最大生存时间 -毫秒
     */
    public CachedBatchFunction(BatchFunction<T, R> function, int maxSize, long alive) {
        this.maxSize = maxSize;
        this.alive = alive;
        core = Collections.synchronizedMap(new LinkedHashMap<T, CacheItem<R>>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<T, CacheItem<R>> entry) {
                int thisMaxSize = CachedBatchFunction.this.maxSize;
                return thisMaxSize > 0 && size() > thisMaxSize;
            }
        });
        this.function = function;
    }

    //正在加载的cache 防止本部分内容，在过期时，好几个线程同时加载。
    private final Set<T> waiting = Collections.synchronizedSet(new HashSet<>());
    //执行加载动作的加载器
    protected ExecutorService exec = ThreadUtil.getCachedThreadPool();

    @Override
    public Map<T, R> apply(Set<T> input) {
        if (input == null) {
            return Collections.emptyMap();
        }
        Map<T, R> result = new HashMap<>(input.size());
        Set<T> doApplySet = new HashSet<>(input.size());
        Set<T> applyLazySet = new HashSet<>(input.size());
        for (T key : input) {
            CacheItem<R> item = core.get(key);
            if (item == null) {
                doApplySet.add(key);
            } else if (alive > 0) {
                if (System.currentTimeMillis() - item.getBorn() > 2 * alive) {
                    doApplySet.add(key);
                } else if (System.currentTimeMillis() - item.getBorn() > alive) {
                    // 缓存失效的时候开始尝试获取新缓存
                    applyLazySet.add(key);
                    result.put(key, item.getValue());
                } else {
                    result.put(key, item.getValue());
                }
            }
        }
        if (doApplySet.size() > 0) {
            Map<T, R> quickResult=doApply(doApplySet);
            for(Map.Entry<T, R> entry:quickResult.entrySet()) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        applyLazy(applyLazySet);
        return result;
    }

    private Map<T, R> doApply(Set<T> keys) {
        if (keys == null || keys.isEmpty() || function == null) {
            return Collections.emptyMap();
        }
        //过程中部影响原始keys
        Set<T> tmpKeys = new HashSet<>(keys);
        Map<T, R> valueMap = function.apply(tmpKeys);
        if (valueMap != null) {
            for (Map.Entry<? extends T, ? extends R> entry : valueMap.entrySet()) {
                core.put(entry.getKey(), new CacheItem<>(entry.getValue()));
            }
            // 获取完成,将已获取的key移除
            tmpKeys.removeAll(valueMap.keySet());
        } else {
            valueMap = Collections.emptyMap();
        }
        // 获取不到的数据默认补空值,否则将穿透缓存
        for (T key : tmpKeys) {
            core.put(key, new CacheItem<>(null));
        }
        return valueMap;
    }

    private void applyLazy(Set<T> set) {
        if(set.isEmpty()){
            return;
        }
        boolean add = false;
        synchronized (waiting) {
            for (T key : set) {
                add = waiting.add(key) || add;
            }
        }
        if (!add) {
            return;
        }
        exec.execute(() ->{
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Set<T> keys = null;
            synchronized (waiting) {
                keys = new HashSet<>(waiting);
                waiting.clear();
            }
            if (keys.size()>0) {
                doApply(keys);
            }
        });
    }

    /**
     * 清理缓存，防止不繁忙的时候，还是过期数据还是常驻内存。
     */
    public void clearExpiredData() {
        // 2倍存活时间
        long limitBorn = System.currentTimeMillis() - alive << 1;
        for (Iterator<Map.Entry<T, CacheItem<R>>> iter = core.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<T, CacheItem<R>> entry = iter.next();
            CacheItem<R> item = entry.getValue();
            if (item == null || item.getBorn() < limitBorn) {
                // 存活时间超过限制时间,移除
                iter.remove();
            }
        }
    }
}
