package com.rongji.dfish.base.cache.impl;


import com.rongji.dfish.base.batch.AbstractBaseAction;
import com.rongji.dfish.base.batch.BatchAction;
import com.rongji.dfish.base.cache.CacheItem;
import com.rongji.dfish.base.util.ThreadUtil;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * 带缓存的执行器。它可以通过缓存。减少执行的次数，或每次执行的输入大小。
 * 如果有一个BatchAction 套用的这个类即可获得缓存效果。
 * <pre>
 * CachedBatchAction<String,Object> m=new CachedBatchAction<String,Object>(
 * new BatchAction<String,Object>(){
 * 	public Map<String,Object> act(Set<String> input) {
 * 		return null; // 实际批量获取的代码。
 * 	}
 * });
 * Object o=m.act("something");
 * Map<String,Object> map.=m.get( new HashSet<String>(Arrays.asList("str2","str3")));
 * </pre>
 *  调用的时候，即可生效。
 *  通常这个类是单例的。
 *
 * @param <I> 输入参数类型
 * @param <O> 输出参数类型
 */
public class CachedBatchAction<I, O> extends AbstractBaseAction<I,O> {
    protected int maxSize;
    protected long alive;
    protected Map<I, CacheItem<O>> core;
    protected BatchAction<I, O> action;

    /**
     * 缓存的数据放在这个core中
     * @return Map
     */
    protected Map<I, CacheItem<O>> getCore() {
        return core;
    }

    /**
     * 缓存的数据放在这个Core当中
     * @param core Map
     */
    protected void setCore(Map<I, CacheItem<O>> core) {
        this.core = core;
    }

    /**
     * 构造函数
     */
    public CachedBatchAction() {
        this(null);
    }

    /**
     * 缓存最大数量8192，最长时间300秒
     * @param act
     */
    public CachedBatchAction(BatchAction<I, O> act) {
        this( act,8192, 300000L);
    }

    /**
     * 构造函数
     *
     * @param maxSize 最大缓存数量
     * @param alive   最大生存时间 -毫秒
     */
    public CachedBatchAction( BatchAction<I, O> action,int maxSize, long alive) {
        this.maxSize = maxSize;
        this.alive = alive;
        core = Collections.synchronizedMap(new LinkedHashMap<I, CacheItem<O>>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<I, CacheItem<O>> entry) {
                int thisMaxSize = CachedBatchAction.this.maxSize;
                return thisMaxSize > 0 && size() > thisMaxSize;
            }
        });
        this.action = action;
    }

    //正在加载的cache 防止本部分内容，在过期时，好几个线程同时加载。
    private final Set<I> waiting = Collections.synchronizedSet(new HashSet<I>());
    //执行加载动作的加载器
    protected ExecutorService exec = ThreadUtil.getCachedThreadPool();




    @Override
    public Map<I, O> act(Set<I> input) {
        if (input == null) {
            return Collections.emptyMap();
        }
        Map<I, O> result = new HashMap<>(input.size());
        Set<I> doActSet = new HashSet<>(input.size());
        Set<I> actLazySet = new HashSet<>(input.size());
        for (I key : input) {
            CacheItem<O> item = core.get(key);
            if (item == null) {
                doActSet.add(key);
            } else if (alive > 0) {
                if (System.currentTimeMillis() - item.getBorn() > 2 * alive) {
                    doActSet.add(key);
                } else if (System.currentTimeMillis() - item.getBorn() > alive) {
                    // 缓存失效的时候开始尝试获取新缓存
                    actLazySet.add(key);
                    result.put(key, item.getValue());
                } else {
                    result.put(key, item.getValue());
                }
            }
        }
        if (doActSet.size() > 0) {
            Map<I,O> quickResult=doAct(doActSet);
            for(Map.Entry<I,O> entry:quickResult.entrySet()) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        actLazy(actLazySet);
        return result;
    }

    private Map<I, O> doAct(Set<I> keys) {
        if (keys == null || keys.isEmpty() || action == null) {
            return Collections.emptyMap();
        }
        //过程中部影响原始keys
        Set<I> tmpKeys = new HashSet<>(keys);
        Map<I, O> valueMap = action.act(tmpKeys);
        if (valueMap != null) {
            for (Map.Entry<? extends I, ? extends O> entry : valueMap.entrySet()) {
                core.put(entry.getKey(), new CacheItem<>(entry.getValue()));
            }
            // 获取完成,将已获取的key移除
            tmpKeys.removeAll(valueMap.keySet());
        } else {
            valueMap = Collections.emptyMap();
        }
        // 获取不到的数据默认补空值,否则将穿透缓存
        for (I key : tmpKeys) {
            core.put(key, new CacheItem<>(null));
        }
        return valueMap;
    }

    private void actLazy(Set<I> actLazySet) {
        if(actLazySet.isEmpty()){
            return;
        }
        boolean add = false;
        synchronized (waiting) {
            for (I key : actLazySet) {
                add = waiting.add(key) || add;
            }
        }
        if (!add) {
            return;
        }
        exec.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Set<I> keys = null;
                synchronized (waiting) {
                    keys = new HashSet<>(waiting);
                    waiting.clear();
                }
                if (keys != null&&keys.size()>0) {
                    doAct(keys);
                }
            }
        });
    }

    /**
     * 清理缓存，防止不繁忙的时候，还是过期数据还是常驻内存。
     */
    public void clearExpiredData() {
        // 2倍存活时间
        long limitBorn = System.currentTimeMillis() - alive << 1;
        for (Iterator<Map.Entry<I, CacheItem<O>>> iter = core.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<I, CacheItem<O>> entry = iter.next();
            CacheItem<O> item = entry.getValue();
            if (item == null || item.getBorn() < limitBorn) {
                // 存活时间超过限制时间,移除
                iter.remove();
            }
        }
    }
}
