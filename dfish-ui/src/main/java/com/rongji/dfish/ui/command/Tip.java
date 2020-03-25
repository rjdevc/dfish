package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.*;

/**
 * 提示信息
 *
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 3.0
 */
public class Tip extends AbstractPopup<Tip> implements Command<Tip>, HasText<Tip> {

    private static final long serialVersionUID = -3534531697064109684L;

    public static final String FACE_NORMAL = "normal";
    public static final String FACE_WARN = "warn";

    private String face;
    private String text;
    private Boolean closable;

    /**
     * 样式效果
     *
     * @return String
     * @see #FACE_NORMAL
     * @see #FACE_WARN
     */
    public String getFace() {
        return face;
    }

    /**
     * 样式效果
     *
     * @param face String
     * @return 本身，这样可以继续设置其他属性
     * @see #FACE_NORMAL
     * @see #FACE_WARN
     */
    public Tip setFace(String face) {
        this.face = face;
        return this;
    }

    /**
     * 构造函数
     *
     * @param text 提示文本
     */
    public Tip(String text) {
        this.text = text;
    }

    /**
     * 内容
     *
     * @return String
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * 设置内容
     *
     * @param text 内容
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Tip setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 是否显示关闭图标
     *
     * @return Boolean
     */
    public Boolean getClosable() {
        return closable;
    }

    /**
     * 是否显示关闭图标
     *
     * @param closable
     * @return Boolean
     */
    public Tip setClosable(Boolean closable) {
        this.closable = closable;
        return this;
    }
//    /**
//     * 是否允许多个实例存在。
//     * @return Boolean
//     */
//    public Boolean getMultiple() {
//        return multiple;
//    }
//
//    /**
//     * 是否允许多个实例存在。
//     * @param multiple Boolean
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public Tip setMultiple(Boolean multiple) {
//        this.multiple = multiple;
//        return this;
//    }
}
