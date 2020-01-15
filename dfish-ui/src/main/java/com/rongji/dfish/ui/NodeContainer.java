package com.rongji.dfish.ui;

import java.util.List;
/**
 * Container 为容器。
 * 这里只规定Cotainer 有getNodes属性，但不规定它的addNode，因为不同的Container很有可能参数不同
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
public interface NodeContainer<T extends NodeContainer<T>> {
   
	/**
	 * 取得它容纳的实际元素，而不是传输给页面的JSON元素。
     * 通常情况下和getNodes是一致的。但部分元素可能并不一样。比如GridLayout的nodes并不显示，而是以rows的方式存在。
     * 但调用findNodes方法的话他们是存在的。
	 * @return List
	 * @param <W> 子元素类型
	 */
    <W extends Node> List<W> findNodes();
	/**
	 * 可以根据子Widget的Id取得这个Widget。
	 * 如果子集里面没有包含这个Widget，那么返回空
	 * 如果子集的Widget本身也是一个Layout那么将递归查找
	 * 如果这个Layout中有多个Widget对应这个ID，仅找到第一个。
	 * @param id String
	 * @return Widget
	 */
	Node findNodeById(String id);



	/**
	 * 根据子Widget的Id，移除这个Widget
	 * 如果子集的Widget本身也是一个Layout那么将递归查找
	 * @param id String
	 * @return 本身，这样可以继续设置其他属性
	 */
	T removeNodeById(String id);
	/**
	 * 替代一个Widget,同find仅替换第一个找到的。
	 * <p>Widget 的种类可以不一致。但是ID必须一致，并且不能为空</p>
	 * @param w 需要替换的Widget
	 * @return 是否替换成功。一般失败的原因有:<ol><li>这个Widget的ID不存在</li>
	 * <li>原先Layout中并不存在这个id的Widget。</li>
	 * <li>不能替代Layout本身</li></ol>
	 */
	boolean replaceNodeById(Node w);

	/**
	 * 将所有子节点清空
	 */
	void clearNodes();

}
