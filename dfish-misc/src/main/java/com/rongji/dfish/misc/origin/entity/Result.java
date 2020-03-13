package com.rongji.dfish.misc.origin.entity;

import java.util.List;

/**
 * Json解析格式
 */
public class Result {
    /**
     * 行政区划名称后缀集合
     */
    private List<String> tails;

    /**
     * 行政区划名称前缀集合
     */
    private List<String> pres;

    /**
     * 县级及以上行政区划集合
     */
    private List<Region> tops;

    /**
     * 县级以下下属行政区划信息集合
     */
    private List<Detail> details;

    /**
     * 获得行政区划名称后缀集合
     * @return
     */
    public List<String> getTails() {
        return tails;
    }

    /**
     * 行政区划名称后缀集合
     * @param tails
     */
    public void setTails(List<String> tails) {
        this.tails = tails;
    }

    /**
     * 获得行政区划名称前缀集合
     * @return
     */
    public List<String> getPres() {
        return pres;
    }

    /**
     * 行政区划名称前缀集合
     * @param pres
     */
    public void setPres(List<String> pres) {
        this.pres = pres;
    }

    /**
     * 获得县级及以上行政区划集合
     * @return
     */
    public List<Region> getTops() {
        return tops;
    }

    /**
     * 县级及以上行政区划集合
     * @param tops
     */
    public void setTops(List<Region> tops) {
        this.tops = tops;
    }

    /**
     * 获得县级以下下属行政区划信息集合
     * @return
     */
    public List<Detail> getDetails() {
        return details;
    }

    /**
     * 县级以下下属行政区划信息集合
     * @param details
     */
    public void setDetails(List<Detail> details) {
        this.details = details;
    }
}
