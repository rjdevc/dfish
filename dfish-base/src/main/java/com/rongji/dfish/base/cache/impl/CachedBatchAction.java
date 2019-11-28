package com.rongji.dfish.base.cache.impl;


import com.rongji.dfish.base.BatchAction;
import com.rongji.dfish.base.cache.CacheItem;
import com.rongji.dfish.base.util.ThreadUtil;

import java.util.*;
import java.util.concurrent.ExecutorService;

public class CachedBatchAction<I, O> implements BatchAction<I, O> {
    protected int maxSize;
    protected long alive;
    protected Map<I, CacheItem<O>> core;
    protected BatchAction<I, O> act;

    protected Map<I, CacheItem<O>> getCore() {
        return core;
    }

    protected void setCore(Map<I, CacheItem<O>> core) {
        this.core = core;
    }

    public CachedBatchAction(BatchAction<I, O> act) {
        this( act,8192, 300000L);
    }

    /**
     * 构造函数
     *
     * @param maxSize 最大缓存数量
     * @param alive   最大生存时间 -毫秒
     */
    public CachedBatchAction( BatchAction<I, O> act,int maxSize, long alive) {
        this.maxSize = maxSize;
        this.alive = alive;
        core = Collections.synchronizedMap(new LinkedHashMap<I, CacheItem<O>>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<I, CacheItem<O>> entry) {
                int thisMaxSize = CachedBatchAction.this.maxSize;
                return thisMaxSize > 0 && size() > thisMaxSize;
            }
        });
        this.act = act;
    }

    //正在加载的cache 防止本部分内容，在过期时，好几个线程同时加载。
    private final Set<I> GETTING_KEYS = Collections.synchronizedSet(new HashSet<I>());
    //执行加载动作的加载器
    protected ExecutorService EXEC = ThreadUtil.getCachedThreadPool();
    //第一次加载的时候的锁。防止，第一次加载某个cache的饿时候，好几个线程同时加载。
//    private Map<I,Object> LOCKS=Collections.synchronizedMap(new WeakHashMap<I,Object>());

    /**
     * 返回缓存的值
     *
     * @param key
     * @return V
     */
    public O act(I key) {
        CacheItem<O> item = core.get(key);
        if (item == null) {
            return getWithLock(key);
        }
        if (alive > 0) {
            if (System.currentTimeMillis() - item.getBorn() > 2 * alive) {
                return getWithLock(key);
            } else if (System.currentTimeMillis() - item.getBorn() > alive) {
                // 缓存失效的时候开始尝试获取新缓存
                boolean add;
                synchronized (GETTING_KEYS) {
                    add = GETTING_KEYS.add(key);
                }
                if (add) {
                    actInThread();
                }
            }
        }
        return item.getValue();
    }

    private O getWithLock(I key) {
        Set<I> keys = new HashSet<>(1);
        keys.add(key);
        Map<I, O> valueMap = getsWithLock(keys);
        if (valueMap == null) {
            return null;
        }
        return valueMap.get(key);
    }

    private Map<I, O> getsWithLock(Set<I> keys) {
        if (keys == null || keys.isEmpty() || act == null) {
            return Collections.emptyMap();
        }
        Set<I> lockKeys = new HashSet<>(keys);
//        for (I key : lockKeys) {
//            Object lock = LOCKS.get(key);
//            if(lock == null){
//                LOCKS.put(key, new Object());
//            }
//        }
        // FIXME 有可能线程安全问题??
        Map<I, O> valueMap = act.act(lockKeys);
        // 获取完成,将已获取的key移除
//        for (I key : lockKeys) {
//            LOCKS.remove(key);
//        }
        if (valueMap != null) {
            for (Map.Entry<? extends I, ? extends O> entry : valueMap.entrySet()) {
                core.put(entry.getKey(), new CacheItem<>(entry.getValue()));
            }
            lockKeys.removeAll(valueMap.keySet());
        } else {
            valueMap = new HashMap<>(0);
        }
        // 获取不到的数据默认补空值,否则将穿透缓存
        for (I key : lockKeys) {
            core.put(key, new CacheItem<>(null));
        }
        return valueMap;
    }

    @Override
    public Map<I, O> act(Set<I> input) {
        if (input == null) {
            return Collections.emptyMap();
        }
        Map<I, O> result = new HashMap<>(input.size());
        Set<I> actAtOnce = new HashSet<>(input.size());
        Set<I> actDelay = new HashSet<>(input.size());
        for (I key : input) {
            CacheItem<O> item = core.get(key);
            if (item == null) {
                actAtOnce.add(key);
            }
            if (alive > 0) {
                if (System.currentTimeMillis() - item.getBorn() > 2 * alive) {
                    actAtOnce.add(key);
                } else if (System.currentTimeMillis() - item.getBorn() > alive) {
                    // 缓存失效的时候开始尝试获取新缓存
                    actDelay.add(key);
                    result.put(key, item.getValue());
                } else {
                    result.put(key, item.getValue());
                }
            }
        }
        if (actAtOnce.size() > 0) {
            getsWithLock(actAtOnce);
        }
        if (actDelay.size() > 0) {
            boolean add = false;
            synchronized (GETTING_KEYS) {
                for (I key : actDelay) {
                    add = GETTING_KEYS.add(key) || add;
                }
            }
            if (add) {
                actInThread();
            }
        }
        return result;
    }

    private void actInThread() {
        EXEC.execute(new Runnable() {
            @Override
            public void run() {
                Set<I> keys = null;
                synchronized (GETTING_KEYS) {
                    keys = new HashSet<>(GETTING_KEYS);
                    GETTING_KEYS.clear();
                }
                if (keys != null) {
                    getsWithLock(keys);
                }
            }
        });
    }
}
