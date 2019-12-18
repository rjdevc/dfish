package com.rongji.dfish.base.img;

import java.awt.*;

/**
 * 图片处理定义
 * @author lamontYu
 * @create 2019-12-16
 * @since 5.0
 */
public class ImageHandler {
    /**
     * 图片宽度
     */
    protected int width;
    /**
     * 图片高度
     */
    protected int height;
    /**
     * 图片处理方式
     */
    protected ImageHandleWay way = ImageHandleWay.ZOOM;
    /**
     * 文件别名(有name时可以不需要alias,但推荐别名方式,按照一定规则命名)
     */
    protected String alias;
    /**
     * 文件名称(有name时可以不需要alias,但推荐别名方式,按照一定规则命名)
     */
    protected String name;
    /**
     * 是否可延迟执行
     */
    protected boolean lazy = true;
    /**
     * 边距填充颜色
     */
    protected Color paddingColor;

    public ImageHandler() {
    }

    public ImageHandler(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ImageHandler(int width, int height, ImageHandleWay way) {
        this.width = width;
        this.height = height;
        this.way = way;
    }

    public ImageHandler(int width, int height, ImageHandleWay way, String alias) {
        this.width = width;
        this.height = height;
        this.way = way;
        this.alias = alias;
    }

    public ImageHandler(int width, int height, ImageHandleWay way, String alias, boolean lazy) {
        this.width = width;
        this.height = height;
        this.way = way;
        this.alias = alias;
        this.lazy = lazy;
    }

    public ImageHandleWay getWay() {
        return way;
    }

    public ImageHandler setWay(ImageHandleWay way) {
        this.way = way;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public ImageHandler setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public ImageHandler setHeight(int height) {
        this.height = height;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public ImageHandler setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getName() {
        return name;
    }

    public ImageHandler setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isLazy() {
        return lazy;
    }

    public ImageHandler setLazy(boolean lazy) {
        this.lazy = lazy;
        return this;
    }

    public Color getPaddingColor() {
        return paddingColor;
    }

    public ImageHandler setPaddingColor(Color paddingColor) {
        this.paddingColor = paddingColor;
        return this;
    }
}
