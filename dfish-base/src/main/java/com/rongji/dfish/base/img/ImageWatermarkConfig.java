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
    private float imageAlpha;
    private int x;
    private int y;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getTextFont() {
        return textFont;
    }

    public void setTextFont(Font textFont) {
        this.textFont = textFont;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public float getImageAlpha() {
        return imageAlpha;
    }

    public void setImageAlpha(float imageAlpha) {
        this.imageAlpha = imageAlpha;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
