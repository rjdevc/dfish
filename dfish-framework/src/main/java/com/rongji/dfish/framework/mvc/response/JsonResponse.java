package com.rongji.dfish.framework.mvc.response;

import com.rongji.dfish.base.Pagination;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JsonResponse<T> {
    private Header header = new Header();
    private Error error;
    T data;

    public JsonResponse() {
    }

    public JsonResponse(T data) {
        setData(data);
    }

    public JsonResponse(T data, Pagination pagination) {
        setData(data);
        setPagination(pagination);
    }

    public static <T> JsonResponse<T> of(T data) {
        return of(data, null);
    }
    public static <T> JsonResponse<T> of(T data, Pagination pagination) {
        JsonResponse<T> response = new JsonResponse<>(data, pagination);
        return response;
    }

    public Header getHeader() {
        return header;
    }

    private Header header() {
        if (header == null) {
            header = new Header();
        }
        return header;
    }

    public JsonResponse<T> setHeader(Header header) {
        this.header = header;
        return this;
    }

    public Error getError() {
        return error;
    }

    public JsonResponse<T> setError(Error error) {
        this.error = error;
        return this;
    }

    public Error error() {
        if (error == null) {
            error = new Error();
        }
        return error;
    }

    public T getData() {
        return data;
    }

    public JsonResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public JsonResponse<T> setErrMsg(String msg) {
        error().setMsg(msg);
        return this;
    }

    public JsonResponse<T> setErrCode(String code) {
        error().setCode(code);
        return this;
    }

    public JsonResponse<T> setPagination(Pagination pagination) {
        if (pagination != null) {
            Header header = header();
            header.setLimit(pagination.getLimit());
            header.setOffset(pagination.getOffset());
            header.setSize(pagination.getSize());
        }
        return this;
    }

    public JsonResponse<T> setPrincipal(HeaderPrincipal principal) {
        header().setPrincipal(principal);
        return this;
    }

    public JsonResponse<T> setPrincipalName(String principalName) {
        setPrincipal(new HeaderPrincipal(principalName));
        return this;
    }

    public static class Header {

        private String timestamp;
        private Integer size;
        private Integer offset;
        private Integer limit;
        private HeaderPrincipal principal;

        public static final SimpleDateFormat DF = new SimpleDateFormat("yyyyMMddHHmmssZ");

        public Header() {
            synchronized (DF) {
                this.setTimestamp(DF.format(new Date()));
            }
        }

        public String getTimestamp() {
            return timestamp;
        }

        public Header setTimestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Integer getSize() {
            return size;
        }

        public Header setSize(Integer size) {
            this.size = size;
            return this;
        }

        public Integer getOffset() {
            return offset;
        }

        public Header setOffset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Integer getLimit() {
            return limit;
        }

        public Header setLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public HeaderPrincipal getPrincipal() {
            return principal;
        }

        public Header setPrincipal(HeaderPrincipal principal) {
            this.principal = principal;
            return this;
        }

    }

    public static class HeaderPrincipal implements ResponsePrincipal {
        private String name;
        private String natureName;
        private String fullName;

        public HeaderPrincipal(String name) {
            this.name = name;
        }

        public HeaderPrincipal(String name, String natureName) {
            this.name = name;
            this.natureName = natureName;
        }

        @Override
        public String getName() {
            return name;
        }

        public HeaderPrincipal setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public String getNatureName() {
            return natureName;
        }

        public HeaderPrincipal setNatureName(String natureName) {
            this.natureName = natureName;
            return this;
        }

        @Override
        public String getFullName() {
            return fullName;
        }

        public HeaderPrincipal setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
    }

    public static class Error {
        private String code;
        private String msg;

        public String getCode() {
            return code;
        }

        public Error setCode(String code) {
            this.code = code;
            return this;
        }

        public String getMsg() {
            return msg;
        }

        public Error setMsg(String msg) {
            this.msg = msg;
            return this;
        }

    }
}
