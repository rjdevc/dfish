package com.rongji.dfish.framework.plugin.code.dto;

import java.io.Serializable;

/**
 * @author lamontYu
 * @date 2019-12-10
 */
public class JigsawImgResult implements Serializable {
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
    private Number minvalue;
    /**
     * 最大值
     */
    private Number maxvalue;

    private JigsawImgResultError error;

    public JigsawImgItem getBig() {
        return big;
    }

    public JigsawImgResult setBig(JigsawImgItem big) {
        this.big = big;
        return this;
    }

    public JigsawImgItem getSmall() {
        return small;
    }

    public JigsawImgResult setSmall(JigsawImgItem small) {
        this.small = small;
        return this;
    }

    public Number getMinvalue() {
        return minvalue;
    }

    public JigsawImgResult setMinvalue(Number minvalue) {
        this.minvalue = minvalue;
        return this;
    }

    public Number getMaxvalue() {
        return maxvalue;
    }

    public JigsawImgResult setMaxvalue(Number maxvalue) {
        this.maxvalue = maxvalue;
        return this;
    }

    public JigsawImgResultError getError() {
        return error;
    }

    public JigsawImgResult setError(JigsawImgResultError error) {
        this.error = error;
        return this;
    }
}
