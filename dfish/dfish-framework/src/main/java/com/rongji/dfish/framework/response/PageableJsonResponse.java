package com.rongji.dfish.framework.response;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Pagination;

public class PageableJsonResponse<T> extends JsonResponse<T> {
	
	public PageableJsonResponse(T data) {
		super(data);
	}

	public PageableJsonResponse(T data, Page page) {
		super(data);
		this.setPage(page);
	}

	public PageableJsonResponse(T data, Pagination pagination) {
		super(data);
		this.setPagination(pagination);
	}

	public PageableJsonResponse<T> setPagination(Pagination pagination) {
		if (pagination != null) {
			Header header = getHeader();
			header.setLimit(pagination.getLimit());
			header.setOffset(pagination.getOffset());
			header.setSize(pagination.getSize());
		}
		return this;
	}

	public PageableJsonResponse<T> setPage(Page page) {
		return setPagination(Pagination.fromPage(page));
	}
	
}
