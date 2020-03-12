package com.rongji.dfish.base.context;

/**
 * 上下文内容中放置的内容包含javaBean 则可以通过Class获取对应的javaBean
 * 这样的用法则被标记为BeanContextHolder
 *  比如说系统启动的时候。把spring的内容封装在这个ContextHolder中
 *  那么获取某个对象。就和spring相同的 通过 get(Class)  即可获取。
 */
public interface BeanContextHolder extends ContextHolder {
    /**
     * 如果这个Class在这个Context中有且仅有一个实例。则通过Class获取这个实例
     * @param clz Class
     * @param <T> 该Class的类型
     * @return 实例
     */
    <T> T get(Class<T> clz);
}
