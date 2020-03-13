package com.rongji.dfish.framework.plugin.code.dto;

import java.io.Serializable;

/**
 * 拼图验证对象
 *
 * @author lamontYu
 * @date 2019-12-10
 * @since 5.0
 */
public class JigsawAuthResult implements Serializable {
    private static final long serialVersionUID = -2543015857399605124L;

    /**
     * 校验结果
     */
    private boolean success;
    /**
     * 校验信息
     */
    private String msg;

    /**
     * 构造函数
     *
     * @param success 是佛验证通过
     */
    public JigsawAuthResult(boolean success) {
        this.success = success;
    }

    /**
     * 构造函数
     *
     * @param success 是否验证通过
     * @param msg     验证信息
     */
    public JigsawAuthResult(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    /**
     * 是否验证通过
     *
     * @return boolean
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 是否验证通过
     *
     * @param success boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawAuthResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    /**
     * 验证信息
     *
     * @return String
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 验证信息
     *
     * @param msg String
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawAuthResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
