package com.rongji.dfish.base.context;

/**
 * ContextHolder 标识这个对对象里面含有Context上下文内容。
 */
public interface ContextHolder {
    /**
     * 这个对象内容的范围
     * @return String 范围
     */
    String getScope();

    /**
     * 根据内容名字获得内容
     * @param name 名称
     * @return 内容对象
     */
    Object get(String name);
}
