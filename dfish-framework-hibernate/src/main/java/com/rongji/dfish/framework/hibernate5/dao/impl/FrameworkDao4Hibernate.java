package com.rongji.dfish.framework.hibernate5.dao.impl;

import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.base.exception.MarkedRuntimeException;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.dao.FrameworkDao;
import com.rongji.dfish.framework.dao.impl.AbstractFrameworkDao;
import com.rongji.dfish.framework.dto.QueryParam;
import com.rongji.dfish.framework.hibernate.support.EntitySupport;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 接口，Hibernate的数据访问层定义
 * @author lamontYu
 * @since DFish5.0
 */
public class FrameworkDao4Hibernate<P, ID extends Serializable> extends AbstractFrameworkDao<P, ID> {
    @Resource(name = "hibernateTemplate")
    private HibernateTemplate hibernateTemplate;

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    private EntitySupport<P> entitySupport;

    @PostConstruct
    protected void init() {
        entitySupport = new EntitySupport<>(getClass());
    }

    /**
     * 查询返回对象
     * @param hql  hql语句
     * @param args 参数
     * @param <T>  数据记录对象
     * @return 结果数据对象
     */
    public <T> T queryForObject(final String hql, final Object... args) {
        List<T> list = queryForList(hql, args);
        if (Utils.notEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询返回对象集合
     * @param hql  hql语句
     * @param args 参数
     * @param <T>  数据记录对象
     * @return 结果数据对象集合
     */
    public <T> List<T> queryForList(final String hql, final Object... args) {
        return getHibernateTemplate().execute((session) -> queryForList(session, hql, args));
    }

    /**
     * 查询返回对象集合
     * @param session 会话
     * @param hql  hql语句
     * @param args 参数
     * @param <T>  数据记录对象
     * @return 结果数据对象
     */
    private <T> List<T> queryForList(Session session, String hql, Object... args) {
        Query query = session.createQuery(hql);
        if (args != null) {
            int i = 0;
            for (Object arg : args) {
                query.setParameter(i++, arg);
            }
        }
        List<T> list = query.list();
        return list;
    }

    /**
     * 查询返回对象集合
     * @param hql  hql语句
     * @param pagination 分页信息
     * @param args 参数
     * @param <T>  数据记录对象
     * @return 结果数据对象
     */
    public <T> List<T> queryForList(final String hql, final Pagination pagination, final Object... args) {
        if (pagination == null || pagination.getLimit() < 0) {
            return queryForList(hql, args);
        }
        List<T> result = getHibernateTemplate().execute((session) -> {
            Query query = session.createQuery(hql);
            if (args != null) {
                int i = 0;
                for (Object arg : args) {
                    query.setParameter(i++, arg);
                }
            }

            query.setFirstResult(pagination.getOffset());
            query.setMaxResults(pagination.getLimit());

            List<T> resultList = query.list();
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
                        List countResult = queryForList(session, countSql, args);
                        int count = ((Number) countResult.get(0)).intValue();
                        pagination.setSize(count);
                    } catch (Exception ex) {
                        LogUtil.error("自动计算数据行数时发生未知错误", ex);
                        throw new MarkedRuntimeException("自动计算数据行数时发生未知错误，建议设置page.setAutoRowCount(false);并自行计算数据行数。\r\n" + countSql, "DFISH-01000");
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
    public void evict(Object entity) {
        if (entity != null) {
            getHibernateTemplate().evict(entity);
        }
    }

    /**
     * 批量更新/删除
     * @param hql hql语句
     * @param args 参数
     * @return 更新记录数
     */
    public int bulkUpdate(String hql, Object... args) {
        return getHibernateTemplate().bulkUpdate(hql, args);
    }

    /**
     * 批量更新/删除
     * @param hql hql语句
     * @param argList 参数集合
     * @return 更新记录数组
     */
    public int[] batchUpdate(String hql, List<Object[]> argList) {
        if (Utils.isEmpty(argList)) {
            return new int[0];
        }
        return getHibernateTemplate().execute((session) -> {
            Query query = session.createQuery(hql);
            int[] result = new int[argList.size()];
            int index = 0;
            for (Object[] args : argList) {
                if (Utils.notEmpty(args)) {
                    int i = 0;
                    for (Object arg : args) {
                        query.setParameter(i++, arg);
                    }
                }
                result[index++] = query.executeUpdate();
            }
            return result;
        });
    }

    @Override
    public P get(ID id) {
        if (id == null) {
            return null;
        }
        Class<P> entityClass = entitySupport.getEntityClass();
        P po = getHibernateTemplate().get(entityClass, id);
        return po;
    }

    @Override
    public ID getEntityId(P entity) {
        Method getter = entitySupport.getIdGetter();
        ID id = null;
        try {
            id = (ID) getter.invoke(entity);
        } catch (Exception e) {
            LogUtil.error("", e);
        }
        return id;
    }

    @Override
    public List<P> listByIds(List<ID> ids) {
        Set<ID> idSet = new HashSet<>(ids);
        idSet.remove(null);
        idSet.remove("");
        List<ID> noRepeat = new ArrayList<>(idSet);
        List<P> dbList = getHibernateTemplate().execute((session) -> {
            List<ID> leftList = noRepeat;
            List<P> result = new ArrayList<>(leftList.size());
            String idFieldName = entitySupport.getIdName();
            Class<P> entityClass = entitySupport.getEntityClass();
            try {
                while (leftList.size() > 0) {
                    List<ID> currentList;
                    int leftSize = leftList.size();
                    boolean hasNext = leftSize > FrameworkDao.BATCH_SIZE;
                    if (hasNext) {
                        currentList = leftList.subList(0, FrameworkDao.BATCH_SIZE);
                        leftList = leftList.subList(FrameworkDao.BATCH_SIZE, leftSize);
                    } else {
                        currentList = leftList;
                    }
                    List list = session.createCriteria(entityClass).add(Restrictions.in(idFieldName, currentList)).list();
                    if (list != null) {
                        result.addAll(list);
                    }
                    if (!hasNext) {
                        break;
                    }
                }
            } catch (Exception e) {
                LogUtil.error("批量获取数据异常@" + entityClass + ".idFieldName:" + idFieldName, e);
            }
            return result;
        });
        return dbList;
    }

    @Override
    public List<P> list(Pagination pagination, QueryParam queryParam) {
        // 默认按照ID排序
        String hql = "FROM " + entitySupport.getEntityName() + " t ORDER BY t." + entitySupport.getIdName();
        return queryForList(hql, pagination);
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

    @Override
    public int bulkSave(Collection<P> entities) {
        if (Utils.isEmpty(entities)) {
            return 0;
        }
        return getHibernateTemplate().execute((session) -> {
            for (P entity : entities) {
                session.save(entity);
            }
            return entities.size();
        });
    }

    @Override
    public int bulkUpdate(Collection<P> entities) {
        if (Utils.isEmpty(entities)) {
            return 0;
        }
        return getHibernateTemplate().execute((session) -> {
            for (P entity : entities) {
                session.update(entity);
            }
            return entities.size();
        });
    }

    @Override
    public int bulkDelete(Collection<P> entities) {
        if (Utils.isEmpty(entities)) {
            return 0;
        }
        return getHibernateTemplate().execute((session) -> {
            for (P entity : entities) {
                session.delete(entity);
            }
            return entities.size();
        });
    }

}
