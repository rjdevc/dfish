package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.*;

/**
 * 图片平铺
 *
 * @author DFish Team
 */
public class Album extends AbstractPubNodeContainer<Album, Img,Img> implements Scrollable<Album>  {

    private static final long serialVersionUID = 7141941441960631331L;
    private Integer space;
    private Boolean focusMultiple;
    private Boolean scroll;
    private String face;
    private Boolean br;


    /**
     * 皮肤-默认
     */
    public static final String FACE_NONE = "none";
    /**
     * 皮肤-平铺
     */
    public static final String FACE_STRAIGHT = "straight";

    /**
     * 构造函数
     *
     * @param id String
     */
    public Album(String id) {
        super(id);
    }

    /**
     * 构造函数
     */
    public Album() {
        super(null);
    }

    @Override
    protected Img newPub() {
        return new Img(null);
    }

    @Override
    public Album setScroll(Boolean scroll) {
        this.scroll = scroll;
        return this;
    }

    @Override
    public Boolean getScroll() {
        return scroll;
    }

    /**
     * 图片之间的间隔。
     *
     * @return space
     */
    public Integer getSpace() {
        return space;
    }

    /**
     * 图片之间的间隔。
     *
     * @param space 间距(像素)
     * @return 本身，这样可以继续设置其他属性
     */
    public Album setSpace(Integer space) {
        this.space = space;
        return this;
    }

    /**
     * 是否有多个按钮可同时设为焦点状态。
     * @return
     */
    public Boolean getFocusMultiple() {
        return focusMultiple;
    }

    /**
     * 是否有多个按钮可同时设为焦点状态。
     * @param focusMultiple
     * @return 本身，这样可以继续设置其他属性
     */
    public Album setFocusMultiple(Boolean focusMultiple) {
        this.focusMultiple = focusMultiple;
        return this;
    }

    /**
     * 图片皮肤
     *
     * @return face 皮肤
     */
    public String getFace() {
        return face;
    }

    /**
     * 图片皮肤
     *
     * @param face 图片皮肤
     * @return 本身，这样可以继续设置其他属性
     */
    public Album setFace(String face) {
        this.face = face;
        return this;
    }


}
