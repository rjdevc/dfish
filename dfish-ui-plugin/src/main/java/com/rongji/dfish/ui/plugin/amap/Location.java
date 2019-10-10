package com.rongji.dfish.ui.plugin.amap;

import com.rongji.dfish.ui.AbstractNode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 地理位置类,主要提供经纬度和位置等信息
 *
 * @author DFish Team
 * @version 1.0
 */
public class Location extends AbstractNode<Location> {

    /**
     * 经度longitude
     */
    private double lng;
    /**
     * 纬度latitude
     */
    private double lat;
    /**
     * 缩放比例
     */
    private int zoom;
    /**
     * 具体地址信息
     */
    private String address;
    /**
     * 提示文本
     */
    private String tip;
    /**
     * 显示图标
     */
    private String icon;
    /**
     * 默认聚焦
     */
    private Boolean focus;


    public Location() {

    }

    public Location(double lng, double lat, String address) {
        this.lng = lng;
        this.lat = lat;
        this.address = address;
    }

    /**
     * 经度
     *
     * @return double
     */
    public double getLng() {
        return lng;
    }

    /**
     * 精度
     *
     * @param lng double
     * @return 本身，这样可以继续设置其他属性
     */
    public Location setLng(double lng) {
        this.lng = lng;
        return this;
    }

    /**
     * 纬度
     *
     * @return double
     */
    public double getLat() {
        return lat;
    }

    /**
     * 纬度
     *
     * @param lat double
     * @return 本身，这样可以继续设置其他属性
     */
    public Location setLat(double lat) {
        this.lat = lat;
        return this;
    }

    /**
     * 缩放比例
     *
     * @return int
     */
    public int getZoom() {
        return zoom;
    }

    /**
     * 缩放比例
     *
     * @param zoom int
     * @return 本身，这样可以继续设置其他属性
     */
    public Location setZoom(int zoom) {
        this.zoom = zoom;
        return this;
    }

    /**
     * 地址信息
     *
     * @return String
     */
    public String getAddress() {
        return address;
    }

    /**
     * 地址信息
     *
     * @param address String
     * @return 本身，这样可以继续设置其他属性
     */
    public Location setAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * 提示文本
     *
     * @return String
     */
    public String getTip() {
        return tip;
    }

    /**
     * 提示文本
     *
     * @param tip String
     * @return 本身，这样可以继续设置其他属性
     */
    public Location setTip(String tip) {
        this.tip = tip;
        return this;
    }

    @Override
    public String getType() {
        return null;
    }

    /**
     * 坐标点图标
     * @return String
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 坐标点图标
     * @param icon String
     * @return 本身，这样可以继续设置其他属性
     */
    public Location setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    /**
     * 是否聚焦
     * @return Booolean
     */
    public Boolean getFocus() {
        return focus;
    }

    /**
     * 是否聚焦
     * @param focus Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Location setFocus(Boolean focus) {
        this.focus = focus;
        return this;
    }
}
