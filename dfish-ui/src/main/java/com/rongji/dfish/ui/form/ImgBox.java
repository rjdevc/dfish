package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.command.Tip;

import java.util.List;


/**
 * Imgbox 图片下拉选择表单
 *
 * @author DFish Team
 * @version 1.2
 * @since 1.0
 */
public class ImgBox extends AbstractOptionContainer<ImgBox> {

    private static final long serialVersionUID = 666601483514746346L;

    private Integer imgHeight;
    private Integer imgWidth;

    /**
     * 构造函数
     *
     * @param name    表单元素名
     * @param label   标题
     * @param value   选中的值
     * @param options 候选项
     */
    public ImgBox(String name, String label, Object value, List<?> options) {
        super(name, label, value, options);
    }

    /**
     * 构造函数
     *
     * @param name    表单元素名
     * @param label   标题
     * @param value   选中的值
     * @param options 候选项
     */
    public ImgBox(String name, Label label, Object value, List<?> options) {
        super(name, label, value, options);
    }

    /**
     * 图标高度
     *
     * @return Integer
     */
    public Integer getImgHeight() {
        return imgHeight;
    }

    /**
     * 设置图标高度
     *
     * @param imgHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public ImgBox setImgHeight(Integer imgHeight) {
        this.imgHeight = imgHeight;
        return this;
    }

    /**
     * 图标宽度
     *
     * @return Integer
     */
    public Integer getImgWidth() {
        return imgWidth;
    }

    /**
     * 设置图标宽度
     *
     * @param imgWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public ImgBox setImgWidth(Integer imgWidth) {
        this.imgWidth = imgWidth;
        return this;
    }

    @Override
    @Deprecated
    public ImgBox setTip(Boolean tip) {
        return super.setTip(tip);
    }

    @Override
    @Deprecated
    public ImgBox setTip(String tip) {
        return super.setTip(tip);
    }

    @Override
    @Deprecated
    public ImgBox setTip(Tip tip) {
        return super.setTip(tip);
    }

}
