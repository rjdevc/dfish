package com.rongji.dfish.base;

import java.io.Serializable;

/**
 * Page作用就是当数据量太大的时候分页
 * <p>
 * 一般来说对于人员阅读的话能理解的是第几页，
 * 而不是offset到第10条。limit到10条大小。
 * 人们一般把这种情况称做每页十条，第二页。
 * 注意这里是从1开始而不是0开始
 *
 * @author v1.0 王志亮
 * v1.1 LinLW将该类移动到dfish.base
 * v1.2 LinLW 增加autoRowCount字段。以便远程方法中如果用到此类信息不需要额外封装。
 * v1.3 lamontYu 增加currentCount字段。
 * @version 1.3
 */
public class Page implements Serializable {


    private static final long serialVersionUID = -3394734675120176877L;

    private int currentPage;

    private int pageSize;

    private int rowCount;

    private int currentCount = -1;

    private boolean autoRowCount = true;

    /**
     * 构造函数
     */
    public Page() {
    }

    /**
     * 构造函数
     * @param currentPage 当前页
     */
    public Page(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 构造函数
     * @param currentPage 当前页
     * @param pageSize 一页最大记录数
     */
    public Page(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    /**
     * 设置当前页号
     *
     * @param currentPage 当前页
     * @return 本身，这样可以继续设置属性
     */
    public Page setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    /**
     * 取得当前页号
     *
     * @return 当前页
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * 当前页大小
     *
     * @param pageSize 当前页大小
     * @return 本身，这样可以继续设置属性
     */
    public Page setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * 当前页大小
     *
     * @return 当前页大小
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 行数
     *
     * @param rowCount 行数
     * @return 本身，这样可以继续设置属性
     */
    public Page setRowCount(int rowCount) {
        this.rowCount = rowCount;
        return this;
    }

    /**
     * 行数
     *
     * @return 行数
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * 取得页数
     *
     * @return 总页数
     */
    public int getPageCount() {
        if (pageSize > 0) {
            //(rowCount != 0) &&
            if (rowCount < 0) {
                rowCount = 0;
            }
            int pageCount = ((rowCount + pageSize) - 1) / pageSize;
            return pageCount;
        }

        return 0;
    }

    /**
     * 是否自动统计总记录数
     *
     * @return Boolean 是否自动统计总记录数
     * @see #isAutoRowCount()
     */
    @Deprecated
    public Boolean getAutoRowCount() {
        return autoRowCount;
    }

    /**
     * 是否自动统计总记录数
     *
     * @param autoRowCount Boolean 是否自动统计总记录数
     * @return 本身，这样可以继续设置属性
     * @see #setAutoRowCount(boolean)
     */
    @Deprecated
    public Page setAutoRowCount(Boolean autoRowCount) {
        autoRowCount = autoRowCount == null ? true : autoRowCount;
        this.autoRowCount = autoRowCount;
        return this;
    }

    /**
     * 是否自动统计总记录数
     *
     * @return boolean 是否自动统计总记录数
     */
    public boolean isAutoRowCount() {
        return autoRowCount;
    }

    /**
     * 是否自动统计总记录数
     *
     * @param autoRowCount boolean 是否自动统计总记录数
     * @return 本身，这样可以继续设置属性
     */
    public Page setAutoRowCount(boolean autoRowCount) {
        this.autoRowCount = autoRowCount;
        return this;
    }

    /**
     * 当前页记录数
     *
     * @return 当前页结果记录数
     */
    public int getCurrentCount() {
        if (currentCount < 0) {
            int currTotalCount = currentPage * pageSize;
            if (currTotalCount <= rowCount) { // 总数超过当前页计算出的总数,说明当前页数据是满的
                currentCount = pageSize;
            } else if (pageSize > 0 && getPageCount() == currentPage) { // 最后一页的情况
                currentCount = rowCount % pageSize;
            }
        }
        if (currentCount < 0) {
            currentCount = 0;
        }
        return currentCount;
    }

    /**
     * 当前页记录数
     *
     * @param currentCount 当前页记录数
     * @return 本身，这样可以继续设置属性
     */
    public Page setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
        return this;
    }
}
