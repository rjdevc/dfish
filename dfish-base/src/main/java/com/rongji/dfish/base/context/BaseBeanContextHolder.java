package com.rongji.dfish.base.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * BaseBeanContextHolder 为基础的上下文拥有者。
 * 它可以用于放置普通对象（Bean）或文本属性（Properties）
 */
public class BaseBeanContextHolder implements BeanContextHolder {
    private String scope;
    private Map<Class, Object> contextByClass = new HashMap<>();
    private Map<String, Object> context = Collections.synchronizedMap(new HashMap<>());

    /**
     * 构造函数
     *
     * @param scope 范围
     */
    public BaseBeanContextHolder(String scope) {
        this.scope = scope;
    }

    @Override
    public <T> T get(Class<T> clz) {
        synchronized (contextByClass) {
            T obj = (T) contextByClass.get(clz);
            if (obj == null) {
                Iterator<Map.Entry<Class, Object>> iterator = contextByClass.entrySet().iterator();
                for (;iterator.hasNext();) {
                    Object o = iterator.next().getValue();
                    if (clz.isAssignableFrom(o.getClass())) {
                        obj = (T) o;
                        break;
                    }
                }
                if (obj != null) {
                    contextByClass.put(clz, obj);
                }
            }
            return obj;
        }
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public Object get(String name) {
        return context.get(name);
    }

    /**
     * 增加一个元素
     *
     * @param obj 增加的元素
     */
    public void add(Object obj) {
        if (obj == null) {
            return;
        }
        Class clz = obj.getClass();
        synchronized (contextByClass) {
            contextByClass.put(clz, obj);
        }
        String name = clz.getSimpleName();
        char firstChar = name.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            firstChar += 32;
        }
        name = firstChar + name.substring(1);
        context.put(name, obj);
    }
}
