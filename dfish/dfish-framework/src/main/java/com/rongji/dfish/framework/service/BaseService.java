package com.rongji.dfish.framework.service;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.dao.BaseDao;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 
 * @author DFish Team
 *
 * @param <T> 实体对象类型Entity
 * @param <ID> ID对象类型通常是String
 */
public abstract class BaseService<T, ID extends Serializable> extends BaseDao<T, ID> {

    protected void beforeSave(T entity) throws Exception {
    }

    protected void afterSave(T entity) throws Exception {
    }

    protected void beforeUpdate(T entity) throws Exception {
    }

    protected void afterUpdate(T entity) throws Exception {
    }

    protected void beforeDelete(T entity)  throws Exception {
    }

    protected void afterDelete(T entity) throws Exception {
    }

    public int saveOrUpdate(T entity) throws Exception {
        if (entity == null) {
            return 0;
        }
        Method idGetter = getEntityIdGetter();
        Object result = idGetter.invoke(entity);
        if (result == null || "".equals(result)) {
            return save(entity);
        } else {
            return update(entity);
        }
    }

    @Override
    @Transactional
    public int save(T entity) throws Exception {
        beforeSave(entity);
        int result = super.save(entity);
        if (result > 0) {
            afterSave(entity);
        }
        return result;
    }

    @Override
    @Transactional
    public int update(T entity) throws Exception {
        beforeUpdate(entity);
        int result = super.update(entity);
        if (result > 0) {
            afterUpdate(entity);
        }
        return result;
    }

    @Override
    @Transactional
    public <S extends T> int deleteAll(Collection<S> entities) throws Exception {
        if (Utils.isEmpty(entities)) {
            return 0;
        }
        for (S e : entities) {
            beforeDelete(e);
        }
        int result = super.deleteAll(entities);
        if (result > 0) {
            for (S e : entities) {
                afterDelete(e);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public int delete(T entity) throws Exception {
        beforeDelete(entity);
        int result = super.delete(entity);
        if (result > 0) {
            afterDelete(entity);
        }
        return result;
    }

    @Override
    @Transactional
    public int delete(ID id) throws Exception {
        T e = get(id);
        beforeDelete(e);
        int result = super.delete(id);
        if (result > 0) {
            afterDelete(e);
        }
        return result;
    }

}
