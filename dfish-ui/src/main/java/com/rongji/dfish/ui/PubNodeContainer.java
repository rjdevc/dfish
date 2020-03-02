package com.rongji.dfish.ui;

/**
 * 默认参数
 *
 * @param <T> 当前对象类型
 * @param <N> 子节点对象类型
 * @author DFish Team
 */
public interface PubNodeContainer<T extends PubNodeContainer<T, N, PN>, N extends Node,PN extends Node> extends MultiNodeContainer<T,N> {

	/**
	 * 安全取得默认对象,如果未设置过默认对象,将创建对象
	 * @return pub 默认对象
	 */
    PN pub();

    /**
     * 取得默认对象。
     * 通常取得默认对象属性是最安全的。
     * 直接用设置的话，有可能会覆盖上次的设置
     *
     * @return pub 默认对象
     */
    PN getPub();

    /**
     * 设置默认对象
     * 通常取得默认对象属性是最安全的。
     * 直接用设置的话，有可能会覆盖上次的设置
     *
     * @param pub 默认对象
     * @return 本身，这样可以继续设置其他属性
     */
    T setPub(PN pub);
}
