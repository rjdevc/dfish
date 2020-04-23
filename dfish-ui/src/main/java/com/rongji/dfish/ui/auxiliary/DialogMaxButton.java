package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.widget.AbstractButton;

/**
 * 对话框最大化按钮
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class DialogMaxButton extends AbstractButton<DialogMaxButton> {

    private static final long serialVersionUID = 2042161439344881725L;

    /**
     * 构造函数
     *
     * @param icon    String 图标
     * @param text    String 标题
     * @param onclick String 所触发的动作(JS)
     */
    public DialogMaxButton(String text, String onclick, String icon) {
        super(text, onclick, icon);
    }

    /**
     * 构造函数
     *
     * @param text    String 图标
     * @param onclick String 标题
     */
    public DialogMaxButton(String text, String onclick) {
        super(text, onclick, null);
    }

    /**
     * 构造函数
     *
     * @param text String 标题
     */
    public DialogMaxButton(String text) {
        super(text, null, null);
    }

}
