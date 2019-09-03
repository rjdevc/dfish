package com.rongji.dfish.misc.docpreview.data;

public class Drawing implements ParagraphElement {

    // 图片路径
    private String picPath;

    // 图片宽度
    private Integer picWidth;
    // 图片高度
    private Integer picHeight;

    // 图片宽度
    private Integer hintWidth;

    // 图片高度

    private Integer hintHeight;

    public Integer getHintWidth() {
        return hintWidth;
    }

    public void setHintWidth(Integer hintWidth) {
        this.hintWidth = hintWidth;
    }

    public Integer getHintHeight() {
        return hintHeight;
    }

    public void setHintHeight(Integer hintHeight) {
        this.hintHeight = hintHeight;
    }

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

    @Override
    public String getType() {
        return TYPE_DRAWING;
    }
}
