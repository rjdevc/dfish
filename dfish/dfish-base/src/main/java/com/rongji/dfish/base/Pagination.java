package com.rongji.dfish.base;

import java.io.Serializable;

import com.rongji.dfish.base.Page;

/**
 * Pagination 与Page 作用相当，就是当数据量太大的时候分页。
 * Pagination 使用offset/limit 模式，和网络上大多数接口相似。
 *
 * @author DFish team
 */
public class Pagination implements Serializable {

    private static final long serialVersionUID = 8563611090473067679L;

    private Integer limit;
    private Integer offset;
    private Integer size;
    private boolean autoRowCount = true;

    public Pagination() {
    }

    public Pagination(int offset) {
        this.offset = offset;
    }

    public Pagination(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
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
    public Integer getLimit() {
        return limit;
    }

    /**
     * 结果最多显示多少行 Integer
     *
     * @param limit Integer
     */
    public Pagination setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * 偏移量，结果从第几条开始显示，初始是0条
     *
     * @return Integer
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * 偏移量，结果从第几条开始显示，初始是0条
     *
     * @param offset Integer
     */
    public Pagination setOffset(Integer offset) {
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
     */
    public Pagination setSize(Integer size) {
        this.size = size;
        return this;
    }

    /**
     * 转化
     *
     * @param page Page
     * @return Pagination
     */
    public static Pagination fromPage(Page page) {
        if (page == null) {
            return null;
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
     * @return Page
     */
    public Page toPage() {
        Page page = new Page();
        page.setAutoRowCount(this.isAutoRowCount());
        page.setRowCount(this.getSize() == null ? 0 : this.getSize());
        page.setPageSize(this.getLimit() == null ? 0 : this.getLimit());
        if (this.getLimit() != null) {
            int offset = this.getOffset() == null ? 0 : this.getOffset();
            page.setCurrentPage(offset / this.getLimit() + 1);
        }
        return page;
    }

}