package com.rongji.dfish.misc.origin.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 县级以下下属行政区划信息
 */
public class Detail {

    /**
     * 上级行政区划(县级及县级以上)代码前6位
     */
    @JSONField(name = "topcode")
    private String topCode;

    /**
     * 下属行政区划(县级以下)集合
     */
    private List<Region> subs;

    public String getTopCode() {
        return topCode;
    }

    public void setTopCode(String topCode) {
        this.topCode = topCode;
    }

    public List<Region> getSubs() {
        return subs;
    }

    public void setSubs(List<Region> subs) {
        this.subs = subs;
    }

}
