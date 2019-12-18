package com.rongji.dfish.framework.plugin.code.dto;

import java.io.Serializable;

/**
 * @author lamontYu
 * @date 2019-12-10
 */
public class JigsawImgResultError implements Serializable {
    private static final long serialVersionUID = -9063472952742234038L;

    private String msg;
    private long timeout;

    public JigsawImgResultError(String msg, long timeout) {
        this.msg = msg;
        this.timeout = timeout;
    }

    public String getMsg() {
        return msg;
    }

    public JigsawImgResultError setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    public JigsawImgResultError setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }
}
