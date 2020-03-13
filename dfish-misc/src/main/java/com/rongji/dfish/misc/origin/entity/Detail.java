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

    /**
     * 上级行政区划(县级及县级以上)代码前6位
     * @return String
     */
    public String getTopCode() {
        return topCode;
    }

    /**
     * 上级行政区划(县级及县级以上)代码前6位
     * @param topCode
     */
    public void setTopCode(String topCode) {
        this.topCode = topCode;
    }

    /**
     * 获取下属行政区划(县级以下)集合
     * @return
     */
    public List<Region> getSubs() {
        return subs;
    }

    /**
     * 下属行政区划(县级以下)集合
     * @param subs
     */
    public void setSubs(List<Region> subs) {
        this.subs = subs;
    }

}
