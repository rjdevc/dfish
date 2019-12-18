package com.rongji.dfish.framework.service.impl;

import com.rongji.dfish.base.crypt.Cryptor;
import com.rongji.dfish.framework.service.FrameworkService;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * service默认实现基类
 * @author lamontYu
 * @date 2019-12-04
 */
public abstract class AbstractFrameworkService<V, P, ID extends Serializable> implements FrameworkService<V, P, ID> {
    @Resource(name = "cryptor")
    protected Cryptor cryptor;
    protected Class<P> poClass;
    protected Class<V> voClass;

    @Override
    public Cryptor getCryptor() {
        return cryptor;
    }

    public void setCryptor(Cryptor cryptor) {
        this.cryptor = cryptor;
    }

    @Override
    public P newInstance4Po() {
        try {
            if (poClass == null) {
                // PO在第2个泛型参数
                poClass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            }
            return poClass.newInstance();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Unable to create PO automatically, please implement method " + getClass().getSimpleName() + "#newInstance4Po by yourself", e);
        }
    }

    @Override
    public V newInstance4Vo() {
        try {
            if (voClass == null) {
                // VO在第1个泛型参数
                voClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            }
            return voClass.newInstance();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Unable to create VO automatically, please implement method " + getClass().getSimpleName() + "#newInstance4Vo by yourself", e);
        }
    }
}
