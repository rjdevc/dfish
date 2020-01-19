package com.rongji.dfish.framework.service;

import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.crypto.Cryptor;
import com.rongji.dfish.framework.dao.FrameworkDao;
import com.rongji.dfish.framework.dto.QueryParam;

import java.io.Serializable;
import java.util.*;

/**
 * 框架默认service的接口,定义基本增删查改等接口
 * @author lamontYu
 * @date 2019-12-04
 * @since 5.0
 */
public interface FrameworkService<V, P, ID extends Serializable> {

    /**
     * 获取dao
     * @return FrameworkDao&lt;P, ID&gt;
     */
    FrameworkDao<P, ID> getDao();

    /**
     * 创建实体对象实例
     * @return P 实体对象实例
     */
    P newInstance4Po();

    /**
     * 根据视图对象获取实体对象,仅将已有信息拷贝
     * @param vo 视图对象
     * @return P 实体对象
     */
    default P getInstance4Po(V vo) {
        if (vo == null) {
            return null;
        }
        P po = newInstance4Po();
        Utils.copyPropertiesExact(po, vo);
        return po;
    }

    /**
     * 将视图对象转换成实体对象
     * @param vo 视图对象
     * @return P 实体对象
     */
    default P parsePo(V vo) {
        if (vo == null) {
            return null;
        }
        List<P> pos = parsePos(Arrays.asList(vo));
        return pos.get(0);
    }

    /**
     * 批量将视图对象转换成实体对象
     * @param vos 视图对象集合
     * @return List&lt;P&gt; 实体对象集合
     */
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

    /**
     * 创建视图对象实例
     * @return V 视图对象
     */
    V newInstance4Vo();

    /**
     * 根据实体对象获取视图对象
     * @param po 实体对象
     * @return V 视图对象
     */
    default V getInstance4Vo(P po) {
        if (po == null) {
            return null;
        }
        V vo = newInstance4Vo();
        Utils.copyPropertiesExact(vo, po);
        return vo;
    }

    /**
     * 将实体对象转换成视图对象
     * @param po 实体对象
     * @return V 视图对象
     */
    default V parseVo(P po) {
        return parseVo(po, true);
    }

    /**
     * 将实体对象转换成视图对象
     * @param po 实体对象
     * @param evict 是否驱逐缓存
     * @return V 视图对象
     */
    default V parseVo(P po, boolean evict) {
        if (po == null) {
            return null;
        }
        List<V> voList = parseVos(Arrays.asList(po), evict);
        if (Utils.notEmpty(voList)) {
            return voList.get(0);
        }
        return null;
    }

    /**
     * 批量将实体对象转换成视图对象
     * @param pos 实体对象集合
     * @return 视图对象集合
     */
    default List<V> parseVos(List<P> pos) {
        return parseVos(pos, true);
    }

    /**
     * 批量将实体对象转换成视图对象
     * @param pos 实体对象集合
     * @param evict 是否驱逐缓存
     * @return 视图对象集合
     */
    default List<V> parseVos(List<P> pos, boolean evict) {
        if (Utils.isEmpty(pos)) {
            return new ArrayList<>(0);
        }
        List<V> vos = new ArrayList<>(pos.size());
        for (P po : pos) {
            if (evict) {
                evict(po);
            }
            vos.add(getInstance4Vo(po));
        }
        return vos;
    }

    /**
     * 获取加密器
     * @return Cryptor
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

    /**
     * 获取新编号
     * @return String 新编号
     */
    default String getNewId() {
        return IdGenerator.getSortedId32();
    }

    /**
     * 保存或修改方法
     * @param vo 视图对象
     * @return int 更新记录数
     * @throws Exception
     */
    default int saveOrUpdate(V vo) throws Exception {
        if (vo == null) {
            return 0;
        }

        P po = parsePo(vo);
        int result = getDao().saveOrUpdate(po);
        return result;
    }

    /**
     * 保存方法
     * @param vo 视图对象
     * @return int 更新记录数
     * @throws Exception
     */
    default int save(V vo) throws Exception {
        if (vo == null) {
            return 0;
        }
        P po = parsePo(vo);
        int result = getDao().save(po);
        return result;
    }

    /**
     * 修改方法
     * @param vo 视图对象
     * @return int 更新记录数
     * @throws Exception
     */
    default int update(V vo) throws Exception {
        if (vo == null) {
            return 0;
        }
        P po = parsePo(vo);
        int result = getDao().update(po);
        return result;
    }

    /**
     * 删除方法
     * @param vo 视图对象
     * @return int 更新记录数
     * @throws Exception
     */
    default int delete(V vo) throws Exception {
        if (vo == null) {
            return 0;
        }
        return deleteAll(Arrays.asList(vo));
    }

    /**
     * 批量删除
     * @param vos 视图对象集合
     * @return int 更新记录数
     * @throws Exception
     */
    default int deleteAll(Collection<V> vos) throws Exception {
        if (Utils.isEmpty(vos)) {
            return 0;
        }
        List<P> pos = parsePos(vos);
        return deletePos(pos);
    }

    /**
     * 批量删除
     * @param pos 实体对象集合
     * @return int 更新记录数
     */
    default int deletePos(Collection<P> pos) {
        int result = getDao().deleteAll(pos);
        return result;
    }

    /**
     * 删除记录
     * @param id 主键
     * @return int 更新记录数
     * @throws Exception
     */
    default int delete(ID id) throws Exception {
        V vo = get(id);
        return delete(vo);
    }

    /**
     * 获取视图对象
     * @param id 主键
     * @return 视图对象
     */
    default V get(ID id) {
        // 默认驱逐,这样更安全,但在某些需求下性能有所降低,自主调用不驱逐方法
        return get(id, true);
    }

    /**
     * 获取视图对象
     * @param id 主键
     * @param evict 是否驱逐缓存
     * @return 视图对象
     */
    default V get(ID id, boolean evict) {
        P po = getDao().get(id);
        V vo = parseVo(po, evict);
        return vo;
    }

    /**
     * 批量获取视图对象
     * @param ids 主键集合
     * @return 视图对象集合
     */
    default Map<ID, V> gets(Collection<ID> ids) {
        return gets(ids, true);
    }

    /**
     * 批量获取视图对象
     * @param ids 主键集合
     * @param evict 是否驱逐缓存
     * @return 视图对象集合
     */
    default Map<ID, V> gets(Collection<ID> ids, boolean evict) {
        Map<ID, P> pos = getDao().gets(ids);
        Map<ID, V> vos = new HashMap<>(pos.size());
        for (Map.Entry<ID, P> entry : pos.entrySet()) {
            vos.put(entry.getKey(), parseVo(entry.getValue(), evict));
        }
        return vos;
    }

    /**
     * 获取视图列表
     * @param pagination 分页信息
     * @param queryParam 查询条件
     * @return 视图对象列表
     */
    default List<V> list(Pagination pagination, QueryParam<?> queryParam) {
        return list(pagination, queryParam, true);
    }

    /**
     * 获取视图列表
     * @param pagination 分页信息
     * @param queryParam 查询条件
     * @param evict 是否驱逐缓存
     * @return 视图对象列表
     */
    default List<V> list(Pagination pagination, QueryParam<?> queryParam, boolean evict) {
        List<P> pos = getDao().list(pagination, queryParam);
        return parseVos(pos, evict);
    }

    /**
     * 从缓存中驱逐
     * @param entity 实体类
     */
    default void evict(Object entity) {
        getDao().evict(entity);
    }

}
