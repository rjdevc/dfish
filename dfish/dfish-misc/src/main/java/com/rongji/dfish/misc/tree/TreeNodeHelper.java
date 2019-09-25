package com.rongji.dfish.misc.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 构建树节点帮助类
 * @author DFish team
 * @deprecated 该包如果用JAVA8改写效果将会更好。现在价值不明。 不建议使用。
 */
@Deprecated
public class TreeNodeHelper {
	

	/**
	 * 需要指定元素的ID和父ID，才能进行构建
	 *
	 * @param <ID> ID
	 * @param <T> T
	 */
	public static interface GetIdAndParentId<ID extends java.io.Serializable,T>{
		/**
		 * 取得ID
		 * @param o 对象本身
		 * @return ID
		 */
		ID getId(T o);
		/**
		 * 取得父ID
		 * @param o 对象本身
		 * @return ID
		 */
		ID getParentId(T o);
	}
	/**
	 * 构建树
	 * @param list 数据列表
	 * @param g 指定ID和父ID的方法。
	 * @return 构建好的结果
	 */
	public static<ID extends java.io.Serializable,T> TreeNode<ID,T> buildTree(List<T> list,GetIdAndParentId<ID,T> g){
		HashSet<ID> allId=new HashSet<ID>();
		HashMap<ID,List<T>> subMap=new HashMap<ID,List<T>>();
		List<T> topItems=new ArrayList<T>();
		for(T t:list){
			allId.add(g.getId(t));
		}
		for(T t:list){
			ID parentId=g.getParentId(t);
			if(allId.contains(parentId)){
				List<T> sublist=subMap.get(parentId);
				if(sublist==null){
					sublist=new ArrayList<T>();
					subMap.put(parentId, sublist);
				}
				sublist.add(t);
			}else{
				topItems.add(t);
			}
		}
		TreeNode<ID,T> result=new TreeNode<ID,T>();
		fillSubItem(result,topItems,subMap,g);
		return result;
	}
	private static <ID extends java.io.Serializable,T>  void fillSubItem(
			TreeNode<ID,T> shell,List<T>subItems,HashMap<ID,List<T>> subMap,GetIdAndParentId<ID,T>g){
		for(T item :subItems){
			TreeNode<ID,T> node=new TreeNode<ID,T>();
			ID id=g.getId(item);
			node.setId(id);
			node.setItem(item);
			shell.addSubNode(node);
			List<T> subs2=subMap.get(id);
			if(subs2!=null){
				fillSubItem(node, subs2, subMap, g);
			}
		}
	}
}
