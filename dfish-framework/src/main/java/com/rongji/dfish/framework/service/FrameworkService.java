package com.rongji.dfish.framework.service;

import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.crypt.Cryptor;
import com.rongji.dfish.framework.dao.FrameworkDao;
import com.rongji.dfish.framework.dto.QueryParam;

import java.io.Serializable;
import java.util.*;

/**
 * 框架默认service的接口,定义基本增删查改等接口
 * @author lamontYu
 * @create 2019-12-04
 */
public interface FrameworkService<V, P, ID extends Serializable> {

    /**
     * 获取dao
     * @return FrameworkDao&lt;P, ID&gt;
     */
    FrameworkDao<P, ID> getDao();

    /**
     *
     * @return
     */
    P newInstance4Po();

    default P getInstance4Po(V vo) {
        if (vo == null) {
            return null;
        }
        P po = newInstance4Po();
        Utils.copyPropertiesExact(po, vo);
        return po;
    }

    default P parsePo(V vo) {
        if (vo == null) {
            return null;
        }
        List<P> pos = parsePos(Arrays.asList(vo));
        return pos.get(0);
    }

    default List<P> parsePos(Collection<V> vos) {
        if (Utils.isEmpty(vos)) {
            return new ArrayList<>(0);
        }
        List<P> pos = new ArrayList<>(vos.size());
        for (V vo : vos) {
            pos.add(getInstance4Po(vo));
        }
        return pos;
    }

    V newInstance4Vo();

    default V getInstance4Vo(P po) {
        if (po == null) {
            return null;
        }
        V vo = newInstance4Vo();
        Utils.copyPropertiesExact(vo, po);
        return vo;
    }

    default V parseVo(P po) {
        if (po == null) {
            return null;
        }
        List<V> voList = parseVos(Arrays.asList(po));
        if (Utils.notEmpty(voList)) {
            return voList.get(0);
        }
        return null;
    }

    default List<V> parseVos(List<P> pos) {
        if (Utils.isEmpty(pos)) {
            return new ArrayList<>(0);
        }
        List<V> vos = new ArrayList<>(pos.size());
        for (P po : pos) {
            vos.add(getInstance4Vo(po));
        }
        return vos;
    }

    /**
     * 获取加密器
     * @return StringCryptor
     */
    Cryptor getCryptor();

    /**
     * 加密字符
     *
     * @param str 加密前的字符
     * @return 加密后的密文
     */
    default String encrypt(String str) {
        return getCryptor().encrypt(str);
    }

    /**
     * 解密字符
     *
     * @param str 加密的密文
     * @return 解密后的字符
     */
    default String decrypt(String str) {
        return getCryptor().decrypt(str);
    }

    default String getNewId() {
        return IdGenerator.getSortedId32();
    }

    default int saveOrUpdate(V vo) throws Exception {
        if (vo == null) {
            return 0;
        }

        P po = parsePo(vo);
        int result = getDao().saveOrUpdate(po);
        return result;
    }

    default int save(V vo) throws Exception {
        if (vo == null) {
            return 0;
        }
        P po = parsePo(vo);
        int result = getDao().save(po);
        return result;
    }

    default int update(V vo) throws Exception {
        if (vo == null) {
            return 0;
        }
        P po = parsePo(vo);
        int result = getDao().update(po);
        return result;
    }

    default int delete(V vo) throws Exception {
        if (vo == null) {
            return 0;
        }
        return deleteAll(Arrays.asList(vo));
    }

    default int deleteAll(Collection<V> vos) throws Exception {
        if (Utils.isEmpty(vos)) {
            return 0;
        }
        List<P> pos = parsePos(vos);
        return deletePos(pos);
    }

    default int deletePos(Collection<P> pos) {
        int result = getDao().deleteAll(pos);
        return result;
    }

    default int delete(ID id) throws Exception {
        V vo = get(id);
        return delete(vo);
    }

    default V get(ID id) {
        P po = getDao().get(id);
        V vo = parseVo(po);
        return vo;
    }

    default Map<ID, V> gets(Collection<ID> ids) {
        Map<ID, P> pos = getDao().gets(ids);
        Map<ID, V> vos = new HashMap<>(pos.size());
        for (Map.Entry<ID, P> entry : pos.entrySet()) {
            vos.put(entry.getKey(), getInstance4Vo(entry.getValue()));
        }
        return vos;
    }

    default List<V> list(Pagination pagination, QueryParam<?> queryParam) {
        List<P> pos = getDao().list(pagination, queryParam);
        return parseVos(pos);
    }

}
