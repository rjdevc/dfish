package com.rongji.dfish.base.context;

public interface BeanContextHolder extends ContextHolder {
    <T> T get(Class<T> clz);
}
