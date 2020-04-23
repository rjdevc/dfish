package com.rongji.dfish.base.img;

import java.awt.*;

/**
 * 图片处理定义
 * @author lamontYu
 * @since DFish5.0
 */
public class ImageProcessConfig {

    public static final String WAY_ZOOM = "zoom";
    public static final String WAY_CUT = "cut";
    public static final String WAY_RESIZE = "resize";
    /**
     * 图片宽度
     */
    private int width;
    /**
     * 图片高度
     */
    private int height;
    /**
     * 图片处理方式
     */
    private String way = WAY_ZOOM;
    /**
     * 文件别名(有name时可以不需要alias,但推荐别名方式,按照一定规则命名)
     */
    private String alias;
    /**
     * 文件名称(有name时可以不需要alias,但推荐别名方式,按照一定规则命名)
     */
    private String name;
    /**
     * 是否可延迟执行
     */
    private boolean lazy = true;
    /**
     * 边距填充颜色
     */
    private Color paddingColor;
    /**
     * 水印设置
     */
    private ImageWatermarkConfig watermark;
    /**
     * 标记基准点
     */
    private boolean markPoint;

    public ImageProcessConfig() {
    }

    public ImageProcessConfig(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ImageProcessConfig(int width, int height, String way) {
        this.width = width;
        this.height = height;
        this.way = way;
    }

    public ImageProcessConfig(int width, int height, String way, String alias) {
        this.width = width;
        this.height = height;
        this.way = way;
        this.alias = alias;
    }

    public ImageProcessConfig(int width, int height, String way, String alias, boolean lazy) {
        this.width = width;
        this.height = height;
        this.way = way;
        this.alias = alias;
        this.lazy = lazy;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public Color getPaddingColor() {
        return paddingColor;
    }

    public void setPaddingColor(Color paddingColor) {
        this.paddingColor = paddingColor;
    }

    public ImageWatermarkConfig getWatermark() {
        return watermark;
    }

    public void setWatermark(ImageWatermarkConfig watermark) {
        this.watermark = watermark;
    }

    public boolean isMarkPoint() {
        return markPoint;
    }

    public void setMarkPoint(boolean markPoint) {
        this.markPoint = markPoint;
    }
}
