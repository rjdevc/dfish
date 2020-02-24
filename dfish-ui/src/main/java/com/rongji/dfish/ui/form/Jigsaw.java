package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.AbstractResultingNode;

/**
 * 拼图(滑动验证码)
 * @author lamontYu
 * @date 2019-12-11
 * @since 5.0
 */
public class Jigsaw extends AbstractFormElement<Jigsaw, String> {

    private Img img;
    private Auth auth;
    private String placeholder;
    private Boolean transparent;

    public Jigsaw(String name, String label) {
        this.setName(name);
        this.setLabel(label);
    }

    /**
     * 拼图图片
     * @return JigsawImg
     */
    public Img getImg() {
        return img;
    }

    /**
     * 拼图图片
     * @param img JigsawImg
     * @return 本身，这样可以继续设置其他属性
     */
    public Jigsaw setImg(Img img) {
        this.img = img;
        return this;
    }

    /**
     * 拼图验证
     * @return JigsawAuth
     */
    public Auth getAuth() {
        return auth;
    }

    /**
     * 拼图验证
     * @param auth JigsawAuth
     * @return 本身，这样可以继续设置其他属性
     */
    public Jigsaw setAuth(Auth auth) {
        this.auth = auth;
        return this;
    }

    /**
     * 当表单没有值时显示的提示文本。
     * @return String
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * 当表单没有值时显示的提示文本。
     * @param placeholder String
     * @return 本身，这样可以继续设置其他属性
     */
    public Jigsaw setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    /**
     * 设置为true，表单将成为无边框无背景的状态。
     * @return Boolean
     */
    public Boolean getTransparent() {
        return transparent;
    }

    /**
     * 设置为true，表单将成为无边框无背景的状态。
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

    /**
     * 拼图验证
     * @author lamontYu
     * @date 2019-12-11
     * @since 5.0
     */
    public static class Auth extends AbstractResultingNode<Auth> {

        @Override
        public String getType() {
            return "JigsawAuth";
        }

    }

    /**
     * 拼图图片
     * @author lamontYu
     * @date 2019-12-11
     * @since 5.0
     */
    public static class Img extends AbstractResultingNode<Img> {

        @Override
        public String getType() {
            return "JigsawImg";
        }

    }

}
