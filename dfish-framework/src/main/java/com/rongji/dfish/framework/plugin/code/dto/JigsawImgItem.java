package com.rongji.dfish.framework.plugin.code.dto;

import java.io.Serializable;

/**
 * 拼图的图片对象
 *
 * @author lamontYu
 * @date 2019-12-10
 * @since 5.0
 */
public class JigsawImgItem implements Serializable {
    private static final long serialVersionUID = -4474116526766117185L;

    private String src;
    private int width;
    private int height;

    /**
     * 构造函数
     *
     * @param src    图片地址
     * @param width  图片宽度
     * @param height 图片高度
     */
    public JigsawImgItem(String src, int width, int height) {
        this.src = src;
        this.width = width;
        this.height = height;
    }

    /**
     * 图片地址
     *
     * @return String
     */
    public String getSrc() {
        return src;
    }

    /**
     * 图片地址
     *
     * @param src String
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawImgItem setSrc(String src) {
        this.src = src;
        return this;
    }

    /**
     * 图片宽度
     *
     * @return String
     */
    public int getWidth() {
        return width;
    }

    /**
     * 图片宽度
     *
     * @param width int
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawImgItem setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * 图片高度
     *
     * @return int
     */
    public int getHeight() {
        return height;
    }

    /**
     * 图片高度
     *
     * @param height int
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawImgItem setHeight(int height) {
        this.height = height;
        return this;
    }
}
