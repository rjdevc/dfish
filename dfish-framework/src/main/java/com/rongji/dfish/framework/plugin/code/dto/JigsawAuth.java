package com.rongji.dfish.framework.plugin.code.dto;

import java.io.Serializable;

/**
 * 拼图验证对象
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class JigsawAuth implements Serializable {
    private static final long serialVersionUID = -2543015857399605124L;

    /**
     * 校验结果
     */
    private boolean success;
    /**
     * 校验信息
     */
    private String text;

    /**
     * 构造函数
     *
     * @param success 是佛验证通过
     */
    public JigsawAuth(boolean success) {
        this.success = success;
    }

    /**
     * 构造函数
     *
     * @param success 是否验证通过
     * @param text     验证信息
     */
    public JigsawAuth(boolean success, String text) {
        this.success = success;
        this.text = text;
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
    public JigsawAuth setSuccess(boolean success) {
        this.success = success;
        return this;
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
    public JigsawAuth setText(String text) {
        this.text = text;
        return this;
    }
}
