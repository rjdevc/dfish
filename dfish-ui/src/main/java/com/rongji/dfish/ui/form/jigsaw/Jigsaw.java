package com.rongji.dfish.ui.form.jigsaw;

import com.rongji.dfish.ui.form.AbstractFormElement;

/**
 * 拼图(滑动验证码)
 * @author lamontYu
 * @create 2019-12-11
 * @since 5.0
 */
public class Jigsaw extends AbstractFormElement<Jigsaw, String> {

    private JigsawImg img;
    private JigsawAuth auth;

    public Jigsaw(String name, String label) {
        this.setName(name);
        this.setLabel(label);
    }

    /**
     * 拼图图片
     * @return JigsawImg
     */
    public JigsawImg getImg() {
        return img;
    }

    /**
     * 拼图图片
     * @param img JigsawImg
     * @return this
     */
    public Jigsaw setImg(JigsawImg img) {
        this.img = img;
        return this;
    }

    /**
     * 拼图验证
     * @return JigsawAuth
     */
    public JigsawAuth getAuth() {
        return auth;
    }

    /**
     * 拼图验证
     * @param auth JigsawAuth
     * @return this
     */
    public Jigsaw setAuth(JigsawAuth auth) {
        this.auth = auth;
        return this;
    }

    @Override
    public Jigsaw setValue(Object value) {
        throw new UnsupportedOperationException("value must be null.");
    }

    @Override
    public String getType() {
        return "jigsaw";
    }

}
