package com.rongji.dfish.framework.service;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.crypt.CryptProvider;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.framework.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;

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
            throw new UnsupportedOperationException("Unable to create PO automatically, please implement method " + getClass().getSimpleName() + "#newInstance4Po by yourself", e);
        }
    }

    protected P newInstance4Po(V vo) {
        if (vo == null) {
            return null;
        }
        P po = newInstance4Po();
        Utils.copyPropertiesExact(po, vo);
        return po;
    }

    protected P parsePo(V vo) {
        if (vo == null) {
            return null;
        }
        List<P> pos = parsePos(Arrays.asList(vo));
        return pos.get(0);
    }

    protected List<P> parsePos(Collection<V> vos) {
        if (Utils.isEmpty(vos)) {
            return new ArrayList<>(0);
        }
        List<P> pos = new ArrayList<>(vos.size());
        for (V vo : vos) {
            pos.add(newInstance4Po(vo));
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
            throw new UnsupportedOperationException("Unable to create VO automatically, please implement method " + getClass().getSimpleName() + "#newInstance4Vo by yourself", e);
        }
    }

    protected V newInstance4Vo(P po) {
        if (po == null) {
            return null;
        }
        V vo = newInstance4Vo();
        Utils.copyPropertiesExact(vo, po);
        return vo;
    }

    protected V parseVo(P po) {
        if (po == null) {
            return null;
        }
        List<V> voList = parseVos(Arrays.asList(po));
        if (Utils.notEmpty(voList)) {
            return voList.get(0);
        }
        return null;
    }

    protected List<V> parseVos(List<P> pos) {
        if (Utils.isEmpty(pos)) {
            return new ArrayList<>(0);
        }
        List<V> vos = new ArrayList<>(pos.size());
        for (P po : pos) {
            // 将Po关联的缓存断掉
            getDao().evictObject(po);
            vos.add(newInstance4Vo(po));
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

    public int delete(V vo) throws Exception {
        if (vo == null) {
            return 0;
        }
        return deleteAll(Arrays.asList(vo));
    }

    public int deleteAll(Collection<V> voList) throws Exception {
        if (Utils.isEmpty(voList)) {
            return 0;
        }
        List<P> pos = parsePos(voList);
        return doDelete(pos);
    }

    protected int doDelete(List<P> poList) {
        int result = getDao().deleteAll(poList);
        return result;
    }

    public int delete(ID id) throws Exception {
//        P po = getDao().get(id);
//        if (po == null) {
//            return 0;
//        }
//        return doDelete(Arrays.asList(po));
        V vo = get(id);
        return delete(vo);
    }

    public V get(ID id) {
        P po = getDao().get(id);
        V vo = parseVo(po);
        return vo;
    }

    public Map<ID, V> gets(Collection<ID> ids) {
        Map<ID, P> pos = getDao().gets(ids);
        Map<ID, V> vos = new HashMap<>(pos.size());
        for (Map.Entry<ID, P> entry : pos.entrySet()) {
            vos.put(entry.getKey(), newInstance4Vo(entry.getValue()));
        }
        return vos;
    }

}
