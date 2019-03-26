package com.rongji.dfish.framework.plugin.file.controller.img;

import java.awt.*;

/**
 * 图片缩放配置
 */
public class ImageZoomDefine {

    public static final String WAY_ZOOM = "zoom";
    public static final String WAY_RESIZE = "resize";
    public static final String WAY_CUT = "cut";

    private String alias;
    private String way;
    private int width;
    private int height;
    private Color bgColor;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }
}
