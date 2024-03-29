package com.rongji.dfish.framework.dto;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.util.DateUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.framework.util.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import java.beans.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.*;

/**
 * 查询参数类,用于数据传输过程中查询参数封装对象
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class QueryParam<T extends QueryParam<T>> implements Serializable {

    private static final long serialVersionUID = -4197926894871919855L;
    /**
     * 查询开始时间
     */
    protected Date beginTime;
    /**
     * 查询结束时间
     */
    protected Date endTime;

    /**
     * 查询开始时间
     *
     * @return Date 查询开始时间
     */
    public Date getBeginTime() {
        return beginTime;
    }

    /**
     * 查询开始时间
     *
     * @param beginTime Date 查询开始时间
     * @return 本身，这样可以继续设置其他属性
     */
    public T setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
        return (T) this;
    }

    /**
     * 查询结束时间
     *
     * @return 查询结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 查询结束时间
     *
     * @param endTime 查询结束时间
     * @return 本身，这样可以继续设置其他属性
     */
    public T setEndTime(Date endTime) {
        this.endTime = endTime;
        return (T) this;
    }

    /**
     * 修正endTime的时间,主要用于日期查询,结束时间天数要加1
     *
     * @param field 单位
     * @param amount 数量
     * @return 本身，这样可以继续设置其他属性
     */
    public T addEndTime(int field, int amount) {
        return setEndTime(addTime(this.endTime, field, amount));
    }

    /**
     * 修正时间,主要用于日期查询,结束时间天数要加1
     * @param date 时间
     * @param field 单位
     * @param amount 数量
     * @return Date 修正的时间
     */
    public Date addTime(Date date, int field, int amount) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }

    protected static Map<Class, Map<String, FieldMethods>> paramMethods = new HashMap<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Map<String, FieldMethods> fields = getDefineFields();
        if (fields != null) {
            for (Map.Entry<String, FieldMethods> entry : fields.entrySet()) {
                FieldMethods field = entry.getValue();
                String fieldName = entry.getKey();
                try {
                    // 获取属性的类型
                    Class fieldType = field.getFieldType();
                    Method getterMethod = field.getGetter();
                    Object value = getterMethod.invoke(this);
                    if (value == null) {
                        continue;
                    }
                    String[] strValues = toStringValue(value, fieldType);
                    String valueStr = StringUtil.join(strValues);
                    if (Utils.notEmpty(valueStr)) {
                        sb.append('&').append(fieldName).append('=');
                        try {
                            sb.append(URLEncoder.encode(valueStr, "UTF-8"));
                        } catch (Exception e) {
                            sb.append(valueStr);
                        }
                    }
                } catch (Exception e) {
                    LogUtil.error("获取属性值异常@" + getClass().getName() + "." + fieldName + "", e);
                }
            }
        }
        return sb.toString();
    }

    private String[] toStringValue(Object value, Class fieldType) {
        String[] strArray;
        // Boolean型显示的值是0/1
        if (fieldType == Boolean.class || fieldType == boolean.class) {
            Boolean cast = (Boolean) value;
            // Boolean的值
            strArray = new String[]{cast.toString()};
        } else if (fieldType == String[].class) {
            // 字符串数组
            strArray = (String[]) value;
        } else if (Collection.class.isAssignableFrom(fieldType)) {
            // 字符串集合
            Collection cast = (Collection) value;
            strArray = new String[cast.size()];
            if (Utils.notEmpty(cast)) {
                int i = 0;
                for (Object obj : cast) {
                    if (obj != null) {
                        strArray[i++] = obj.toString();
                    }
                }
            }
        } else if (fieldType == Date.class) {
            strArray = new String[]{DateUtil.format((Date) value, DateUtil.LV_FULL)};
        } else {
            strArray = new String[]{value.toString()};
        }
        return strArray;
    }

    /**
     * 转换为参数请求对象
     *
     * @return RequestParam
     */
    public RequestParam toRequestParam() {
        RequestParam requestParam = new RequestParam();
        Map<String, FieldMethods> fields = getDefineFields();
        if (fields != null) {
            for (Map.Entry<String, FieldMethods> entry : fields.entrySet()) {
                FieldMethods field = entry.getValue();
                String fieldName = entry.getKey();
                try {
                    Method getterMethod = field.getGetter();
                    Object value = getterMethod.invoke(this);
                    if (value == null) {
                        continue;
                    }
                    // 获取属性的类型
//                    Class fieldType = field.getFieldType();
//                    String[] strValues = toStringValue(value, fieldType);
                    requestParam.put(fieldName, value);
                } catch (Exception e) {
                    LogUtil.error("获取属性值异常@" + getClass().getName() + "." + fieldName + "", e);
                }
            }
        }
        return requestParam;
    }

    protected Map<String, FieldMethods> getDefineFields() {
        Class cls = getClass();
        Map<String, FieldMethods> fieldMethods = paramMethods.get(cls);
        if (fieldMethods == null) {
            synchronized (paramMethods) {
                fieldMethods = new TreeMap<>();
                synchronized (fieldMethods) {
                    Class superClass = cls;
                    while (superClass != null) {
                        Field[] fields = superClass.getDeclaredFields();
                        for (Field field : fields) {
                            try {
                                if (Modifier.isStatic(field.getModifiers())) {
                                    continue;
                                }
                                String fieldName = field.getName();
                                Class fieldType = field.getType();
                                String suffixName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                                String setterName = "set" + suffixName;
                                String getterName = fieldType == boolean.class ? "is" + suffixName : "get" + suffixName;
                                Method getter = superClass.getDeclaredMethod(getterName);
                                Transient getterTransient = getter.getAnnotation(Transient.class);
                                if (getterTransient != null) {
                                    continue;
                                }
                                Method setter = superClass.getDeclaredMethod(setterName, fieldType);
                                Transient setterTransient = setter.getAnnotation(Transient.class);
                                if (setterTransient != null) {
                                    continue;
                                }
                                FieldMethods methods = new FieldMethods();
                                methods.setFieldName(fieldName);
                                methods.setFieldType(fieldType);
                                methods.setSetter(setter);
                                methods.setGetter(getter);
                                fieldMethods.put(fieldName, methods);
                            } catch (Exception e) {
                                LogUtil.error("参数绑定属性获取异常", e);
                            }
                        }
                        superClass = superClass.getSuperclass();
                    }
                    paramMethods.put(cls, fieldMethods);
                }
            }
        }
        return fieldMethods;
    }

    protected static class FieldMethods {
        private String fieldName;
        private Class fieldType;
        private Method setter;
        private Method getter;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public Class getFieldType() {
            return fieldType;
        }

        public void setFieldType(Class fieldType) {
            this.fieldType = fieldType;
        }

        public Method getSetter() {
            return setter;
        }

        public void setSetter(Method setter) {
            this.setter = setter;
        }

        public Method getGetter() {
            return getter;
        }

        public void setGetter(Method getter) {
            this.getter = getter;
        }
    }

    /**
     * 绑定数据
     *
     * @param request 请求
     * @return 本身，这样可以继续设置其他属性
     */
    public T bind(HttpServletRequest request) {
        if (request == null) {
            return (T) this;
        }
        Map<String, FieldMethods> fields = getDefineFields();
        if (fields != null) {
            for (Map.Entry<String, FieldMethods> entry : fields.entrySet()) {
                FieldMethods field = entry.getValue();
                String fieldName = entry.getKey();
                try {
                    String paramValue = ServletUtil.getParameter(request, fieldName);
                    Object value = null;
                    if (Utils.notEmpty(paramValue)) {
                        Class paramType = field.getFieldType();
                        if (paramType == Boolean.class || paramType == boolean.class) {
                            // Boolean 特殊处理
                            value = "1".equals(paramValue) || Boolean.parseBoolean(paramValue);
                        } else if (paramType == String[].class) {
                            // String数组,逗号分隔开
                            value = paramValue.split(",");
                        } else if (paramType == Set.class) {
                            String[] values = paramValue.split(",");
                            // 只能当做是Set<String>
                            Set<String> set = new HashSet<>(values.length);
                            for (String v : values) {
                                set.add(v);
                            }
                            value = set;
                        } else if (Collection.class.isAssignableFrom(paramType)) {
                            // 只能当做是List<String>
                            value = Arrays.asList(paramValue.split(","));
                        } else if (paramType == Date.class) {
                            // 日期
                            value = DateUtil.parse(paramValue);
                        } else {
                            value = paramValue;
                        }
                    }
                    if (value != null) {
                        Method setter = field.getSetter();
                        setter.invoke(this, value);
                    }
                } catch (Exception e) {
                    LogUtil.error("参数绑定异常@" + getClass().getName() + "." + fieldName + "", e);
                }
            }
        }
        return (T) this;
    }

}
