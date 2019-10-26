/*
 * Path.java 1.0.0
 *
 * Copyright 2010 Rongji Enterprise, Inc. All rights reserved.
 * Rongji PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.rongji.dfish.misc;

import java.util.*;

/**
 * Path 路径
 * <p>一个路径以<code>/</code>分隔各个节点。比如：</p>
 * <code>/02570001/02570013/02570351</code>
 * 与操作系统不同，空路径不是<code>/</code>而是空串。
 * 不过一般用不到空路径。
 * 
 * @author	I-Task Team
 * @version	1.1.0
 * @since	1.0.0	LinLW	创建基础的Path
 * @since	1.1.0	ZHL		在1.0.0的基础上扩展,提供更多的方法,并使可定义,支持Path循环,去除向外提供节点List
 */
public class Path{
	/** 默认风格符 */
	private static char DEFAULT_SEPERATOR='/';
	/** 缓存区大小 */
	private static int CACHE_LENGTH=64;
	/** 路径 */
	private String path;
	/** 分割符 */
	private char seperator;
	/** 解析后的分割符 */
	private String paseSeperator;
	/** 路径节点 */
	private List<String> pathNodes=new ArrayList<String>(CACHE_LENGTH);
	/** 标记 */
	private int token=0;
	/** 节点数 */
	private int level=0;
	
	public Path(){
		init(null,DEFAULT_SEPERATOR);
	}
	
	public Path(String path){
		init(path,DEFAULT_SEPERATOR);
	}
	
	public Path(String path,char seperator){
		init(path, seperator);
	}
	
	/**
	 * 设置节点集合
	 * 
	 * @param pathNodes
	 */
	public void setNodes(List<String> pathNodes){
		clean();
		addNodes(0,pathNodes);
	}
	
	/**
	 * 设置节点集合
	 * 
	 * @param pathNodes
	 */
	public void setNodes(String pathNodes){
		clean();
		addNodes(0, Collections.singletonList(pathNodes));
	}
	
	/**
	 * 获取节点集合
	 * 
	 * @return
	 */
	public List<String> getNodes(){
		return pathNodes;
	}
	
	/**
	 * 设置分割符
	 * 
	 * @param seperator
	 */
	public void setSeperator(char seperator){
		this.seperator=seperator;
		getSeperator(seperator);
	}
	
	/**
	 * 获取分割符
	 * 
	 * @return
	 */
	public char getSeperator(){
		return seperator;
	}
	
	/**
	 * 设置Token
	 * 
	 * @param token
	 */
	public void setToken(int token){
		if(checkNode(token)){
			this.token=token;
		}else{
			throw new IllegalArgumentException("The 'token' is not within the scope!");
		}
	}
	
	/**
	 * 获取token
	 * 
	 * @return
	 */
	public int getToken(){
		return token;
	}
	
	
	public int getLevel(){
		return level;
	}
	
	/**
	 * 获取节点
	 * 
	 * @param index
	 * @return
	 */
	public String getNode(int index){
		String node=null;
		if(checkNode(index)){
			pathNodes.get(index);
		}else{
			throw new IllegalArgumentException("The 'index' is not within the scope!");
		}
		
		return node;
	}
	
	/**
	 * 在最后添加一个节点
	 * 
	 * @param pathNode
	 */
	public void addNode(String pathNode){
		pathNodes.add(pathNode);
		token=0;
		++level;
	}
	
	/**
	 * 在指定位置添加一个节点
	 * 
	 * @param index
	 * @param pathNode
	 */
	public void addNode(int index,String pathNode){
		if(checkNode(index, pathNode)){
			pathNodes.add(index, pathNode);
			token=0;
			++level;
		}else{
			throw new IllegalArgumentException("The 'pathNode' must be a valid character!");
		}
	}
	
	/**
	 * 在path最后添加一组节点
	 * 
	 * @param c
	 * @return
	 */
	public boolean addNodes(Collection<String> c){
		boolean isAdded=false;
		
		if(c!=null && c.size()>0){
			isAdded=pathNodes.addAll(c);
			level+=c.size();
			token=0;
		}
		
		return isAdded;
	}
	
	/**
	 * 在指定位置添加一组节点
	 * 
	 * @param index
	 * @param c
	 * @return
	 */
	public boolean addNodes(int index,Collection<String> c){
		boolean isAdded=false;
		
		if(checkNode(index) && c!=null && c.size()>0){
			isAdded=pathNodes.addAll(index, c);
			level+=c.size();
			token=0;
		}
		
		return isAdded;
	}
	
	/**
	 * 设置指定位置的节点
	 * 
	 * @param index
	 * @param pathNode
	 */
	public void setNode(int index,String pathNode){
		if(checkNode(index+1, pathNode)){
			pathNodes.set(index, pathNode);
			token=0;
		}else{
			throw new IllegalArgumentException("The 'pathNode' must be a valid character!");
		}
	}
	
	/**
	 * 是否包含指定的节点
	 * 
	 * @param node
	 * @return
	 */
	public boolean contains(String node){
		
		return pathNodes.contains(node);
	}
	
	/**
	 * 找出节点所在的位置
	 * 没有则返回-1
	 * 
	 * @param node
	 * @return
	 */
	public int indexOf(String node){
		return pathNodes.indexOf(node);
	}
	
	/**
	 * 移除指定节点
	 * 
	 * @param index
	 * @return
	 */
	public String removeNode(int index){
		String node=null;
		
		if(checkNode(index)){
			node=pathNodes.remove(index);
			--level;
			token=0;
		}else{
			throw new IllegalArgumentException("The 'index' is not within the scope!");
		}
		
		return node;
	}
	
	/**
	 * 移除指定节点及节点往后的节点
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeNodes(int index){
		boolean isRemoved=false;
		
		if(checkNode(index)){
			for(int i=index; i<level; i++){
				pathNodes.remove(i);
				--level;
			}
			token=0;
			isRemoved=true;
		}else{
			throw new IllegalArgumentException("The 'index' is not within the scope!");
		}
		
		return isRemoved;
	}
	
	/**
	 * 移除指定节点,如果path中有多个node,则只删除第一个,并返回删除位置;
	 * 
	 * @param node
	 * @return
	 */
	public int removeNode(String node){
		int index=indexOf(node);
		
		if(checkNode(index)){
			pathNodes.remove(index);
			--level;
			token=0;
		}
		
		return index;
	}
	
	/**
	 * 移除指定节点及节点往后的节点,如果path中有多个node则从第一个开始删除,并返回开始删除位置
	 * 
	 * @param node
	 * @return
	 */
	public int removeNodes(String node){
		int index=indexOf(node);
		
		if(checkNode(index)){
			for(int i=index; i<level; i++){
				pathNodes.remove(i);
				--level;
			}
			token=0;
		}

		return index;
	}
	
	/**
	 * 清理Path
	 * 
	 */
	public void clean(){
		this.pathNodes=new ArrayList<String>(CACHE_LENGTH);
		level=0;
		token=0;
	}
	
	/**
	 * 重新开始
	 * 
	 */
	public void first(){
		token=0;
	}
	
	/**
	 * token到最后
	 * 
	 */
	public void last(){
		token=level-1;
	}
	
	/**
	 * 判断是否还有下一节点
	 * 
	 * @return
	 */
	public boolean hasNode(){
		
		return level-token>=1;
	}
	
	/**
	 * 向上移动一个节点
	 * 
	 * @return
	 */
	public String previous(){
		String node=pathNodes.get(token);
		--token;
		
		return node;
	}
	
	/**
	 * 获取下一节点值
	 * 
	 * @return
	 */
	public String next(){
		String node=pathNodes.get(token);
		++token;
		
		return node;
	}
	
	/**
	 * 获取部分Path
	 * 
	 * @param fromIndex		从0开始
	 * @return
	 */
	public String subPath(int fromIndex){
		return subPath(fromIndex, level-1);
	}
	
	/**
	 * 获取部分Path,
	 * 
	 * @param fromIndex	开始下标,从0开始
	 * @param toIndex  结束下标
	 * @return
	 */
	public String subPath(int fromIndex, int toIndex){
		StringBuilder sb=new StringBuilder();
		if(checkNode(fromIndex) && checkNode(toIndex) && fromIndex<toIndex){
			for(int i=fromIndex; i<toIndex ;i++){
				sb.append(seperator).append(pathNodes.get(i));
			}
		}else{
			throw new IllegalArgumentException("The 'fromIndex' and 'toIndex' are not within the scope!");
		}
		
		return sb.substring(1);
	}
	
	/**
	 * 替换节点,支持char序列替换
	 * 
	 * @param targetNode
	 * @param replavement
	 */
	public void replace(String targetNode,String replavement){
		if(checkNode(targetNode) && checkNode(replavement)){
			path=path.replace(targetNode, replavement);
			divideUp();
		}
		
	}
	
	/**
	 * toString();
	 * 
	 */
	@Override
    public String toString(){
		format();
		
		return this.path;
	}
	
	/**
	 * equals();
	 * 
	 */
	@Override
    public boolean equals(Object o){
		boolean equal=false;
		if(o!=null && o!=this && o instanceof Path){
			equal=this.pathNodes.equals(((Path)o).pathNodes);
		}
		
		return equal;
	}

	/**
	 * hashCode();
	 * 
	 */
	@Override
    public int hashCode(){
		int hashCode=0;
		
		if(level>0){
			for(String node : pathNodes){
				hashCode=node.hashCode()^hashCode;
			}
		}

		return hashCode;
	}
	
	/**
	 *  初始化
	 * 
	 * @param path
	 * @param seperator
	 */
	private void init(String path,char seperator){
		this.path=path;
		this.seperator=seperator;
		getSeperator(seperator);
		
		divideUp();
//		this.token=0;
//		this.level=pathNodes.size();
	}
	
	/**
	 * 转换分割符
	 * 
	 * @param seperator
	 * @return
	 */
	private void getSeperator(char seperator){
		switch(seperator){
			case '$':
			case '*':
			case '+':
			case '.':
			case '?':
			case '\\':
			case '^':
			case '|':
				this.paseSeperator="\\"+seperator; break;
				
			default:this.paseSeperator=String.valueOf(seperator);
		}
	}
	
	/**
	 * 分割路径
	 * 
	 */
	private void divideUp(){
		clean();
		
		if(checkNode(this.path)){
			String seperator=String.valueOf(this.seperator);
			if(this.path.startsWith(seperator)){
				String path=this.path.substring(seperator.length());
				addNodes(0, Arrays.asList(path.split(paseSeperator)));
			}
		}
	}
	
	/**
	 * 格式化路径
	 * 
	 */
	private void format(){
		StringBuilder sb=new StringBuilder();
		
		for(String node : pathNodes){
			sb.append(seperator).append(node);
		}
		
		path=sb.toString();
	}
	
	/**
	 * 判断添加的节点是否有效
	 * 
	 * @param index
	 * @param pathNode
	 * @return
	 */
	private boolean checkNode(int index,String pathNode){
		return index>=0 && index<level && pathNode!=null && pathNode.matches("\\w+");
	}
	
	/**
	 * 判断添加的节点是否有效
	 * 
	 * @param pathNode
	 * @return
	 */
	private boolean checkNode(String pathNode){
		return pathNode!=null && !"".equals(pathNode);
	}
	
	/**
	 * 判断添加的节点是否有效
	 * 
	 * @param index
	 * @return
	 */
	private boolean checkNode(int index){
		return index>=0 && index<=level;
	}
}
