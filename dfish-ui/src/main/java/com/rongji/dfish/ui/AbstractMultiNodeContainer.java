package com.rongji.dfish.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象布局类，默认的布局同时还是一个Widget
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
public abstract class AbstractMultiNodeContainer<T extends AbstractMultiNodeContainer<T>> extends AbstractWidget<T> implements MultiNodeContainer<T> {

    private static final long serialVersionUID = 6322077434879898040L;

    /**
     * 构造函数
     *
     * @param id String
     */
    public AbstractMultiNodeContainer(String id) {
        this.id = id;
    }

    protected List<Node> nodes = new ArrayList<>();

    public List<Node> getNodes(){
        return nodes;
    }


    /**
     * 拷贝属性
     *
     * @param to   AbstractLayout
     * @param from AbstractLayout
     */
    protected void copyProperties(AbstractMultiNodeContainer<?> to, AbstractMultiNodeContainer<?> from) {
        super.copyProperties(to, from);
        to.nodes = from.nodes;
    }

    protected AbstractNodeContainerPart containerPart=new AbstractNodeContainerPart() {
        @Override
        protected  List<Node> nodes() {
            return nodes;
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

    @Override
    public Node findNode(Filter filter) {
        return containerPart.findNode(filter);
    }

    @Override
    public List<Node> findAllNodes(Filter filter) {
        return containerPart.findAllNodes(filter);
    }

    @Override
    public Node replaceNode(Filter filter, Node node) {
        return containerPart.replaceNode(filter,node);
    }

    @Override
    public int replaceAllNodes(Filter filter, Node node) {
        return containerPart.replaceAllNodes(filter,node);
    }

}
