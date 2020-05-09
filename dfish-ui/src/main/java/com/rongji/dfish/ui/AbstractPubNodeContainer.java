package com.rongji.dfish.ui;

import java.util.List;

/**
 * AbstractPubNodeContainer 为抽象PubNode类，为方便Pub类型的Node构建而创立
 * @author lamontYu
 * @since DFish5.0
 */
public abstract class AbstractPubNodeContainer<T extends AbstractPubNodeContainer<T, N, PN>, N extends Node,PN extends Node> extends AbstractMultiNodeContainer<T,N> implements PubNodeContainer<T, N,PN> {
    private static final long serialVersionUID = 5077405748817820249L;

    private PN pub;

    /**
     * 构造函数
     *
     */
    public AbstractPubNodeContainer() {
        super();
    }

    /**
     * 构造函数
     *
     * @param id String
     */
    public AbstractPubNodeContainer(String id) {
        super(id);
    }

    /**
     * 当需要的时候，创建一个 当前容器合适的Pub对象。
     * 而尽量减少用setPub 方法，防止因为被覆盖而造成数据丢失。
     * 这要求，各个实现类，需要提供新建的方法。
     * @return PN
     */
    protected abstract PN newPub();

    @Override
    public PN pub() {
        if (pub == null) {
            pub = newPub();
        }
        return pub;
    }

    @Override
    public PN getPub() {
        return pub;
    }

    @Override
    public T setPub(PN pub) {
        this.pub = pub;
        return (T) this;
    }

}
