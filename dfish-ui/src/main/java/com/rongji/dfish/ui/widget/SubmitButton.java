package com.rongji.dfish.ui.widget;

/**
 * 默认提交按钮。在 text 等表单上按回车，将触发此按钮的点击事件。
 *
 * @author DFish Team
 */
public class SubmitButton extends AbstractButton<SubmitButton> {

    private static final long serialVersionUID = 5355751716469309761L;

    /**
     * 构造函数
     *
     * @param icon    String 图标
     * @param text    String 标题
     * @param onclick String 所触发的动作(JS)
     */
    public SubmitButton( String text, String onclick,String icon) {
        super(text,onclick,icon);
    }

    /**
     * 构造函数
     *
     * @param text String 图标
     * @param onclick String 标题
     */
    public SubmitButton(String text, String onclick) {
        super(text,onclick,null);
    }

    /**
     * 构造函数
     *
     * @param text String 标题
     */
    public SubmitButton(String text) {
        super(text,null,null);
    }

}
