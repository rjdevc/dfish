package com.rongji.dfish.framework.mvc.response;

import com.rongji.dfish.base.Pagination;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 接口响应的头部信息
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class ResponseHeader implements Serializable {

    private static final long serialVersionUID = -2024520031394505326L;
    private String timestamp;
    private Integer offset;
    private Integer limit;
    private Integer size;
    private ResponsePrincipal principal;

    public static final SimpleDateFormat DF = new SimpleDateFormat("yyyyMMddHHmmssZ");

    /**
     * 构造函数
     */
    public ResponseHeader() {
        synchronized (DF) {
            this.setTimestamp(DF.format(new Date()));
        }
    }

    /**
     * 接口响应时间戳
     * @return String
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * 接口响应时间戳
     * @param timestamp String
     * @return 本身，这样可以继续设置其他属性
     */
    public ResponseHeader setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * 分批接口调用时,请求的偏移量(即跳过记录数)
     * @return Integer
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * 分批接口调用时,请求的偏移量(即跳过记录数)
     * @param offset Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public ResponseHeader setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    /**
     * 分批接口调用时,请求的当前批数量
     * @return Integer
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * 分批接口调用时,请求的当前批数量
     * @param limit Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public ResponseHeader setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * 分批接口调用时,响应的总记录数
     * @return Integer
     */
    public Integer getSize() {
        return size;
    }

    /**
     * 分批接口调用时,响应的总记录数
     * @param size Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public ResponseHeader setSize(Integer size) {
        this.size = size;
        return this;
    }

    /**
     * 接口调用时,响应头部信息
     * @return HeaderPrincipal
     */
    public ResponsePrincipal getPrincipal() {
        return principal;
    }

    /**
     * 接口调用时,响应头部信息
     * @param principal HeaderPrincipal
     * @return 本身，这样可以继续设置其他属性
     */
    public ResponseHeader setPrincipal(ResponsePrincipal principal) {
        this.principal = principal;
        return this;
    }

    /**
     * 设置分页信息
     * @param pagination
     * @return
     */
    public ResponseHeader setPagination(Pagination pagination) {
        if (pagination != null) {
            this.setLimit(pagination.getLimit());
            this.setOffset(pagination.getOffset());
            this.setSize(pagination.getSize());
        }
        return this;
    }

}