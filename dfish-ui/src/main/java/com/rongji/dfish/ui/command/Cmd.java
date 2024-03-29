package com.rongji.dfish.ui.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.rongji.dfish.ui.*;


/**
 * CommandGroup 为系列命令
 * <p>有时候需要顺序执行一些列的命令来达到一定的效果。这时候，用CommandGroup把这些命令包含起来</p>
 * <p>CommandGroup里面有path属性，指这个命令在那个窗口(View)执行。
 * 某些时刻如果其他命令需要在默认窗口之外的窗口执行。
 * 那么需要用CommandGroup包括起来</p>
 *
 * @author DFish Team
 * @version 2.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @since DFish2.0
 */
public class Cmd extends AbstractNode<Cmd> implements MultiNodeContainer<Cmd,Command>, Command<Cmd> {

    private static final long serialVersionUID = 6990584366431273097L;
    
    /**
     * 如果想在当前弹出窗口的父窗口(打开该弹出窗口的窗口)可以设置path为该常量
     */
    public static final String PATH_OWNER_VIEW = "javascript:return $.dialog(this).ownerView.path;";
    /**
     * 如果想在当前弹出窗口的父窗口(打开该弹出窗口的窗口)可以设置path为该常量
     *
     * @deprecated {@link #PATH_OWNER_VIEW}
     */
    @Deprecated
    public static final String PATH_FROM_VIEW = PATH_OWNER_VIEW;

    protected List<Command> nodes;
    protected String path;
    protected String target;
    private Long delay;

    /**
     * 执行路径，如果不设置为当前view
     *
     * @return String
     */
    public String getPath() {
        return path;
    }

    /**
     * 指定一个 widget id，当前的命令集由这个 widget 作为执行主体。path 和 target 同时指定时，相当于 VM( path ).find( target ).cmd( args );
     *
     * @param path String
     * @return 本身，这样可以继续设置其他属性
     */
    public Cmd setPath(String path) {
        this.path = path;
        return this;
    }

    /**
     * 指定一个 widget id，当前的命令集由这个 widget 作为执行主体。path 和 target 同时指定时，相当于 VM( path ).find( target ).cmd( args );
     *
     * @return String
     */
    public String getTarget() {
        return target;
    }

    /**
     * 执行路径，如果不设置为当前view
     *
     * @param target String
     * @return 本身，这样可以继续设置其他属性
     */
    public Cmd setTarget(String target) {
        this.target = target;
        return this;
    }

    /**
     * 默认构造函数
     */
    public Cmd() {
        nodes = new ArrayList<>();
    }


    /**
     * 延迟执行，单位:毫秒
     *
     * @return delay
     */
    public Long getDelay() {
        return delay;
    }

    /**
     * 设置该组里面包含的动作在多长时间后执行。
     * 单位秒
     *
     * @param delay 延迟执行时间,单位:毫秒
     * @return 本身，这样可以继续设置其他属性
     */
    public Cmd setDelay(Long delay) {
        this.delay = delay;
        return this;
    }


    @Override
    public List<Node> getNodes() {
        return (List)nodes;
    }
    @Override
    public Cmd setNodes(List<Command> nodes) {
        this.nodes=nodes;
        return this;
    }

    @Override
    public void clearNodes() {
        nodes.clear();
    }

    /**
     * 获得装饰器
     * @see com.rongji.dfish.ui.NodeContainerDecorator
     * @return NodeContainerDecorator
     */
    protected NodeContainerDecorator getNodeContainerDecorator(){
        return new NodeContainerDecorator() {
            @Override
            protected  List<Command> nodes() {
                return Cmd.this.nodes;
            }

            @Override
            protected void setNode(int i, Node node) {
                if(node==null){
                    nodes.remove(i);
                }else{
                    nodes.set(i,(Command)node);
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
