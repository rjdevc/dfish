package com.rongji.dfish.misc;

import java.io.Serializable;

public class MapPosition implements Serializable {

    public MapPosition() {

    }

    public MapPosition(double lng, double lat, String address) {
        this.lng=lng;
        this.lat=lat;
        this.address=address;
    }

    public MapPosition(double lng, double lat, int zoom, String address) {
        this.lng=lng;
        this.lat=lat;
        this.zoom=zoom;
        this.address=address;
    }

    private double lng;
    private double lat;
    private int zoom;
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
