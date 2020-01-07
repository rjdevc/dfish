package com.rongji.dfish.ui.json;

import com.rongji.dfish.base.util.LogUtil;

import java.beans.Transient;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;


/**
 * <p>自定义类的json构建器。</p>
 * <p>和Gson不同，在DFISH3.0中 不同class在初次装载的时候，将会构建一个类反射的Json构建器。
 * 这个构建器会在初次进行属性的探测，排序，改名，忽略部分属性的功能。
 * 因为有这个构建器的存在，在第二次装载的时候，将会跳过这个步骤，直接进入json构建。
 * 从而达到加速的目的。</p>
 * <p>这个构建器是可以被改变的。比如说HtmlPanel控件中text属性可能是需要HTML转移——根据unescape属性的值
 * ——这时候需要忽略控件本身的text属性。然后在buildJson中补充上这个属性的正确格式。
 * 不需要在buildJson中重构全部属性。</p>
 * <p>改变这个构建器的方法，请参看#removeProperty #replaceProperty
 * 以及#buildJsonProps</p>
 * <p>构建器在创建的时候，会自动扫描该clz的所有getter方法(根据getXxx或isXxx匹配)。分析出属性名。
 * 然后，过滤banNames中的属性，并且完成propNameMap中指定的属性名转化。
 * 然后会根据ORDERED_PORPS 进行排序，在这个里面的属性，将会按其顺序严格排序，
 * 接下来按字母顺序排序。每个类通过buildJson添加的属性将在最后。</p>
 * <p>各个类中可能通过<strong>static</strong>标签，引入构建器的变更。比如说Checkbox可能会移除value属性</p>
 * <pre>
 * static{
 *   J.getClass(Checkbox.class).removeProperty("value");
 * }
 * </pre>
 *
 * @author DFish Team LinLW
 */
public class ClassJsonBuilder extends AbstractJsonBuilder {
    /**
     * 构造函数
     *
     * @param clz Class
     */
    public ClassJsonBuilder(Class<?> clz) {
        HashSet<String> propNames = new HashSet<String>();
        for (Method m : clz.getMethods()) {
            if (Modifier.isTransient(m.getModifiers()) || Modifier.isStatic(m.getModifiers()) || !Modifier.isPublic(m.getModifiers())) {
                continue;
            }
            if ((m.getName().startsWith("get") || m.getName().startsWith("is"))
                    && m.getParameterTypes().length == 0) {
                // 过时的注解不输出
                if (m.getAnnotation(Deprecated.class) != null) {
                    continue;
                }
                if (m.getAnnotation(Transient.class) != null) {
                    continue;
                }
                String fieldName = getFiledNameByGetterName(m.getName());
                if (BAN_NAMES.contains(fieldName)) {
                    continue;
                }
                String propName = PROP_NAME_MAP.get(fieldName);
                if (propName == null) {
                    propName = fieldName;
                }
                if (propNames.add(propName)) {
                    methods.add(new ReflectAppender(propName, m));
                } else {
                    //取出已经压入的属性，判断其是不是子类自己添加的，因为子类可以更改返回值成更小的范围
                    for (JsonPropAppender g : methods) {
                        if (g.getPropName().equals(propName) && g instanceof ReflectAppender) {
                            ReflectAppender cast = (ReflectAppender) g;
                            Class<?> oldClz = cast.getterMethod.getDeclaringClass();
                            Class<?> newClz = m.getDeclaringClass();
                            if (oldClz != newClz && oldClz.isAssignableFrom(newClz)) {
                                cast.getterMethod = m;
                            }
                            Class<?> oldReturnType = cast.getterMethod.getReturnType();
                            Class<?> newReturnType = m.getReturnType();
                            if (oldReturnType != newReturnType && oldReturnType.isAssignableFrom(newReturnType)) {
                                cast.getterMethod = m;
                            }
                            break;
                        }
                    }
                    LogUtil.debug(getClass(), "find a new property {name:'" + propName + "',type:'" + m.getReturnType().getName() + "',declareingCalss:'" + m.getDeclaringClass().getName() + "'}");
                }
            }
            Collections.sort(methods, new java.util.Comparator<JsonPropAppender>() {
                @Override
                public int compare(JsonPropAppender o1,
                                   JsonPropAppender o2) {
                    String name1 = o1.getPropName();
                    String name2 = o2.getPropName();
                    int pos1 = ORDERED_PORPS.indexOf(name1);
                    int pos2 = ORDERED_PORPS.indexOf(name2);
                    if (pos1 >= 0 && pos2 >= 0) {
                        return pos1 - pos2;
                    } else if (pos1 >= 0) {
                        return -1;
                    } else if (pos2 >= 0) {
                        return 1;
                    } else {
                        return name1.compareTo(name2);
                    }
                }
            });
        }
    }

    @Override
    public void removeProperty(String propName) {
        for (Iterator<JsonPropAppender> iter = methods.iterator(); iter.hasNext(); ) {
            JsonPropAppender g = iter.next();
            if (propName.equals(g.getPropName())) {
                iter.remove();
                break;
            }
        }
    }

    @Override
    public void replaceProperty(String propName, String newName) {
        for (Iterator<JsonPropAppender> iter = methods.iterator(); iter.hasNext(); ) {
            JsonPropAppender g = iter.next();
            if (propName.equals(g.getPropName())) {
                g.setPropName(newName);
                break;
            }
        }
    }

    protected List<JsonPropAppender> methods = new ArrayList<JsonPropAppender>();

    /**
     * 获得模板
     *
     * @return List
     */
    public List<JsonPropAppender> getMethods() {
        return methods;
    }

    private static final HashMap<String, String> PROP_NAME_MAP = new HashMap<String, String>();
    private static final HashSet<String> BAN_NAMES = new HashSet<String>();
    /**
     * 指定以下属性在JSON中按指定的顺序排列，而其他未考虑到的属性，排在json背后，并按字母表顺序排列
     */
    public static final List<String> ORDERED_PORPS = Arrays.asList(
            //控制属性
            "type", "id", "gid", "func", "face",
            //普遍属性
            "name", "label", "title", "value", "text", "options",
            //细节属性
            "src", "format", "legend", "checked", "multiple", "placeholder", "target",
            "suggest", "dropsrc", "step", "picker", "nobr", "nocache", "strict", "checkmodify", "dft", "focus", "labelWidth", "description", "tip",
            //表单属性
            "readonly", "disabled", "hidden", "star", "hideTitle",
            //样式属性
            "cls", "styleClass", "style", "height", "width", "hmin", "wmin", "align", "valign", "dir", "space", "scroll", "scrollClass",
            //样式细节
            "focusable", "focusmultiple", "group", "groups",
            //校验
            "notnull", "required", "requiredtext",
            "minlength", "minlengthtext", "maxlength", "maxlengthtext",
            "minsize", "minsizetext", "maxsize", "maxsizetext",
            "minvalue", "minvaluetext", "maxvalue", "maxvaluetext",
            "pattern", "patterntext", "comparemode", "compare", "comparetext", "groups",
            //子属性
            "pub", "columns", "thead", "tbody", "rows", "data", "nodes", "validate", "validategroup", "events", "on",
            //rows有两个Grid里面有，Textarea里面也有。因为textarea建议使用height来控制，所以这里排序按Grid为准
            //
            "NO_EXISTS"//占位符该属性不存在
    );

    static {
        /*
         * 转化元素属性成json属性
         */

        /*
         * 标识不要关心这部分属性
         */
        BAN_NAMES.add("");
        BAN_NAMES.add("class");
        BAN_NAMES.add("declaringClass");

        BAN_NAMES.add("hideTitle");
        BAN_NAMES.add("autoEscape");
        BAN_NAMES.add("star");
    }

    private static String getFiledNameByGetterName(String name) {
        String part = null;
        if (name.startsWith("get")) {
            if (name.length() == 3) {
                return "";
            }
            part = name.substring(3);
        }
        if (name.startsWith("is")) {
            if (name.length() == 2) {
                return "";
            }
            part = name.substring(2);
        }
        char first = part.charAt(0);
        if (first >= 'A' && first <= 'Z') {
            return ((char) (first + 32)) + part.substring(1);
        } else {
            return part;
        }
    }

    @Override
    public void buildJson(Object o, StringBuilder sb, Stack<PathInfo> path) {
        sb.append('{');
        boolean begin = true;
        for (JsonPropAppender entry : methods) {
            try {
                begin = entry.appendProperty(o, sb, path, begin);
            } catch (Exception e) {
                LogUtil.error(getClass(), null, e);
            }
        }
        sb.append('}');
    }

    public static class ReflectAppender implements JsonPropAppender {
        public ReflectAppender(String propName, Method getterMethod) {
            this.propName = propName;
            this.getterMethod = getterMethod;
        }

        String propName;
        Method getterMethod;

        @Override
        public String getPropName() {
            return propName;
        }

        @Override
        public void setPropName(String propName) {
            this.propName = propName;
        }

        @Override
        public boolean appendProperty(Object o, StringBuilder sb, Stack<PathInfo> path, boolean begin) throws Exception {
            Object v = null;
            try {
                v = getterMethod.invoke(o);
            } catch (Exception ex) {/* 如果属性获取没权限等情况这里不做任何记录，否则日志会很长 */}
            if (v == null) {//为空则跳出
                return begin;
            }
            while (v instanceof JsonWrapper) {
                JsonWrapper<?> warp = (JsonWrapper<?>) v;
                v = warp.getPrototype();
                if (v == warp) {
                    break;
                }
            }
            if (v == null) {//为空则跳出
                return begin;
            }
            //这里不能简单的使用J.buildJson. 因为很多属性是要去除的。如disabled为false的时候不需要显示。
            if (v instanceof String) {
                if ("".equals(v)) {
                    return begin;
                }
            } else if (v instanceof Collection<?>) {//&&((Number)v).intValue()==0
                Collection<?> cast = (Collection<?>) v;
                if (cast.size() == 0) {
                    return begin;
                }
            } else if (v.getClass().isArray()) {//&&((Number)v).intValue()==0
                String clzName = v.getClass().getName();
                char c = clzName.charAt(1); //Object的情况可能是Object[]或者是多维度数据 name应该是[LString 或[[LObject等。
                switch (c) {
                    case 'B': {
                        byte[] oa = (byte[]) v;
                        if (oa.length == 0) {
                            return begin;
                        }
                        break;
                    }
                    case 'C': {
                        char[] oa = (char[]) v;
                        if (oa.length == 0) {
                            return begin;
                        }
                        break;
                    }

                    case 'D': {
                        double[] oa = (double[]) v;
                        if (oa.length == 0) {
                            return begin;
                        }
                        break;
                    }
                    case 'F': {
                        float[] oa = (float[]) v;
                        if (oa.length == 0) {
                            return begin;
                        }
                        break;
                    }
                    case 'I': {
                        int[] oa = (int[]) v;
                        if (oa.length == 0) {
                            return begin;
                        }
                        break;
                    }
                    case 'J': {
                        long[] oa = (long[]) v;
                        if (oa.length == 0) {
                            return begin;
                        }
                        break;
                    }
                    case 'S': {
                        short[] oa = (short[]) v;
                        if (oa.length == 0) {
                            return begin;
                        }
                        break;
                    }
                    case 'Z': {
                        boolean[] oa = (boolean[]) v;
                        if (oa.length == 0) {
                            return begin;
                        }
                        break;
                    }
                    default:
                        Object[] cast = (Object[]) v;
                        if (cast.length == 0) {
                            return begin;
                        }
                }
            } else if (v instanceof Map<?, ?>) {//&&((Number)v).intValue()==0
                Map<?, ?> cast = (Map<?, ?>) v;
                if (cast.size() == 0) {
                    return begin;
                }
            }


            //回滚技术 如果过程当中sb是一个空信息{}需要将当前状态全部回滚
            int propBegin = sb.length();
            boolean beginOld = begin;//如果ID为空，可能直接回滚，这种情况下,第二个属性前面不会输出逗号
            if (begin) {
                begin = false;
            } else {
                sb.append(',');
            }
            //部分属性不输出
            sb.append('"').append(propName).append('"').append(':');
            int propValueBegin = sb.length();
            path.push(new PathInfo(propName, v));
            try {
                J.get(v.getClass()).buildJson(v, sb, path);
            } catch (Exception ex) {
                LogUtil.error(getClass(), v.getClass().getName(), ex);
            }
            path.pop();
            if (equals(sb, propValueBegin, "{}")) {
                //回滚
                sb.delete(propBegin, sb.length());
                begin = beginOld;//状态也回滚
            }
            return begin;
        }

        private boolean equals(StringBuilder sb, int begin, String v) {
            if (sb.length() - begin != v.length()) {
                return false;
            }
            for (int i = 0; i < v.length(); i++) {
                if (sb.charAt(begin + i) != v.charAt(i)) {
                    return false;
                }
            }
            return true;
        }

        public Method getGetterMethod() {
            return this.getterMethod;
        }
    }


}
