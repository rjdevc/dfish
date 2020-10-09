package com.rongji.dfish.framework.mvc.response;

import java.io.Serializable;

/**
 * 接口响应的错误信息
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class ResponseError implements Serializable {
    private static final long serialVersionUID = -1372076976712453441L;
    private String code;
    private String message;

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
    public ResponseError setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * 错误信息
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * 错误信息
     * @param message String
     * @return 本身，这样可以继续设置其他属性
     */
    public ResponseError setMessage(String message) {
        this.message = message;
        return this;
    }

}
