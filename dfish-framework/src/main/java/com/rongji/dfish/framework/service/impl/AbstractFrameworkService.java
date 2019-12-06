package com.rongji.dfish.framework.service.impl;

import com.rongji.dfish.base.crypt.CryptProvider;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.framework.dao.FrameworkDao;
import com.rongji.dfish.framework.service.FrameworkService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * service默认实现基类
 * @author lamontYu
 * @create 2019-12-04
 */
public abstract class AbstractFrameworkService<V, P, ID extends Serializable> implements FrameworkService<V, P, ID> {
    @Resource(name = "cryptProvider")
    protected CryptProvider cryptProvider;

    public CryptProvider getCryptProvider() {
        return cryptProvider;
    }

    public void setCryptProvider(CryptProvider cryptProvider) {
        this.cryptProvider = cryptProvider;
    }

    @Override
    public StringCryptor getCryptor() {
        if (cryptProvider == null) {
            cryptProvider = new CryptProvider();
        }
        return cryptProvider.getCryptor();
    }

    protected Class<P> poClass;
    protected Class<V> voClass;

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
