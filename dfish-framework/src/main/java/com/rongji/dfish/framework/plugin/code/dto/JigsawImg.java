package com.rongji.dfish.framework.plugin.code.dto;

import java.io.Serializable;

/**
 * @author lamontYu
 * @create 2019-12-10
 */
public class JigsawImg implements Serializable {
    private static final long serialVersionUID = -4474116526766117185L;

    private String src;
    private int width;
    private int height;

    public JigsawImg(String src, int width, int height) {
        this.src = src;
        this.width = width;
        this.height = height;
    }

    public String getSrc() {
        return src;
    }

    public JigsawImg setSrc(String src) {
        this.src = src;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public JigsawImg setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public JigsawImg setHeight(int height) {
        this.height = height;
        return this;
    }
}
