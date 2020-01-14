package com.rongji.dfish.ui.command;

import java.util.List;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.widget.Button;

/**
 * 该命令用于打开一个警告框(Dialog)
 *
 * @author DFish Team
 * @version 2.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @since 2.0
 */
public class Alert extends AbstractDialog<Alert> implements Command<Alert>, Positionable<Alert>, HasText<Alert> {

    private static final long serialVersionUID = 3046146830347964521L;

    private String btnCls;
    private String icon;
    private String text;
    private List<Button> buttons;
    /**
     * 点击"确定"执行的命令。
     */
    private Command<?> yes;

    /**
     * 构造函数
     *
     * @param text 文本
     */
    public Alert(String text) {
        this.text = text;
    }

    @Override
    public Alert setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String getText() {
        return text;
    }

    /**
     * 按钮样式名
     *
     * @return String
     */
    public String getBtnCls() {
        return btnCls;
    }

    /**
     * 按钮样式名
     *
     * @param btnCls String
     * @return this
     */
    public Alert setBtnCls(String btnCls) {
        this.btnCls = btnCls;
        return this;
    }

    /**
     * 图标
     *
     * @return the icon
     * @since XMLTMPL 2.1
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 图标
     *
     * @param icon the icon to set
     * @return 本身，这样可以继续设置其他属性
     * @since XMLTMPL 2.1
     */
    public Alert setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public Alert setButtons(List<Button> buttons) {
        this.buttons = buttons;
        return this;
    }

    /**
     * 点击"确定"执行的命令。
     *
     * @return Command
     */
    public Command<?> getYes() {
        return yes;
    }

    /**
     * 点击"确定"执行的命令。
     *
     * @param yes ConfirmCommand
     * @return this
     */
    public Alert setYes(Command<?> yes) {
        this.yes = yes;
        return this;
    }

    @Override
    public UiNode<?> findNodeById(String id) {
        return null;
    }

    @Override
    public Alert removeNodeById(String id) {
        return null;
    }

    @Override
    public boolean replaceNodeById(Widget<?> w) {
        return false;
    }

    @Override
    public void clearNodes() {

    }
}
