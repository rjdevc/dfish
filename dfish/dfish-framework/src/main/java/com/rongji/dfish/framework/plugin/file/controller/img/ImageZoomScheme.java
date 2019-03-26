package com.rongji.dfish.framework.plugin.file.controller.img;

import java.util.List;

/**
 * 图片缩放支持的模块
 */
public class ImageZoomScheme {

    private String name;

    private List<String> zooms;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getZooms() {
        return zooms;
    }

    public void setZooms(List<String> zooms) {
        this.zooms = zooms;
    }

}
