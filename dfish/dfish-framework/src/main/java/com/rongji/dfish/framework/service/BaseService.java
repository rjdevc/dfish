package com.rongji.dfish.framework.service;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.crypt.CryptFactory;
import com.rongji.dfish.base.crypt.CryptProvider;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.dao.BaseDao;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author DFish Team
 *
 * @param <V> 数据接口对象
 * @param <P> 实体对象Entity
 * @param <ID> ID对象类型通常是String
 */
public abstract class BaseService<V, P, ID extends Serializable> {
    
    protected BaseDao<P, ID> dao;

    protected CryptProvider cryptProvider;

    public BaseDao<P, ID> getDao() {
        return dao;
    }

    public void setDao(BaseDao<P, ID> dao) {
        this.dao = dao;
    }

    public CryptProvider getCryptProvider() {
        return cryptProvider;
    }

    public void setCryptProvider(CryptProvider cryptProvider) {
        this.cryptProvider = cryptProvider;
    }

    protected StringCryptor getCryptor() {
        if (cryptProvider == null) {
            cryptProvider = new CryptProvider();
        }
        return cryptProvider.getCryptor();
    }

    /**
     * 加密字符
     *
     * @param str 加密前的字符
     * @return 加密后的密文
     */
    public String encrypt(String str) {
        return getCryptor().encrypt(str);
    }

    /**
     * 解密字符
     *
     * @param str 加密的密文
     * @return 解密后的字符
     */
    public String decrypt(String str) {
        return getCryptor().decrypt(str);
    }

    protected P newInstance4Po() throws Exception {
        @SuppressWarnings("unchecked")
        Class<P> entityClass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        P entity = entityClass.newInstance();
        return entity;
    }

    protected P parsePo(V vo) {
        if (vo == null) {
            return null;
        }
        P entity = null;
        try {
            entity = newInstance4Po();
            Utils.copyPropertiesExact(entity, vo);
        } catch (Exception e) {
            FrameworkHelper.LOG.error("对象解析异常", e);
        }
        return entity;
    }

    protected List<P> parsePos(Collection<V> vos) {
        if (Utils.isEmpty(vos)) {
            return new ArrayList<>(0);
        }
        List<P> pos = new ArrayList<>(vos.size());
        for (V vo : vos) {
            try {
                pos.add(parsePo(vo));
            } catch (Exception e) {
                FrameworkHelper.LOG.error("对象解析异常", e);
            }
        }
        return pos;
    }

    protected V newInstance4Vo() throws Exception {
        @SuppressWarnings("unchecked")
        Class<V> voClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        V vo = voClass.newInstance();
        return vo;
    }

    protected V parseVo(P po) {
        if (po == null) {
            return null;
        }
        V vo = null;
        try {
            vo = newInstance4Vo();
            Utils.copyPropertiesExact(vo, po);
        } catch (Exception e) {
            FrameworkHelper.LOG.error("对象解析异常", e);
        }
        return vo;
    }

    protected List<V> parseVos(Collection<P> pos) {
        if (Utils.isEmpty(pos)) {
            return new ArrayList<>(0);
        }
        List<V> vos = new ArrayList<>(pos.size());
        for (P po : pos) {
            try {
                vos.add(parseVo(po));
            } catch (Exception e) {
                FrameworkHelper.LOG.error("对象解析异常", e);
            }
        }
        return vos;
    }

    public String getNewId() {
        return getDao().getNewId();
    }

    protected void beforeSave(V vo) throws Exception {
    }

    protected void afterSave(V vo) throws Exception {
    }

    protected void beforeUpdate(V vo) throws Exception {
    }

    protected void afterUpdate(V vo) throws Exception {
    }

    protected void beforeDelete(V vo)  throws Exception {
    }

    protected void afterDelete(V vo) throws Exception {
    }

    public int saveOrUpdate(V vo) throws Exception {
        return getDao().saveOrUpdate(parsePo(vo));
    }

    @Transactional
    public int save(V vo) throws Exception {
        beforeSave(vo);
        int result = getDao().save(parsePo(vo));
        if (result > 0) {
            afterSave(vo);
        }
        return result;
    }

    @Transactional
    public int update(V vo) throws Exception {
        beforeUpdate(vo);
        int result = getDao().update(parsePo(vo));
        if (result > 0) {
            afterUpdate(vo);
        }
        return result;
    }

    @Transactional
    public int deleteAll(Collection<V> voList) throws Exception {
        if (Utils.isEmpty(voList)) {
            return 0;
        }
        for (V vo : voList) {
            beforeDelete(vo);
        }
        int result = getDao().deleteAll(parsePos(voList));
        if (result > 0) {
            for (V vo : voList) {
                afterDelete(vo);
            }
        }
        return result;
    }

    @Transactional
    public int delete(V vo) throws Exception {
        beforeDelete(vo);
        int result = getDao().delete(parsePo(vo));
        if (result > 0) {
            afterDelete(vo);
        }
        return result;
    }

    @Transactional
    public int delete(ID id) throws Exception {
        P po = getDao().get(id);
        V vo = parseVo(po);
        beforeDelete(vo);
        int result = getDao().delete(po);
        if (result > 0) {
            afterDelete(vo);
        }
        return result;
    }

    public V get(ID id) {
        P po = getDao().get(id);
        return parseVo(po);
    }

    public List<V> findAll(List<ID> ids) {
        List<P> pos = getDao().findAll(ids);
        return parseVos(pos);
    }

}
