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

    /**
     * 获得行政区划代码
     * @return String
     */
    public String getCode() {
        return code;
    }

    /**
     * 行政区划代码
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获得行政区划名称
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * 行政区划名称
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获得行政区划名称后缀
     * @return Integer
     */
    public Integer getTail() {
        return tail;
    }

    /**
     * 行政区划名称后缀
     * @param tail
     */
    public void setTail(Integer tail) {
        this.tail = tail;
    }

    /**
     * 获得行政区划名称前缀
     * @return Integer
     */
    public Integer getPrefix() {
        return prefix;
    }

    /**
     * 行政区划名称前缀
     * @param prefix
     */
    public void setPrefix(Integer prefix) {
        this.prefix = prefix;
    }
}
