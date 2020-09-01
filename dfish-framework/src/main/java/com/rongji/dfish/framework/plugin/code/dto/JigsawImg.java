package com.rongji.dfish.framework.plugin.code.dto;

import java.io.Serializable;

/**
 * 滑动验证码加载的图片结果
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class JigsawImg implements Serializable {
    private static final long serialVersionUID = -2076497536245307557L;

    /**
     * 大图片
     */
    private JigsawImgItem big;
    /**
     * 小图片
     */
    private JigsawImgItem small;
    /**
     * 最小值
     */
    private Number minValue;
    /**
     * 最大值
     */
    private Number maxValue;

    /**
     * 滑动验证码错误信息类
     */
    private JigsawImgError error;
    /**
     * 标识字符串
     */
    private String token;

    /**
     * 大图片
     *
     * @return JigsawImgItem
     */
    public JigsawImgItem getBig() {
        return big;
    }

    /**
     * 大图片
     *
     * @param big JigsawImgItem
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawImg setBig(JigsawImgItem big) {
        this.big = big;
        return this;
    }

    /**
     * 小图片
     *
     * @return JigsawImgItem
     */
    public JigsawImgItem getSmall() {
        return small;
    }

    /**
     * 小图片
     *
     * @param small JigsawImgItem
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawImg setSmall(JigsawImgItem small) {
        this.small = small;
        return this;
    }

    /**
     * 最小值
     *
     * @return Number
     */
    public Number getMinValue() {
        return minValue;
    }

    /**
     * 最小值
     *
     * @param minValue Number
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawImg setMinValue(Number minValue) {
        this.minValue = minValue;
        return this;
    }

    /**
     * 最大值
     *
     * @return Number
     */
    public Number getMaxValue() {
        return maxValue;
    }

    /**
     * 最大值
     *
     * @param maxValue Number
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawImg setMaxValue(Number maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    /**
     * 滑动验证码错误信息
     *
     * @return JigsawImgResultError
     */
    public JigsawImgError getError() {
        return error;
    }

    /**
     * 滑动验证码错误信息
     *
     * @param error JigsawImgResultError
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawImg setError(JigsawImgError error) {
        this.error = error;
        return this;
    }

    /**
     * 标识字符串
     * @return String
     */
    public String getToken() {
        return token;
    }

    /**
     * 标识字符串
     * @param token String
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawImg setToken(String token) {
        this.token = token;
        return this;
    }
}
