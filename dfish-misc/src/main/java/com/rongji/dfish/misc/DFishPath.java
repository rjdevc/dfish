package com.rongji.dfish.misc;

import java.util.ArrayList;
import java.util.List;

/**
 * Path 路径
 * <p>一个路径以<code>/</code>分隔各个节点。比如：</p>
 * <code>/02570001/02570013/02570351</code>
 * 与操作系统不同，空路径不是<code>/</code>而是空串。
 * 不过一般用不到空路径。
 * @author LinLW
 */
public class DFishPath {
	private ArrayList<String> nodes=new ArrayList<String>();
	/**
	 * 构建一个空Path
	 */
	public DFishPath(){}
	/**
	 * 根据path的字符串形式进行构建
	 * @param path
	 */
	public DFishPath(String path){
		if(path==null)return;
		String[] nds=path.split("/");
		for(String n:nds){
			if(!n.equals("")){
				nodes.add(n);
			}
		}
	}
	/**
	 * 这个只适合于转化异构的PATH
	 * @param path
	 * @param seperator
	 */
	public DFishPath(String path,char seperator){
		if(path==null)return;
		String s=null;//String.valueOf(seperator);
		switch(seperator){
		case '$':
		case '*':
		case '+':
		case '.':
		case '?':
		case '\\':
		case '^':
		case '|':
			s="\\"+seperator;
			break;
		default:s=String.valueOf(seperator);
		}
		String[] nds=path.split(s);
		for(String n:nds){
			if(!n.equals("")){
				nodes.add(n);
			}
		}
	}
	/**
	 * 根据node进行构建
	 * @param nodes
	 */
	public DFishPath(List<String> nodes){
		if(nodes==null)return;
		for(String n:nodes){
			if(!n.equals("")){
				this.nodes.add(n);
			}
		}
	}
	/**
	 * 根据node进行构建
	 * @param nodes
	 */
	public DFishPath(String[] nodes){
		if(nodes==null)return;
		for(String n:nodes){
			if(!n.equals("")){
				this.nodes.add(n);
			}
		}		
	}
	/**
	 * 取得该path的层次
	 * @return
	 */
	public int getLevel(){
		return nodes.size();
	}

	/**
	 * 取得各个节点
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getNodes(){
		return (List<String>) nodes.clone();
	}
	
	/**
	 * 取得某一个节点
	 * @return
	 */
	public String getNode(int index){
		return nodes.get(index);
	}

	/**
	 * 增加一个节点。
	 * @param node
	 */
	public void addNode(String node){
		nodes.add(node);
	}

	/**
	 * 找出节点所在的位置
	 * 没有则返回-1
	 * @param node
	 * @return
	 */
	public int indexOf(String node){
		return nodes.indexOf(node);
	}

	/**
	 * path是否包含某节点
	 * @param node
	 * @return
	 */
	public boolean contains(String node){
		return nodes.contains(node);
	}

	@Override
	public boolean equals(Object o){
		if(o==null)return false;
		if(o==this)return true;
		if(o instanceof DFishPath){
			DFishPath cast=(DFishPath)o;
			return this.nodes.equals(cast.nodes);
		}
		return false;
	}

	@Override
	public String toString(){
		StringBuilder sb=new StringBuilder();
		for(String node:nodes){
			sb.append('/').append(node);
		}
		return sb.toString();
	}
	
	@Override
	public int hashCode(){
		if(nodes.size()==0){
			return 0;
		}
		int i=0;
		for(String node:nodes){
			i=node.hashCode()^i;
		}
		return i;
	}
}
