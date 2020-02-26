package com.rongji.dfish.ui;

import java.util.List;

/**
 * SingleContainer 为只能容纳一个元素的容器
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
public interface MultiNodeContainer<T extends MultiNodeContainer<T>> extends NodeContainer {
    /**
     * <p>取得它容纳的内容。在JSON中表示为下级。"nodes":[ ... ]</p>
     * 如果这写下级内容(N)还有下级，将<b>不</b>包含多层级的内容。
     * 大部分情况下getNodes只是视觉效果，比如说Table的nodes在这里可能会分属于自己的rows和head的rows中去。
     * 如果要取得对象，请调用，findNodes
     * @return List
     */
    <W extends Node>List<W> getNodes();
    default void clearNodes(){
        getNodes().clear();
    }

    /**
     * 添加需要插入元素
     * 一般是增加Widget。也允许增加Command等其他部件
     *
     * @param node Node
     * @return 本身，这样可以继续设置其他属性
     */
    default T add(Node node){
        if (node == null) {
            return (T) this;
        }
        if (node == this) {
            throw new IllegalArgumentException("can not add widget itself as a sub widget");
        }
        getNodes().add(node);
        return (T) this;
    }
}
