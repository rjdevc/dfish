package com.rongji.dfish.framework.plugin.code.dto;

import java.io.Serializable;

/**
 * @author lamontYu
 * @date 2019-12-10
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

    public JigsawAuthResult(boolean success) {
        this.success = success;
    }

    public JigsawAuthResult(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public JigsawAuthResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public JigsawAuthResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
