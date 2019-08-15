package com.rongji.dfish.framework.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Id;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.IdGenerator;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.rongji.dfish.framework.FrameworkHelper;

/**
 * @param <T>  实体对象类型Entity
 * @param <ID> ID对象类型通常是String
 * @author DFish Team
 */
@SuppressWarnings("unchecked")
public abstract class BaseDao<T, ID extends Serializable> {
    @Autowired
    protected PubCommonDAO pubCommonDAO;

    public PubCommonDAO getPubCommonDAO() {
        return pubCommonDAO;
    }

    public void setPubCommonDAO(PubCommonDAO pubCommonDAO) {
        this.pubCommonDAO = pubCommonDAO;
    }

    protected Class<?> entityClass;
    protected String entityIdName;
    protected Method entityIdGetter;

    protected Class<?> getEntiyType() {
        if (entityClass == null) {
            entityClass = getEntiyType(getClass());
        }
        return entityClass;
    }

    protected static Class<?> getEntiyType(Class<?> clz) {
//		clz.getSuperclass();
        Class<?> workinClz = clz;
        while (true) {
            if (workinClz == Object.class) {
                break;
            }
            Type genType = workinClz.getGenericSuperclass();
            if (!(genType instanceof ParameterizedType)) {
                workinClz = workinClz.getSuperclass();
                continue;
            }

            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (params == null || params.length == 0 || !(params[0] instanceof Class)) {
                workinClz = workinClz.getSuperclass();
                continue;
            }
            Class<?> entityClass = (Class<?>) params[0];
            return entityClass;
        }
        throw new UnsupportedOperationException("can not recognize the entity TYPE of " + clz.getName());
    }

    protected Method getEntityIdGetter() {
        if (entityIdGetter == null) {
            entityIdGetter = getEntityIdGetter(getEntiyType());
        }
        return entityIdGetter;
    }

    protected static Method getEntityIdGetter(Class<?> entityClass) {
        if (entityClass != null) {
            for (Method method : entityClass.getMethods()) {
                if (method.getAnnotation(Id.class) != null) {
                    String methodsName = method.getName();
                    if (methodsName.startsWith("get")) {
                        return method;
                    }
                    break;
                }
            }
        }
        return null;
    }

    protected String getEntityIdName() {
        if (Utils.isEmpty(entityIdName)) {
            entityIdName = getFieldName(getEntityIdGetter());
        }
        return entityIdName;
    }

    private static String getFieldName(Method method) {
        if (method == null) {
            return null;
        }
        String fieldName = null;
        if (method != null) {
            fieldName = method.getName().substring(3);
            char c = fieldName.charAt(0);
            if (c >= 'A' && c <= 'Z') {
                fieldName = ((char) (c + 32)) + fieldName.substring(1);
            }
        }
        return fieldName;
    }

    protected static String getEntityIdName(Class<?> entityClass) {
        return getFieldName(getEntityIdGetter(entityClass));
    }
    @SuppressWarnings("unchecked")
    @Deprecated
    public T find(ID id) {
        if (id == null) {
            return null;
        }
        String idName = getEntityIdName();
        return (T) pubCommonDAO.queryAsAnObject("FROM " + getEntiyType().getName() + " t WHERE t." + idName + "=?", id);
    }

    public String getNewId() {
        return IdGenerator.getSortedId32();
    }

    /**
     * 根据给定的ID列表 返回指定该ID指定的列表
     * 如果其中，ID指定的数据不存在，返回结果List中，相对应的位置，为null
     * 如果ID 有重复，返回结果也是有重复。但并不会进行数据库的多次查询
     * 改方法将批量调用数据库根据主键查询的方法，形如
     * SELECT t.xx FROM Xxx t WHERE t.id=? OR t.id=?;
     * 每个批次默认不多于50个ID。超过，则进行下一批查询。
     *
     * @param ids ID
     * @return List
     */
    public List<T> findAll(List<ID> ids) {
        if (ids == null) {
            return null;
        }
        final Class<?> entityClass = getEntiyType();
        String idName = null;
        Method getterMethod = null;
        for (Method m : entityClass.getMethods()) {
            if (m.getAnnotation(Id.class) != null) {
                String methodsName = m.getName();
                if (methodsName.startsWith("get") || methodsName.startsWith("set")) {
                    idName = methodsName.substring(3);
                    char c = idName.charAt(0);
                    if (c >= 'A' && c <= 'Z') {
                        idName = ((char) (c + 32)) + idName.substring(1);
                    }
                    if (methodsName.startsWith("get")) {
                        getterMethod = m;
                    }
                }
                break;
            }
        }
        if (idName == null) {
            for (Field f : entityClass.getFields()) {
                if (f.getAnnotation(Id.class) != null) {
                    idName = f.getName();
                    break;
                }
            }
        }
        if (getterMethod == null) {
            char c = idName.charAt(0);
            if (c >= 'a' && c <= 'z') {
                String getterName = "get" + ((char) (c - 32)) + idName.substring(1);
                try {
                    getterMethod = entityClass.getMethod(getterName, NO_PARAMS);
                } catch (Exception e) {
                    FrameworkHelper.LOG.error("", e);
                }
            }
        }
        final String idName2 = idName;
        Set<ID> idSet = new HashSet<ID>(ids);
        idSet.remove(null);
        final List<ID> norepeat = new ArrayList<ID>(idSet);
        List<T> dbrs = (List<T>) pubCommonDAO.getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
            @Override
            public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
                List<ID> tofetch = norepeat;
                List<T> result = new ArrayList<T>();
                while (tofetch.size() > 0) {
                    if (tofetch.size() > FETCH_SIZE) {
                        List<ID> cur = tofetch.subList(0, FETCH_SIZE);
                        tofetch = tofetch.subList(FETCH_SIZE, tofetch.size());
                        result.addAll(session.createCriteria(entityClass).add(Restrictions.in(idName2, cur)).list());
                    } else {
                        result.addAll(session.createCriteria(entityClass).add(Restrictions.in(idName2, tofetch)).list());
                        tofetch = tofetch.subList(tofetch.size(), tofetch.size());
                    }
                }
                return result;
            }
        });
        //重新排序并且,补充没查到的数据
        HashMap<ID, T> map = new HashMap<ID, T>();
        for (T item : dbrs) {
            try {
                ID i = (ID) getterMethod.invoke(item, NO_ARGS);
                map.put(i, item);
            } catch (Exception e) {
                FrameworkHelper.LOG.error("", e);
            }
        }
        List<T> result = new ArrayList<T>();
        for (ID i : ids) {
            result.add(map.get(i));
        }
        return result;
    }

    private static final Object[] NO_ARGS = new Object[0];
    private static final Class<?>[] NO_PARAMS = new Class<?>[0];

    /**
     * 利用hibernate默认的get方法获取对象
     * 注意此方法，取出的对象如果进行了set操作，很可能会影响其他线程
     * 并且不用显式调用update也可能会被写入到数据库。
     *
     * @param id
     * @return
     */
    public T get(ID id) {
        if (id == null) {
            return null;
        }
        final Class<?> entityClass = getEntiyType();
        return (T) pubCommonDAO.getHibernateTemplate().get(entityClass, id);
    }

    public int update(T entity) throws Exception {
        return pubCommonDAO.update(entity);
    }

    public <S extends T> int deleteAll(Collection<S> entities) throws Exception {
        if (entities == null) {
            return 0;
        }
        pubCommonDAO.getHibernateTemplate().deleteAll(entities);
        return entities.size();
    }

    public int delete(T entity) throws Exception {
        return pubCommonDAO.delete(entity);
    }

    public int delete(ID id) throws Exception {
        return pubCommonDAO.delete(get(id));
    }

    public int save(T entity) throws Exception {
        return pubCommonDAO.save(entity);
    }

    public static final int BATCH_SIZE = 512;
    public static final int FETCH_SIZE = 50;

    public static void appendParamStr(StringBuilder sql, int paramCount) {
        if (sql == null) {
            return;
        }
        boolean isFirst = true;
        for (int i = 0; i < paramCount; ++i) {
            if (isFirst) {
                isFirst = false;
            } else {
                sql.append(',');
            }
            sql.append('?');
        }
    }

    public static String getParamStr(int paramCount) {
        StringBuilder sql = new StringBuilder();
        appendParamStr(sql, paramCount);
        return sql.toString();
    }

}
