package com.rongji.dfish.framework.util;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.StringUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lamontYu
 * @create 2019-12-04 13:27
 */
public class ServletUtil {

    private static final String ENCODING = "UTF-8";
    public static final String DOWNLOAD_ENCODING = "ISO8859-1";

    public static String getParameter(HttpServletRequest request, String key) {
        String[] values = getParameterValues(request, key);
        return StringUtil.toString(values);
    }

    public static String[] getParameterValues(HttpServletRequest request, String key) {
        // 处理tomcat下的中文URL的问题 tomcat在GET方法下传递URL
        // encode的数据会出错。它并是不是按照request设置的字符集进行解码的。
        String query = request.getQueryString();
        if (Utils.notEmpty(query)) {
            String[] pairs = query.split("[&]");

            boolean hasFind = false;
            List<String> values = new ArrayList<>();
            for (String string : pairs) {
                String[] pair = string.split("[=]");
                if (pair.length == 2 && key.equals(pair[0])) {
                    try {
                        String value = java.net.URLDecoder.decode(pair[1].replace("%C2%A0", "%20"), ENCODING);
                        if (Utils.notEmpty(value)) {
                            values.add(value);
                            hasFind = true;
                        }
                    } catch (UnsupportedEncodingException e) {
                        LogUtil.error("获取参数异常", e);
                    }
                }
            }
            if (hasFind) {
                return values.toArray(new String[]{});
            }
        }

        return request.getParameterValues(key);
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
        if (Utils.isEmpty(value)) {
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
        if (Utils.isEmpty(value)) {
            cookie.setMaxAge(0);
        } else {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    /**
     * 下载数据流
     *
     * @param response
     * @param is
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @deprecated 经过实测证明，部分浏览器/应用可能在没有content-length 属性的时候，直接停止下载。
     * 请慎用该方法
     */
    public static void downLoadData(final HttpServletResponse response,
                                    InputStream is) throws UnsupportedEncodingException, IOException {
        OutputStream os = response.getOutputStream();
        try {

            byte[] buff = new byte[8192];
            int bytesRead;

            while (-1 != (bytesRead = is.read(buff, 0, buff.length))) {
                os.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            LogUtil.error("FileUtil.downLoadData error", e);
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * 下载方法
     *
     * @param response   应答
     * @param attachFile 文件全路径+全名(服务器端的URL)
     * @param fileName   用户另存为的文件名
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static void downLoadFile(final HttpServletResponse response, String attachFile, String fileName)
            throws UnsupportedEncodingException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        File file = new File(attachFile);
        fileName = FileUtil.getSafeFileName(fileName);
        //String contentType = getContentType(fileName);
        String contentType = "application/octet-stream";
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Accept-Charset", DOWNLOAD_ENCODING);
        response.setHeader("Content-type", contentType);
        response.setHeader("Content-Disposition", "attachment; filename="
                + fileName);
        response.setHeader("Content-Length", String.valueOf(file.length()));

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        if (!file.exists() || file.length() == 0) {
            response.sendError(404);
            return;
        }
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(response.getOutputStream());

            byte[] buff = new byte[8192];
            int bytesRead;

            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            LogUtil.error("FileUtil.downLoadFile error", e);
        } finally {
            if (bis != null) {
                bis.close();
            }

            if (bos != null) {
                bos.close();
            }
        }
    }

    /**
     * 下载方法 支持断点续传
     *
     * @param response   应答
     * @param attachFile 文件全路径+全名(服务器端的URL)
     * @param fileName   用户另存为的文件名
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static void downLoadFile(HttpServletRequest request, HttpServletResponse response, String attachFile,
                                    String fileName) throws UnsupportedEncodingException, IOException {
        File file = new File(attachFile);
        //String contentType = getContentType(fileName);
        String contentType = "application/octet-stream";
        downLoadFile(request, response, file, fileName, contentType);
    }

    /**
     * 把byte当成文件下载的方法
     *
     * @param source   byte数组
     * @param response -
     * @param fileName 文件另存为的名字,现版本可以有中文
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static void downloadByteArrAsFile(byte[] source, final HttpServletResponse response, String fileName)
            throws UnsupportedEncodingException, IOException {
        //String contentType = getContentType(fileName);
        //response.setHeader("Content-type", contentType);
        response.setHeader("Content-type", "application/octet-stream");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Accept-Charset", DOWNLOAD_ENCODING);

        fileName = FileUtil.getSafeFileName(fileName);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("Content-Length", String.valueOf(source.length));

        BufferedOutputStream bos = null;

        try {
            bos = new BufferedOutputStream(response.getOutputStream());
            // 1 << 13 = 8192
            int blocks = ((source.length - 1) >> 13) + 1;

            for (int i = 0; i < blocks; i++) {
                bos.write(source, i << 13, ((source.length - (i << 13)) > 8192) ? 8192 : (source.length - (i << 13)));
            }
        } catch (Exception e) {
            LogUtil.error("FileUtil.downloadByteArrAsFile error", e);
        } finally {
            if (bos != null) {
                bos.close();
            }
        }
    }

    public static void downLoadFile(HttpServletRequest request, HttpServletResponse response, File file, String fileName,
                                    String contentType) throws IOException {
        downLoadFile(request, response, false, contentType, new FileInputStream(file), file.length(), fileName);
    }

    public static boolean downLoadFile(HttpServletRequest request, HttpServletResponse response, boolean inline, String contentType,
                                       InputStream fileInput, long fileLength, String fileName) throws IOException {
        String range = request.getHeader("Range");
        long from = 0L;
        long to = fileLength;
        if (range != null && !"".equals(range)) {
            range = range.toLowerCase();
            if (range.startsWith("bytes")) {
                int equotPos = range.indexOf("=");
                int toPos = range.indexOf("-");
                if (toPos < 0 && equotPos > 0) {
                    toPos = range.length();
                }
                if (toPos > 0) {
                    try {
                        String fromStr;
                        if (equotPos > 0) {
                            fromStr = range.substring(equotPos + 1, toPos);
                        } else {
                            // 从bytes开始截取
                            fromStr = range.substring(5, toPos);
                        }
                        if (Utils.notEmpty(fromStr) && Utils.notEmpty(fromStr.trim())) {
                            from = Long.parseLong(fromStr.trim());
                        }
                    } catch (Exception e) {
                        LogUtil.error("FileUtil.downLoadFile:getting from value error", e);
                    }
                    try {
                        // range中可能有斜杠,这个格式判断不是标准的,可能需要调整
                        int divide = range.indexOf("/");
                        String toStr;
                        if (divide > to) {
                            toStr = range.substring(toPos + 1, divide);
                        } else {
                            toStr = range.substring(toPos + 1);
                        }
                        if (Utils.notEmpty(toStr) && Utils.notEmpty(toStr.trim())) {
                            to = Long.parseLong(toStr.trim());
                        }
                    } catch (Exception e) {
                        LogUtil.error("FileUtil.downLoadFile:getting to value error", e);
                    }
                }
            }
        }

        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Accept-Charset", FileUtil.ENCODING);
        if (Utils.isEmpty(contentType)) {
            contentType = "application/octet-stream";
        }
        response.setHeader("Content-type", contentType);

        long partLength = to - from;
//		boolean complete = to >= fileLength;
        if (from == 0 && to >= fileLength) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Content-Length", String.valueOf(fileLength));
            response.setHeader("Content-Disposition", (inline ? "inline" : "attachment") + "; filename="
                    + URLEncoder.encode(fileName, FileUtil.ENCODING));
        } else {
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Length", String.valueOf(partLength));
            String contentRange = "bytes " + from + "-" + (to - 1) + "/" + fileLength;
            response.setHeader("Content-Range", contentRange);
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        if (fileInput == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }
        // long totalRead = 0;
        boolean complete = false;
        try {
            bis = new BufferedInputStream(fileInput);
            bos = new BufferedOutputStream(response.getOutputStream());

            byte[] buff = new byte[8192];
            int bytesRead;
            if (from > 0) {
                bis.skip(from);
            }
            // 75%节点有被加载到认定为"完成节点"
            long completePointLeft = (long) (fileLength * 0.25);
            long loadedLeft = fileLength - from;
            // 剩余字节大于完成节点的剩余,有机会可被加载到"完成节点"
            boolean canLoadedPoint = loadedLeft >= completePointLeft;

            long currLeftLength = partLength;
            // org.apache.catalina.servlets.DefaultServlet
            while (currLeftLength > 0 && -1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
                long readBytes = Long.parseLong(String.valueOf(bytesRead));
                currLeftLength -= readBytes;
                if (canLoadedPoint) { // 有机会加载到"完成节点"
                    // 剩余加载字节
                    loadedLeft -= readBytes;
                    // 剩余加载字节比需完成的小,说明"完成节点已被加载"
                    if (loadedLeft <= completePointLeft) {
                        // 达到或超过"完成节点"
                        complete = true;
                        // "完成节点"已被加载,无需再计算判断加载
                        canLoadedPoint = false;
                    }
                }
            }
        } catch (IOException e) {
            LogUtil.error("FileUtil.downLoadFile error", e);
        } finally {
            if (bis != null) {
                bis.close();
            }

            if (bos != null) {
                bos.close();
            }
        }
//        LOG.debug("ResponseRange[" + from + "-" + to + "/" + fileLength + "(" + (((double) from * 100) / fileLength) + "%-)],complete:" + complete);
        return complete;
    }

}
