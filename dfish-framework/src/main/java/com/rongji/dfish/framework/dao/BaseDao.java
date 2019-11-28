package com.rongji.dfish.framework.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.persistence.Id;

import com.rongji.dfish.base.Utils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.rongji.dfish.framework.FrameworkHelper;

/**
 * @param <P>  实体对象类型Entity
 * @param <ID> ID对象类型通常是String
 * @author DFish Team
 */
@SuppressWarnings("unchecked")
public abstract class BaseDao<P, ID extends Serializable> {
    /**
     * 排序字段-序号
     */
    public static final String SORT_FIELD_ORDER = "ORDER";
    /**
     * 排序字段-名称
     */
    public static final String SORT_FIELD_NAME = "NAME";
    /**
     * 排序字段-修改时间
     */
    public static final String SORT_FIELD_TIME = "TIME";
    /**
     * 排序字段-主键编号
     */
    public static final String SORT_FIELD_ID = "ID";

    /**
     * 排序方向-正序
     */
    public static final String SORT_DIRECTION_ASC = "ASC";
    /**
     * 排序方向-倒序
     */
    public static final String SORT_DIRECTION_DESC = "DESC";
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

    protected Class<?> getEntityType() {
        if (entityClass == null) {
            entityClass = getEntityType(getClass());
        }
        return entityClass;
    }

    protected static Class<?> getEntityType(Class<?> clz) {
//		clz.getSuperclass();
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
            Class<?> entityClass = (Class<?>) params[0];
            return entityClass;
        }
        throw new UnsupportedOperationException("can not recognize the entity TYPE of " + clz.getName());
    }

    protected Method getEntityIdGetter() {
        if (entityIdGetter == null) {
            entityIdGetter = getEntityIdGetter(getEntityType());
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
    public Map<ID, P> gets(Collection<ID> ids) {
        if (ids == null) {
            return null;
        }
        final Class<?> entityClass = getEntityType();
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
        final List<ID> noRepeat = new ArrayList<ID>(idSet);
        List<P> dbList = (List<P>) pubCommonDAO.getHibernateTemplate().execute(new HibernateCallback<List<P>>() {
            @Override
            public List<P> doInHibernate(Session session) throws HibernateException, SQLException {
                List<ID> tofetch = noRepeat;
                List<P> result = new ArrayList<P>();
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
        Map<ID, P> map = new HashMap<>();
        for (P item : dbList) {
            try {
                ID i = (ID) getterMethod.invoke(item, NO_ARGS);
                map.put(i, item);
            } catch (Exception e) {
                FrameworkHelper.LOG.error("", e);
            }
        }
        return map;
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
    public P get(ID id) {
        if (id == null) {
            return null;
        }
        final Class<?> entityClass = getEntityType();
        return (P) pubCommonDAO.getHibernateTemplate().get(entityClass, id);
    }

    public int deleteAll(Collection<P> entities) {
        if (Utils.isEmpty(entities)) {
            return 0;
        }
        Set<P> set = new HashSet<>(entities.size());
        for (P p : entities) {
            if (p != null) {
                set.add(p);
            }
        }
        pubCommonDAO.getHibernateTemplate().deleteAll(set);
        return set.size();
    }

    public int delete(P entity) {
        if (entity == null) {
            return 0;
        }
        return pubCommonDAO.delete(entity);
    }

    public int delete(ID id) {
        return delete(get(id));
    }

    public int save(P entity) {
        return pubCommonDAO.save(entity);
    }

    public int update(P entity) {
        return pubCommonDAO.update(entity);
    }

    public int saveOrUpdate(P entity) {
        return pubCommonDAO.saveOrUpdate(entity);
    }

    public int saveOrUpdateAll(final List<P> entities) {
        if (entities == null) {
            return 0;
        }
        pubCommonDAO.getHibernateTemplate().saveOrUpdateAll(entities);
        return entities.size();
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

    private Map<String, String> fieldMap = new HashMap<>();

    @PostConstruct
    protected void init() {
        registerSortField(SORT_FIELD_TIME, "updateTime");

        registerSortField4Suffix(SORT_FIELD_ID, "Id");
        registerSortField4Suffix(SORT_FIELD_ORDER, "Order");
        registerSortField4Suffix(SORT_FIELD_NAME, "Name");
    }

    protected void registerSortField(String sortField, String sortFieldName) {
        if (Utils.isEmpty(sortField) || Utils.isEmpty(sortFieldName)) {
            return;
        }
        String oldName = fieldMap.get(sortField);
        if (oldName != null) {
            FrameworkHelper.LOG.warn("The field[" + sortField + "] has been registered.");
        } else {
            fieldMap.put(sortField, sortFieldName);
        }
    }

    protected void registerSortField4Suffix(String sortField, String sortSuffixFieldName) {
        if (Utils.isEmpty(sortField) || Utils.isEmpty(sortSuffixFieldName) || Utils.isEmpty(sortPreFieldName())) {
            return;
        }
        registerSortField(sortField, sortPreFieldName() + sortSuffixFieldName);
    }

    protected String sortPreFieldName() {
        return "";
    }

    protected String getSortFieldName(String sortField) {
        return fieldMap.get(sortField);
    }

    protected String getSortSql(List<SortDefine> sortList, String tableAlias) {
        StringBuilder sortSql = new StringBuilder();
        if (Utils.notEmpty(sortList)) {
            boolean isFirst = true;
            tableAlias = tableAlias == null ? "" : tableAlias;
            for (SortDefine sort : sortList) {
                String fieldName = getSortFieldName(sort.getField());
                if (Utils.notEmpty(fieldName)) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        sortSql.append(',');
                    }
                    sortSql.append(tableAlias).append('.').append(fieldName);
                    if (Utils.notEmpty(sort.getDirection())) {
                        sortSql.append(' ').append(sort.getDirection());
                    }
                }
            }
        }
        return sortSql.toString();
    }

    protected String getSortFields(List<SortDefine> sortList) {
        return getSortSql(sortList, "t");
    }

    public void evictObject(Object obj) {
        getPubCommonDAO().evictObject(obj);
    }

}
