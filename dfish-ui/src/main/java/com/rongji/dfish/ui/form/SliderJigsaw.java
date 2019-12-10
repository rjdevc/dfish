package com.rongji.dfish.ui.form;

public class SliderJigsaw extends AbstractFormElement<SliderJigsaw, String> {

    private String imgsrc = "./jigsaw/img";
    private String authsrc = "./jigsaw/auth?offset=$value";

    public SliderJigsaw(String name, String label) {
        this.setName(name);
        this.setLabel(label);
    }

    @Override
    public SliderJigsaw setValue(Object value) {
        this.value = toString(value);
        return this;
    }

    @Override
    public String getType() {
        return "slider/jigsaw";
    }

    /**
     * 拼图的数据获取地址
     * @return String
     */
    public String getImgsrc() {
        return imgsrc;
    }

    /**
     * 设置拼图的数据获取地址
     * @param imgsrc 拼图的数据获取地址
     * @return 本身，这样可以继续设置属性
     */
    public SliderJigsaw setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
        return this;
    }

    /**
     * 验证拼图是否正确的地址
     * @return String
     */
    public String getAuthsrc() {
        return authsrc;
    }

    /**
     * 设置验证拼图是否正确的地址
     * @param authsrc 验证拼图是否正确的地址
     * @return 本身，这样可以继续设置属性
     */
    public SliderJigsaw setAuthsrc(String authsrc) {
        this.authsrc = authsrc;
        return this;
    }
}
