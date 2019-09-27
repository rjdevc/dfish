package com.rongji.dfish.framework.mvc.response;

import com.rongji.dfish.base.Pagination;

import java.text.SimpleDateFormat;
import java.util.Date;


public class JsonResponse<T> {
    private Header header = new Header();
    private Error error = new Error();
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setErrMsg(String msg) {
        if (error == null) {
            error = new Error();
        }
        error.setMsg(msg);
    }

    public void setErrCode(String code) {
        if (error == null) {
            error = new Error();
        }
        error.setCode(code);
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

        public static final SimpleDateFormat DF = new SimpleDateFormat("yyyyMMddHHmmssZ");

        public Header() {
            synchronized (DF) {
                this.setTimestamp(DF.format(new Date()));
            }
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

    }

    public static class Error {
        private String code;
        private String msg;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    }
}
