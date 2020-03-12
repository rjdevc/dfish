package com.rongji.dfish.base.util;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * BeanUtil是JAVA Bean常用工具类合集。
 * 对于JAVA Bean 这些工具通常都是在做类放射。
 */
public class BeanUtil {


    /**
     * 用于标记一个方法。
     *
     * @author DFish Team
     * @version 1.1 参照java.util.Locale 改进了性能。
     */
    static final class MethodKey implements java.io.Serializable {
        private final Class<?> c;
        private final String p;
        private static final long serialVersionUID = 0x00E04C1505620901L;
        /**
         * Placeholder for the object's hash code. Always -1.
         *
         * @serial
         */
        @SuppressWarnings("unused")
        private volatile int hashcode = -1; // lazy evaluate ????I can not
        // understand the meaning. Just copy
        // it form java.util.Locale
        private transient volatile int hashCodeValue = 0;

        /**
         * 构造函数
         *
         * @param c Class 所属class
         * @param p String 方法名
         */
        public MethodKey(Class<?> c, String p) {
            if (p == null) {
                throw new NullPointerException();
            }
            this.c = c;
            this.p = p.intern();
            hashCodeValue = 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof MethodKey)) {
                return false;
            }
            return equals((MethodKey) o);
        }

        /**
         * 比对是否相同
         *
         * @param o 对比的对象
         * @return 是否相同
         * @see #equals(Object)
         */
        public boolean equals(MethodKey o) {
            if (o == null) {
                return false;
            }
            if (o == this) {
                return true;
            }
            return o.c == c && o.p == p;
        }

        @Override
        public int hashCode() {
            int hc = hashCodeValue;
            if (hc == 0) {
                hc = (c.hashCode() << 8) ^ p.hashCode();
                hashCodeValue = hc;
            }
            return hc;
        }

        @Override
        public String toString() {
            return c.getName() + "#" + p;
        }
    }

    /**
     * 取得item 里面某个属性的值。
     *
     * @param item Object
     * @param prop String
     * @return Object
     * @throws NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getProperty(Object item, String prop)
            throws NoSuchMethodException, InvocationTargetException,
            IllegalArgumentException, IllegalAccessException {
        if (item == null || prop == null || "".equals(prop)) {
            return null;
        }
        String[] subprops = prop.split("[.]");
        Object result = item;
        for (String sub : subprops) {
            MethodKey key = new MethodKey(result.getClass(), sub);
            Method m = methodMap.get(key);
            if (m == null) {
                m = receiveGetterMethod(result.getClass(), sub);
                methodMap.put(key, m);
            }
            result = m.invoke(result, NO_ARG);
        }
        return result;
    }

    private static HashMap<MethodKey, Method> methodMap = new HashMap<MethodKey, Method>();
    private static HashMap<Class<?>, List<String>> methodNameMap = new HashMap<Class<?>, List<String>>();
    private static final Object[] NO_ARG = new Object[0];
    private static final Class<?>[] NO_CLZ = new Class<?>[0];

    /**
     * 取得getter方法
     *
     * @param c Class
     * @param p String
     * @return Method
     * @throws NoSuchMethodException
     */
    private static Method receiveGetterMethod(Class<?> c, String p)
            throws NoSuchMethodException {

        char bc = p.charAt(0);
        String methodName = "get"
                + ((bc >= 'a' && bc <= 'z') ? (char) (bc - 32) : bc)
                + p.substring(1);
        Method m;
        try {
            m = c.getMethod(methodName, NO_CLZ);
        } catch (NoSuchMethodException ex) {
            methodName = "is"
                    + ((bc >= 'a' && bc <= 'z') ? (char) (bc - 32) : bc)
                    + p.substring(1);
            try {
                m = c.getMethod(methodName, NO_CLZ);
            } catch (Exception ex1) {
                throw new NoSuchMethodException(
                        "There is no getter method for property " + p
                                + " in class " + c.getName());
            }
        }
        return m;
    }

    /**
     * 调用某个方法
     *
     * @param clzInstance
     * @param methodName
     * @param params
     * @return Object
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(Object clzInstance, String methodName,
                                      Object... params) throws NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        Class<?> targetClz = clzInstance.getClass();
        Method mth = findBestMethod(targetClz, methodName, params);
        return mth.invoke(clzInstance, params);
    }

    private static Method findBestMethod(Class<?> targetClz, String methodName,
                                         Object... params) throws NoSuchMethodException {
        // 首先如果PARAM为空，那么可以精确查找
        if (params == null || params.length == 0) {
            Method m = targetClz.getMethod(methodName, NO_CLZ);
            return m;
        }
        // 否则看PARAM的类型取得方法
        boolean hasNullParam = false;
        Class<?>[] clzs = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            Object obj = params[i];
            if (obj == null) {
                hasNullParam = true;
                break;
            }
            clzs[i] = obj.getClass();
        }
        if (!hasNullParam) {
            // 尝试找到方法
            try {
                Method m = targetClz.getMethod(methodName, clzs);
                return m;
            } catch (NoSuchMethodException nsme) {
            }
        }
        // 如果方法仍旧找不到，尝试查找
        Method[] mths = targetClz.getMethods();
        // ArrayList<Method> candidate=new ArrayList<Method>();
        outter:
        for (Method method : mths) {
            if (!methodName.equals(method.getName())) {
                continue;
            }
            // 参数个数应该匹配
            if (method.getParameterTypes().length != params.length) {
                continue;
            }
            Class<?>[] paramClzs = method.getParameterTypes();
            for (int i = 0; i < paramClzs.length; i++) {
                if (params[i] == null) {
                    continue;
                }
                if (!isAssignableFrom(paramClzs[i], params[i].getClass())) {
                    continue outter;
                }
            }
            return method;
        }
        throw new NoSuchMethodException(methodName);
    }

    private static Constructor findBestConstructor(Class<?> targetClz,
                                                   Object... params) throws NoSuchMethodException {
        // 首先如果PARAM为空，那么可以精确查找
        if (params == null || params.length == 0) {
            Constructor m = targetClz.getConstructor(NO_CLZ);
            return m;
        }
        // 否则看PARAM的类型取得方法
        boolean hasNullParam = false;
        Class<?>[] clzs = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            Object obj = params[i];
            if (obj == null) {
                hasNullParam = true;
                break;
            }
            clzs[i] = obj.getClass();
        }
        if (!hasNullParam) {
            // 尝试找到方法
            try {
                Constructor m = targetClz.getConstructor(clzs);
                return m;
            } catch (NoSuchMethodException nsme) {
            }
        }
        // 如果方法仍旧找不到，尝试查找
        Constructor[] mths = targetClz.getConstructors();
        // ArrayList<Method> candidate=new ArrayList<Method>();
        outter:
        for (Constructor method : mths) {
            // 参数个数应该匹配
            if (method.getParameterTypes().length != params.length) {
                continue;
            }
            Class<?>[] paramClzs = method.getParameterTypes();

            for (int i = 0; i < paramClzs.length; i++) {
                if (params[i] == null) {
                    continue;
                }
                if (!isAssignableFrom(paramClzs[i], params[i].getClass())) {
                    continue outter;
                }
            }
            return method;
        }
        throw new NoSuchMethodException(targetClz.getSimpleName() + "<init>");
    }

    private static boolean isAssignableFrom(Class<?> type, Class<?> target) {
        if (type.isPrimitive()) {
            Class realClz = null;
            if (type == Boolean.TYPE) {
                realClz = Boolean.class;
            } else if (type == Character.TYPE) {
                realClz = Character.class;
            } else if (type == Byte.TYPE) {
                realClz = Byte.class;
            } else if (type == Short.TYPE) {
                realClz = Short.class;
            } else if (type == Integer.TYPE) {
                realClz = Integer.class;
            } else if (type == Long.TYPE) {
                realClz = Long.class;
            } else if (type == Float.TYPE) {
                realClz = Long.class;
            } else if (type == Double.TYPE) {
                realClz = Long.class;
            }
            return realClz.isAssignableFrom(target);
        } else {
            return type.isAssignableFrom(target);
        }
    }

    /**
     * 根据 类，方法名，参数类型获取合适的方法。
     * @param targetClass 类
     * @param name 方法名
     * @param paramTypes 类型
     * @return
     * @throws NoSuchMethodException
     */
    public static Method getMethod(Class<?> targetClass, String name, Class<?>... paramTypes) throws NoSuchMethodException {
        NoSuchMethodException nsme = null;
        try {
            return targetClass.getMethod(name, paramTypes);
        } catch (NoSuchMethodException ex) {
            nsme = ex;
        }

        List<Method> candidates = new ArrayList<Method>();
        for (Method m : targetClass.getMethods()) {

            if (!Modifier.isPublic(m.getModifiers())) {
                continue;
            }
            if (!m.getName().equals(name)) {
                continue;
            }
            if (paramTypes == null || paramTypes.length == 0) {
                if (m.getParameterTypes() != null && m.getParameterTypes().length != 0) {
                    continue;
                }
            } else {
                if (m.getParameterTypes() == null || m.getParameterTypes().length != paramTypes.length) {
                    continue;
                }
            }
            boolean accept = true;
            if (m.getParameterTypes() != null && m.getParameterTypes().length > 0) {
                int i = 0;
                for (Class<?> pc : m.getParameterTypes()) {
                    if (!accept(paramTypes[i], pc)) {
                        accept = false;
                        break;
                    }
                    i++;
                }
            }
            if (accept) {
                candidates.add(m);
            }
        }
        if (candidates.size() == 0) {
            throw nsme;
        } else if (candidates.size() > 0) {
            Collections.sort(candidates, (Method o1,Method o2)-> {
                Class c1=o1.getDeclaringClass();
                Class c2=o2.getDeclaringClass();
                if(c1==c2) {
                }else if(isAssignableFrom(c1,c2)){
                    return 1;
                }else if(isAssignableFrom(c1,c2)){
                    return -1;
                }
                Class[] type1=o1.getParameterTypes();
                Class[] type2=o1.getParameterTypes();
                for(int i=0;i<type1.length;i++){
                    if(type1[i]==type2[i]){
                    }else if(isAssignableFrom(type1[i],type2[i])){
                        return 1;
                    }else if(isAssignableFrom(type1[i],type2[i])){
                        return -1;
                    }
                }
                return 0;
            });
            return candidates.get(0);
        }
        // 多个都吻合的时候要首先更具declareClass做一层判断，如果getParameterTypes 完全一致，只能留子类的方法。
        // JDK 会自动覆盖子类的方法，所以该步骤省略(测试环境oracle hotspot JDK 6)
        // 如果留下来的方法数量，还大于1个。原则上要判断 候选项中的accpet关系。多个参数要都accept才行。
        HashSet<Method> toRemove = new HashSet<Method>();
        for (Method a : candidates) {
            Class<?>[] aptypes = a.getParameterTypes();
            for (Method b : candidates) {
                //能到这步的已经必定重复率很高，所以省去很多判断。
                Class<?>[] bptypes = b.getParameterTypes();
                boolean acceptAll = true;
                for (int i = 0; i < aptypes.length; i++) {
                    if (!accept(aptypes[i], bptypes[i])) {
                        acceptAll = false;
                    }
                }
                if (acceptAll) {
                    toRemove.add(b);
                }
            }
        }
        candidates.removeAll(toRemove);
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        // 最后还没有办法只留下一个的 报错
        throw nsme;
    }

    //	public static void main(String[] args){
//		try {
//			Method m=getMethod(ArrayList.class,"add",Integer.class,String.class);
//			System.out.println(m.getDeclaringClass().getName()+":"+ m.getName()+"("+m.getParameterTypes()[0].getName()+","+m.getParameterTypes()[1].getName()+")");
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		}
//	}
    private static final HashSet<Class<?>> PRIMITIVE_CLASS = new HashSet<Class<?>>(Arrays.asList(
            Integer.class, int.class, Boolean.class, boolean.class,
            Long.class, long.class, Short.class, short.class,
            Byte.class, byte.class, Character.class, char.class,
            Float.class, float.class, Double.class, double.class
//			Void.class,void.class
    ));

    private static boolean accept(Class<?> realType, Class<?> methodParamType) {
        if (realType == methodParamType) {
            return true;
        }
        if (methodParamType.isAssignableFrom(realType)) {
            return true;
        }
        //底层类的关联关系
        if (PRIMITIVE_CLASS.contains(realType)) {
            if (Integer.class == realType || int.class == realType) {
                return Integer.class == methodParamType || int.class == methodParamType ||
                        Long.class == methodParamType || long.class == methodParamType ||
                        Float.class == methodParamType || float.class == methodParamType ||
                        Double.class == methodParamType || double.class == methodParamType;
            } else if (Long.class == realType || long.class == realType) {
                return Long.class == methodParamType || long.class == methodParamType ||
                        Float.class == methodParamType || float.class == methodParamType ||
                        Double.class == methodParamType || double.class == methodParamType;
            } else if (Double.class == realType || double.class == realType) {
                return Double.class == methodParamType || double.class == methodParamType;
            } else if (Boolean.class == realType || boolean.class == realType) {
                return Boolean.class == methodParamType || boolean.class == methodParamType;
            } else if (Character.class == realType || char.class == realType) {
                return Character.class == methodParamType || char.class == methodParamType;
            } else if (Float.class == realType || float.class == realType) {
                return Float.class == methodParamType || float.class == methodParamType ||
                        Double.class == methodParamType || double.class == methodParamType;
            } else if (Short.class == realType || short.class == realType) {
                return Short.class == methodParamType || short.class == methodParamType ||
                        Integer.class == methodParamType || int.class == methodParamType ||
                        Long.class == methodParamType || long.class == methodParamType ||
                        Float.class == methodParamType || float.class == methodParamType ||
                        Double.class == methodParamType || double.class == methodParamType;
            } else if (Byte.class == realType || byte.class == realType) {
                return Byte.class == methodParamType || byte.class == methodParamType ||
                        Short.class == methodParamType || short.class == methodParamType ||
                        Integer.class == methodParamType || int.class == methodParamType ||
                        Long.class == methodParamType || long.class == methodParamType ||
                        Float.class == methodParamType || float.class == methodParamType ||
                        Double.class == methodParamType || double.class == methodParamType;
            }
        }
        return false;
    }

    /**
     * 用于调用静态方法
     *
     * @param clz
     * @param methodName
     * @param params
     * @return Object
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Object invokeMethod(Class<?> clz, String methodName,
                                      Object... params) throws NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        Method mth = findBestMethod(clz, methodName, params);
        return mth.invoke(null, params);
    }

    private static HashMap<CopyPropertiesTemplateKey, CopyPropertiesTemplate> TEMPLATES = new HashMap<CopyPropertiesTemplateKey, CopyPropertiesTemplate>();

    private static CopyPropertiesTemplate getCopyPropertiesTemplate(
            Class<?> clz1, Class<?> clz2) {
        CopyPropertiesTemplateKey key = new CopyPropertiesTemplateKey(clz1,
                clz2);
        CopyPropertiesTemplate tmpl = TEMPLATES.get(key);
        if (tmpl == null) {
            // 构建模板
            tmpl = new CopyPropertiesTemplate(clz1, clz2);
            TEMPLATES.put(key, tmpl);
        }
        return tmpl;
    }

    private static class CopyPropertiesTemplateKey {
        private Class<?> clz1;
        private Class<?> clz2;

        public CopyPropertiesTemplateKey(Class<?> clz1, Class<?> clz2) {
            if (clz1 == null || clz2 == null) {
                throw new NullPointerException("can not copy properties for null.");
            }
            this.clz1 = clz1;
            this.clz2 = clz2;
        }

        @Override
        public boolean equals(Object key) {
            if (key == null) {
                return false;
            }
            if (key == this) {
                return true;
            }
            if (!(key instanceof CopyPropertiesTemplateKey)) {
                return false;
            }
            CopyPropertiesTemplateKey cast = (CopyPropertiesTemplateKey) key;
            return cast.clz1 == clz1 && cast.clz2 == clz2;
        }

        @Override
        public int hashCode() {
            return clz1.hashCode() ^ clz2.hashCode();
        }
    }

    private static final Set<Class<?>> CARE_TYPES = new HashSet<Class<?>>();

    static {
        // 数字
        CARE_TYPES.add(byte.class);
        CARE_TYPES.add(Byte.class);
        CARE_TYPES.add(short.class);
        CARE_TYPES.add(Short.class);
        CARE_TYPES.add(int.class);
        CARE_TYPES.add(Integer.class);
        CARE_TYPES.add(long.class);
        CARE_TYPES.add(Long.class);
        CARE_TYPES.add(float.class);
        CARE_TYPES.add(Float.class);
        CARE_TYPES.add(double.class);
        CARE_TYPES.add(Double.class);
        CARE_TYPES.add(BigInteger.class);
        CARE_TYPES.add(BigDecimal.class);
        CARE_TYPES.add(Number.class);
        // 字符
        CARE_TYPES.add(char.class);
        CARE_TYPES.add(String.class);
        // 布尔
        CARE_TYPES.add(boolean.class);
        CARE_TYPES.add(Boolean.class);
        // 时间
        CARE_TYPES.add(java.util.Date.class);
        CARE_TYPES.add(java.sql.Date.class);
        CARE_TYPES.add(java.sql.Time.class);
        CARE_TYPES.add(java.sql.Timestamp.class);
    }

    /**
     * 拷贝属性,只拷贝基础属性 String Date Number等
     *
     * @param to   Object
     * @param from Object
     */
    public static void copyPropertiesExact(Object to, Object from) {
        if (from == null || to == null) {
            return;
        }
        CopyPropertiesTemplate tmpl = getCopyPropertiesTemplate(to.getClass(), from.getClass());
        tmpl.copyPropertiesExact(to, from);
    }

    private static class CopyPropertiesTemplate {
        private List<Method> fromMethods;
        private List<Method> toMethods;

        public CopyPropertiesTemplate(Class<?> clzto, Class<?> clzfrom) {
            fromMethods = new ArrayList<Method>();
            toMethods = new ArrayList<Method>();
            Method[] fromM = clzfrom.getMethods();
            for (int i = 0; i < fromM.length; i++) {
                String methodName = fromM[i].getName();
                Class<?> returnType = fromM[i].getReturnType();
                Class<?>[] parapType = fromM[i].getParameterTypes();
                if (CARE_TYPES.contains(returnType)
                        && (parapType == null || parapType.length == 0)) {
                    String setterName = null;
                    if (methodName.startsWith("get")) {
                        setterName = "set" + methodName.substring(3);
                    } else if (methodName.startsWith("is")) {
                        setterName = "set" + methodName.substring(2);
                    } else {
                        continue;
                    }
                    try {
                        Method setter = clzto.getMethod(setterName, new Class[]{returnType});
                        if (setter != null) {
                            fromMethods.add(fromM[i]);
                            toMethods.add(setter);
                        }
                    } catch (Exception ex) {
                    } // ignore
                }
            }
        }

        public void copyPropertiesExact(Object to, Object from) {
            for (int i = 0; i < fromMethods.size(); i++) {
                try {
                    toMethods.get(i).invoke(to, new Object[]{fromMethods.get(i).invoke(from, NO_ARG)});
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
     * 把某个对象的所有属性当成Map输出。
     *
     * @param item Object
     * @return Map
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Map<String, Object> getPropMap(Object item) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (item == null) {
            return null;
        }
        Class<?> itemCls = item.getClass();
        List<String> props = methodNameMap.get(itemCls);
        if (props == null) {
            props = findPropNames(itemCls);
            methodNameMap.put(itemCls, props);
        }
        Map<String, Object> propMap = new TreeMap<String, Object>();
        for (String prop : props) {
            MethodKey key = new MethodKey(itemCls, prop);
            Method m = methodMap.get(key);
            if (m == null) {
                m = receiveGetterMethod(itemCls, prop);
                methodMap.put(key, m);
            }
            try {
                Object o = m.invoke(item, NO_ARG);
                if (o != null && !"".equals(o)) {
                    propMap.put(prop, o);
                }
            } catch (Exception ex) {
                System.err.print("=====================\r\n" +
                        "msg:" + ex.getMessage() + "\r\n" +
                        "m:" + m.getName() + "\r\n" +
                        "=====================");
                throw new RuntimeException(ex);
            }
        }
        return propMap;
    }

    private static List<String> findPropNames(Class<?> itemCls) {
        if (itemCls == null) {
            return null;
        }
        List<String> names = new ArrayList<String>();
        for (Method m : itemCls.getMethods()) {
            if (!(m.getName().startsWith("get") ||
                    m.getName().startsWith("is")) ||
                    m.getName().length() <= 3 ||
                    m.getParameterTypes().length > 0 ||
                    !Modifier.isPublic(m.getModifiers()) ||
                    Modifier.isAbstract(m.getModifiers()) ||
                    Modifier.isStatic(m.getModifiers())) {
                continue;
            }
            String propName = m.getName().startsWith("get") ? m.getName().substring(3) : m.getName().substring(2);
            char bc = propName.charAt(0);
            if (bc < 'A' || bc > 'Z') {
                continue;
            }
            propName = ((char) (bc + 32)) + propName.substring(1);
            if ("class".equals(propName)) {//放过item默认的getClass方法
                continue;
            }
            names.add(propName);
        }
        return names;
    }

    /**
     * 取得类简称
     *
     * @param clzName String
     * @return String
     */
    public static String getClassShortName(String clzName) {
        int pos = clzName.lastIndexOf('.');
        if (pos >= 0) {
            return clzName.substring(pos + 1);
        } else {
            return clzName;
        }
    }

    /**
     * 取得真正的返回值类型，因为返回值有可能是泛型定义的。所以方法的返回值类型有可能并不是
     * m.getReturnType();
     * 如果发现方法返回值有泛型的定义，则尝试取得真正的返回值类型。
     *
     * @param m           方法
     * @param sourceClass 定义这个方法的类(m可能是其父类定义的所以这个参数也是必须的);
     * @return 返回值类型。
     */
    public static Class<?> getRealReturnType(Method m, Class<?> sourceClass) {
        if (m.getGenericReturnType() instanceof TypeVariable) {
            String genName = ((TypeVariable<?>) m.getGenericReturnType()).getName();
            TypeVariable<?>[] definedTypes = m.getDeclaringClass().getTypeParameters();
            int typeIndex = -1;
            if (definedTypes == null) {
                return m.getReturnType();
            }
            for (int i = 0; i < definedTypes.length; i++) {
                if (genName.equals(definedTypes[i].getName())) {
                    typeIndex = i;
                }
            }
            if (typeIndex < 0) {
                return m.getReturnType();
            }
            if (sourceClass.getGenericSuperclass() instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) sourceClass.getGenericSuperclass()).getActualTypeArguments();
                if (types.length < typeIndex + 1) {
                    return m.getReturnType();
                } else {
                    if (types[typeIndex] instanceof Class) {
                        return (Class<?>) types[typeIndex];
                    }
                }
            }
        }
        return m.getReturnType();
    }

    private static Map<String, Boolean> existsMap = new HashMap<String, Boolean>();

    /**
     * 判断当前环境里面有没有存在该类
     * @param className String
     * @return boolean
     */
    public static boolean exists(String className) {
        Boolean cachedRet = existsMap.get(className);
        if (cachedRet != null) {
            return cachedRet;
        }
        try {
            Class<?> c = Class.forName(className);
            existsMap.put(className, true);
            return true;
        } catch (ClassNotFoundException e) {
            existsMap.put(className, false);
            LogUtil.warn("Class " + className + " does not exists.");
            return false;
        }
    }

    /**
     * 创建一个对象
     * @param className 类名
     * @param args 参数
     * @return 对象
     */
    public static Object newInstance(String className, Object... args) {
        try {
            Class<?> objectClass = Class.forName(className);
            return newInstance(objectClass, args);
        } catch (Exception e) {
            LogUtil.error("newInstance for " + className + " failed.", e);
        }
        return null;
    }

    /**
     * 创建一个对象
     * @param objectClass Class
     * @param args 参数
     * @return 对象
     */
    public static Object newInstance(Class<?> objectClass, Object... args) {
		try {
			if (args == null || args.length == 0) {
				return objectClass.newInstance();
			} else {
				Constructor m = findBestConstructor(objectClass, args);
				return m.newInstance(args);
			}
		} catch (Exception e) {
			LogUtil.error("newInstance for " + objectClass + " failed.", e);
		}
		return null;
    }
}
