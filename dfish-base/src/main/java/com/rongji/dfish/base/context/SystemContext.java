package com.rongji.dfish.base.context;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SystemContext {
    private static SystemContext instance=new SystemContext();
    private SystemContext(){
        holders=Collections.synchronizedMap(new LinkedHashMap<>());
    }
    public static SystemContext getInstance(){
        return instance;
    }
    private Map<String,ContextHolder> holders;
    public Object get(String name){
        for(ContextHolder ch:holders.values()){
            Object o=ch.get(name);
            if(o!=null){return o;}
        }
        return null;
    }
    public Object get(String name,String scope){
        ContextHolder ch=holders.get(scope);
        if(ch!=null){
            return ch.get(name);
        }
        return null;
    }
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
    public<T>T get(Class<T> clz,String scope){
        ContextHolder ch=holders.get(scope);
        if(ch!=null && ch instanceof BeanContextHolder){
            BeanContextHolder cast=(BeanContextHolder)ch;
            return cast.get(clz);
        }
        return null;
    }
    String getProperty(String name){
        for(ContextHolder ch:holders.values()){
            Object o=ch.get(name);
            if(o!=null && o instanceof String){return (String)o;}
        }
        return null;
    }
    String getProperty(String name,String scope){
        Object o=get(name,scope);
        if(o !=null && o instanceof String){
            return (String)o;
        }
        return null;
    }

    public Set<String> supportScopes(){
        return holders.keySet();
    }
    public void register(ContextHolder holder){
        holders.put(holder.getScope(),holder);
    }
}
