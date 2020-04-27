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
     * @return String 行政区划代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 行政区划代码
     * @param code 行政区划代码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获得行政区划名称
     * @return String 行政区划名称
     */
    public String getName() {
        return name;
    }

    /**
     * 行政区划名称
     * @param name 行政区划名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获得行政区划名称后缀
     * @return Integer 行政区划名称后缀
     */
    public Integer getTail() {
        return tail;
    }

    /**
     * 行政区划名称后缀
     * @param tail 行政区划名称后缀
     */
    public void setTail(Integer tail) {
        this.tail = tail;
    }

    /**
     * 获得行政区划名称前缀
     * @return Integer 行政区划名称前缀
     */
    public Integer getPrefix() {
        return prefix;
    }

    /**
     * 行政区划名称前缀
     * @param prefix 行政区划名称前缀
     */
    public void setPrefix(Integer prefix) {
        this.prefix = prefix;
    }
}
