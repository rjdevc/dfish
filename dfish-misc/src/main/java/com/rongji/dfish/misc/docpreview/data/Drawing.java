package com.rongji.dfish.misc.docpreview.data;

/**
 * 图片 图片是段落的一种
 */
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

    /**
     * 图片的建议显示大小。通常并不是真实大小
     * @return Integer
     */
    public Integer getHintWidth() {
        return hintWidth;
    }

    /**
     * 图片的建议显示大小。通常并不是真实大小
     * @param hintWidth Integer
     */
    public void setHintWidth(Integer hintWidth) {
        this.hintWidth = hintWidth;
    }

    /**
     * 图片的建议显示大小。通常并不是真实大小
     * @return Integer
     */
    public Integer getHintHeight() {
        return hintHeight;
    }

    /**
     * 图片的建议显示大小。通常并不是真实大小
     * @param hintHeight Integer
     */
    public void setHintHeight(Integer hintHeight) {
        this.hintHeight = hintHeight;
    }

    /**
     * 图片相对路径
     * @return String
     */
    public String getPicPath() {
        return picPath;
    }

    /**
     * 图片相对路径
     * @param picPath String
     */
    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    /**
     * 图片宽度
     * @return Integer
     */
    public Integer getPicWidth() {
        return picWidth;
    }

    /**
     * 图片宽度
     * @param picWidth Integer
     */
    public void setPicWidth(Integer picWidth) {
        this.picWidth = picWidth;
    }

    /**
     * 图片高度
     * @return Integer
     */
    public Integer getPicHeight() {
        return picHeight;
    }

    /**
     * 图片高度
     * @param picHeight Integer
     */
    public void setPicHeight(Integer picHeight) {
        this.picHeight = picHeight;
    }

    @Override
    public String getType() {
        return TYPE_DRAWING;
    }
}
