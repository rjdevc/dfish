package com.rongji.dfish.base;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.base.util.StringUtil;

import java.util.Collection;
import java.util.Map;

/**
 * 长期积累的通用方法类。
 *
 * @author DFish-team
 */
public class Utils {
    /**
     * 校验一个字符串是否为空判断 改名为notEmpty LinLW // 2005-09-19 原名为validNull容易产生歧义
     * 考虑到性能当前版本并不判定全部由空格组成的字符串，业务模块如果需要，请自行trim后判断。
     *
     * @param str
     * @return
     */
    public static boolean notEmpty(String str) {
        return str != null && str.length() > 0;
    }

    /**
     * 判断一个字符串为空或空串
     * 考虑到性能当前版本并不判定全部由空格组成的字符串，业务模块如果需要，请自行trim后判断。
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断一个List是否非空 ZHL 2008-07-28
     *
     * @param c
     * @return
     */
    public static boolean notEmpty(Collection<?> c) {
        return c != null && c.size() > 0;
    }

    /**
     * 判断一个List是否为空 ZHL 2008-07-28
     *
     * @param c
     * @return
     */
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.size() <= 0;
    }

    /**
     * 判断一个对象数组是否为空/非空 ZHL 2009-10-19
     *
     * @param os
     * @return
     */
    public static boolean notEmpty(Object[] os) {
        return os != null && os.length > 0;
    }

    public static boolean isEmpty(Object[] os) {
        return os == null || os.length <= 0;
    }

    /**
     * 判断一个Map否非空 ZHL 2009-10-19
     *
     * @param m
     * @return
     */
    public static boolean notEmpty(Map<?, ?> m) {
        return m != null && !m.isEmpty();
    }

    /**
     * 判断一个Map否为空 ZHL 2009-10-19
     *
     * @param m
     * @return
     */
    public static boolean isEmpty(Map<?, ?> m) {
        return m == null || m.isEmpty();
    }

    /**
     * 提供XML转义字符
     *
     * @param appendTo StringBuffer
     * @param src      String
     * @deprecated  参数顺序调整  参数，常量，返回值
     * @see #escapeXMLword(String, StringBuilder)
     */
    @Deprecated
    public static void escapeXMLword(StringBuilder appendTo, String src) {
        escapeXMLword(src,appendTo);
    }
    /**
     * 提供XML转义字符
     *
     * @param appendTo StringBuffer
     * @param src      String
     */
    public static void escapeXMLword( String src,StringBuilder appendTo) {
        // return title.replaceAll("&", "&amp;")
        // .replaceAll("<", "&lt;")
        // .replaceAll(">", "&gt;")
        // .replaceAll("\"", "&quot;")
        // .replaceAll("'", "&#39;");
        // 不用上述写法是为了效率。
        if (src == null) {
            return;
        }
        char[] ca = src.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            switch (ca[i]) {
                case ' ':
                    if (i + 1 < ca.length && ca[i + 1] == ' ') {
                        appendTo.append("&nbsp;");
                    } else {
                        appendTo.append(' ');
                    }
                    break;
                case '&':
                    appendTo.append("&amp;");
                    break;
                case '<':
                    appendTo.append("&lt;");
                    break;
                case '>':
                    appendTo.append("&gt;");
                    break;
                case '\"':
                    appendTo.append("&quot;");
                    break;
                case '\'':
                    appendTo.append("&#39;");
                    break;
                case '\r':
                    if (i + 1 < ca.length && ca[i + 1] == '\n') {
                        i++;
                        appendTo.append("<br/>\r\n");
                    } else {
                        appendTo.append("<br/>\r");
                    }
                    break;
                case '\n':
                    appendTo.append("<br/>\n");
                    break;
                default:
                    if (ca[i] < 32 || (ca[i] > 127 && ca[i] < 256)) {
                        // appendTo.append("&#").append( (int) ca[i]).append(";");
                    } else {
                        appendTo.append(ca[i]);
                    }
            }
        }
    }

    /**
     * 对XML字符进行转义
     *
     * @param src String
     * @return String
     */
    public static String escapeXMLword(String src) {
        StringBuilder sb = new StringBuilder();
        escapeXMLword(src,sb);
        return sb.toString();
    }

    /**
     * 拷贝属性,只拷贝基础属性 String Date Number等
     *
     * @param to   Object
     * @param from Object
     */
    public static void copyPropertiesExact(Object to, Object from) {
        BeanUtil.copyPropertiesExact(to, from);
    }

}
