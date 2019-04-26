package com.rongji.dfish.base;

import java.io.Serializable;

import com.rongji.dfish.base.Page;

/**
 * Pagination 与Page 作用相当，就是当数据量太大的时候分页。
 * Pagination 使用offset/limit 模式，和网络上大多数接口相似。
 * 
 * @author DFish team
 *
 */
public class Pagination implements Serializable{

	private static final long serialVersionUID = 8563611090473067679L;
	
	private Integer limit;
	private Integer offset;
	private Integer size;
	private boolean autoRowCount=true;
	/**
	 * 是否在查询的时候自动统计行数，默认为true
	 * @return boolean
	 */
	public boolean isAutoRowCount() {
		return autoRowCount;
	}
	/**
	 * 是否在查询的时候自动统计行数，默认为true
	 * @param autoRowCount boolean
	 */
	public void setAutoRowCount(boolean autoRowCount) {
		this.autoRowCount = autoRowCount;
	}
	/**
	 * 结果最多显示多少行
	 * @return Integer
	 */
	public Integer getLimit() {
		return limit; 
	}
	/**
	 * 结果最多显示多少行 Integer
	 * @param limit Integer
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	/**
	 * 偏移量，结果从第几条开始显示，初始是0条
	 * @return Integer
	 */
	public Integer getOffset() {
		return offset;
	}
	/**
	 * 偏移量，结果从第几条开始显示，初始是0条
	 * @param offset Integer
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	/**
	 * 如果开启了autoRowCount 统计出来的行数，讲从这里获取
	 * @return Integer
	 */
	public Integer getSize() {
		return size;
	}
	/**
	 * 如果开启了autoRowCount 统计出来的行数，讲从这里获取
	 * @param size Integer
	 */
	public void setSize(Integer size) {
		this.size = size;
	}
	
	/**
	 * 转化
	 * @param page Page
	 * @return Pagination
	 */
	public static Pagination fromPage(Page page){
		if(page==null){
			return null;
		}
		Pagination p=new Pagination();
		p.setLimit(page.getPageSize());
		p.setOffset(page.getCurrentPage()*page.getPageSize()-page.getPageSize());
		p.setAutoRowCount(page.getAutoRowCount()==null||page.getAutoRowCount());
		return p;
	}
	/**
	 * 转化
	 * @return Page
	 */
	public Page toPage(){
		return toPage(this);
	}

	/**
	 * 转化
	 * @param pagination Pagination
	 * @return Page
	 */
	public static Page toPage(Pagination pagination){
		if(pagination==null){
			return null;
		}
		Page page=new Page();
		page.setAutoRowCount(pagination.isAutoRowCount());
		page.setRowCount( pagination.getSize()==null?0:pagination.getSize());
		page.setPageSize(pagination.getLimit()==null?0:pagination.getLimit());
		if( pagination.getSize()!=null&&pagination.getLimit()!=null){
			page.setCurrentPage(pagination.getSize()/pagination.getLimit()+1);
		}
		return page;
	}
}