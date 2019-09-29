package com.rongji.dfish.framework.plugin.file.controller.config;

import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingDefine;

import java.awt.*;

/**
 * 图片处理定义
 * @author lamontYu
 */
public class ImageHandlingDefine extends FileHandlingDefine {
    /**
     * 文件处理方式-等比例缩放(不够的地方留白)
     */
    public static final String WAY_ZOOM = "zoom";
    /**
     * 文件处理方式-按照指定宽高缩放(无裁剪,原图比例失真)
     */
    public static final String WAY_RESIZE = "resize";
    /**
     * 文件处理方式-裁剪(先等比例缩放到合适比例,将多余的部分裁剪)
     */
    public static final String WAY_CUT = "cut";

    /**
     * 文件处理方式
     */
    private String way;
    /**
     * 图片处理后的宽度
     */
    private int width;
    /**
     * 图片处理后的高度
     */
    private int height;
    /**
     * 图片处理后的背景颜色
     */
    private Color bgColor;

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
