package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.auxiliary.JigsawAuth;
import com.rongji.dfish.ui.auxiliary.JigsawImg;

/**
 * 拼图(滑动验证码)
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class Jigsaw extends AbstractFormElement<Jigsaw, String> {

    private JigsawImg img;
    private JigsawAuth auth;
    private String placeholder;
    private Boolean transparent;

    /**
     * 构造函数
     *
     * @param name 名称
     * @param label 标签
     */
    public Jigsaw(String name, String label) {
        this.setName(name);
        this.setLabel(label);
    }

    /**
     * 构造函数
     *
     * @param name  名称
     * @param label 标签
     */
    public Jigsaw(String name, Label label) {
        this.setName(name);
        this.setLabel(label);
    }

    /**
     * 拼图图片
     *
     * @return JigsawImg 拼图图片
     */
    public JigsawImg getImg() {
        return img;
    }

    /**
     * 拼图图片
     *
     * @param img JigsawImg 拼图图片
     * @return 本身，这样可以继续设置其他属性
     */
    public Jigsaw setImg(JigsawImg img) {
        this.img = img;
        return this;
    }

    /**
     * 拼图验证
     *
     * @return JigsawAuth 拼图验证
     */
    public JigsawAuth getAuth() {
        return auth;
    }

    /**
     * 拼图验证
     *
     * @param auth JigsawAuth 拼图验证
     * @return 本身，这样可以继续设置其他属性
     */
    public Jigsaw setAuth(JigsawAuth auth) {
        this.auth = auth;
        return this;
    }

    /**
     * 当表单没有值时显示的提示文本。
     *
     * @return String
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * 当表单没有值时显示的提示文本。
     *
     * @param placeholder String
     * @return 本身，这样可以继续设置其他属性
     */
    public Jigsaw setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    /**
     * 设置为true，表单将成为无边框无背景的状态。
     *
     * @return Boolean
     */
    public Boolean getTransparent() {
        return transparent;
    }

    /**
     * 设置为true，表单将成为无边框无背景的状态。
     *
     * @param transparent Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Jigsaw setTransparent(Boolean transparent) {
        this.transparent = transparent;
        return this;
    }

    @Override
    public Jigsaw setValue(Object value) {
        throw new UnsupportedOperationException("value must be null.");
    }


}
