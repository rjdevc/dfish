package com.rongji.dfish.framework.hibernate.dao.impl;

import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.dao.FrameworkDao;
import com.rongji.dfish.framework.dto.QueryParam;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author lamontYu
 * @create 2019-12-04 16:02
 */
public class FrameworkDao4Hibernate<P, ID extends Serializable> extends HibernateDaoSupport implements FrameworkDao<P, ID> {

    protected Class<P> entityClass;
    protected String entityIdName;
    protected Method entityIdGetter;

    protected Class<P> getEntityClass() {
        if (entityClass == null) {
            entityClass = getEntityClass(getClass());
        }
        return entityClass;
    }

    protected static <P> Class<P> getEntityClass(Class<?> clz) {
        Class<?> workingClz = clz;
        while (true) {
            if (workingClz == Object.class) {
                break;
            }
            Type genType = workingClz.getGenericSuperclass();
            if (!(genType instanceof ParameterizedType)) {
                workingClz = workingClz.getSuperclass();
                continue;
            }

            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (params == null || params.length == 0 || !(params[0] instanceof Class)) {
                workingClz = workingClz.getSuperclass();
                continue;
            }
            Class<P> entityClass = (Class<P>) params[0];
            return entityClass;
        }
        throw new UnsupportedOperationException("can not recognize the entity type.[" + clz.getName() + "]");
    }

    protected Method getEntityIdGetter() {
        if (entityIdGetter == null) {
            entityIdGetter = getEntityIdGetter(getEntityClass());
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

    protected List<?> list(final String hql, final Object... args) {
        return getHibernateTemplate().execute((session) -> list(session, hql, args));
    }

    private List<?> list(Session session, String hql, Object... args) {
        Query query = session.createQuery(hql);
        if (args != null) {
            int i = 0;
            for (Object arg : args) {
                query.setParameter(i++, arg);
            }
        }
        List<?> list = query.list();
        return list;
    }

    protected List<P> list(final String hql, final Pagination pagination, final Object... args) {
        if (pagination == null || pagination.getLimit() < 0) {
            return (List<P>) list(hql, args);
        }
        List<P> result = getHibernateTemplate().execute((session) -> {
            Query query = session.createQuery(hql);
            if (args != null) {
                int i = 0;
                for (Object arg : args) {
                    query.setParameter(i++, arg);
                }
            }

            query.setFirstResult(pagination.getOffset());
            query.setMaxResults(pagination.getLimit());

            List<P> resultList = query.list();
            if (pagination.isAutoRowCount()) {
                // 2017-12-22 LinLW
                // 查询结果页未满,无需count
                //如果翻到某一页后突然没数据了，还是要去count实际大小。
                boolean noCount = pagination.getLimit() > resultList.size() && (resultList.size() > 0 || pagination.getOffset() == 0);
                if (noCount) {
                    pagination.setSize(pagination.getOffset() + resultList.size());
                } else {
                    String countSql = FrameworkDao.getCountSql(hql);
                    try {
                        List<?> countResult = list(session, countSql, args);
                        int count = ((Number) countResult.get(0)).intValue();
                        pagination.setSize(count);
                    } catch (Exception ex) {
                        LogUtil.error("自动计算数据行数时发生未知错误", ex);
                        throw new RuntimeException(new DfishException("自动计算数据行数时发生未知错误，建议设置page.setAutoRowCount(false);并自行计算数据行数。\r\n" + countSql, "DFISH-01000"));
                    }
                }
            }
            //LinLW 2017-07-13 如果当前页号大于1 查询结果数为0，并且autoRowcount为true。很可能是最后一页被删了。要回头显示前面的页。
            if (pagination.isAutoRowCount() && resultList.size() == 0 && pagination.getOffset() > 0) {
                //根据page的rowCount计算当前页的数量
                int offset = Pagination.calculateOffset(pagination.getSize(), pagination.getLimit());
                pagination.setOffset(offset);
                query.setFirstResult(offset);
                query.setMaxResults(pagination.getLimit());
                resultList = query.list();
            }

            return resultList;
        });
        return result;
    }

    @Override
    public P get(ID id) {
        if (id == null) {
            return null;
        }
        Class<P> entityClass = getEntityClass();
        return getHibernateTemplate().get(entityClass, id);
    }

    @Override
    public ID getEntityId(P entity) {
        Method getter = getEntityIdGetter();
        ID id = null;
        try {
            id = (ID) getter.invoke(entity);
        } catch (Exception e) {
            FrameworkHelper.LOG.error("", e);
        }
        return id;
    }

    @Override
    public List<P> listByIds(Collection<ID> ids) {
        Set<ID> idSet = new HashSet<>(ids);
        idSet.remove(null);
        idSet.remove("");
        final List<ID> noRepeat = new ArrayList<>(idSet);
        final String idFieldName = getEntityIdName();
        List<P> dbList = getHibernateTemplate().execute((session) -> {
            List<ID> tofetch = noRepeat;
            List<P> result = new ArrayList<P>();
            while (tofetch.size() > 0) {
                if (tofetch.size() > FrameworkDao.BATCH_SIZE) {
                    List<ID> cur = tofetch.subList(0, FrameworkDao.BATCH_SIZE);
                    tofetch = tofetch.subList(FrameworkDao.BATCH_SIZE, tofetch.size());
                    result.addAll(session.createCriteria(entityClass).add(Restrictions.in(idFieldName, cur)).list());
                } else {
                    result.addAll(session.createCriteria(entityClass).add(Restrictions.in(idFieldName, tofetch)).list());
                    tofetch = tofetch.subList(tofetch.size(), tofetch.size());
                }
            }
            return result;
        });
        return dbList;
    }

    @Override
    public List<P> list(Pagination pagination, QueryParam queryParam) {
        String hql = "FROM " + getEntityClass().getName();
        return list(hql, pagination);
    }

    @Override
    public int save(P entity) {
        if (entity == null) {
            return 0;
        }
        getHibernateTemplate().save(entity);
        return 1;
    }

    @Override
    public int update(P entity) {
        if (entity == null) {
            return 0;
        }
        getHibernateTemplate().update(entity);
        return 1;
    }

    @Override
    public int saveOrUpdate(P entity) {
        if (entity == null) {
            return 0;
        }
        getHibernateTemplate().saveOrUpdate(entity);
        return 1;
    }

    @Override
    public int delete(P entity) {
        if (entity == null) {
            return 0;
        }
        getHibernateTemplate().delete(entity);
        return 1;
    }

    @Override
    public int delete(ID id) {
        return delete(get(id));
    }

    @Override
    public int deleteAll(Collection<P> entities) {
        if (entities == null) {
            return 0;
        }
        getHibernateTemplate().deleteAll(entities);
        return entities.size();
    }

    public void evict(P entity) {
        if (entity != null) {
            getHibernateTemplate().evict(entity);
        }
    }

    public int bulkUpdate(String hql, Object... args) {
        return getHibernateTemplate().bulkUpdate(hql, args);
    }

}
