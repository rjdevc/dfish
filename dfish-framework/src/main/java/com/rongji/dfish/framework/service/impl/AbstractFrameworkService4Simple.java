package com.rongji.dfish.framework.service.impl;

import java.io.Serializable;

/**
 * 简易模式的service实现类,VO和PO是同一个类
 * @author lamontYu
 * @since DFish5.0
 */
public abstract class AbstractFrameworkService4Simple<V, ID extends Serializable> extends AbstractFrameworkService<V, V, ID> {

    @Override
    public V getInstance4Po(V vo) {
        return vo;
    }

    @Override
    public V getInstance4Vo(V po) {
        return po;
    }
}
