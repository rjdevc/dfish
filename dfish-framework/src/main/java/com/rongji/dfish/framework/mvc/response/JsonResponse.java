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

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
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

    public void setData(T data) {
        this.data = data;
    }

    public void setErrMsg(String msg) {
        error().setMsg(msg);
    }

    public void setErrCode(String code) {
        error().setCode(code);
    }

    public JsonResponse<T> setPagination(Pagination pagination) {
        if (pagination != null) {
            Header header = getHeader();
            header.setLimit(pagination.getLimit());
            header.setOffset(pagination.getOffset());
            header.setSize(pagination.getSize());
        }
        return this;
    }

    public static class Header {

        private String timestamp;
        private Integer size;
        private Integer offset;
        private Integer limit;
        private Map<String, Object> context;

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

        public Map<String, Object> getContext() {
            return context;
        }

        public Header setContext(Map<String, Object> context) {
            this.context = context;
            return this;
        }

        public Header addContext(String key, Object value) {
            if (this.context == null) {
                this.context = new HashMap<>();
            }
            this.context.put(key, value);
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
