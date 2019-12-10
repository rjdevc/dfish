package com.rongji.dfish.framework.plugin.code.dto;

import java.io.Serializable;

/**
 * @author lamontYu
 * @create 2019-12-10
 */
public class JigsawCheckResult implements Serializable {
    private static final long serialVersionUID = -2543015857399605124L;

    /**
     * 校验结果
     */
    private boolean result;
    /**
     * 校验信息
     */
    private String msg;

    public JigsawCheckResult(boolean result) {
        this.result = result;
    }

    public JigsawCheckResult(boolean result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public boolean isResult() {
        return result;
    }

    public JigsawCheckResult setResult(boolean result) {
        this.result = result;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public JigsawCheckResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
