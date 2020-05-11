package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractPubNodeContainer;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.auxiliary.StructureItem;

/**
 * 结构图组件
 *
 * @author lamontYu
 * @since 5.0
 */
public class Structure extends AbstractPubNodeContainer<Structure, StructureItem, StructureItem>
        implements Scrollable<Structure>, Alignable<Structure> {

    private String dir;
    private Integer hSpace;
    private Integer vSpace;
    private Boolean scroll;
    private String align;

    /**
     * 排列方式-横向
     */
    public static final String DIR_HORIZONTAL = "h";
    /**
     * 排列方式-纵向(默认)
     */
    public static final String DIR_VERTICAL = "v";

    /**
     * 构造函数
     */
    public Structure() {
        super();
    }

    /**
     * 构造函数
     *
     * @param id String
     */
    public Structure(String id) {
        super(id);
    }

    @Override
    protected StructureItem newPub() {
        return new StructureItem(null);
    }

    @Override
    public Structure setScroll(Boolean scroll) {
        this.scroll = scroll;
        return this;
    }

    @Override
    public Boolean getScroll() {
        return scroll;
    }

    /**
     * 排列方向。可选值: {@link #DIR_VERTICAL}(纵向,默认),{@link #DIR_HORIZONTAL}(横向)
     * @return String
     */
    public String getDir() {
        return dir;
    }

    /**
     * 排列方向。可选值: {@link #DIR_VERTICAL}(纵向,默认),{@link #DIR_HORIZONTAL}(横向)
     * @param dir String 排列方向
     * @return 本身，这样可以继续设置其他属性
     */
    public Structure setDir(String dir) {
        this.dir = dir;
        return this;
    }

    /**
     * 横向间距
     * @return Integer
     */
    public Integer getHSpace() {
        return hSpace;
    }

    /**
     * 横向间距
     * @param hSpace Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public Structure setHSpace(Integer hSpace) {
        this.hSpace = hSpace;
        return this;
    }

    /**
     * 纵向间距
     * @return Integer
     */
    public Integer getVSpace() {
        return vSpace;
    }

    /**
     * 纵向间距
     * @param vSpace Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public Structure setVSpace(Integer vSpace) {
        this.vSpace = vSpace;
        return this;
    }

    @Override
    public String getAlign() {
        return align;
    }

    @Override
    public Structure setAlign(String align) {
        this.align = align;
        return this;
    }
}
