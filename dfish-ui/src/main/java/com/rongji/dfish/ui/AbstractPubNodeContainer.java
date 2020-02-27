package com.rongji.dfish.ui;

import java.util.List;

/**
 * @author lamontYu
 * @date 2020-01-16
 * @since 5.0
 */
public abstract class AbstractPubNodeContainer<T extends AbstractPubNodeContainer<T, N>, N extends Node> extends AbstractMultiNodeContainer<T,N> implements PubNodeContainer<T, N> {
    private static final long serialVersionUID = 5077405748817820249L;

    private N pub;

    /**
     * 构造函数
     *
     * @param id String
     */
    public AbstractPubNodeContainer(String id) {
        super(id);
    }

    protected abstract N newPub();

    @Override
    public N pub() {
        if (pub == null) {
            pub = newPub();
        }
        return pub;
    }

    @Override
    public N getPub() {
        return pub;
    }

    @Override
    public T setPub(N pub) {
        this.pub = pub;
        return (T) this;
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
