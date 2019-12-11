package com.rongji.dfish.base.context;

import java.util.HashMap;
import java.util.Map;

public class BaseBeanContextHodler implements BeanContextHolder {
    private String scope;
    private Map<Class<?>,Object> contextByClass=new HashMap<>();
    private Map<String,Object> context=new HashMap<>();
    public BaseBeanContextHodler(String scope){
        this.scope=scope;
    }
    @Override
    public <T> T get(Class<T> clz) {
        T obj=(T)contextByClass.get(clz);
        if(obj==null){
            for(Object o:contextByClass.values()){
                if(clz.isAssignableFrom(o.getClass())){
                    contextByClass.put(clz,o);
                    return (T)o;
                }
            }
        }
        return obj;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public Object get(String name) {
        return contextByClass.get(name);
    }
    public void add(Object obj){
        if(obj==null){return;}
        Class<?>clz=obj.getClass();
        contextByClass.put(clz,obj);
        String name=clz.getSimpleName();
        char firstChar=name.charAt(0);
        if(firstChar>='A'&&firstChar<='Z'){
            firstChar+=32;
        }
        name=firstChar+name.substring(1);
        context.put(name,obj);
    }
}
