package com.rongji.dfish.framework.service.impl;

import java.io.Serializable;

/**
 * 简易模式的service实现类,VO和PO是同一个类
 * @author lamontYu
 * @date 2019-12-05
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
