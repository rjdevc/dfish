package com.rongji.dfish.ui.command;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.ui.NodeContainerDecorator;
import com.rongji.dfish.ui.MultiNodeContainer;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.Widget;

/**
 * AddCommand为增加一个元素的命令的基础格式，一般增加元素分为
 * {@link Append},{@link Prepend},{@link After}和{@link Before}
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 * @since DFish 2.0 当时为InsertComman通过where属性控制增加在哪里。
 */
@SuppressWarnings("unchecked")
public abstract class AddCommand<T extends AddCommand<T>> extends NodeControlCommand<T> implements
        MultiNodeContainer<T,Widget> {

    private static final long serialVersionUID = -2417775749900268295L;

    protected List<Widget> nodes = new ArrayList<>();
    public AddCommand(String target) {
        setTarget(target);
    }
    @Override
    public List<Widget> getNodes(){
        return nodes;
    }
    @Override
    public T setNodes(List<Widget>nodes){
        this.nodes=nodes;
        return (T)this;
    }


    protected NodeContainerDecorator getNodeContainerDecorator(){
        return new NodeContainerDecorator() {
            @Override
            protected  List<Node> nodes() {
                return (List)AddCommand.this.nodes;
            }

            @Override
            protected void setNode(int i, Node node) {
                if(node==null){
                    nodes.remove(i);
                }else{
                    nodes.set(i,(Widget)node);
                }
            }
        };
    }

    @Override
    public Node findNode(Filter filter) {
        return getNodeContainerDecorator().findNode(filter);
    }

    @Override
    public List<Node> findAllNodes(Filter filter) {
        return getNodeContainerDecorator().findAllNodes(filter);
    }

    @Override
    public Node replaceNode(Filter filter, Node node) {
        return getNodeContainerDecorator().replaceNode(filter,node);
    }

    @Override
    public int replaceAllNodes(Filter filter, Node node) {
        return getNodeContainerDecorator().replaceAllNodes(filter,node);
    }

}
