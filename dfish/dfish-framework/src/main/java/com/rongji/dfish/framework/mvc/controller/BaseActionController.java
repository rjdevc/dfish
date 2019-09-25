package com.rongji.dfish.framework.mvc.controller;

import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.DateUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.mvc.response.JsonResponse;
import com.rongji.dfish.ui.JsonObject;
import com.rongji.dfish.ui.json.J;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.util.*;

public class BaseActionController extends MultiActionController {
    /**
     * 允许用户自定义分页数设置
     */
    protected boolean customPageSize;

    public boolean isCustomPageSize() {
        return customPageSize;
    }

    public void setCustomPageSize(boolean customPageSize) {
        this.customPageSize = customPageSize;
    }

    public Object execute(HttpServletRequest request) {
        return "Enter execute";
    }

    /**
     * 获取Request,该获取方式未完全在所有容器验证清楚,可能会有问题,慎用
     *
     * @return HttpServletRequest
     */
    protected HttpServletRequest getRequest() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        Assert.isInstanceOf(ServletRequestAttributes.class, attrs);
        ServletRequestAttributes servletAttrs = (ServletRequestAttributes) attrs;
        return servletAttrs.getRequest();
    }

    protected String getPathValue(Map<String, List<String>> params, String name) {
        String value = "";
        if (Utils.notEmpty(params) && Utils.notEmpty(name)) {
            List<String> values = params.get(name);
            if (Utils.notEmpty(values)) {
                value = values.get(0);
            }
        }
        return value;
    }

    public String getParameter(HttpServletRequest request, String key) {
        // 处理tomcat下的中文URL的问题 tomcat在GET方法下传递URL
        // encode的数据会出错。它并是不是按照request设置的字符集进行解码的。
        // if(!"GET".equals(request.getMethod()))return
        // request.getParameter(key);
        String query = request.getQueryString();
        if (Utils.notEmpty(query)) {
            String[] pairs = query.split("[&]");
            for (String string : pairs) {
                String[] pair = string.split("[=]");
                if (pair.length == 2 && key.equals(pair[0])) {
                    try {
                        return java.net.URLDecoder.decode(pair[1].replace("%C2%A0", "%20"), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        LogUtil.error("获取参数异常", e);
                    }
                }
            }
        }

        return request.getParameter(key);
    }

    @Override
    protected void bind(HttpServletRequest request, Object obj) throws Exception {
        if (obj == null) {
            return;
        }
        // 时间类型特殊处理
        Convertor adptor = getConvertor(obj.getClass());
        adptor.bind(request, obj);
    }

    protected Convertor getConvertor(Class<?> clz) {
        Convertor addpator = formatMap.get(clz);
        if (addpator == null) {
            addpator = buildConvertor(clz);
            formatMap.put(clz, addpator);
        }
        return addpator;
    }

    protected Convertor buildConvertor(Class<?> clz) {
        Convertor c = new Convertor();
        for (Method method : clz.getMethods()) {
            if (method.getName().startsWith("set") && method.getParameterTypes().length == 1) {
                Format f = new Format();
                f.method = method;
                f.type = method.getParameterTypes()[0];

                String fieldName = method.getName().substring(3);
                f.name = fieldName;
                if (f.name.charAt(0) <= 'Z') {
                    f.name = ((char) (f.name.charAt(0) + 32)) + f.name.substring(1);
                }
//				try {
//					Field field = clz.getField(f.name);
//					// 获取不到属性,名称按照截取的(有可能出现连续2个字母都是大写)
//					if (field == null) {
//						f.name = fieldName;
//					}
//				} catch (Exception e) {
//					FrameworkHelper.LOG.warn("获取属性(" + f.name + ")异常,将采用(" + fieldName + ")来获取页面参数值@" + clz.getName());
//					f.name = fieldName;
//				}
                c.formats.add(f);
            }
        }
        return c;
    }

    protected static HashMap<Class<?>, Convertor> formatMap = new HashMap<Class<?>, Convertor>();

    public static class Convertor {
        List<Format> formats = new ArrayList<Format>();

        public void addFormat(Format format) {
            if (format != null) {
                formats.add(format);
            }
        }

        public void bind(HttpServletRequest request, Object obj) throws Exception {
            for (Format f : formats) {
                f.bind(request, obj);
            }
        }
    }

    public static class Format {
        String name;
        Method method;
        Class<?> type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public Class<?> getType() {
            return type;
        }

        public void setType(Class<?> type) {
            this.type = type;
        }

        public void bind(HttpServletRequest request, Object obj) throws Exception {
            Object value = null;
            String str = Utils.getParameter(request, name);
            if (str == null || str.equals("")) {
                if (name.length() > 1 && name.charAt(0) > 'Z' && name.charAt(1) <= 'Z') {
                    // 第1个字母是小写且第2个字母是大写,尝试修正首字母变大写后获取值;
                    String newName = (char) (name.charAt(0) - 32) + name.substring(1);
                    str = Utils.getParameter(request, newName);
                    if (str == null || str.equals("")) {
                        return;
                    }
                } else {
                    return;
                }
            }
            if (type == String.class) {
                value = str;
            } else if (type == Integer.class || type == int.class) {
                // 先转double再转成整型
                Number num = Double.parseDouble(str);
                value = num.intValue();
            } else if (type == Short.class || type == short.class) {
                Number num = Double.parseDouble(str);
                value = num.shortValue();
            } else if (type == Long.class || type == long.class) {
                // 先转double再转成整型
                Number num = Double.parseDouble(str);
                value = num.longValue();
            } else if (type == Double.class || type == double.class) { // 其他数值型暂不处理,理论上不使用其他数值类型
                value = Double.parseDouble(str);
            } else if (type == Boolean.class || type == boolean.class) {
                try {
                    value = Boolean.parseBoolean(str);
                } catch (Exception e) {
                    value = "1".equals(str) || "T".equalsIgnoreCase(str);
                }
            } else if (type == java.util.Date.class) {
                str = str.trim();
                if (str.length() <= 7) {
                    value = DateUtil.parse(str, "yyyy-MM");
                } else if (str.length() <= 10) {
                    value = DateUtil.parse(str, "yyyy-MM-dd");
                } else if (str.length() <= 16) {
                    value = DateUtil.parse(str, "yyyy-MM-dd HH:mm");
                } else {
                    value = DateUtil.parse(str, "yyyy-MM-dd HH:mm:ss");
                }
            } else {
                throw new java.lang.UnsupportedOperationException("Can not bind value for " + name + " ("
                        + type.getName() + ")");
            }
            method.invoke(obj, new Object[]{value});
        }
    }

    protected void outputJson(HttpServletResponse response, JsonObject jsonObject) {
        FrameworkHelper.outputJson(response, jsonObject);
    }

    /**
     * 默认分页大小
     *
     * @return int
     */
    protected int getPageSize() {
        return 30;
    }

    /**
     * 获取分页信息对象
     *
     * @param request http请求
     * @return Page
     */
    public Page getPage(HttpServletRequest request) {
        if (isCustomPageSize()) {
            int pageSize = 0;
            String pageSizeStr = request.getParameter("pageSize");
            if (Utils.notEmpty(pageSizeStr)) {
                try {
                    pageSize = Integer.parseInt(pageSizeStr);
                } catch (Exception e) {
                    LogUtil.warn("分页信息传递异常:" + convert2JSON(request));
                }
            }
            if (pageSize <= 0) {
                pageSize = getPageSize();
            }
            return getPage(request, pageSize);
        } else {
            return getPage(request, getPageSize());
        }
    }

    /**
     * 获取分页信息对象
     *
     * @param request  http请求
     * @param pageSize 分页大小
     * @return Page
     */
    public Page getPage(HttpServletRequest request, int pageSize) {
        return getPage(request.getParameter("cp"), pageSize);
    }

    /**
     * 获取分页信息对象
     *
     * @param cp       当前页
     * @param pageSize 分页大小
     * @return
     */
    public Page getPage(String cp, int pageSize) {
        int cpValue = 1;
        if (Utils.notEmpty(cp)) {
            try {
                cpValue = Integer.parseInt(cp);
            } catch (Exception e) {
                cpValue = 1;
            }
        }
        return getPage(cpValue, pageSize);
    }

    public Page getPage(int cp, int pageSize) {
        Page page = new Page();
        if (cp < 1) {
            cp = 1;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }
        page.setCurrentPage(cp);
        page.setPageSize(pageSize);
        return page;
    }

    /**
     * 获取进度条编号,由[调用方法#sessionId#dataId]构成
     *
     * @param sessionId
     * @param dataId
     * @return
     */
    protected String getProgressKey(String sessionId, String dataId) {
        String call = "";
        try {
            // 获取调用方法,这里有可能因为不同容器导致调用堆栈不同,未一一验证测试
            StackTraceElement callStack = Thread.currentThread().getStackTrace()[2];
            // 取上级调用方法名
            call = callStack.getClassName() + "." + callStack.getMethodName();
        } catch (Throwable e) {
            LogUtil.error("获取进度条编号-调用堆栈异常", e);
        }
        if (dataId == null) {
            dataId = "";
        } else if (dataId.length() > 64) {
            // 名称太长时强制截取字符,这里给的100是相对比较安全的数字
            dataId = dataId.substring(0, 64);
        }
        return call + "#" + sessionId + "#" + dataId;
    }

    protected String getDataId(String progressKey) {
        if (Utils.isEmpty(progressKey)) {
            return "";
        }
        int endIndex = progressKey.lastIndexOf("#");
        if (endIndex < 0) {
            endIndex = progressKey.length();
        }
        String dataId = progressKey.substring(endIndex + 1);
        return dataId;
    }

    @ExceptionHandler
    @ResponseBody
    public Object exception(Throwable e) {
        JsonResponse<?> jsonResponse = new JsonResponse<>();
        Throwable cause = e;
        if (!(cause instanceof DfishException)) {
            // 防止套路深,而往下寻找DfishException
            while (cause.getCause() != null) {
                if (cause == cause.getCause()) {
                    break;
                }
                if (cause.getCause() instanceof DfishException) {
                    cause = cause.getCause();
                    break;
                }
                cause = cause.getCause();
            }
        }

        HttpServletRequest request = getRequest();
        if (request != null) {
            request.setAttribute("EXCEPTION_HANDLED", true);
        }

        if (cause instanceof DfishException) {
            DfishException cast = (DfishException) cause;
            jsonResponse.setErrCode(cast.getExceptionCode());
            jsonResponse.setErrMsg(cast.getMessage());
        } else {
            String requestJson = convert2JSON(request);
            String errMsg = null;
            if (cause instanceof SocketException) {
                errMsg = "网络异常@" + System.currentTimeMillis();
                FrameworkHelper.LOG.error(requestJson + "\r\n" + errMsg + "@" + e.getClass().getName() + "#" + e.getMessage());
            } else {
                errMsg = "系统内部错误@" + System.currentTimeMillis();
                FrameworkHelper.LOG.error(requestJson + "\r\n" + errMsg, e);
            }
            jsonResponse.setErrMsg(errMsg);
        }

        return jsonResponse;
    }

    protected String convert2JSON(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        Map<String, Object> json = new LinkedHashMap<>();
        Map<String, Object> headMap = new LinkedHashMap<>();
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String, Object> sessionMap = new LinkedHashMap<>();
        json.put("requestURI", request.getRequestURI());
        json.put("head", headMap);
        json.put("parameter", paramMap);
        json.put("session", sessionMap);
        sessionMap.put(FrameworkHelper.LOGIN_USER_KEY, FrameworkHelper.getLoginUser(request));
        if (request.getHeaderNames() != null) {
            java.util.Enumeration<String> headNames = request.getHeaderNames();
            while (headNames.hasMoreElements()) {
                String n = headNames.nextElement();
                List<String> headers = new ArrayList<>();
                if (request.getHeaders(n) != null) {
                    headMap.put(n, headers);
                    java.util.Enumeration<String> headContent = request.getHeaders(n);
                    while (headContent.hasMoreElements()) {
                        String v = headContent.nextElement();
                        headers.add(v);
                    }
                }
            }
        }

        return J.toJson(json);
    }

}
