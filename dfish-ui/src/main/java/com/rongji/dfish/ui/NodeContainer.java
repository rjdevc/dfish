package com.rongji.dfish.ui;

import java.util.Iterator;
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
	default Node findNodeById(String id) {
		List<?> nodes = findNodes();
		if (id == null || nodes == null) {
			return null;
		}
		for (Iterator iter = nodes.iterator(); iter.hasNext(); ) {
			Object item = iter.next();

			if (item instanceof Widget && id.equals(((Widget) item).getId())) {
				return (Node<? extends Node<?>>) item;
			} else if (item instanceof NodeContainer) {
				NodeContainer cast = (NodeContainer) item;
				Node<? extends Node<?>> c = cast.findNodeById(id);
				if (c != null) {
					return c;
				}
			}
		}
		return null;
	}

	/**
	 * 根据子Widget的Id，移除这个Widget
	 * 如果子集的Widget本身也是一个Layout那么将递归查找
	 * @param id String
	 * @return 本身，这样可以继续设置其他属性
	 */
	default T removeNodeById(String id) {
		List nodes = findNodes();
		if (id == null || nodes == null) {
			return (T) this;
		}
		for (Iterator<Widget<?>> iter = nodes.iterator();
			 iter.hasNext(); ) {
			Widget<?> item = iter.next();
			if (id.equals(item.getId())) {
				iter.remove();
			} else if (item instanceof NodeContainer) {
				NodeContainer cast = (NodeContainer) item;
				cast.removeNodeById(id);
			}
		}
		return (T) this;
	}
	/**
	 * 替代一个Widget,同find仅替换第一个找到的。
	 * <p>Widget 的种类可以不一致。但是ID必须一致，并且不能为空</p>
	 * @param w 需要替换的Widget
	 * @return 是否替换成功。一般失败的原因有:<ol><li>这个Widget的ID不存在</li>
	 * <li>原先Layout中并不存在这个id的Widget。</li>
	 * <li>不能替代Layout本身</li></ol>
	 */
	default boolean replaceNodeById(Node w) {
		List<Node> nodes = findNodes();
		if (w == null || w.getId() == null || nodes == null) {
			return false;
		}
		String id = w.getId();
		for (int i = 0; i < nodes.size(); i++) {
			Node item = nodes.get(i);
			if (id.equals((item.getId()))) {
				// 替换该元素
				if (onReplace(item, w)) {
					nodes.set(i, w);
					return true;
				} else {
					return false;
				}
			} else if (item instanceof NodeContainer) {
				NodeContainer cast = (NodeContainer) item;
				boolean replaced = cast.replaceNodeById(w);
				if (replaced) {
					return true;
				}
			}
		}
		return false;
	}

	default boolean onReplace(Node oldNode, Node newNode) {
		return true;
	}

	/**
	 * 将所有子节点清空
	 */
	default void clearNodes() {
		List nodes = findNodes();
		if (nodes != null) {
			nodes.clear();
		}
	}

}
