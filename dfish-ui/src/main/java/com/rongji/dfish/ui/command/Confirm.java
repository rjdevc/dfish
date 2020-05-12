package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.NodeContainer;
import com.rongji.dfish.ui.NodeContainerDecorator;
import com.rongji.dfish.ui.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


/**
 * 确定命令。
 * <p>
 * 在执行系列命令，或单格命令前。允许询问一个信息，在征得用户同意后执行命令，否则则终止命令的执行。
 * </p>
 *
 * @author DFish Team
 * @version 3.1 lamontYu 所有属性和type按照驼峰命名方式调整
 *
 * @since DFish1.0
 */
public class Confirm extends AbstractPopup<Confirm> implements Command<Confirm>, HasText<Confirm> ,NodeContainer {

    private static final long serialVersionUID = 6715410304552489693L;

    private String buttonCls;
    /**
     * 显示文本。这里一般是询问的问题内容
     */
    private String text;
    /**
     * 图标。
     */
    private String icon;
    /**
     * 点击"确定"执行的命令。
     */
    private Command<?> yes;
    /**
     * 点击"取消"执行的命令。
     */
    private Command<?> no;
    /**
     * 底部按钮
     */
    private List<Button> buttons;

    /**
     * 构造函数
     *
     * @param text 显示文本。这里一般是询问的问题内容
     */
    public Confirm(String text) {
        this.text = text;
    }

    /**
     * 构造函数
     *
     * @param text 显示文本。这里一般是询问的问题内容
     * @param yes  点击"确定"执行的命令。
     */
    public Confirm(String text, Command<?> yes) {
        this.text = text;
        this.yes = yes;
    }

    /**
     * 构造函数
     *
     * @param text 显示文本。这里一般是询问的问题内容
     * @param yes  点击"确定"执行的命令。
     * @param no   点击"取消"执行的命令。
     */
    public Confirm(String text, Command<?> yes, Command<?> no) {
        this.text = text;
        this.yes = yes;
        this.no = no;
    }

    /**
     * 按钮样式名
     *
     * @return 按钮样式名
     */
    public String getButtonCls() {
        return buttonCls;
    }

    /**
     * 按钮样式名
     *
     * @param buttonCls 按钮样式名
     * @return 本身
     */
    public Confirm setButtonCls(String buttonCls) {
        this.buttonCls = buttonCls;
        return this;
    }

    /**
     * 显示文本。这里一般是询问的问题内容
     *
     * @return String
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * 显示文本。这里一般是询问的问题内容
     *
     * @param text String
     * @return this
     */
    @Override
    public Confirm setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 图标。
     *
     * @return String
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 图标。
     *
     * @param icon String
     * @return this
     */
    public Confirm setIcon(String icon) {
        this.icon = icon;
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
    public Confirm setYes(Command<?> yes) {
        this.yes = yes;
        return this;
    }

    /**
     * 点击"取消"执行的命令。
     *
     * @return Command
     */
    public Command<?> getNo() {
        return no;
    }

    /**
     * 点击"取消"执行的命令。
     *
     * @param no Command
     * @return this
     */
    public Confirm setNo(Command<?> no) {
        this.no = no;
        return this;
    }

    /**
     * 获取按钮
     * @return 按钮集合
     */
    public List<Button> getButtons() {
        return buttons;
    }

    /**
     * 设置按钮
     * @param buttons 按钮集合
     * @return 本身，这样可以继续设置其他属性
     */
    public Confirm setButtons(List<Button> buttons) {
        this.buttons = buttons;
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
                List<Node> ret=new ArrayList<>(2+(buttons==null?0:buttons.size()));
                ret.add(yes);
                ret.add(no);
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
                    case 1:
                        no=(Command) node;
                        break;
                    default:
                        if(buttons==null||i<0||i-2>=buttons.size()) {
                            throw new ArrayIndexOutOfBoundsException(i-2);
                        }
                        if(node==null){
                            buttons.remove(i-2);
                        }else{
                            buttons.set(i-2,(Button)node);
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
