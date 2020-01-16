package com.rongji.dfish.ui;

import java.util.List;

/**
 * @author lamontYu
 * @date 2020-01-16
 * @since 5.0
 */
public abstract class AbstractPubNodeContainer<T extends AbstractPubNodeContainer<T, N>, N extends Node> extends AbstractNodeContainer<T> implements PubNodeContainer<T, N> {
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

    @Override
    public List<Node> getNodes() {
        return nodes;
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

    public T add(int index, N node) {
        if (node == null) {
            return (T) this;
        }
        if (node == this) {
            throw new IllegalArgumentException("can not add itself as a sub node");
        }
        if (index < 0) {
            nodes.add(node);
        } else {
            nodes.add(index, node);
        }
        return (T) this;
    }

}
