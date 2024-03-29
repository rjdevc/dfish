package com.rongji.dfish.ui.widget;


import com.rongji.dfish.ui.Widget;

/**
 * ExpandableButton 可展开的按钮
 * <p>该按钮本身如果没有绑定命令的话，那么点该按钮就是展开动作。否则要点按钮后的箭头展开</p>
 *
 * @author DFish Team
 * @version 1.0
 * @since DFish2.0
 */
public class Button extends AbstractButton<Button> {

    private static final long serialVersionUID = -3521425601089259935L;

    private String target;

    /**
     * 构造函数
     *
     * @param icon    String 图标
     * @param text    String 标题
     * @param onclick String 所触发的动作(JS)
     */
    public Button(String text, String onclick, String icon) {
        super(text, onclick, icon);
    }

    /**
     * 构造函数
     *
     * @param text    String 图标
     * @param onclick String 标题
     */
    public Button(String text, String onclick) {
        super(text, onclick, null);
    }

    /**
     * 构造函数
     *
     * @param text String 标题
     */
    public Button(String text) {
        super(text, null, null);
    }


    /**
     * 指定一个 frame 内的 widget ID，使 button 的 focus 效果和绑定 widget 的显示隐藏效果。
     *
     * @return String
     */
    public String getTarget() {
        return target;
    }

    /**
     * 指定一个 frame 内的 widget ID，使 button 的 focus 效果和绑定 widget 的显示隐藏效果。
     *
     * @param target String
     * @return 本身，这样可以继续设置其他属性
     */
    public Button setTarget(String target) {
        this.target = target;
        return this;
    }

}
