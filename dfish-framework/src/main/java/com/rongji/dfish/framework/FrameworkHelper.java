/*
 * FrameworkHelper.java
 *
 * Copyright 2009 Rongji Enterprise, Inc. All rights reserved.
 *
 * Rongji PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.rongji.dfish.framework;

import com.rongji.dfish.base.context.SystemContext;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.config.PersonalConfigHolder;
import com.rongji.dfish.framework.config.SystemConfigHolder;
import com.rongji.dfish.framework.info.ServletInfo;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

//import com.rongji.dfish.framework.service.NewIdGetter;

/**
 * 框架帮助类
 *
 * @author I-Task Team
 * @version 1.0.0
 * @since 1.0.0    ZHL		2009-9-8
 */
public class FrameworkHelper {

    private static final String STR_UNKNOWN = "unknown";

    public static final String[] ENCODINGS = {STR_UNKNOWN, "utf-8", "gbk", "gb2312", "iso8859-1", "unicode"};

    /**
     * @see
     */

    public static final int INT_ENCODING_UNKNOWN = 0;
    /**
     * 字符集UTF-8
     */
    public static final int INT_ENCODING_UTF_8 = 1;
    /**
     * 字符集GBK
     */
    public static final int INT_ENCODING_GBK = 2;
    /**
     * 字符集GB2312
     */
    public static final int INT_ENCODING_GB2312 = 3;
    /**
     * 字符集ISO-8859-1
     */
    public static final int INT_ENCODING_ISO8859_1 = 4;
    public static final int INT_ENCODING_UNICODE = 5;

    public static final String ENDLINE = "\r\n";

    /**
     * 默认字符集
     */
    public static final String ENCODING = ENCODINGS[INT_ENCODING_UTF_8];

    /**
     * 登录人员信息在session中的名字
     */
    public static final String LOGIN_USER_KEY = "com.rongji.dfish.LOGIN_USER_KEY";
//	/**
//	 * 图标
//	 */
//	public static final String ICON_SAVE="img/b/save.gif";
//	public static final String ICON_DELETE="img/b/delete.gif";
//	public static final String ICON_NEW="img/b/new.gif";
    /**
     * 资源文件包路径
     */
    private static final String LANG_FILE_PATH = "com.rongji.dfish.lang.framework";
//	public static final String OPENER_PATH = "javascript:DFish.g_dialog(this).fromView.path";
//	public static final String VALUE_STRING_ZERO = "0";
//	public static final String VALUE_STRING_ONE = "1";
//	/** true */
//	public static final String VALUE_STRING_TRUE="true";
//	/** false */
//	public static final String VALUE_STRING_FALSE="false";
    /**
     * 资源文件缓存
     */
    private static HashMap<Locale, ResourceBundle> languageResource = new HashMap<>();


    //	FrameworkCache frameworkCache;
//	NewIdGetter newIdGetter;
    PersonalConfigHolder personnalConfig;
    SystemConfigHolder systemConfig;

    /**
     *
     * 取得request中的Locale定义 首先是session中是否有定义 -- 用户选择的 其次看request本身的字符集 -- 浏览器的
     * 最后看服务器的字符集 -- 当没有足够的信息的时候，使用服务器默认字符集
     *
     * @param request
     * @return
     */

    /**
     * 取得服务器默认字符集。 与Locale.getDefault()不同，它会是用配置的默认字符集。
     *
     * @return
     */
    public static Locale getLocale() {
        String value = SystemContext.getInstance().get(SystemConfigHolder.class).getProperty(
                "framework.pub.sysdefaultlocale");
        if (value != null) {
            Locale[] locs = Locale.getAvailableLocales();
            for (Locale locale : locs) {
                if (value.equals(locale.toString())) {
                    return locale;
                }
            }
        }
        return Locale.getDefault();
    }

    /**
     * 取得定制的语言
     *
     * @param loc    Locale 方言
     * @param msgKey String 消息关键字
     * @param args   Object
     * @return String
     */
    public static String getMsg(Locale loc, String msgKey, Object... args) {
        try {
            String value = findLangResource(loc).getString(msgKey);
            if (args == null || args.length == 0) {
                return value;
            }
            return java.text.MessageFormat.format(value, args);
        } catch (MissingResourceException ex) {
            return msgKey;
        }
    }

    /**
     * 找到语言资源文件
     *
     * @param loc Locale
     * @return ResourceBundle
     */
    private static ResourceBundle findLangResource(Locale loc) {
        ResourceBundle rb = languageResource.get(loc);
        if (rb != null) {
            return rb;
        }
        if (loc == null) {
            loc = Locale.ENGLISH;
        }
        try {
            rb = ResourceBundle.getBundle(LANG_FILE_PATH, loc);
            if (rb != null) {
                languageResource.put(loc, rb);
            }
            return rb;
        } catch (MissingResourceException ex) {
            LogUtil.error("can not find language resource file: " + ex.getClassName(), ex);
            throw ex;
        }
    }

    public static void outputJson(HttpServletResponse response, final String content) {
        outputContent(response, content, "text/json");
    }

    public static void outputContent(HttpServletResponse response, final String content, String contentType) {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Content-Type", contentType + "; charset=" + ENCODING);
        try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            // 调试用语句
            bos.write(content.getBytes(ENCODING));
        } catch (IOException ex) {
            LogUtil.error(null, ex);
        }
    }

    /**
     * 获取bean工厂
     *
     * @return
     */
    public static BeanFactory getBeanFactory() {
        BeanFactory factory = SystemContext.getInstance().get(BeanFactory.class);
        if (factory == null) {
            throw new NullPointerException("Bean factory is not initialized. Please check your environment. (applicationContext.xml) ");
        }
        return factory;
    }

    /**
     * 获取Bean对象
     *
     * @param beanClass the class of bean
     * @return T bean对象
     */
    public static <T> T getBean(Class<T> beanClass) {
        return getBean(getBeanFactory(), null, beanClass);
    }

    /**
     * 获取Bean对象
     *
     * @param beanId    声明的bean id
     * @param beanClass the class of bean
     * @return T bean对象
     */
    public static <T> T getBean(String beanId, Class<T> beanClass) {
        return getBean(getBeanFactory(), beanId, beanClass);
    }

    /**
     * 获取Bean对象
     *
     * @param beanId 声明的bean id
     * @return bean对象
     */
    public static Object getBean(String beanId) {
        return getBean(getBeanFactory(), beanId, null);
    }

    @SuppressWarnings("unchecked")
    /**
     * 获取Bean对象
     * @param factory bean工厂
     * @param beanId 声明的bean id
     * @param beanClass the class of bean
     * @return T bean对象
     * @return
     */
    private static <T> T getBean(BeanFactory factory, String beanId, Class<T> beanClass) {
        if (factory == null || (Utils.isEmpty(beanId) && beanClass == null)) {
            return null;
        }
        T bean = null;
        if (Utils.notEmpty(beanId) && beanClass != null) {
            bean = factory.getBean(beanId, beanClass);
        } else if (Utils.notEmpty(beanId)) {
            bean = (T) factory.getBean(beanId);
        } else {
            try {
                bean = factory.getBean(beanClass);
            } catch (NoUniqueBeanDefinitionException e) {
                // 取出来bean异常的原因有可能是很多Service继承PubService,导致根据Type找的时候找到多个匹配的实例会报错
                beanId = beanClass.getSimpleName();
                if (Utils.notEmpty(beanId)) {
                    beanId = beanId.substring(0, 1).toLowerCase() + beanId.substring(1);
                    bean = factory.getBean(beanId, beanClass);
                } else {
                    throw e;
                }
            }
        }

        return bean;
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    public static String getLoginUser(HttpServletRequest request) {
        return (String) request.getSession().getAttribute(LOGIN_USER_KEY);
    }

    /**
     * 在Session中存储loginUser的key
     * @param request
     * @param loginUser
     */
    public static void setLoginUser(HttpServletRequest request, String loginUser) {
        request.getSession().setAttribute(LOGIN_USER_KEY, loginUser);
    }

    /**
     * Session中移除LoninUser的key
     * @param request
     */
    public static void removeLoginUser(HttpServletRequest request) {
        request.getSession().removeAttribute(LOGIN_USER_KEY);
    }

    /**
     * 取得系统配置，
     *
     * @param key          关键字 如sysarg.attach.freetaskAttachPath等
     * @param defaultValue 默认值。如果系统配置信息没有的时候，则显示默认值
     * @return
     */
    public static String getSystemConfig(String key, String defaultValue) {
        String value = SystemContext.getInstance().get(SystemConfigHolder.class).getProperty(key);
        return Utils.isEmpty(value) ? defaultValue : value;
    }

    public static Integer getSystemConfigAsInteger(String key, Integer defaultValue) {
        String strValue = getSystemConfig(key, null);
        if (Utils.notEmpty(strValue)) {
            try {
                Integer intValue = new Integer(strValue);
                return intValue;
            } catch (Exception ex) {
            }
        }

        return defaultValue;
    }

    /**
     * 将系统配置保存至配置文件中（xml）
     *
     * @param key
     * @param value
     */
    public static void setSystemConfig(String key, String value) {
        SystemContext.getInstance().get(SystemConfigHolder.class).setProperty(key, value);
    }

    /**
     * 更新缓存
     */
    public static void resetSystemConfig() {
        SystemContext.getInstance().get(SystemConfigHolder.class).reset();
    }

    /**
     * 公用获取个人配置参数配置方法 如果当前个人设置为空则自动取默认值
     *
     * @param userId String
     * @param argStr String
     * @return String
     */
    public static String getPersonalConfig(String userId, String argStr) {
        return SystemContext.getInstance().get(PersonalConfigHolder.class).getProperty(userId, argStr);

    }

    public static Integer getPersonalConfigAsInteger(String userId, String argStr) {
        String strValue = SystemContext.getInstance().get(PersonalConfigHolder.class).getProperty(userId, argStr);
        if (strValue == null) {
            return null;
        }
        try {
            return new Integer(strValue);
        } catch (Exception ex) {
            if (!"default".equals(userId)) {
                strValue = SystemContext.getInstance().get(PersonalConfigHolder.class).getProperty("default", argStr);
                if (strValue == null) {
                    return null;
                }
                try {
                    return new Integer(strValue);
                } catch (Exception ex2) {
                }
            }
        }
        return null;
    }

    /**
     * 公用设置个人配置参数配置方法 如果当前个人设置为空则自动新增
     *
     * @param userId String
     * @param argStr String
     * @param value  String
     * @return String
     */
    public static void setPersonalConfig(String userId, String argStr, String value) {
        SystemContext.getInstance().get(PersonalConfigHolder.class).setProperty(userId, argStr, value);
    }

    /**
     * 获取IP
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipStr = request.getHeader("X-Forwarded-For");
        if (isIpUnknown(ipStr)) {
            ipStr = request.getHeader("X-Real-IP");
        }
        if (isIpUnknown(ipStr)) {
            ipStr = request.getHeader("Proxy-Client-IP");
        }
        if (isIpUnknown(ipStr)) {
            ipStr = request.getHeader("WL-Proxy-Client-IP");
        }

        String remoteAddr = request.getRemoteAddr();
        if (!isIpUnknown(remoteAddr)) {
            if (Utils.isEmpty(ipStr)) {
                ipStr = remoteAddr;
            } else if (!ipStr.contains(remoteAddr)) {
                ipStr += ";" + remoteAddr;
            }
        }

        return ipStr;
    }


    /**
     * 获取到远程请求地址清单(按照从用户真实客户端的IP顺序排列)
     *
     * @param request
     * @return
     */
    public static List<String> getRemoteAddresses(HttpServletRequest request) {
        List<String> ipList = new ArrayList<>();
        String ipStr = getIpAddr(request);
        if (Utils.notEmpty(ipStr)) {
            String[] ipArray = ipStr.split("[,;]");
            for (String ip : ipArray) {
                if (ip != null && Utils.notEmpty(ip.trim())) {
                    ipList.add(ip);
                }
            }
        }
        return ipList;
    }

    private static boolean isIpUnknown(String ip) {
        return ip == null || Utils.isEmpty(ip.trim()) || STR_UNKNOWN.equalsIgnoreCase(ip.trim());
    }

    public static void traceHttpParameters(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("============PARAMETERS IN HTTP REQUEST================\r\n");
        sb.append("URI = ");
        sb.append(request.getRequestURI());
        sb.append("\r\n");
        Enumeration<?> enum1 = request.getParameterNames();
        while (enum1.hasMoreElements()) {
            String key = (String) enum1.nextElement();
            String[] values = request.getParameterValues(key);
            if (values == null || values.length == 0) {
                sb.append(key);
                sb.append(" : ");
                sb.append("NULL");
                sb.append("\r\n");
            } else if (values.length == 1) {
                sb.append(key);
                sb.append(" : ");
                sb.append(values[0]);
                sb.append("\r\n");
            } else {
                sb.append(key);
                sb.append(" : [");
                for (int i = 0; i < values.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(values[i]);
                }
                sb.append("]\r\n");
            }
        }
        LogUtil.info(sb);
    }

    /**
     * <p>取得某个路径下的文本文档的内容</p>
     * <p>文本可以是JS/HTML/CSS等，但一般不建议使用JSP的内容。
     * 因为其动态内容不会被执行。</p>
     * <p>path 为从webcontext开始的绝对路径<br/>
     * 如一个应用访问地址是 http://www.foo.com/myapp/<br/>
     * 他的一个动态页面地址是http://www.foo.com/myapp/service/testingServ.sp?act=index<br/>
     * 里面需要一个HTML的内容。这个HTML位于http://www.foo.com/myapp/res/introduce/total.html<br/>
     * 那么这个path应该是/res/introduce/total.html或前面无斜杠res/introduce/total.html<br/>
     * 而不是../res/introduce/total.html<br/>
     * 也不是/myapp/res/introduce/total.html<br/>
     * </p>
     * <p>这个方法会自动识别文本的字符集</p>
     * <p>典型用法:</p>
     * <pre>
     * HtmlPanel htmlPanel =new HtmlPanel("f_intro",null);
     * htmlPanel.setFilter(false);
     * htmlPanel.setHtml(getFileText("/res/introduce/total.html"));
     * </pre>
     *
     * @param path 路径
     * @return 文本内容
     * @since 2012/2/2
     */
    public static String getFileText(String path) {
        @SuppressWarnings("deprecation")
        String rootPath = SystemContext.getInstance().get(ServletInfo.class).getServletRealPath();
        if (path.startsWith("/") || path.startsWith("\\")) {
            path = path.substring(1);
        }
        try {
            String text = getFileText(new File(rootPath + path));
            return text;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 取得某个文件的文本。自动识别字符集
     * swf等非文本文件，会出现乱码。
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileText(File file) throws IOException {
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            return getText(is);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * 从流中读取文件内容。自动识别字符集
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String getText(InputStream is) throws IOException {
        StreamContent content = getContent(is);
        return new String(content.baos.toByteArray(), content.findCharset);
    }

    private static StreamContent getContent(InputStream is) throws IOException {
        final StreamContent content = new StreamContent();
//		nsDetector det = new nsDetector(nsDetector.CHINESE);
//		det.Init(new nsICharsetDetectionObserver() {
//			public void Notify(String s) {
//				content.findCharset = s;
//			}
//		});
        byte buff[] = new byte[1024];
        boolean done = false;
        boolean isAscii = true;
        int j;
        while ((j = is.read(buff, 0, buff.length)) >= 0) {
            content.baos.write(buff, 0, j);
            content.findCharset = StringUtil.detCharset(buff);
//			if (isAscii)
//				isAscii = det.isAscii(buff, j);
//			if (!isAscii && !done)
//				done = det.DoIt(buff, j, false);// 模糊匹配的话，可以第一次找到就跳出
        }


//		if (content.findCharset == null) {
//			String chars[] = det.getProbableCharsets();
//			if (chars != null && chars.length > 1) {
//				content.findCharset = chars[0];
//			} else {
//				content.findCharset = System.getProperty("file.encoding");
//			}
//		}
//		if("GB2312".equalsIgnoreCase(content.findCharset)&&Charset.isSupported("GBK")){
//			content.findCharset="GBK";//一般中文机器支持GBK
//		}
        return content;
    }

    private static class StreamContent {
        String findCharset;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
    }

    static final String[] MOBILE_SPECIFIC_SUBSTRING = {
            "iPad", "iPhone", "Android",
            "Opera Mobi", "Opera Mini", "IEMobile", "MSIEMobile", "UCWEB",
            "Symbian", "Windows Phone", "Windows CE", "WindowsCE", "BlackBerry",
            "HP iPAQ", "Smartphone", "MIDP",
            "240x320", "176x220", "320x320", "160x160",
    };

    /**
     * 判断请求是否是移动设备发来，
     * 该判断是通过UA来判断的，所以只能识别正规的请求。
     *
     * @param request
     * @return
     */
    public static boolean isFromMobile(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        if (userAgent == null) {
            return false;
        }
        for (String mobile : MOBILE_SPECIFIC_SUBSTRING) {
            if (userAgent.contains(mobile) ||
                    userAgent.contains(mobile.toLowerCase()) ||
                    userAgent.contains(mobile.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断请求是否来自DFish框架
     *
     * @param request
     * @return
     */
    public static boolean isFromDFish(HttpServletRequest request) {
        String req = request.getHeader("x-requested-with");
        return "dfish".equals(req);
    }

}