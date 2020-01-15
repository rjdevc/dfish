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
     * @return this
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
     * @return this
     */
    public Jigsaw setAuth(Auth auth) {
        this.auth = auth;
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
