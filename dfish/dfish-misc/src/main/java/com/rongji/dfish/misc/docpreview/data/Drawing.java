package com.rongji.dfish.misc.docpreview.data;

public class Drawing implements ParagraphElement {

    // 图片路径
    private String picPath;

    // 图片宽度
    private Integer picWidth;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public Integer getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(Integer picWidth) {
        this.picWidth = picWidth;
    }

    public Integer getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(Integer picHeight) {
        this.picHeight = picHeight;
    }

    // 图片高度
    private int picHeight;

    @Override
    public String getType() {
        return TYPE_DRAWING;
    }
}
