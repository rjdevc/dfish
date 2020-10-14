package com.rongji.dfish.framework.mvc.response;

import com.rongji.dfish.base.Pagination;

import java.io.Serializable;
import java.util.List;

/**
 * 响应列表内容
 *
 * @param <E> 列表元素
 * @author lamontYu
 * @since 5.0
 */
public class ResponseListContent<E> implements Serializable {
    private static final long serialVersionUID = 3897874383933729307L;

    private List<E> content;
    private Pagination pagination;

    public ResponseListContent(List<E> content) {
        this.content = content;
    }

    public ResponseListContent(List<E> content, Pagination pagination) {
        this.content = content;
        this.pagination = pagination;
    }

    public List<E> getContent() {
        return content;
    }

    public ResponseListContent setContent(List<E> content) {
        this.content = content;
        return this;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public ResponseListContent setPagination(Pagination pagination) {
        this.pagination = pagination;
        return this;
    }
}
