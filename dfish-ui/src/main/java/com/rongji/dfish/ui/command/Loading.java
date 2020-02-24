package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.*;

/**
 * 显示一个"请稍候"的信息窗
 *
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 3.0
 */
public class Loading extends AbstractDialog<Loading> implements Command<Loading>, HasText<Loading> {

    private static final long serialVersionUID = 2229794408494180794L;

    private String text;
//    private Boolean hide;

    /**
     * 构造函数
     */
    public Loading() {
    }

    /**
     * 构造函数
     *
     * @param text 提示文本
     */
    public Loading(String text) {
        this.text = text;
    }

    /**
     * 显示加载的文本
     *
     * @return String
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * 显示加载的文本
     *
     * @param text String
     * @return this
     */
    @Override
    public Loading setText(String text) {
        this.text = text;
        return this;
    }

//    /**
//     * 关闭loading提示
//     *
//     * @return Boolean
//     */
//    public Boolean getHide() {
//        return hide;
//    }
//
//    /**
//     * 关闭loading提示
//     *
//     * @param hide Boolean
//     * @return this
//     */
//    public Loading setHide(Boolean hide) {
//        this.hide = hide;
//        return this;
//    }

}
