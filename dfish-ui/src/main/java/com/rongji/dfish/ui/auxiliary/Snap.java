package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.Positionable;

/**
 * 吸附对象
 */
public class Snap implements Positionable<Snap> {
    /**
     * 吸附的对象
     */
    private String target;
    /**
     * 指定snap的位置
     */
    private String position;
    /**
     * 是否在吸附对象里面，默认值为false
     */
    private Boolean inner;
    /**
     * 指定相对于初始位置缩进多少个像素
     */
    private Integer indent;


    /**
     * 吸附的对象
     * @return
     */
    public String getTarget() {
        return target;
    }

    /**
     * 设置吸附的对象
     * @param target
     * @return 本身，这样可以继续设置其他属性
     */
    public Snap setTarget(String target) {
        this.target = target;
        return this;
    }

    /**
     * 指定snap的位置
     * @param position 位置
     * @return
     */
    @Override
    public Snap setPosition(String position) {
        this.position = position;
        return this;
    }


    /**
     * 获得指定snap的位置
     * @return snap的位置
     */
    @Override
    public String getPosition() {
        return position;
    }


    /**
     * 是否在吸附对象里面，默认值为false
     * @return
     */
    public Boolean getInner() {
        return inner;
    }

    /**
     * 是否在吸附对象里面，默认值为false
     * @param inner
     * @return 本身，这样可以继续设置其他属性
     */
    public Snap setInner(Boolean inner) {
        this.inner = inner;
        return this;
    }

    /**
     * 指定相对于初始位置缩进多少个像素
     * @return
     */
    public Integer getIndent() {
        return indent;
    }

    /**
     * 指定相对于初始位置缩进多少个像素
     * @param indent
     * @return 本身，这样可以继续设置其他属性
     */
    public Snap setIndent(Integer indent) {
        this.indent = indent;
        return this;
    }

}
