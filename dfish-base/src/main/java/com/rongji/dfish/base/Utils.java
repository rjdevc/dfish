package com.rongji.dfish.base;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.StringUtil;

/**
 * 长期积累的通用方法类。
 *
 * @author DFish-team
 */
public class Utils {

    private static final String ENCODING = "UTF-8";

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

    public static String getParameter(HttpServletRequest request, String key) {
        // 处理tomcat下的中文URL的问题 tomcat在GET方法下传递URL
        // encode的数据会出错。它并是不是按照request设置的字符集进行解码的。
        // if(!"GET".equals(request.getMethod()))return
        // request.getParameter(key);
        String query = request.getQueryString();
        if (notEmpty(query)) {
            String[] pairs = query.split("[&]");

            boolean hasFind = false;
            List<String> values = new ArrayList<>();
            for (String string : pairs) {
                String[] pair = string.split("[=]");
                if (pair.length == 2 && key.equals(pair[0])) {
                    try {
                        String value = java.net.URLDecoder.decode(pair[1].replace("%C2%A0", "%20"), ENCODING);
                        if (notEmpty(value)) {
                            values.add(value);
                            hasFind = true;
                        }
                    } catch (UnsupportedEncodingException e) {
                        LogUtil.error("获取参数异常", e);
                    }
                }
            }
            if (hasFind) {
                return StringUtil.toString(values);
            }
        }

        return StringUtil.toString(request.getParameterValues(key));
    }

    /**
     * 从COOKIE中取得值
     *
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 设置 cookie
     *
     * @param response HttpServletResponse 句柄
     * @param name     cookie的名称
     * @param value    cookie的值
     */
    public static void setCookieValue(HttpServletResponse response,
                                      String name, String value) {
        Cookie cookie = new Cookie(name, value);
        if (isEmpty(value)) {
            cookie.setMaxAge(0);
        } else {
            cookie.setMaxAge(30 * 86400);
        }
        response.addCookie(cookie);
    }

    /**
     * 设置 cookie
     *
     * @param response HttpServletResponse 句柄
     * @param name     cookie的名称
     * @param value    cookie的值
     * @param maxAge   有效时间(秒) 0表示马上清除该cookie, -1表示关闭浏览器时，cookie删除
     * @param domain   有效的访问域，默认只有同一个域才能访问。例如"www.163.com"
     *                 如果需要在子网也能访问，如要让mail.163.com也能访问到这个cookie那么这里应该设置为".163.com"
     * @param path     有效的路径
     *                 如网址"http://2008.163.com/08/0807/10/4IO4QADS00742437.html"
     *                 所对应得path为"/08/0807/10/";
     */
    public static void setCookieValue(HttpServletResponse response,
                                      String name, String value, int maxAge, String domain, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        if (isEmpty(value)) {
            cookie.setMaxAge(0);
        } else {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
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
