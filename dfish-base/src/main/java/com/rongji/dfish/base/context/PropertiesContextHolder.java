package com.rongji.dfish.base.context;

import java.util.Map;

public class PropertiesContextHolder implements ContextHolder {
    private String scope;
    private Map<?,?> properteis;
    public PropertiesContextHolder(String scope,Map<?,?> properteis){
        this.scope=scope;
        this.properteis=properteis;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public Object get(String name) {
        return properteis.get(name);
    }
}
