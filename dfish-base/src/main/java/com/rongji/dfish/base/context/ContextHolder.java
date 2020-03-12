package com.rongji.dfish.base.context;

/**
 * ContextHolder 标识这个对对象里面含有Context上下文内容。
 */
public interface ContextHolder {
    /**
     * 这个对象内容的范围
     * @return String
     */
    String getScope();

    /**
     * 根据内容名字获得内容
     * @param name
     * @return
     */
    Object get(String name);
}
