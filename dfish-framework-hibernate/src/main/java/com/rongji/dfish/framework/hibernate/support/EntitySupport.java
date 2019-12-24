package com.rongji.dfish.framework.hibernate.support;

import com.rongji.dfish.base.util.LogUtil;

import javax.persistence.Id;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author lamontYu
 * @date 2019-12-18
 * @since 5.0
 */
public class EntitySupport<P> {

    private Class<P> entityClass;
    private String entityName;
    private String idName;
    private Method idGetter;

    public EntitySupport(Class<?> clz) {
        if (clz == null) {
            throw new IllegalArgumentException("clz == null");
        }
        try {
            entityClass = getEntityClass(clz);
            entityName = entityClass.getName();
            if (entityClass != null) {
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
        } catch (Exception e) {
            LogUtil.error("加载实体类信息异常", e);
        }

    }

    private <P> Class<P> getEntityClass(Class<?> clz) {
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

    public Class<P> getEntityClass() {
        return entityClass;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getIdName() {
        return idName;
    }

    public Method getIdGetter() {
        return idGetter;
    }
}
