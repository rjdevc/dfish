package com.rongji.dfish.ui.auxiliary;

/**
 * 表格列排序
 *
 * @author lamontYu
 * @date 2017-07-31
 * @since 3.1
 */
public class ColumnSort {

    /**
     * 排序状态-默认
     */
    public static final String STATUS_DEFAULT = "default";
    /**
     * 排序状态-正序
     */
    public static final String STATUS_ASC = "asc";
    /**
     * 排序状态-倒序
     */
    public static final String STATUS_DESC = "desc";

    private String field;
    private Boolean number;
    private String status;
    private String src;

    /**
     * 排序的字段名
     *
     * @return String
     */
    public String getField() {
        return field;
    }

    /**
     * 设置排序的字段名
     *
     * @param field 字段名
     * @return 本身，这样可以继续设置其他属性
     */
    public ColumnSort setField(String field) {
        this.field = field;
        return this;
    }

    /**
     * 是否按照数字方式排序
     *
     * @return Boolean
     */
    public Boolean getNumber() {
        return number;
    }

    /**
     * 设置是否按照数字方式排序
     *
     * @param number 按照数字方式排序
     * @return 本身，这样可以继续设置其他属性
     */
    public ColumnSort setNumber(Boolean number) {
        this.number = number;
        return this;
    }

    /**
     * 当前排序状态:{@link #STATUS_ASC} ,{@link #STATUS_DESC}
     *
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置当前排序状态:{@link #STATUS_ASC} ,{@link #STATUS_DESC}
     *
     * @param status 排序状态
     * @return 本身，这样可以继续设置其他属性
     */
    public ColumnSort setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * 如果排序数据需要后台支持，设置当前排序url
     *
     * @return String
     */
    public String getSrc() {
        return src;
    }

    /**
     * 如果排序数据需要后台支持，设置当前排序url
     *
     * @param src String
     * @return 本身，这样可以继续设置其他属性
     */
    public ColumnSort setSrc(String src) {
        this.src = src;
        return this;
    }
}
