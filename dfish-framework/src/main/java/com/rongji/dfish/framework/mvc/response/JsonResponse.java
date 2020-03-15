package com.rongji.dfish.framework.mvc.response;

import com.rongji.dfish.base.Pagination;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 适用于处理json格式的数据，将数据转换为json字符串
 *
 * @param <T>
 */
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

    /**
     * 接口响应的头部信息
     * @return
     */
    public Header getHeader() {
        return header;
    }

    private Header header() {
        if (header == null) {
            header = new Header();
        }
        return header;
    }

    /**
     * 接口响应的头部信息
     * @param header
     * @return 本身，这样可以继续设置其他属性
     */
    public JsonResponse<T> setHeader(Header header) {
        this.header = header;
        return this;
    }

    /**
     * 接口响应的错误信息
     * @return
     */
    public Error getError() {
        return error;
    }

    /**
     * 接口响应的错误信息
     * @param error
     * @return 本身，这样可以继续设置其他属性
     */
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

    /**
     * 设置错误信息
     * @param msg
     * @return 本身，这样可以继续设置其他属性
     */
    public JsonResponse<T> setErrMsg(String msg) {
        error().setMsg(msg);
        return this;
    }

    /**
     * 设置错误代码
     * @param code
     * @return 本身，这样可以继续设置其他属性
     */
    public JsonResponse<T> setErrCode(String code) {
        error().setCode(code);
        return this;
    }

    /**
     * 设置分批调用接口时的信息
     * @param pagination
     * @return 本身，这样可以继续设置其他属性
     */
    public JsonResponse<T> setPagination(Pagination pagination) {
        if (pagination != null) {
            Header header = header();
            header.setLimit(pagination.getLimit());
            header.setOffset(pagination.getOffset());
            header.setSize(pagination.getSize());
        }
        return this;
    }

    /**
     * 接口调用时,响应头部信息
     * @param principal
     * @return 本身，这样可以继续设置其他属性
     */
    public JsonResponse<T> setPrincipal(HeaderPrincipal principal) {
        header().setPrincipal(principal);
        return this;
    }

    /**
     * 调用者主信息
     * @param principalName
     * @return 本身，这样可以继续设置其他属性
     */
    public JsonResponse<T> setPrincipalName(String principalName) {
        setPrincipal(new HeaderPrincipal(principalName));
        return this;
    }

    /**
     * 接口响应的头部信息
     *
     * @author lamontYu
     * @date 2019-09-23
     * @since 5.0
     */
    public static class Header {

        private String timestamp;
        private Integer size;
        private Integer offset;
        private Integer limit;
        private HeaderPrincipal principal;

        public static final SimpleDateFormat DF = new SimpleDateFormat("yyyyMMddHHmmssZ");

        /**
         * 构造函数
         */
        public Header() {
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
        public Header setTimestamp(String timestamp) {
            this.timestamp = timestamp;
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
        public Header setSize(Integer size) {
            this.size = size;
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
        public Header setOffset(Integer offset) {
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
        public Header setLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        /**
         * 接口调用时,响应头部信息
         * @return HeaderPrincipal
         */
        public HeaderPrincipal getPrincipal() {
            return principal;
        }

        /**
         * 接口调用时,响应头部信息
         * @param principal HeaderPrincipal
         * @return 本身，这样可以继续设置其他属性
         */
        public Header setPrincipal(HeaderPrincipal principal) {
            this.principal = principal;
            return this;
        }

    }

    /**
     * 接口响应头部的调用者信息
     *
     * @author lamontYu
     * @date 2019-11-29
     * @since 5.0
     */
    public static class HeaderPrincipal implements ResponsePrincipal {
        private String name;
        private String natureName;
        private String fullName;

        /**
         * 构造函数
         * @param name 调用者主信息
         */
        public HeaderPrincipal(String name) {
            this.name = name;
        }

        /**
         * 构造函数
         * @param name 调用者主信息
         * @param natureName 调用者名称
         */
        public HeaderPrincipal(String name, String natureName) {
            this.name = name;
            this.natureName = natureName;
        }

        /**
         * 调用者主信息
         * @return
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * 调用者主信息
         * @param name
         * @return
         */
        public HeaderPrincipal setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public String getNatureName() {
            return natureName;
        }

        /**
         * 调用者名称
         * @param natureName
         * @return
         */
        public HeaderPrincipal setNatureName(String natureName) {
            this.natureName = natureName;
            return this;
        }

        @Override
        public String getFullName() {
            return fullName;
        }

        /**
         * 调用者的完整名称
         * @param fullName
         * @return
         */
        public HeaderPrincipal setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
    }

    /**
     * 接口响应的错误信息
     *
     * @author lamontYu
     * @date 2019-09-23
     * @since 5.0
     */
    public static class Error {
        private String code;
        private String msg;

        /**
         * 错误代码
         * @return String
         */
        public String getCode() {
            return code;
        }

        /**
         * 错误代码
         * @param code String
         * @return 本身，这样可以继续设置其他属性
         */
        public Error setCode(String code) {
            this.code = code;
            return this;
        }

        /**
         * 错误信息
         * @return String
         */
        public String getMsg() {
            return msg;
        }

        /**
         * 错误信息
         * @param msg String
         * @return 本身，这样可以继续设置其他属性
         */
        public Error setMsg(String msg) {
            this.msg = msg;
            return this;
        }

    }
}
