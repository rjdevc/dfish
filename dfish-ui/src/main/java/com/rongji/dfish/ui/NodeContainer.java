package com.rongji.dfish.ui;

import com.rongji.dfish.ui.form.FormElement;

import java.util.Iterator;
import java.util.List;
/**
 * Container 为容器。
 * 这里只规定Cotainer 有getNodes属性，但不规定它的addNode，因为不同的Container很有可能参数不同
 * @author DFish Team
 *
 */
public interface NodeContainer{
	/**
	 * 用于搜索，删除，替换时定位内部的Node
	 */
	interface Filter{
		/**
		 * 条件判断器，返回true则是符合条件
		 * @param node Node 这里不会为空
		 * @return boolean
		 */
		boolean accept(Node node);
	}

	/**
	 * 找到第一个符合条件节点
	 * @param filter Filter
	 * @return Node
	 */
	Node findNode(Filter filter);

	/**
	 * 找到所有符合条件的节点
	 * @param filter
	 * @return
	 */
	List<Node> findAllNodes(Filter filter);

	/**
	 * 替换第一个符合条件的节点
	 * @param filter Filter
	 * @param node Node 替换成该node 如果为null则类似于removeNode
	 * @return 返回被替换前的Node 返回null的话，则没替换成功
	 */
	Node replaceNode(Filter filter,Node node);

	/**
	 * 替换所有符合条件的节点
	 * @param filter Filter
	 * @param node Node 替换成该node 如果为null则类似于removeAllNodes
	 * @return 成功替换的个数，0的话则没有替换成功
	 */
	int replaceAllNodes(Filter filter,Node node);

	/**
	 * 移除第一个符合条件的节点
	 * @param filter Filter
	 * @return 返回被删除的Node 返回null的话，则没删除成功
	 */
    default Node removeNode(Filter filter){
        return replaceNode(filter,null);
    }

	/**
	 * 移除所有符合条件的节点
	 * @param filter Filter
	 * @return 成功删除的个数，0的话则没有删除成功
	 */
	default int removeAllNodes(Filter filter){
        return replaceAllNodes(filter,null);
    }

	/**
	 * 根据node的ID 找到第一个ID符合的节点。找不到则返回null
	 * @param id String
	 * @return Node
	 */
	default Node findNodeById(String id) {
        if(id==null||id.isEmpty()){
			return null;
		}
		return findNode(node -> id.equals(node.getId()));
	}

	/**
	 * 根据node的ID 替换第一个ID相同节点。没替换成功则返回null
	 * @param node Node
	 * @return Node
	 */
	default Node replaceNodeById(Node node) {
		if(node==null||node.getId()==null||node.getId().isEmpty()){
			return null;
		}
		return replaceNode(n -> node.getId().equals(n.getId()),
                node);
	}

	/**
	 * 删除第一个指定ID 的Node 返回被删除的节点
	 * @param id String
	 * @return 返回被删除的节点， 如果没删除成功则返回null
	 */
	default Node removeNodeById(String id) {
		if(id==null||id.isEmpty()){
			return null;
		}
		return removeNode(node -> id.equals(node.getId()));
	}

	/**
	 * 找到所有Node 中name为指定Name的FormElement
	 * @param name String
	 * @return List
	 */
	default List<Node> findFormElementsByName(String name) {
		if(name==null||name.isEmpty()){
			return null;
		}
		return findAllNodes((node) -> {
			if(node instanceof FormElement){
				return name.equals(((FormElement) node).getName());
			}
			return false;
		});
	}

}
