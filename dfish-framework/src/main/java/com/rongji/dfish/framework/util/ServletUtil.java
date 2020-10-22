package com.rongji.dfish.framework.util;

import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.base.util.Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 服务层工具类
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class ServletUtil {

    public static final String ENCODING_UTF8 = "UTF-8";
    public static final String DOWNLOAD_ENCODING = "ISO8859-1";
    private static final SimpleDateFormat DF_GMT;

    static {
        //    Fri, 09 Oct 2020 09:20:50 GMT
        DF_GMT = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z", Locale.ENGLISH);
        DF_GMT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * 获取参数值
     *
     * @param request 请求
     * @param key     参数名
     * @return String
     */
    public static String getParameter(HttpServletRequest request, String key) {
        String[] values = getParameterValues(request, key);
        return StringUtil.toString(values);
    }

    /**
     * 获取参数值数组
     *
     * @param request 请求
     * @param key     参数名
     * @return Strnig[] 参数值数组
     */
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
                        String value = java.net.URLDecoder.decode(pair[1].replace("%C2%A0", "%20"), ENCODING_UTF8);
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
     * @param request 请求
     * @param name    名称
     * @return String
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


//    /**
//     * 下载方法
//     *
//     * @param response   应答
//     * @param attachFile 文件全路径+全名(服务器端的URL)
//     * @param fileName   用户另存为的文件名
//     * @throws IOException
//     * @throws UnsupportedEncodingException
//     */
//    public static void downLoadFile(final HttpServletResponse response, String attachFile, String fileName)
//            throws UnsupportedEncodingException, IOException {
//        response.setStatus(HttpServletResponse.SC_OK);
//        File file = new File(attachFile);
//        fileName = FileUtil.getSafeFileName(fileName);
//        //String contentType = getContentType(fileName);
//        String contentType = "application/octet-stream";
//        response.setHeader("Accept-Ranges", "bytes");
//        response.setHeader("Accept-Charset", DOWNLOAD_ENCODING);
//        response.setHeader("Content-type", contentType);
//        response.setHeader("Content-Disposition", "attachment; filename="
//                + fileName);
//        response.setHeader("Content-Length", String.valueOf(file.length()));
//
//        BufferedInputStream bis = null;
//        BufferedOutputStream bos = null;
//        if (!file.exists() || file.length() == 0) {
//            response.sendError(404);
//            return;
//        }
//        try {
//            bis = new BufferedInputStream(new FileInputStream(file));
//            bos = new BufferedOutputStream(response.getOutputStream());
//
//            byte[] buff = new byte[8192];
//            int bytesRead;
//
//            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
//                bos.write(buff, 0, bytesRead);
//            }
//        } catch (final IOException e) {
//            LogUtil.error("FileUtil.downLoadFile error", e);
//        } finally {
//            if (bis != null) {
//                bis.close();
//            }
//
//            if (bos != null) {
//                bos.close();
//            }
//        }
//    }
//
//    /**
//     * 下载方法 支持断点续传
//     *
//     * @param response   应答
//     * @param attachFile 文件全路径+全名(服务器端的URL)
//     * @param fileName   用户另存为的文件名
//     * @throws IOException
//     * @throws UnsupportedEncodingException
//     */
//    public static void downLoadFile(HttpServletRequest request, HttpServletResponse response, String attachFile,
//                                    String fileName) throws UnsupportedEncodingException, IOException {
//        File file = new File(attachFile);
//        //String contentType = getContentType(fileName);
//        String contentType = "application/octet-stream";
//        downLoadFile(request, response, file, fileName, contentType);
//    }
//
//    /**
//     * 把byte当成文件下载的方法
//     *
//     * @param source   byte数组
//     * @param response -
//     * @param fileName 文件另存为的名字,现版本可以有中文
//     * @throws IOException
//     * @throws UnsupportedEncodingException
//     */
//    public static void downloadByteArrAsFile(byte[] source, final HttpServletResponse response, String fileName)
//            throws UnsupportedEncodingException, IOException {
//        //String contentType = getContentType(fileName);
//        //response.setHeader("Content-type", contentType);
//        response.setHeader("Content-type", "application/octet-stream");
//        response.setHeader("Accept-Ranges", "bytes");
//        response.setHeader("Accept-Charset", DOWNLOAD_ENCODING);
//
//        fileName = FileUtil.getSafeFileName(fileName);
//        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
//        response.setHeader("Content-Length", String.valueOf(source.length));
//
//        BufferedOutputStream bos = null;
//
//        try {
//            bos = new BufferedOutputStream(response.getOutputStream());
//            // 1 << 13 = 8192
//            int blocks = ((source.length - 1) >> 13) + 1;
//
//            for (int i = 0; i < blocks; i++) {
//                bos.write(source, i << 13, ((source.length - (i << 13)) > 8192) ? 8192 : (source.length - (i << 13)));
//            }
//        } catch (Exception e) {
//            LogUtil.error("FileUtil.downloadByteArrAsFile error", e);
//        } finally {
//            if (bos != null) {
//                bos.close();
//            }
//        }
//    }
//
//    /**
//     * 下载文件
//     *
//     * @param request     请求
//     * @param response    响应
//     * @param file        文件
//     * @param fileName    下载的文件名
//     * @param contentType 类型
//     * @throws IOException
//     */
//    public static void downLoadFile(HttpServletRequest request, HttpServletResponse response, File file, String fileName,
//                                    String contentType) throws IOException {
//        downLoadFile(request, response, false, contentType, new FileInputStream(file), file.length(), fileName);
//    }
//
//    /**
//     * 下载文件
//     *
//     * @param request     请求
//     * @param response    响应
//     * @param inline      是否内联
//     * @param contentType 类型
//     * @param fileInput   文件输入流
//     * @param fileLength  文件大小
//     * @param fileName    下载的文件名
//     * @return boolean 下载完成
//     * @throws IOException
//     */
//    public static boolean downLoadFile(HttpServletRequest request, HttpServletResponse response, boolean inline, String contentType,
//                                       InputStream fileInput, long fileLength, String fileName) throws IOException {
//        String range = request.getHeader("Range");
//        long from = 0L;
//        long to = fileLength;
//        if (range != null && !"".equals(range)) {
//            range = range.toLowerCase();
//            if (range.startsWith("bytes")) {
//                int equotPos = range.indexOf("=");
//                int toPos = range.indexOf("-");
//                if (toPos < 0 && equotPos > 0) {
//                    toPos = range.length();
//                }
//                if (toPos > 0) {
//                    try {
//                        String fromStr;
//                        if (equotPos > 0) {
//                            fromStr = range.substring(equotPos + 1, toPos);
//                        } else {
//                            // 从bytes开始截取
//                            fromStr = range.substring(5, toPos);
//                        }
//                        if (Utils.notEmpty(fromStr) && Utils.notEmpty(fromStr.trim())) {
//                            from = Long.parseLong(fromStr.trim());
//                        }
//                    } catch (Exception e) {
//                        LogUtil.error("FileUtil.downLoadFile:getting from value error", e);
//                    }
//                    try {
//                        // range中可能有斜杠,这个格式判断不是标准的,可能需要调整
//                        int divide = range.indexOf("/");
//                        String toStr;
//                        if (divide > to) {
//                            toStr = range.substring(toPos + 1, divide);
//                        } else {
//                            toStr = range.substring(toPos + 1);
//                        }
//                        if (Utils.notEmpty(toStr) && Utils.notEmpty(toStr.trim())) {
//                            to = Long.parseLong(toStr.trim());
//                        }
//                    } catch (Exception e) {
//                        LogUtil.error("FileUtil.downLoadFile:getting to value error", e);
//                    }
//                }
//            }
//        }
//
//        response.setHeader("Accept-Ranges", "bytes");
//        response.setHeader("Accept-Charset", FileUtil.ENCODING);
//        if (Utils.isEmpty(contentType)) {
//            contentType = "application/octet-stream";
//        }
//        response.setHeader("Content-type", contentType);
//
//        long partLength = to - from;
////		boolean complete = to >= fileLength;
//        if (from == 0 && to >= fileLength) {
//            response.setStatus(HttpServletResponse.SC_OK);
//            response.setHeader("Content-Length", String.valueOf(fileLength));
//            response.setHeader("Content-Disposition", (inline ? "inline" : "attachment") + "; filename="
//                    + URLEncoder.encode(fileName, FileUtil.ENCODING));
//        } else {
//            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
//            response.setHeader("Content-Length", String.valueOf(partLength));
//            String contentRange = "bytes " + from + "-" + (to - 1) + "/" + fileLength;
//            response.setHeader("Content-Range", contentRange);
//        }
//
//        BufferedInputStream bis = null;
//        BufferedOutputStream bos = null;
//        if (fileInput == null) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND);
//            return false;
//        }
//        // long totalRead = 0;
//        boolean complete = false;
//        try {
//            bis = new BufferedInputStream(fileInput);
//            bos = new BufferedOutputStream(response.getOutputStream());
//
//            byte[] buff = new byte[8192];
//            int bytesRead;
//            if (from > 0) {
//                bis.skip(from);
//            }
//            // 75%节点有被加载到认定为"完成节点"
//            long completePointLeft = (long) (fileLength * 0.25);
//            long loadedLeft = fileLength - from;
//            // 剩余字节大于完成节点的剩余,有机会可被加载到"完成节点"
//            boolean canLoadedPoint = loadedLeft >= completePointLeft;
//
//            long currLeftLength = partLength;
//            // org.apache.catalina.servlets.DefaultServlet
//            while (currLeftLength > 0 && -1 != (bytesRead = bis.read(buff, 0, buff.length))) {
//                bos.write(buff, 0, bytesRead);
//                long readBytes = Long.parseLong(String.valueOf(bytesRead));
//                currLeftLength -= readBytes;
//                if (canLoadedPoint) { // 有机会加载到"完成节点"
//                    // 剩余加载字节
//                    loadedLeft -= readBytes;
//                    // 剩余加载字节比需完成的小,说明"完成节点已被加载"
//                    if (loadedLeft <= completePointLeft) {
//                        // 达到或超过"完成节点"
//                        complete = true;
//                        // "完成节点"已被加载,无需再计算判断加载
//                        canLoadedPoint = false;
//                    }
//                }
//            }
//        } catch (IOException e) {
//            LogUtil.error("FileUtil.downLoadFile error", e);
//        } finally {
//            if (bis != null) {
//                bis.close();
//            }
//
//            if (bos != null) {
//                bos.close();
//            }
//        }
////        LOG.debug("ResponseRange[" + from + "-" + to + "/" + fileLength + "(" + (((double) from * 100) / fileLength) + "%-)],complete:" + complete);
//        return complete;
//    }

    /**
     * 下载一个资源
     *
     * @param request          HttpServletRequest
     * @param response         HttpServletResponse
     * @param downloadResource 资源
     * @param inline           true的时候当成文件直接打开。 false的时候当成附件另存为。
     * @return
     */
    public static DownloadStatus download(HttpServletRequest request, HttpServletResponse response,
                                          DownloadResource downloadResource, boolean inline) throws IOException {
        //处理缓存。如果已经缓存了。则无需下载，直接返回HttpServletResponse.SC_NOT_MODIFIED
        DownloadStatus status = new DownloadStatus();
        status.setContentLength(downloadResource.getLength());
        status.setResourceLength(downloadResource.getLength());
        status.setRangeBegin(0);
        status.setRangeEnd(downloadResource.getLength() - 1);
        long headerValue = request.getDateHeader("If-Modified-Since");
        if (headerValue != -1) {
            if ((request.getHeader("If-None-Match") == null) && (downloadResource.getLastModified() < headerValue + 1000L)) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.setHeader("ETag", getEtag(downloadResource));
                status.setCached(true);
                return status;
            }
        }

        // 计算 需要下载数据的范围
        String range = request.getHeader("Range");
        long from = 0L;
        long to = downloadResource.getLength() - 1;
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
                        LogUtil.warn("getting from value error from range:"+ range );
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
                        LogUtil.warn("getting to value error from range:"+ range);
                    }
                }
            }
        }
        long contentLength = to - from + 1;

        status.setContentLength(contentLength);
        status.setRangeBegin(from);
        status.setRangeEnd(to);

        String contentType = downloadResource.getContentType();
        if (Utils.isEmpty(contentType)) {
            String extName = FileUtil.getFileExtName(downloadResource.getName());
            contentType = getMimeType(extName);
            if (Utils.isEmpty(contentType)) {
                contentType = "application/octet-stream";
            }
        }
        response.setHeader("Content-type", contentType);

        if (range != null) {
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Length", String.valueOf(contentLength));
            String contentRange = "bytes " + from + "-" + to + "/" + downloadResource.getLength();
            response.setHeader("Content-Range", contentRange);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Content-Length", String.valueOf(downloadResource.getLength()));
        }
        if (from == 0) {
            if (!inline) {
                response.setHeader("Content-Disposition", "attachment; filename="
                        + URLEncoder.encode(downloadResource.getName(), FileUtil.ENCODING));
            }
            if (downloadResource.getLastModified() > 0L) {
                synchronized (DF_GMT) {
                    response.setHeader("Last-Modified", DF_GMT.format(new Date(downloadResource.getLastModified())));
                }

                response.setHeader("ETag", getEtag(downloadResource));
            }
        }


        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream is = null;
        String errorMessage = null;
        try {
            if (downloadResource.getInputStreamProvider() != null) {
                is = downloadResource.getInputStreamProvider().open();
            }
        } catch(FileNotFoundException | IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
        if (is == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            errorMessage = Utils.isEmpty(errorMessage) ? "DownloadResource " + downloadResource.getName() + " does not exists." : errorMessage;
            LogUtil.error(errorMessage);
            return status;
        }

        try {
            bis = new BufferedInputStream(is);
            status.setSent(true);
            bos = new BufferedOutputStream(response.getOutputStream());

            int bytesRead;
            if (from > 0) {
                bis.skip(from);
            }

            byte[] buff = new byte[8192];
            long currLeftLength = contentLength;
            // org.apache.catalina.servlets.DefaultServlet
            while (currLeftLength > 0 && -1 != (bytesRead = bis.read(buff, 0, (int) (currLeftLength > 8192 ? 8192 : currLeftLength)))) {
                bos.write(buff, 0, bytesRead);
                long readBytes = Long.parseLong(String.valueOf(bytesRead));
                currLeftLength -= readBytes;
                status.setCompleteLength(status.getCompleteLength() + readBytes);
            }

        } catch (IOException e) {
            if (e.getClass().getSimpleName().equals("ClientAbortException")) {
                // 日志暂时不输出,这个日志也没什么意思
//                String msg="client abort when download "+request.getRequestURI() +" name="+downloadResource.getName();
//                if(range!=null){
//                    msg+=" range="+range;
//                }
//                LogUtil.info(msg);
            } else {
                LogUtil.error("download error", e);
            }
//            LogUtil.error("FileUtil.downLoadFile error", e);
        } finally {
            if (bis != null) {
                bis.close();
            }

            if (bos != null) {
                // FIXME 这里可能是隐患,当client abort的时候close会抛异常,这里的OutputStream很可能不手动关闭,似乎容器会自动关闭,待验证
                bos.close();
            }
        }

        return status;
    }

    private static String getEtag(DownloadResource resource) {
        return "W/\"" + resource.getLength() + "-" + resource.getLastModified() + "\"";
    }

    public static DownloadStatus download(HttpServletRequest request, HttpServletResponse response, File file, boolean inline) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("file is null");
        }
        DownloadResource resource = new DownloadResource(file.getName(), file.length(), file.lastModified(),
                () -> new FileInputStream(file));
        return download(request, response, resource, inline);
    }

    protected static final Map<String, String> MIME_MAP = new HashMap<>();

    /**
     * 根据文件拓展名获取内联类型
     *
     * @param extName
     * @return
     */
    public static String getMimeType(String extName) {
        if (extName == null) {
            return null;
        }
        if (extName.startsWith(".")) {
            extName = extName.substring(1).toLowerCase();
        }
        if (MIME_MAP.size() == 0) {
            //尝试读取配置。
            try {
                String classPath = ServletUtil.class.getName();
                int lastDotIndex = classPath.lastIndexOf(".");
                if (lastDotIndex > 0) {
                    classPath = classPath.substring(0, lastDotIndex + 1).replace(".", "/");
                }

                ResourceBundle rb = ResourceBundle.getBundle(classPath + "mimetypes");
                Enumeration<String> keys = rb.getKeys();
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();
                    String value = rb.getString(key);
                    MIME_MAP.put(key, value);
                }
            } catch (Throwable e) {
                LogUtil.error("获取内联类型异常", e);
                MIME_MAP.put("[NONE]", "application/octet-stream");
            }
        }
        return MIME_MAP.get(extName);
    }

}
