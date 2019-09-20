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

//    /**
//     * 自动检查,设置该参数后,增删改前后将有检查方法锚点
//     */
//    protected boolean autoCheck;

    protected CryptProvider cryptProvider;

    public BaseDao<P, ID> getDao() {
        return dao;
    }

    public void setDao(BaseDao<P, ID> dao) {
        this.dao = dao;
    }

//    public boolean isAutoCheck() {
//        return autoCheck;
//    }
//
//    public void setAutoCheck(boolean autoCheck) {
//        this.autoCheck = autoCheck;
//    }

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
        // PO在第2个泛型参数
        @SuppressWarnings("unchecked")
        Class<P> entityClass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        P entity = entityClass.newInstance();
        return entity;
    }

    protected P parsePo(V vo) throws Exception {
        if (vo == null) {
            return null;
        }
        P entity = newInstance4Po();
        Utils.copyPropertiesExact(entity, vo);
        return entity;
    }

    protected List<P> parsePos(Collection<V> vos) throws Exception {
        if (Utils.isEmpty(vos)) {
            return new ArrayList<>(0);
        }
        List<P> pos = new ArrayList<>(vos.size());
        for (V vo : vos) {
            pos.add(parsePo(vo));
        }
        return pos;
    }

    protected V newInstance4Vo() throws Exception {
        // VO在第1个泛型参数
        @SuppressWarnings("unchecked")
        Class<V> voClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        V vo = voClass.newInstance();
        return vo;
    }

    protected V parseVo(P po) throws Exception {
        if (po == null) {
            return null;
        }
        V vo = newInstance4Vo();
        Utils.copyPropertiesExact(vo, po);
        return vo;
    }

    protected List<V> parseVos(Collection<P> pos) throws Exception {
        if (Utils.isEmpty(pos)) {
            return new ArrayList<>(0);
        }
        List<V> vos = new ArrayList<>(pos.size());
        for (P po : pos) {
            vos.add(parseVo(po));
        }
        return vos;
    }

    public String getNewId() {
        return getDao().getNewId();
    }

    protected void beforeSave(V vo) throws Exception {
        this.beforeSaveOrUpdate(vo, null);
    }

    protected void afterSave(V vo) throws Exception {
        this.afterSaveOrUpdate(vo, null);
    }

    protected void beforeUpdate(V newVo, V oldVo) throws Exception {
        this.beforeSaveOrUpdate(newVo, oldVo);
    }

    protected void afterUpdate(V newVo, V oldVo) throws Exception {
        this.afterSaveOrUpdate(newVo, oldVo);
    }

    protected void beforeDelete(V vo)  throws Exception {
    }

    protected void afterDelete(V vo) throws Exception {
    }

    protected void beforeSaveOrUpdate(V newVo, V oldVo) throws Exception {
    }

    protected void afterSaveOrUpdate(V newVo, V oldVo) throws Exception {
    }

    public int saveOrUpdate(V vo) throws Exception {
        return saveOrUpdate(vo, null);
    }

    public int saveOrUpdate(V newVo, V oldVo) throws Exception {
        if (newVo == null) {
            return 0;
        }

        beforeSaveOrUpdate(newVo, oldVo);
        P po = parsePo(newVo);
        int result = getDao().saveOrUpdate(po);
        if (result > 0) {
            afterSaveOrUpdate(newVo, oldVo);
        }
        return result;
    }

    @Transactional
    public int save(V vo) throws Exception {
        if (vo == null) {
            return 0;
        }
        beforeSave(vo);
        P po = parsePo(vo);
        if (po != null) {

        }
        int result = getDao().save(po);
        if (result > 0) {
            afterSave(vo);
        }
        return result;
    }

    @Transactional
    public int update(V newVo) throws Exception {
        return update(newVo, null);
    }

    @Transactional
    public int update(V newVo, V oldVo) throws Exception {
        if (newVo == null) {
            return 0;
        }
        beforeUpdate(newVo, oldVo);
        P po = parsePo(newVo);
        int result = getDao().update(po);
        if (result > 0) {
            afterUpdate(newVo, oldVo);
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
       return delete(get(id));
    }

    public V get(ID id) throws Exception {
        P po = getDao().get(id);
        return parseVo(po);
    }

    public List<V> findAll(List<ID> ids) throws Exception {
        List<P> pos = getDao().findAll(ids);
        return parseVos(pos);
    }

}
