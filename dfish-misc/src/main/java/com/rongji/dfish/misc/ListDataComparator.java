package com.rongji.dfish.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 对比两个数据列表
 * @author DFish Team
 *
 * @param <T> 类型
 */
public class ListDataComparator<T> {
	private List<T> oldList;
	private List<T> newList;
	private List<T> insertList = new ArrayList<T>();
	private List<T> deleteList = new ArrayList<T>();
	private List<T> restrictList = new ArrayList<T>();
	private List<T> updateList = new ArrayList<T>();
	public void setOldList(List<T> oldList){
		this.oldList=oldList;
	}
	public void setNewList(List<T> newList){
		this.newList=newList;
	}
	public void compare(){
		//尝试用有序的方式来比对
		try{
			this.compare(new OrderedDataIndentifer<T>(){
				@Override
				public boolean isRowChanges(T row1, T row2) {
					if(row1==null) {
                        return row2!=null;
                    }
					return row1!=row2;
				}
				@Override
				public boolean isSameRow(T row1, T row2) {
					return compareRow(row1,row2)==0;
				}
				@Override
				@SuppressWarnings("unchecked")
				public int compareRow(T row1, T row2) {
					if(row1==null) {
                        return row2==null?0:-1;
                    }
					if(row2==null) {
                        return 1;
                    }
					Comparable<T> c1=(Comparable<T>)row1;
//					Comparable<T> c2=(Comparable<T>)row2;
					return c1.compareTo(row2);
				}
			});
		}catch(ClassCastException ex){
			this.compare(new DataIndentifer<T>(){
				@Override
				public boolean isRowChanges(T row1, T row2) {
					if(row1==null) {
                        return row2!=null;
                    }
					return row1!=row2;
				}
				@Override
				public boolean isSameRow(T row1, T row2) {
					if(row1==null) {
                        return row2==null;
                    }
					return row1.equals(row2);
				}
			});
		}
	}
	private DataIndentifer<T> indent;
	public void compare(DataIndentifer<T> indent){
		this.indent = indent;
		// 对比两个队列不同;
		if(indent instanceof OrderedDataIndentifer){
			compareWithOrder();
		}else{
			compareWithoutOrder();
		}
	}
	private void compareWithOrder(){
		final OrderedDataIndentifer<T> oind=(OrderedDataIndentifer<T>)this.indent;
		List<T> sortedNewList=new ArrayList<T>(newList);
		List<T> sortedOldList=new ArrayList<T>(oldList);
		Collections.sort(sortedNewList,new Comparator<T>(){
			@Override
			public int compare(T row1, T row2) {
				return oind.compareRow(row1, row2);
			}
		});
		Collections.sort(sortedOldList,new Comparator<T>(){
			@Override
			public int compare(T row1, T row2) {
				return oind.compareRow(row1, row2);
			}
		});
		Iterator<T> oldIter=sortedOldList.iterator();
		Iterator<T> newIter=sortedNewList.iterator();
		int compareResult=0;
		T workingOldItem=null;
		T workingNewItem=null;
		while(true){
			//如果上次对比结果为0则两边都下翻
			//如果上次对比结果为小于0的数，那么oldIter下翻
			//否则newList 下翻。

			//判断两个值是否相等
			if(compareResult==0){
				if(!oldIter.hasNext()||!newIter.hasNext()) {
                    break;
                }
				workingOldItem=oldIter.next();
				workingNewItem=newIter.next();
//				System.out.println("old="+workingOldItem+"\t new="+workingNewItem);
			}else if(compareResult<0){
				if(!oldIter.hasNext()) {
                    break;
                }
				workingOldItem=oldIter.next();
//				System.out.println("old="+workingOldItem);
			}else{
				if(!newIter.hasNext()) {
                    break;
                }
				workingNewItem=newIter.next();
//				System.out.println("new="+workingNewItem);
			}
			compareResult=oind.compareRow(workingOldItem, workingNewItem);
			if(compareResult==0){
				if (indent.isRowChanges(workingOldItem, workingNewItem)) {
					// 不变的数据
					updateList.add(workingNewItem);
				} else {// 改变的数据
					restrictList.add(workingNewItem);
				}
			}else if(compareResult<0){
				deleteList.add(workingOldItem);
			}else{
				insertList.add(workingNewItem);
			}
		}
		if(compareResult==0){
			
		}else if(compareResult<0){
			insertList.add(workingNewItem);
		}else{
			deleteList.add(workingOldItem);
		}
		//一个队列已经遍历完了，剩余的数据，都在insert或delete内容中了
		while(newIter.hasNext()){
			insertList.add(newIter.next());
		}
		while(oldIter.hasNext()){
			deleteList.add(oldIter.next());
		}
	}
	/**
	 * 如果数据无序，先把新列表都当成要插入的，旧列表当成要删除的
	 * 然后用双重循环来判定他们相同的行，每次发现相同的行，就从两个列表中抽取出来。
	 * 并根据isRowChanges来决定抽取出来的数据属于updateList还是restrictList
	 */
	private void compareWithoutOrder(){
		// 拿出newList的每条数据在oldList循环一遍，找出不变的、新增的、修改的数据
		insertList=new LinkedList<T>(newList);
		deleteList=new LinkedList<T>(oldList);
		outer:for (Iterator<T> newIter=insertList.iterator();newIter.hasNext();) {
			T tn=newIter.next();
			for (Iterator<T> oldIter=deleteList.iterator();oldIter.hasNext();){
				T to = oldIter.next();
				if (indent.isSameRow(to, tn)) {
					// 两条数据在同一行
					if (indent.isRowChanges(to, tn)) {
						// 不变的数据
						updateList.add(tn);
					} else {// 改变的数据
						restrictList.add(tn);
					}
					oldIter.remove();
					newIter.remove();
					continue outer;
				} 
			}
		}
	}	
	public static interface DataIndentifer<T>{
		/**
		 * 判定两条数据逻辑上是否属于同一条数据
		 * <p>比如持久化对象POJO。如果ID一致，我们可能就认为它是同一行
		 * @param row1
		 * @param row2
		 * @return
		 */
		boolean isSameRow(T row1,T row2);
		/**
		 * 判定两条数据是否有更改
		 * <p>比如持久化对象POJO。如果任何一个值(除ID外)不同，则认为修改过。
		 * 在判定isRowChanges前，会先判断isSamRow，不同的行不会进行Changes判断。
		 * @param row1
		 * @param row2
		 * @return
		 */
		boolean isRowChanges(T row1,T row2);
	}
	/**
	 * <p>可排序的数据辨识器</p>
	 * 如果放在列表中的数据是可排序的，一般来说我们可以利用排序信息来提高对比的效率。
	 * 这时候，提供compareRow的方法。如果row1在row2前。那么返回一个小于0的数，如果row1在row2后，返回一个大于0的数
	 * 如果他们是同一行，那么返回0；
	 * 所以如果实现这个接口的话，一般来说isSameRow的写法是 return compareRow(row1,row2)==0;
	 * @author DFish Team
	 *
	 * @param <T>
	 */
	public static interface OrderedDataIndentifer<T> extends DataIndentifer<T>{
		/**
		 * 如果row1在row2前。那么返回一个小于0的数，如果row1在row2后，返回一个大于0的数
		 * 如果他们是同一行，那么返回0；
		 * @param row1
		 * @param row2
		 * @return
		 */
		int compareRow(T row1,T row2);
	}
	/**
	 * <p>取得新增的数据列表</p>
	 * 在compare后进行调用
	 * @return
	 */
	public List<T> getInsertList(){
		return insertList;
	}
	/**
	 * <p>取得修改的数据列表</p>
	 * 在compare后进行调用
	 * @return
	 */
	public List<T> getUpdateList(){
		return updateList;
	}
	/**
	 * <p>取得不变的数据列表</p>
	 * 在compare后进行调用
	 * @return
	 */
	public List<T> getRestrictList(){
		return restrictList;
	}
	/**
	 * <p>取得删除的数据列表</p>
	 * 在compare后进行调用
	 * @return
	 */
	public List<T> getDeleteList(){
		return deleteList;
	}

}
