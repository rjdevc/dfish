package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.HtmlContentHolder;
import com.rongji.dfish.ui.MultiNodeContainer;
import com.rongji.dfish.ui.PubHolder;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.layout.AbstractNodeContainer;
import com.rongji.dfish.ui.layout.ListView;

import java.util.List;

/**
 * 图片平铺
 *
 * @author DFish Team
 */
public class Album extends AbstractNodeContainer<Album> implements HtmlContentHolder<Album>, Scrollable<Album>, ListView<Album>, PubHolder<Album, Img>,
        MultiNodeContainer<Album, Img> {

    private static final long serialVersionUID = 7141941441960631331L;
    private Integer space;
    private Boolean focusMultiple;
    private Img pub;
    private Boolean scroll;
    private String face;
    private Boolean noBr;


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


    @Deprecated
    public Album setFocusable(Boolean focusable) {
        getPub().setFocusable(focusable);
        return this;
    }

    @Override
    public Boolean getFocusMultiple() {
        return focusMultiple;
    }

    @Override
    public Album setFocusMultiple(Boolean focusMultiple) {
        this.focusMultiple = focusMultiple;
        return this;
    }

    @Override
    @Deprecated
    public Boolean getHoverable() {
        return null;
    }

    @Override
    @Deprecated
    public Album setHoverable(Boolean hoverable) {
//		this.hoverable = hoverable;
        return this;
    }

    /**
     * 子节点的默认配置项
     *
     * @return pub
     */
    @Override
    public Img getPub() {
        if (pub == null) {
            pub = new Img(null);
        }
        return pub;
    }

    /**
     * 子节点的默认配置项
     *
     * @param pub 默认配置项
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Album setPub(Img pub) {
        this.pub = pub;
        return this;
    }

    /**
     * 是否对html内容转义
     *
     * @return escape
     * @see #getPub()
     */
    @Override
    @Deprecated
    public Boolean getEscape() {
        return getPub().getEscape();
    }

    /**
     * 是否对html内容转义
     *
     * @param escape Boolean
     * @return 本身，这样可以继续设置其他属性
     * @see #getPub()
     */
    @Override
    @Deprecated
    public Album setEscape(Boolean escape) {
        getPub().setEscape(escape);
        return this;
    }

    @Override
    public List<Img> getNodes() {
        return (List) nodes;
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


    @Override
    public Album setNoBr(Boolean noBr) {
        this.noBr = noBr;
        return this;
    }

    @Override
    public Boolean getNoBr() {
        return noBr;
    }

    /**
     * 在指定的位置添加子面板
     *
     * @param index 位置
     * @param img   Img
     * @return 本身，这样可以继续设置其他属性
     */
    public Album add(int index, Img img) {
        if (img == null) {
            return this;
        }
        if (index < 0) {
            nodes.add(img);
        } else {
            nodes.add(index, img);
        }
        return this;
    }
}
