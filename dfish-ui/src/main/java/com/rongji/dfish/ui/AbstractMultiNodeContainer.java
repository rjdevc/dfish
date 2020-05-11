package com.rongji.dfish.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象布局类，默认的布局同时还是一个Widget
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
public abstract class AbstractMultiNodeContainer<T extends AbstractMultiNodeContainer<T,N>,N extends Node> extends AbstractWidget<T> implements MultiNodeContainer<T,N> {

    private static final long serialVersionUID = 6322077434879898040L;

    /**
     * 构造函数
     *
     */
    public AbstractMultiNodeContainer() {
        this.id = id;
    }
    /**
     * 构造函数
     *
     * @param id String
     */
    public AbstractMultiNodeContainer(String id) {
        this.id = id;
    }

    protected List<Node> nodes = new ArrayList<>();

    @Override
    public List<Node> getNodes(){
        return nodes;
    }
    @Override
    public T setNodes(List<N> nodes){
        this.nodes=(List)nodes;
        return (T)this;
    }


    /**
     * 拷贝属性
     *
     * @param to   AbstractLayout
     * @param from AbstractLayout
     */
    protected void copyProperties(AbstractMultiNodeContainer to, AbstractMultiNodeContainer from) {
        super.copyProperties(to, from);
        to.nodes = from.nodes;
    }

    /**
     * 获得装饰器
     * @see com.rongji.dfish.ui.NodeContainerDecorator
     * @return NodeContainerDecorator
     */
    protected NodeContainerDecorator getNodeContainerDecorator(){
        return new NodeContainerDecorator() {
            @Override
            protected  List<Node> nodes() {
                return (List)nodes;
            }

            @Override
            protected void setNode(int i, Node node) {
                if(node==null){
                    nodes.remove(i);
                }else{
                    nodes.set(i,node);
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
