package com.rongji.dfish.ui.form;


import com.rongji.dfish.ui.command.Dialog;
import com.rongji.dfish.ui.command.Tip;

/**
 * <p>
 * Title: XML模板
 * </p>
 * <p>
 * Description: XML模板
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: RJ-SOFT
 * </p>
 * 有输入提示的输入框,可以选择,下拉,以及双击察看详细的功能
 *
 * @author DFish Team
 * @version 3.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2005
 * @since 1.0
 */
public class ComboBox extends LinkableSuggestionBox<ComboBox> {

    private static final long serialVersionUID = 3265703320204676156L;

    /**
     * 显示样式-默认
     */
    public static final String FACE_DEFAULT = "default";
    /**
     * 显示样式-标签
     */
    public static final String FACE_TAG = "tag";

    private String face;

    /**
     * 构造函数
     *
     * @param name    表单名
     * @param label   标题
     * @param value   初始值
     * @param suggest 候选项的URL
     */
    public ComboBox(String name, String label, String value, String suggest) {
        super(name, label, value, suggest);
    }

    /**
     * 构造函数
     *
     * @param name    表单名
     * @param label   标题
     * @param value   初始值
     * @param suggest 候选项的弹窗命令
     */
    public ComboBox(String name, Label label, String value, Dialog suggest) {
        super(name, label, value, suggest);
    }

    /**
     * 设置已选项的外观效果
     *
     * @return
     */
    public String getFace() {
        return face;
    }

    /**
     * 设置已选项的外观效果
     *
     * @param face
     * @return 本身，这样可以继续设置其他属性
     */
    public ComboBox setFace(String face) {
        this.face = face;
        return this;
    }

    @Override
    @Deprecated
    public ComboBox setTip(Boolean tip) {
        return this;
    }

    @Override
    @Deprecated
    public ComboBox setTip(String tip) {
        return this;
    }

    @Override
    @Deprecated
    public ComboBox setTip(Tip tip) {
        return this;
    }

}
