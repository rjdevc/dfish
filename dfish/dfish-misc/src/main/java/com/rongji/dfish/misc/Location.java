package com.rongji.dfish.misc;

import java.io.Serializable;


/**
 * 地理位置类,主要提供经纬度和位置等信息
 *
 * @author DFish Team
 * @version 1.0
 */
public class Location implements Serializable {

    public Location() {

    }

    public Location(double lng, double lat, String address) {
        this.lng = lng;
        this.lat = lat;
        this.address = address;
    }

    public Location(double lng, double lat, int zoom, String address) {
        this.lng = lng;
        this.lat = lat;
        this.zoom = zoom;
        this.address = address;
    }

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

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
