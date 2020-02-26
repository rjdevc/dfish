package com.rongji.dfish.ui;

/**
 * SingleContainer 为只能容纳一个元素的容器
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
public interface SingleNodeContainer<T extends SingleNodeContainer<T,N>,N extends Node> extends NodeContainer {
	 /**
     * 取得下级的Widget
     * @return N
     */
    N getNode();
    /**
     * 设置下级的Widget
     * @param node N
     * @return 本身，这样可以继续设置其他属性
     */
    T setNode(N node);
}
