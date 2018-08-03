package com.rongji.dfish.ui;

import java.util.List;
/**
 * Container 为容器。
 * 这里只规定Cotainer 有getNodes属性，但不规定它的addNode，因为不同的Container很有可能参数不同
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
public interface Container<T extends Container<T>> {
   
	/**
	 * 取得它容纳的实际元素，而不是传输给页面的JSON元素。
     * 通常情况下和getNodes是一致的。但部分元素可能并不一样。比如GridLayout的nodes并不显示，而是以rows的方式存在。
     * 但调用findNodes方法的话他们是存在的。
	 * @return List
	 * @param <W> 子元素类型
	 */
    <W extends Object> List<W> findNodes();


}
