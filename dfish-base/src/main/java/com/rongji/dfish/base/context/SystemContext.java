package com.rongji.dfish.base.context;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 系统上下文
 * 它是上下文管理器的总入口。
 * 通常启动的时候，会把所有相关的上下文都注册到该入口上
 * 用于替代DFish3.x的SystemData
 * 它比SystemData可容纳更多的上下文内容，比如spring的上下文也可以封装到里面去。
 * 并且，支持动态注册。所以，有更高的扩展性。
 * 用法
 * String value = SystemContext.getInstance().get(SystemConfigHolder.class).getProperty(
 *                 "framework.pub.sysdefaultlocale");
 * BeanFactory factory = SystemContext.getInstance().get(BeanFactory.class);
 * String rootPath = SystemContext.getInstance().get(ServletInfo.class).getServletRealPath();
 * 等
 */
public class SystemContext {
    private static SystemContext instance=new SystemContext();
    private SystemContext(){
        holders=Collections.synchronizedMap(new LinkedHashMap<>());
    }

    /**
     * 获得唯一实例
     * @return SystemContext实例对象
     */
    public static SystemContext getInstance(){
        return instance;
    }
    private Map<String,ContextHolder> holders;

    /**
     * 根据内容名字取得对象
     * @param name String 名字
     * @return Object 对象
     */
    public Object get(String name){
        for(ContextHolder ch:holders.values()){
            Object o=ch.get(name);
            if(o!=null){return o;}
        }
        return null;
    }
    /**
     * 根据内容名字和范围取得对象
     * @param name String 名字
     * @param scope String 范围
     * @return Object 对象
     */
    public Object get(String name,String scope){
        ContextHolder ch=holders.get(scope);
        if(ch!=null){
            return ch.get(name);
        }
        return null;
    }

    /**
     * 如果这个类型对象在全局内是唯一的。可以通过Class来获取它
     * BeanFactory factory = SystemContext.getInstance().get(BeanFactory.class);
     * @param clz Class
     * @param <T> Class 的类型
     * @return Object bean
     */
    public <T>T get(Class<T> clz){
        for(ContextHolder ch:holders.values()){
            if(ch instanceof BeanContextHolder){
                BeanContextHolder cast=(BeanContextHolder)ch;
                T o=cast.get(clz);
                if(o!=null){return o;}
            }
        }
        return null;
    }
    /**
     * 如果这个类型对象在全局内是唯一的。可以通过Class来获取它
     * BeanFactory factory = SystemContext.getInstance().get(BeanFactory.class);
     * @param clz Class
     * @param scope  范围
     * @param <T> Class 的类型
     * @return Object bean
     */
    public<T>T get(Class<T> clz,String scope){
        ContextHolder ch=holders.get(scope);
        if(ch!=null && ch instanceof BeanContextHolder){
            BeanContextHolder cast=(BeanContextHolder)ch;
            return cast.get(clz);
        }
        return null;
    }

    /**
     * 根据属性的名称获取属性值
     * @param name String 名称
     * @return String
     */
    public String getProperty(String name){
        for(ContextHolder ch:holders.values()){
            Object o=ch.get(name);
            if(o!=null && o instanceof String){return (String)o;}
        }
        return null;
    }
    /**
     * 根据属性的名称和范围获取属性值
     * @param name String 名称
     * @param scope 范围
     * @return String
     */
    public String getProperty(String name,String scope){
        Object o=get(name,scope);
        if(o !=null && o instanceof String){
            return (String)o;
        }
        return null;
    }

    /**
     * 例举出现在实例中已注册的范围值。
     * @return 范围集合
     */
    public Set<String> supportScopes(){
        return holders.keySet();
    }

    /**
     * 注册一个 上下文，如果它的范围和实例中已经有的重复，则会替换掉。
     * @param holder ContextHolder
     */
    public void register(ContextHolder holder){
        holders.put(holder.getScope(),holder);
    }
}
