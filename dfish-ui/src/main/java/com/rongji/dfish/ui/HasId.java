package com.rongji.dfish.ui;

/**
 * 任何实现 HasId 接口对象有一个字符串值的唯一编号
 * 自定义的ID。前端可通过 view.find( id ) 方法来获取 widget。
 * 后端一般也有对应的findNodeById 等方法。一般建议，需要控制的面板都要设置ID
 * @author DFish Team
 * @version 1.0
 * @param <T> 当前对象类型
 * @since XMLTMPL 2.0
 */
public interface HasId<T extends HasId<T>> {
    /**
     * 设置ID
     * 自定义的ID。前端可通过 view.find( id ) 方法来获取 widget。
     * 后端一般也有对应的findNodeById 等方法。一般建议，需要控制的面板都要设置ID
     * @param id String
     * @return 本身，这样可以继续设置其他属性
     */
    T setId(String id);

    /**
     * 取得ID
     * 自定义的ID。前端可通过 view.find( id ) 方法来获取 widget。
     *  后端一般也有对应的findNodeById 等方法。一般建议，需要控制的面板都要设置ID
     * @return String
     */
    String getId();
}
