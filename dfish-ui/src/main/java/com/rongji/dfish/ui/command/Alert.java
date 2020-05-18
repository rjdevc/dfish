package com.rongji.dfish.ui.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.widget.Button;

/**
 * 该命令用于打开一个警告框(Dialog)
 *
 * @author DFish Team
 * @version 2.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @since DFish2.0
 */
public class Alert extends AbstractPopup<Alert> implements Command<Alert>, Positionable<Alert>, HasText<Alert>,NodeContainer{

    private static final long serialVersionUID = 3046146830347964521L;

    private String buttonCls;
    private String icon;
    private String text;
    private List<Button> buttons;
    /**
     * 点击"确定"执行的命令。
     */
    private Command yes;

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
    public String getButtonCls() {
        return buttonCls;
    }

    /**
     * 按钮样式名
     *
     * @param buttonCls String
     * @return this
     */
    public Alert setButtonCls(String buttonCls) {
        this.buttonCls = buttonCls;
        return this;
    }

    /**
     * 图标
     *
     * @return the icon
     * @since DFish2.1
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 图标
     *
     * @param icon the icon to set
     * @return 本身，这样可以继续设置其他属性
     * @since DFish2.1
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
    public Command getYes() {
        return yes;
    }

    /**
     * 点击"确定"执行的命令。
     *
     * @param yes ConfirmCommand
     * @return this
     */
    public Alert setYes(Command yes) {
        this.yes = yes;
        return this;
    }

    /**
     * 获得装饰器
     * @see com.rongji.dfish.ui.NodeContainerDecorator
     * @return NodeContainerDecorator
     */
    protected NodeContainerDecorator getNodeContainerDecorator(){
        return new NodeContainerDecorator() {
            @Override
            protected List<Node> nodes() {
                List<Node> ret=new ArrayList<>(1+(buttons==null?0:buttons.size()));
                ret.add(yes);
                if(buttons!=null){
                    ret.addAll(buttons);
                }
                return ret;
            }

            @Override
            protected void setNode(int i, Node node) {
                switch (i){
                    case 0:
                        yes=(Command) node;
                        break;
                    default:
                        if(buttons==null||i<0||i-1>=buttons.size()) {
                            throw new ArrayIndexOutOfBoundsException(i-1);
                        }
                        if(node==null){
                            buttons.remove(i-1);
                        }else{
                            buttons.set(i-1,(Button)node);
                        }
                }
            }
        };
    }

    @Override
    public Node findNode(Predicate<Node> filter) {
        return getNodeContainerDecorator().findNode(filter);
    }

    @Override
    public List<Node> findAllNodes(Predicate<Node> filter) {
        return getNodeContainerDecorator().findAllNodes(filter);
    }

    @Override
    public Node replaceNode(Predicate<Node> filter, Node node) {
        return getNodeContainerDecorator().replaceNode(filter,node);
    }

    @Override
    public int replaceAllNodes(Predicate<Node> filter, Node node) {
        return getNodeContainerDecorator().replaceAllNodes(filter,node);
    }

}
