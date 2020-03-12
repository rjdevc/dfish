package com.rongji.dfish.base.context;

import java.util.Map;

/**
 * 专门用于放置 属性的上下文
 * 通常对应某个配置配置文件。
 * 或者System.getProperties() 或 System.getEnv() 这种文本属性。
 */
public class PropertiesContextHolder implements ContextHolder {
    private String scope;
    private Map properties;

    /**
     * 构造函数
     * @param scope 范围名字
     * @param properties 已知的值
     */
    public PropertiesContextHolder(String scope,Map properties){
        this.scope=scope;
        this.properties=properties;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public Object get(String name) {
        return properties.get(name);
    }
}
