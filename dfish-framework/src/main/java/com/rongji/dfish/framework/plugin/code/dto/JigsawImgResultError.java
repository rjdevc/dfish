package com.rongji.dfish.framework.plugin.code.dto;

import java.io.Serializable;

/**
 * 滑动验证码错误结果信息
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class JigsawImgResultError implements Serializable {
    private static final long serialVersionUID = -9063472952742234038L;

    private String text;
    private long timeout;

    /**
     * 构造函数
     *
     * @param text     String 验证信息
     * @param timeout long 下次恢复可操作时间(毫秒)
     */
    public JigsawImgResultError(String text, long timeout) {
        this.text = text;
        this.timeout = timeout;
    }

    /**
     * 验证信息
     *
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * 验证信息
     *
     * @param text String
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawImgResultError setText(String text) {
        this.text = text;
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
    public JigsawImgResultError setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }
}
