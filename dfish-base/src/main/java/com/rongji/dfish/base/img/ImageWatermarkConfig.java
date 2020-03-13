package com.rongji.dfish.base.img;

import java.awt.*;
import java.io.File;

/**
 * 图片处理水印配置
 * @author lamontYu
 * @date 2019-12-18
 * @since 5.0
 */
public class ImageWatermarkConfig {

    private String text;
    private Font textFont;
    private Color textColor;
    private File imageFile;
    private String imagePath;
    private float imageAlpha = 1.0f;
    private int x;
    private int y;

    /**
     * 水印文本
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     *  水印文本
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 字体
     * @return Font
     */
    public Font getTextFont() {
        return textFont;
    }

    /**
     * 字体
     * @param textFont Font
     */
    public void setTextFont(Font textFont) {
        this.textFont = textFont;
    }

    /**
     * 文字颜色
     * @return textColor
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * 文字颜色
     * @param textColor
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    /**
     * 图片文件
     * @return File
     */
    public File getImageFile() {
        return imageFile;
    }

    /**
     * 图片文件
     * @param  File
     */
    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * 文件相对路径
     * @return String
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * 文件相对路径
     * @param imagePath String
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     *图片透明度
     * @return float
     */
    public float getImageAlpha() {
        return imageAlpha;
    }

    /**
     * 图片透明度
     *
     * @param imageAlpha float
     */
    public void setImageAlpha(float imageAlpha) {
        this.imageAlpha = imageAlpha;
    }

    /**
     * 位置-x
     * @return int
     */
    public int getX() {
        return x;
    }

    /**
     * 位置-x
     * @param x int
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * 位置-y
     * @return  int
     */
    public int getY() {
        return y;
    }

    /**
     * 位置-y
     * @param y int
     */
    public void setY(int y) {
        this.y = y;
    }
}
