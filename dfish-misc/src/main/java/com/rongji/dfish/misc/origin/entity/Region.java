package com.rongji.dfish.misc.origin.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 行政区划类
 */
public class Region {

    /**
     * 行政区划代码
     */
    @JSONField(name = "c")
    private String code;

    /**
     * 行政区划名称
     */
    @JSONField(name = "n")
    private String name;

    /**
     * 行政区划名称后缀
     */
    @JSONField(name = "t")
    private Integer tail;

    /**
     * 行政区划名称前缀
     */
    @JSONField(name = "p")
    private Integer prefix;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTail() {
        return tail;
    }

    public void setTail(Integer tail) {
        this.tail = tail;
    }

    public Integer getPrefix() {
        return prefix;
    }

    public void setPrefix(Integer prefix) {
        this.prefix = prefix;
    }
}
