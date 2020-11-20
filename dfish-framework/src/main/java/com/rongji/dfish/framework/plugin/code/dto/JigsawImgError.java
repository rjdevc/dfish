package com.rongji.dfish.framework.plugin.code.dto;

import java.io.Serializable;

/**
 * 滑动验证码错误结果信息
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class JigsawImgError implements Serializable {
    private static final long serialVersionUID = -9063472952742234038L;

    private String message;
    private long timeout;

    /**
     * 构造函数
     *
     * @param message     String 验证信息
     * @param timeout long 下次恢复可操作时间(毫秒)
     */
    public JigsawImgError(String message, long timeout) {
        this.message = message;
        this.timeout = timeout;
    }

    /**
     * 验证信息
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * 验证信息
     *
     * @param message String
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawImgError setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * 下次恢复可操作时间(毫秒)
     *
     * @return long
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * 下次恢复可操作时间(毫秒)
     *
     * @param timeout long
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawImgError setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }
}
