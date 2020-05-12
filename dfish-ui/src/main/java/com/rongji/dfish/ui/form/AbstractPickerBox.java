package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.NodeContainer;
import com.rongji.dfish.ui.NodeContainerDecorator;
import com.rongji.dfish.ui.command.Dialog;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * picker选择框组件,这里定义选择框组件该有特性
 * @param <T> 组件本身
 * @since DFish5.0
 * @author lamontYu
 */
public abstract class AbstractPickerBox<T extends AbstractPickerBox<T>> extends AbstractInput<T, String> implements NodeContainer {

    private static final long serialVersionUID = 647561925546899578L;
    protected Dialog drop;
    protected Dialog picker;

    /**
     * 构造函数
     *
     * @param name  表单元素名
     * @param label 标题
     * @param value 值
     * @since DFish3.0
     */
    public AbstractPickerBox(String name, String label, String value) {
        super(name,label,value);
    }
    /**
     * 构造函数
     *
     * @param name  表单元素名
     * @param label 标题
     * @param value 值
     * @since DFish3.0
     */
    public AbstractPickerBox(String name, Label label, String value) {
        super(name,label,value);
    }

    @Override
    public T setValue(Object value) {
        return setValue(toString(value));
    }

    /**
     * 获取下拉对话框,如果不存在则新建
     *
     * @return DialogCommand
     */
    protected Dialog drop() {
        if (this.drop == null) {
            this.drop = new Dialog(null,null,null);
        }
        return this.drop;
    }

    /**
     * 显示所有选项的下拉对话框。
     *
     * @return DialogCommand
     */
    public Dialog getDrop() {
        return drop;
    }

    /**
     * 显示所有选项的下拉对话框。
     *
     * @param drop DialogCommand
     * @return 本身，这样可以继续设置其他属性
     */
    public T setDrop(Dialog drop) {
        this.drop = drop;
        return (T) this;
    }

    /**
     * "选择"按钮点击动作
     *
     * @return "选择"组件
     */
    public Dialog getPicker() {
        return picker;
    }

    /**
     * 组件最右侧显示的"选择"组件
     *
     * @param picker "选择"组件
     * @return 本身，这样可以继续设置其他属性
     */
    public T setPicker(Dialog picker) {
        this.picker = picker;
        return (T) this;
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
                return Arrays.asList(picker,drop) ;
            }

            @Override
            protected void setNode(int i, Node node) {
                switch (i){
                    case 0:
                        picker=(Dialog) node;
                        break;
                    case 1:
                        drop=(Dialog) node;
                        break;
                    default:
                        throw new IllegalArgumentException("expect 0-picker 1-drop, but get "+i);
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
