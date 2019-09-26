package com.rongji.dfish.framework.service;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.crypt.CryptProvider;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @param <V>  数据接口对象
 * @param <P>  实体对象Entity
 * @param <ID> ID对象类型通常是String
 * @author DFish Team
 */
public abstract class BaseService<V, P, ID extends Serializable> {
    protected BaseDao<P, ID> dao;

//    /**
//     * 自动检查,设置该参数后,增删改前后将有检查方法锚点
//     */
//    protected boolean autoCheck;

    @Autowired(required = false)
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

    protected Class<P> poClass;

    protected P newInstance4Po() {
        try {
            if (poClass == null) {
                // PO在第2个泛型参数
                poClass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            }
            return poClass.newInstance();
        } catch (Exception e) {
            FrameworkHelper.LOG.error("对象创建异常", e);
        }
        return null;
    }

    protected P parsePo(V vo) {
        if (vo == null) {
            return null;
        }
        P entity = newInstance4Po();
        Utils.copyPropertiesExact(entity, vo);
        return entity;
    }

    protected List<P> parsePos(Collection<V> vos) {
        if (Utils.isEmpty(vos)) {
            return new ArrayList<>(0);
        }
        List<P> pos = new ArrayList<>(vos.size());
        for (V vo : vos) {
            P po = parsePo(vo);
            if (po != null) {
                pos.add(po);
            }
        }
        return pos;
    }

    protected Class<V> voClass;

    protected V newInstance4Vo() {
        try {
            if (voClass == null) {
                // VO在第1个泛型参数
                voClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            }
            return voClass.newInstance();
        } catch (Exception e) {
            FrameworkHelper.LOG.error("对象创建异常", e);
        }
        return null;
    }

    protected V parseVo(P po) {
        if (po == null) {
            return null;
        }
        // 将Po关联的缓存断掉
        getDao().getPubCommonDAO().evictObject(po);
        V vo = newInstance4Vo();
        Utils.copyPropertiesExact(vo, po);
        return vo;
    }

    protected List<V> parseVos(Collection<P> pos) {
        if (Utils.isEmpty(pos)) {
            return new ArrayList<>(0);
        }
        List<V> vos = new ArrayList<>(pos.size());
        for (P po : pos) {
            V vo = parseVo(po);
            if (vo != null) {
                vos.add(vo);
            }
        }
        return vos;
    }

    public String getNewId() {
        return IdGenerator.getSortedId32();
    }

    public int saveOrUpdate(V vo) throws Exception {
        if (vo == null) {
            return 0;
        }

        P po = parsePo(vo);
        int result = getDao().saveOrUpdate(po);
        return result;
    }

    public int save(V vo) throws Exception {
        if (vo == null) {
            return 0;
        }
        P po = parsePo(vo);
        int result = getDao().save(po);
        return result;
    }

    public int update(V vo) throws Exception {
        if (vo == null) {
            return 0;
        }
        P po = parsePo(vo);
        int result = getDao().update(po);
        return result;
    }

    public int deleteAll(Collection<V> voList) throws Exception {
        if (Utils.isEmpty(voList)) {
            return 0;
        }
        int result = getDao().deleteAll(parsePos(voList));
        return result;
    }

    public int delete(V vo) throws Exception {
        int result = getDao().delete(parsePo(vo));
        return result;
    }

    public int delete(ID id) throws Exception {
        P po = getDao().get(id);
        if (po == null) {
            return 0;
        }
        V vo = parseVo(po);
        int result = getDao().delete(po);
        return result;
    }

    public V get(ID id) {
        P po = getDao().get(id);
        V vo = parseVo(po);
        return vo;
    }

    public List<V> gets(List<ID> ids) {
        List<P> pos = getDao().gets(ids);
        List<V> vos = parseVos(pos);
        return vos;
    }

}
