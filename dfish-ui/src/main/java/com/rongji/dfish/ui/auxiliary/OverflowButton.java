package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.widget.AbstractButton;

/**
 * 按钮栏空间不够现实足够多按钮的时候的效果。
 *
 * @author DFish team
 */
public class OverflowButton extends AbstractButton<OverflowButton> {

    private static final long serialVersionUID = 8592484788559422098L;
    /**
     * 效果-默认
     */
    public static final String EFFECT_NORMAL = "normal";
    /**
     * 效果:有个折叠按钮，点击被折叠的按钮，被点击的按钮将会被交换到未被折叠的按钮的最后一个。
     */
    public static final String EFFECT_SWAP = "swap";

    private String effect;

    /**
     * 构造函数
     * @param text 文本
     */
    public OverflowButton(String text) {
        super(text, null, null);
    }

    /**
     * 构造函数
     * @param text 文本
     * @param onClick 点击动作
     * @param icon 图标
     */
    public OverflowButton(String text, String onClick, String icon) {
        super(text, onClick, icon);
    }

    /**
     * 效果
     *
     * @return String
     * @see #EFFECT_SWAP
     */
    public String getEffect() {
        return effect;
    }

    /**
     * 效果
     *
     * @param effect String
     * @return this
     */
    public OverflowButton setEffect(String effect) {
        this.effect = effect;
        return this;
    }

}
