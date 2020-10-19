package com.rongji.dfish.base;

/**
 * 位置信息,主要用于存放位置的经纬度,位置,缩放比例等信息
 * @author lamontYu
 * @since 5.0
 */
public class Location {
    /**
     * 经度longitude
     */
    private double lng;
    /**
     * 纬度latitude
     */
    private double lat;
    /**
     * 具体地址信息
     */
    private String address;
    /**
     * 缩放比例
     */
    private int zoom;

    public Location() {
    }

    public Location(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public Location(double lng, double lat, String address) {
        this.lng = lng;
        this.lat = lat;
        this.address = address;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }
}
