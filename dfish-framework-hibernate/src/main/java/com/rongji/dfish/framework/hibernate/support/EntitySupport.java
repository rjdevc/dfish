package com.rongji.dfish.framework.hibernate.support;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 实体类支持类,用于实现框架内核中默认方法实体类的创建,这样让默认的子类只关注本身的方法,而不用关注常用方法的实现
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class EntitySupport<P> {

    private Class<P> entityClass;
    private String entityName;
    private String idName;
    private Method idGetter;

    /**
     * 构造函数
     *
     * @param clz 实体类Class
     */
    public EntitySupport(Class clz) {
        if (clz == null) {
            throw new IllegalArgumentException("clz == null");
        }
        try {
            entityClass = getEntityClass(clz);
            entityName = entityClass.getName();
            if (entityClass != null) {
                // FIXME 联合主键的问题以后有时间再考虑优化
                for (Field field : entityClass.getDeclaredFields()) {
                    if (field.getAnnotation(Id.class) != null) {
                        idName = field.getName();
                        String methodName = "get" + idName.substring(0, 1).toUpperCase() + idName.substring(1);
                        idGetter = entityClass.getMethod(methodName);
                        break;
                    }
                }
                if (Utils.isEmpty(idName)) {
                    for (Method method : entityClass.getMethods()) {
                        if (method.getAnnotation(Id.class) != null) {
                            String methodName = method.getName();
                            if (methodName.startsWith("get")) {
                                idGetter = method;
                                idName = methodName.substring(3);
                                idName = idName.substring(0, 1).toLowerCase() + idName.substring(1);
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.error("加载实体类信息异常@" + clz, e);
        }

    }

    /**
     * 获取实体类Class
     *
     * @param clz Class 当前实现类,一般是DAO
     * @return Class 实体类Class
     */
    private <P> Class<P> getEntityClass(Class<P> clz) {
        Class workingClz = clz;
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

    /**
     * 获取实体类Class
     *
     * @return Class
     */
    public Class<P> getEntityClass() {
        return entityClass;
    }

    /**
     * 获取实体类名称
     *
     * @return String
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * 获取实体类中ID名称
     *
     * @return String
     */
    public String getIdName() {
        return idName;
    }

    /**
     * 获取实体类中ID属性的getter方法
     *
     * @return Method
     */
    public Method getIdGetter() {
        return idGetter;
    }
}
