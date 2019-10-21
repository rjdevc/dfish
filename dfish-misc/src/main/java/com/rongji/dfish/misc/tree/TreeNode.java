package com.rongji.dfish.misc.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 树节点数据结构类。
 * @author DFish team
 *
 * @param <ID> 编号多为String Integer 或Long等基础类型
 * @param <T> 元素类型
 */
public class TreeNode<ID extends java.io.Serializable,T> implements Cloneable {
	private T item;
	private ID id;
	private TreeNode<ID,T> parent;
	private List<TreeNode<ID,T>> subNodes;
	private int level;
	/**
	 * 元素
	 * @return T
	 */
	public T getItem() {
		return item;
	}
	/**
	 * 元素
	 * @param item T
	 */
	public void setItem(T item) {
		this.item = item;
	}
	/**
	 * 编号
	 * @return ID
	 */
	public ID getId() {
		return id;
	}
	/**
	 * 编号
	 * @param id ID
	 */
	public void setId(ID id) {
		this.id = id;
	}
	/**
	 * 父节点
	 * @return TreeNode
	 */
	public TreeNode<ID,T> getParent() {
		return parent;
	}
	/**
	 * 父节点
	 * @param parent TreeNode
	 */
	public void setParent(TreeNode<ID,T> parent) {
		this.parent = parent;
	}
	/**
	 * 层级 根节点为0
	 * @return int
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * 层级 根节点为0
	 * @param level int
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	/**
	 * 子节点
	 * @return List
	 */
	public List<TreeNode<ID,T>> getSubNodes(){
		return this.subNodes;
	}
	/**
	 * 子节点
	 * @param subNodes List
	 */
	public void setSubNodes(List<TreeNode<ID,T>> subNodes) {
		this.subNodes = subNodes;
	}

	/**
	 * 默认构造函数
	 */
	public TreeNode(){}
	
	/**
	 * 获取树节点下指定编号(ID)的节点。
	 * 如果没有则返回空
	 * 如果节点下ID有重复，只返回第一个找到的。
	 * @param id ID
	 * @return TreeNode
	 */
	public TreeNode<ID,T> get(ID id){
		if(id==null){
			return null;
		}
		if(id.equals(this.id)){
			return this;
		}
		if(subNodes!=null){
			for(TreeNode<ID,T> n:subNodes){
				TreeNode<ID,T> f=n.get(id);
				if(f!=null){
					return f;
				}
			}
		}
		return null;
	}
	/**
	 *  获取树节点下指定编号(ID)的节点,它的内容
	 *  如果没有则返回空
	 * 如果节点下ID有重复，只返回第一个找到的。
	 * @param id ID
	 * @return T
	 */
	public T getItem(ID id){
		TreeNode<ID,T> n=get(id);
		if(n!=null){
			return n.getItem(); 
		}
		return null;
	}
	/**
	 * 删除树节点下指定编号(ID)的节点 
	 * 不会删除自己。
	 *  如果没有则无动作
	 *  如果ID有重复，只删除第一个找到的，并将原来的值返回
	 * @param id ID
	 * @return TreeNode
	 */
	public TreeNode<ID,T> remove(ID id){
		if(id==null){
			return null;
		}
		if(subNodes!=null){
			for(Iterator<TreeNode<ID,T>> iter=subNodes.iterator();iter.hasNext();){
				TreeNode<ID,T> n=iter.next();
				if(id.equals(n.getId())){
					iter.remove();
					return n;
				}
				TreeNode<ID,T> m=n.remove(id);
				if(m!=null){
					return m;
				}
			}
		}
		return null;
	}
	/**
	 * 替换树节点下指定编号(ID)的节点 ，内容
	 * 如果没有找到则无动作，返回空
	 *  如果ID有重复，只替换第一个找到的，并将原来的内容返回
	 * @param id ID
	 * @param item T
	 * @return T
	 */
	public T replace(ID id,T item){
		if(id==null){
			return null;
		}
		if(id.equals(getId())){
			T oldItem=getItem();
			setItem(item);
			return oldItem;
		}
		if(subNodes!=null){
			for(Iterator<TreeNode<ID,T>> iter=subNodes.iterator();iter.hasNext();){
				TreeNode<ID,T> n=iter.next();
				T m=n.replace(id,item);
				if(m!=null){
					return m;
				}
			}
		}
		return null;
	}
	/**
	 * 取得直属下级的内容列表
	 * @return List
	 */
	public List<T> getSubItems(){
		if(getSubNodes()!=null){
			List<T>result=new ArrayList<T>(getSubNodes().size());
			for(TreeNode<ID,T> n:getSubNodes()){
				result.add(n.getItem());
			}
			return result;
		}
		return null;
	}

	/**
	 * 取得级联下级的内容列表
	 * @param cotainsSelf 是否包含当前节点的内容。如果是，当前节点内容放在第0位
	 * @return List
	 */
	public List<T> getSubItemsCascade(boolean cotainsSelf){
		List<T>result=new ArrayList<T>();
		if(cotainsSelf){
			result.add(getItem());
		}
		this.findSubItemsCascade(result);
		return result;
	}
	private void findSubItemsCascade(List<T>result){
		if(getSubNodes()==null){return;}
		for(TreeNode<ID,T> n:getSubNodes()){
			result.add(n.getItem());
			n.findSubItemsCascade(result);
		}
	}
	/**
	 * 增加一个节点
	 * @param node TreeNode
	 */
	public void addSubNode(TreeNode<ID,T> node){
		if(node==null){
			return;
		}
		if(subNodes==null){
			subNodes=new ArrayList<TreeNode<ID,T>>();
		}
		subNodes.add(node);
		node.parent=this;
		node.level=this.level+1;
	}
	
	/**
	 * 复制一份完整的树，这棵树的节点操作将不会影响原树。
	 * 这里的节点操作仅限定在树操作上。item里面的内容如果改变，还是会影响到原树。
	 * 克隆的对象不再有parent会自动断开关联。层级也会变成0级。
	 * @return 克隆的对象。 
	 */
	@Override
    public TreeNode<ID,T> clone(){
		return clone(null,0);
	}
	private TreeNode<ID,T> clone(TreeNode<ID,T> parent,int level){
		TreeNode<ID,T> cloned=new TreeNode<ID,T>();
		cloned.setId(id);
		cloned.setItem(item);
		cloned.setLevel(level);
		cloned.setParent(parent);
		if(subNodes!=null){
			List<TreeNode<ID,T>> subNodes=new ArrayList<TreeNode<ID,T>>();
			cloned.setSubNodes(subNodes);
			for(TreeNode<ID,T> n:this.subNodes){
				subNodes.add(n.clone(cloned, level+1));
			}
		}
		return cloned;
	}
	

}
