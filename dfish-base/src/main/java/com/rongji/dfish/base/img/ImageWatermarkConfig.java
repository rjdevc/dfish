package com.rongji.dfish.base.img;

import java.awt.*;
import java.io.File;

/**
 * 图片处理水印配置
 *
 * @author lamontYu
 * @since DFish5.0
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
     * 文本水印内容
     *
     * @return String 文本水印内容
     */
    public String getText() {
        return text;
    }

    /**
     * 文本水印内容
     *
     * @param text 文本水印内容
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 文本水印字体
     *
     * @return Font 文本水印字体
     */
    public Font getTextFont() {
        return textFont;
    }

    /**
     * 文本水印字体
     *
     * @param textFont 文本水印字体
     */
    public void setTextFont(Font textFont) {
        this.textFont = textFont;
    }

    /**
     * 文本水印颜色
     *
     * @return textColor 文本水印颜色
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * 文本水印颜色
     *
     * @param textColor 文本水印颜色
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    /**
     * 图片文件
     *
     * @return File
     */
    public File getImageFile() {
        return imageFile;
    }

    /**
     * 图片文件
     *
     * @param imageFile 图片文件
     */
    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * 文件相对路径
     *
     * @return String
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * 文件相对路径
     *
     * @param imagePath String
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * 图片透明度
     *
     * @return float
     */
    public float getImageAlpha() {
        return imageAlpha;
    }

    /**
     * 图片透明度
     *
     * @param imageAlpha float 图片透明度
     */
    public void setImageAlpha(float imageAlpha) {
        this.imageAlpha = imageAlpha;
    }

    /**
     * 位置-x
     *
     * @return int 水印横坐标位置
     */
    public int getX() {
        return x;
    }

    /**
     * 位置-x
     *
     * @param x int 水印横坐标位置
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * 位置-y
     *
     * @return int 水印纵坐标位置
     */
    public int getY() {
        return y;
    }

    /**
     * 位置-y
     *
     * @param y int 水印纵坐标位置
     */
    public void setY(int y) {
        this.y = y;
    }
}
