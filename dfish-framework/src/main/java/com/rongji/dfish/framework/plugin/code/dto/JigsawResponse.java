package com.rongji.dfish.framework.plugin.code.dto;

import java.io.Serializable;

/**
 * @author lamontYu
 * @create 2019-12-10
 */
public class JigsawResponse implements Serializable {
    private static final long serialVersionUID = -2076497536245307557L;

    /**
     * 大图片
     */
    private JigsawImg big;
    /**
     * 小图片
     */
    private JigsawImg small;
    /**
     * 最小值
     */
    private Number minvalue;
    /**
     * 最大值
     */
    private Number maxvalue;

    private JigsawResponseError error;

    public JigsawImg getBig() {
        return big;
    }

    public JigsawResponse setBig(JigsawImg big) {
        this.big = big;
        return this;
    }

    public JigsawImg getSmall() {
        return small;
    }

    public JigsawResponse setSmall(JigsawImg small) {
        this.small = small;
        return this;
    }

    public Number getMinvalue() {
        return minvalue;
    }

    public JigsawResponse setMinvalue(Number minvalue) {
        this.minvalue = minvalue;
        return this;
    }

    public Number getMaxvalue() {
        return maxvalue;
    }

    public JigsawResponse setMaxvalue(Number maxvalue) {
        this.maxvalue = maxvalue;
        return this;
    }

    public JigsawResponseError getError() {
        return error;
    }

    public JigsawResponse setError(JigsawResponseError error) {
        this.error = error;
        return this;
    }
}
