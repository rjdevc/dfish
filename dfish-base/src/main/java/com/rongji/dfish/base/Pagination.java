package com.rongji.dfish.base;

import java.io.Serializable;

/**
 * Pagination 与Page 作用相当，就是当数据量太大的时候分页。
 * Pagination 使用offset/limit 模式，和网络上大多数接口相似。
 *
 * @author DFish team
 */
public class Pagination implements Serializable {

    private static final long serialVersionUID = 8563611090473067679L;

    private int limit = -1;
    private int offset;
    private Integer size;
    private boolean autoRowCount = true;

    /**
     * 构造函数
     */
    public Pagination() {
    }

    /**
     * 构造函数
     *
     * @param offset 跳过记录数
     */
    public Pagination(int offset) {
        this.offset = offset;
    }

    /**
     * 构造函数
     *
     * @param offset 跳过记录数
     * @param limit 一页最大记录数
     */
    public Pagination(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    /**
     * 构建Pagination
     *
     * @return 对象本身
     */
    public static Pagination of() {
        return new Pagination();
    }

    /**
     * 构建Pagination
     *
     * @param offset 偏移量，结果从第几条开始显示，初始是0条
     * @return 对象本身
     */
    public static Pagination of(int offset) {
        return new Pagination(offset);
    }

    /**
     * 构建Pagination
     *
     * @param offset 偏移量，结果从第几条开始显示，初始是0条
     * @param limit  结果最多显示多少行
     * @return 对象本身
     */
    public static Pagination of(int offset, int limit) {
        return new Pagination(offset, limit);
    }

    /**
     * 是否在查询的时候自动统计行数，默认为true
     *
     * @return boolean
     */
    public boolean isAutoRowCount() {
        return autoRowCount;
    }

    /**
     * 是否在查询的时候自动统计行数，默认为true
     *
     * @param autoRowCount boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Pagination setAutoRowCount(boolean autoRowCount) {
        this.autoRowCount = autoRowCount;
        return this;
    }

    /**
     * 结果最多显示多少行
     *
     * @return Integer
     */
    public int getLimit() {
        return limit;
    }

    /**
     * 结果最多显示多少行 Integer
     *
     * @param limit Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public Pagination setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * 偏移量，结果从第几条开始显示，初始是0条
     *
     * @return int
     */
    public int getOffset() {
        return offset;
    }

    /**
     * 偏移量，结果从第几条开始显示，初始是0条
     *
     * @param offset int
     * @return 本身，这样可以继续设置其他属性
     */
    public Pagination setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    /**
     * 如果开启了autoRowCount 统计出来的行数，讲从这里获取
     *
     * @return Integer
     */
    public Integer getSize() {
        return size;
    }

    /**
     * 如果开启了autoRowCount 统计出来的行数，讲从这里获取
     *
     * @param size Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public Pagination setSize(Integer size) {
        this.size = size;
        return this;
    }

    /**
     * 计算结果从第几条开始显示
     *
     * @param size  总记录数
     * @param limit 每页记录数
     * @return int 计算得出跳过多少记录数
     */
    public static int calculateOffset(int size, int limit) {
        int offset = (size - 1) / limit * limit;
        return offset < 0 ? 0 : offset;
    }

    public static Pagination of(Page page) {
        if (page == null) {
            throw new IllegalArgumentException("page is null");
        }
        Pagination pagination = new Pagination();
        pagination.setLimit(page.getPageSize());
        pagination.setOffset(page.getCurrentPage() * page.getPageSize() - page.getPageSize());
        pagination.setAutoRowCount(page.isAutoRowCount());
        pagination.setSize(page.getRowCount());
        return pagination;
    }

    /**
     * 转化
     *
     * @param page Page 分页信息
     * @return Pagination对象
     */
    public static Pagination fromPage(Page page) {
        return of(page);
    }

    /**
     * 转化
     *
     * @return Page对象
     */
    public Page toPage() {
        Page page = new Page();
        page.setAutoRowCount(this.isAutoRowCount());
        page.setRowCount(this.getSize() == null ? 0 : this.getSize());
        page.setPageSize(this.getLimit());
        if (this.getLimit() > 0) {
            int offset = this.getOffset();
            page.setCurrentPage(offset / this.getLimit() + 1);
        }
        return page;
    }

    /**
     * 计算总分页数
     * @return int
     */
    public int calculatePageCount() {
        if (this.getLimit() <= 0) {
            throw new UnsupportedOperationException("limit must greater than zero.");
        }
        if (this.getSize() == null) {
            return 0;
        }
        int pageCount = (this.getSize() + this.getLimit() - 1) / this.getLimit();
        return pageCount;
    }

}