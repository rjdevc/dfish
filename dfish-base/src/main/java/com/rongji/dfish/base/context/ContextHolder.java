package com.rongji.dfish.base.context;

public interface ContextHolder {
    String getScope();
    Object get(String name);
}
