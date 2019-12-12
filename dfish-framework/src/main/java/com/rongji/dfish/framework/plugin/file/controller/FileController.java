package com.rongji.dfish.framework.plugin.file.controller;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.context.SystemContext;
import com.rongji.dfish.base.exception.MarkedException;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.ThreadUtil;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.info.ServletInfo;
import com.rongji.dfish.framework.mvc.controller.BaseActionController;
import com.rongji.dfish.framework.mvc.response.JsonResponse;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingDefine;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingManager;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingScheme;
import com.rongji.dfish.framework.plugin.file.controller.config.ImageHandlingDefine;
import com.rongji.dfish.framework.plugin.file.controller.plugin.FileUploadPlugin;
import com.rongji.dfish.framework.plugin.file.dto.PreviewResponse;
import com.rongji.dfish.framework.plugin.file.dto.UploadItem;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.framework.util.ServletUtil;
import com.rongji.dfish.base.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 附件相关控制层
 *
 * @author lamontYu
 * @version 1.3 去掉类注解,采用配置加载模式,有利于项目自定义配置 lamontYu 2019-12-5
 * @create 2018-08-03 before
 * @since 3.0
 */
@RequestMapping("/file")
public class FileController extends BaseActionController {

    @Resource(name = "fileService")
    private FileService fileService;
    @Autowired(required = false)
    private List<FileUploadPlugin> uploadPlugins;
    @Autowired
    private FileHandlingManager fileHandlingManager;

    private Map<String, FileUploadPlugin> uploadPluginMap = new HashMap<>();

    private static final DateFormat DF_GMT = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss z", Locale.ENGLISH);

    static {
        DF_GMT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @PostConstruct
    private void init() {
        if (Utils.notEmpty(uploadPlugins)) {
            for (FileUploadPlugin uploadPlugin : uploadPlugins) {
                registerUploadPlugin(uploadPlugin);
            }
        }
    }

    private void registerUploadPlugin(FileUploadPlugin uploadPlugin) {
        if (uploadPlugin == null) {
            return;
        }
        if (Utils.isEmpty(uploadPlugin.name())) {
            LogUtil.warn("The name is empty.[" + uploadPlugin.getClass().getName() + "]");
        }
        FileUploadPlugin old = uploadPluginMap.put(uploadPlugin.name(), uploadPlugin);
        if (old != null) {
            LogUtil.warn("The FileUploadPlugin[" + old.getClass().getName() + "] is replaced by [" + uploadPlugin.getClass().getName() + "]");
        }
    }

    /**
     * 附件保存方法,根据定义情况对上传附件类型进行限制
     *
     * @param request
     * @param acceptTypes
     * @return
     */
    public UploadItem saveFile(HttpServletRequest request, String acceptTypes) {
        boolean accept = false;
        UploadItem uploadItem = null;
        try {
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            MultipartFile fileData = mRequest.getFile("Filedata");

            String extName = FileUtil.getFileExtName(fileData.getOriginalFilename());
            accept = fileService.accept(extName, acceptTypes);
            if (accept) {
                String loginUserId = FrameworkHelper.getLoginUser(request);
                uploadItem = fileService.saveFile(fileData.getInputStream(), fileData.getOriginalFilename(), fileData.getSize(), loginUserId);
            }
        } catch (Exception e) {
            String error = "上传失败,系统异常@" + System.currentTimeMillis();
            LogUtil.error(error, e);
            uploadItem = new UploadItem();
            uploadItem.setError(true);
            uploadItem.setText(error);
        }

        if (uploadItem == null) {
            uploadItem = new UploadItem();
            uploadItem.setError(true);
            uploadItem.setText("上传文件失败" + (!accept ? "：当前文件类型不符合系统规范" : ""));
        }
        return uploadItem;
    }

    /**
     * 上传附件
     *
     * @param request
     * @return
     */
    @RequestMapping("/upload/file")
    @ResponseBody
    public JsonResponse<?> uploadFile(HttpServletRequest request) {
        String scheme = request.getParameter("scheme");
        FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);
        // 其实这里根据不同业务模块的判断限制意义不大,根据全局的设置即可
        String acceptTypes = handlingScheme != null && Utils.notEmpty(handlingScheme.getHandlingTypes()) ? handlingScheme.getHandlingTypes() : fileService.getFileTypes();
        UploadItem uploadItem = saveFile(request, acceptTypes);
        return new JsonResponse<>(uploadItem);
    }


    private static final ExecutorService EXECUTOR_IMAGE = ThreadUtil.getCachedThreadPool();

    /**
     * 上传图片
     *
     * @param request
     * @return
     */
    @RequestMapping("/upload/image")
    @ResponseBody
    public JsonResponse<?> uploadImage(HttpServletRequest request) {
        final JsonResponse<?> jsonResponse = uploadFile(request);
        UploadItem uploadItem = (UploadItem) jsonResponse.getData();
        if (uploadItem == null || Utils.isEmpty(uploadItem.getId())) {
            // 这样异常结果返回可能导致前端显示异常
            return jsonResponse;
        }

        String scheme = request.getParameter("scheme");
        final FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);
        if (handlingScheme == null || Utils.isEmpty(handlingScheme.getDefines())) {
            // 无需进行图片压缩
            return jsonResponse;
        }
        final AtomicInteger doneFileCount = new AtomicInteger(0);
        EXECUTOR_IMAGE.execute(() -> {
            // 本应该先提前判断再进行获取压缩,暂时不考虑这些个别情况(多损耗性能)
            String fileId = fileService.decrypt(uploadItem.getId());
            PubFileRecord fileRecord = fileService.get(fileId);
            if (fileRecord == null || Utils.isEmpty(fileRecord.getFileUrl())) {
                LogUtil.warn("生成缩略图出现异常:原记录文件存储记录丢失[" + fileId + "]");
                return;
            }
            int dotIndex = fileRecord.getFileUrl().lastIndexOf(".");
            if (dotIndex < 0) {
                LogUtil.warn("生成缩略图出现异常:原记录文件存储记录异常[" + fileId + "]");
                return;
            }

            String fileExtName = fileRecord.getFileUrl().substring(dotIndex + 1);
            File imageFile = fileService.getFile(fileRecord);
            if (imageFile == null || !imageFile.exists()) {
                LogUtil.warn("生成缩略图出现异常:原图丢失[" + fileId + "]");
                return;
            }
            for (String defineAlias : handlingScheme.getDefines()) {
                try {
                    File outputFile = fileService.getFile(fileRecord, defineAlias, false);
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                    }
                    try (InputStream input = new FileInputStream(imageFile);
                         OutputStream output = new FileOutputStream(outputFile)) {
                        FileHandlingDefine handlingDefine = fileHandlingManager.getDefine(defineAlias);
                        if (handlingDefine == null || !(handlingDefine instanceof ImageHandlingDefine)) {
                            continue;
                        }
                        ImageHandlingDefine realDefine = (ImageHandlingDefine) handlingDefine;
                        if (ImageHandlingDefine.WAY_ZOOM.equals(realDefine.getWay())) {
                            ImageUtil.zoom(input, output, fileExtName, realDefine.getWidth(), realDefine.getHeight());
                        } else if (ImageHandlingDefine.WAY_CUT.equals(realDefine.getWay())) {
                            ImageUtil.cut(input, output, fileExtName, realDefine.getWidth(), realDefine.getHeight());
                        } else if (ImageHandlingDefine.WAY_RESIZE.equals(realDefine.getWay())) {
                            ImageUtil.resize(input, output, fileExtName, realDefine.getWidth(), realDefine.getHeight());
                        }
                        doneFileCount.incrementAndGet();
                    } catch (Exception e) {
                        LogUtil.error("生成缩略图出现异常", e);
                    }
                } catch (Exception e) {
                    LogUtil.error("生成缩略图出现异常", e);
                }

            }
        });
        int waitCount = 0;
        // 至少保证第1个缩略图生成才返回结果;最多等待3秒
        while (doneFileCount.get() < 1 && waitCount++ < 30) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LogUtil.error("生成缩略图等待异常", e);
            }
        }
        return jsonResponse;
    }

    /**
     * 第三方插件附件上传
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/upload/plugin")
    @ResponseBody
    public Object uploadPlugin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String plugin = request.getParameter("plugin");
        // 取得附件上传插件,根据插件对应的上传方法进行附件上传处理
        FileUploadPlugin uploadPlugin = uploadPluginMap.get(plugin);
        if (uploadPlugin == null) {
            return null;
        }
        Object result = uploadPlugin.doRequest(request);
        return result;
    }

    /**
     * 下载附件方法
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/download")
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String enFileId = request.getParameter("fileId");
        String fileId = fileService.decrypt(enFileId);
        PubFileRecord fileRecord = fileService.get(fileId);
        if (fileRecord == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        boolean inline = "1".equals(request.getParameter("inline"));
        DownloadParam downloadParam = getDownloadParam(fileRecord, null);
        downloadParam.setInline(inline);
        downloadFileData(response, fileService.getFileInputStream(fileRecord), downloadParam);
    }

    protected static final Map<String, String> MIME_MAP = new HashMap<>();

    protected Class<?> getMimeClass() {
        return getClass();
    }

    /**
     * 根据文件拓展名获取内联类型
     *
     * @param extName
     * @return
     */
    protected String getMimeType(String extName) {
        if (extName == null) {
            return null;
        }
        if (extName.startsWith(".")) {
            extName = extName.substring(1).toLowerCase();
        }
        if (MIME_MAP.size() == 0) {
            //尝试读取配置。
            try {
                String classPath = getMimeClass().getName();
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

    /**
     * 下载附件方法
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @deprecated 下载附件方法统一使用download
     */
    @RequestMapping("/download/file")
    @ResponseBody
    @Deprecated
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 下载附件方法统一使用download
        download(request, response);
    }

    private static class DownloadParam {
        boolean inline;
        String fileName;
        long fileSize;
        Date lastModified;
        String alias;

        public boolean isInline() {
            return inline;
        }

        public DownloadParam setInline(boolean inline) {
            this.inline = inline;
            return this;
        }

        public String getFileName() {
            return fileName;
        }

        public DownloadParam setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public long getFileSize() {
            return fileSize;
        }

        public DownloadParam setFileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public Date getLastModified() {
            return lastModified;
        }

        public DownloadParam setLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public String getAlias() {
            return alias;
        }

        public DownloadParam setAlias(String alias) {
            this.alias = alias;
            return this;
        }
    }

    private boolean downloadFileData(HttpServletResponse response, InputStream input, DownloadParam downloadParam) throws Exception {
        if (input == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }
        String encoding = "UTF-8";
        try {
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Accept-Charset", encoding);
            String contentType = null;

            if (downloadParam.isInline()) {
                String extName = FileUtil.getFileExtName(downloadParam.getFileName());
                if (Utils.notEmpty(extName)) {
                    contentType = getMimeType(extName);
                }
            }
            if (Utils.isEmpty(contentType)) {
                contentType = "application/octet-stream";
            }

            response.setHeader("Content-type", contentType);
            response.setHeader("Content-Disposition", (downloadParam.isInline() ? "inline" : "attachment") + "; filename=" + URLEncoder.encode(downloadParam.getFileName(), encoding));
            response.setHeader("Content-Length", String.valueOf(downloadParam.getFileSize()));
            if (downloadParam.getLastModified() != null) {
                synchronized (DF_GMT) {
                    response.setHeader("Last-Modified", DF_GMT.format(downloadParam.getLastModified()));
                }
                response.setHeader("ETag", getEtag(downloadParam));
            }
            response.setStatus(HttpServletResponse.SC_OK);
            ServletUtil.downLoadData(response, input);
            return true;
        } catch (Exception e) {
            String error = "下载附件异常@" + System.currentTimeMillis();
            LogUtil.error(error, e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, error);
            return false;
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    private static String getEtag(DownloadParam downloadParam) {
        long lastModified = downloadParam.getLastModified() == null ? 0 : downloadParam.getLastModified().getTime();
        String etag = getIntHex(downloadParam.getFileSize()) + getIntHex(lastModified);
        return etag;
    }

    private static String getIntHex(long l) {
        l = (l & 0xFFFFFFFFL) | 0x100000000L;
        String s = Long.toHexString(l);
        return s.substring(1);
    }

    private static final String FILE_SCHEME_AUTO = "AUTO";
    private static final String FILE_ALIAS_AUTO = "AUTO";

    protected boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, DownloadParam downloadParam) {
        try {
            long headerValue = request.getDateHeader("If-Modified-Since");
            long lastModified = downloadParam.getLastModified() == null ? 0 : downloadParam.getLastModified().getTime();
            if (headerValue != -1) {

                // If an If-None-Match header has been specified, if modified since
                // is ignored.
                if ((request.getHeader("If-None-Match") == null) && (lastModified < headerValue + 1000L)) {
                    // The entity has not been modified since the date
                    // specified by the client. This is not an error case.
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    response.setHeader("ETag", getEtag(downloadParam));

                    return false;
                }
            }
        } catch (IllegalArgumentException illegalArgument) {
            return true;
        }
        return true;

    }

    private DownloadParam getDownloadParam(PubFileRecord fileRecord, String alias) throws Exception {
        DownloadParam downloadParam = new DownloadParam();
        String fileName = fileRecord.getFileName();
        long fileSize = fileService.getFileSize(fileRecord, alias);
        downloadParam.setFileName(fileName);
        downloadParam.setFileSize(fileSize);
        downloadParam.setLastModified(fileRecord.getUpdateTime());
        downloadParam.setAlias(alias);
        return downloadParam;
    }

    /**
     * 显示缩略图方法
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/thumbnail")
    @ResponseBody
    public void thumbnail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String scheme = request.getParameter("scheme");
        String alias = request.getParameter("alias");
        String fileId = request.getParameter("fileId");
        thumbnail(request, response, scheme, alias, fileId);
    }

    /**
     * 显示缩略图方法
     *
     * @param response
     * @param scheme
     * @param alias
     * @param fileId
     * @throws Exception
     */
    @RequestMapping("/thumbnail/{scheme}/{alias}/{fileId}")
    @ResponseBody
    public void thumbnail(HttpServletRequest request, HttpServletResponse response, @PathVariable String scheme,
                          @PathVariable String alias, @PathVariable String fileId) throws Exception {
        String decFileId = fileService.decrypt(fileId);
        PubFileRecord fileRecord = fileService.get(decFileId);

        if (fileRecord == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 获取附件别名
        String realAlias = getFileAlias(alias, scheme);

        DownloadParam downloadParam = getDownloadParam(fileRecord, realAlias);
        // 目前文件下载统一默认都是原件下载
        if (!checkIfModifiedSince(request, response, downloadParam)) {
            return;
        }

        downloadParam.setInline(true);
        boolean success = downloadFileData(response, fileService.getFileInputStream(fileRecord, realAlias), downloadParam);
        // 下载不成功,用默认图片代替
        if (!success) {
            FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);
            String defaultIcon = null;
            if (handlingScheme != null) {
                if (Utils.notEmpty(handlingScheme.getDefaultIcon())) {
                    defaultIcon = handlingScheme.getDefaultIcon();
                }
            }
            defaultIcon = Utils.isEmpty(defaultIcon) ? "default.png" : defaultIcon;
            if (Utils.notEmpty(realAlias)) {
                int lastDot = defaultIcon.lastIndexOf(".");
                if (lastDot >= 0) {
                    defaultIcon = defaultIcon.substring(0, lastDot) + "_" + realAlias + defaultIcon.substring(lastDot);
                } else {
                    defaultIcon += "_" + realAlias;
                }
            }

            // 这里可能考虑重定向到具体文件目录去
            File defaultImageFile = new File(SystemContext.getInstance().get(ServletInfo.class).getServletRealPath() + "m/default/img/" + defaultIcon);
            if (defaultImageFile.exists()) {
                downloadParam.setFileName(defaultImageFile.getName()).setFileSize(defaultImageFile.length())
                        .setLastModified(new Date(defaultImageFile.lastModified()));

                downloadFileData(response, new FileInputStream(defaultImageFile), downloadParam);
                return;
            } else {
                String error = "附件不存在@" + System.currentTimeMillis();
                LogUtil.warn(error + "[" + fileId + "->" + decFileId + "]");
                response.sendError(HttpServletResponse.SC_NOT_FOUND, error);
            }
        }
    }

    private String getFileAlias(String alias, String scheme) {
        if (Utils.isEmpty(alias) || FILE_ALIAS_AUTO.equals(alias)) {
            FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);
            if (handlingScheme != null && Utils.notEmpty(handlingScheme.getDefines())) {
                alias = handlingScheme.getDefines().get(0);
            } else {
                alias = null;
            }
        }
        return alias;
    }

    /**
     * 预览文件方法(目前仅图片预览方法,如果是文件预览需做处理,不支持预览可能直接下载)
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/preview")
    @ResponseBody
    public Object preview(HttpServletRequest request) throws Exception {
        String enFileId = request.getParameter("fileId");
        String fileId = fileService.decrypt(enFileId);
        PubFileRecord fileRecord = fileService.get(fileId);

        if (fileRecord != null) {
            String fileType = fileRecord.getFileType();
            boolean isImage = fileService.accept(fileType, fileService.getImageTypes());
            String scheme = request.getParameter("scheme");
            scheme = scheme == null ? "" : scheme;
            String alias = request.getParameter("alias");
            alias = alias == null ? "" : alias;

            PreviewResponse response = new PreviewResponse();
            if (isImage) {
                // 图片形式使用框架的预览方式
//                previewJs = "$.previewImage('file/thumbnail/" + (Utils.isEmpty(scheme) ? FILE_SCHEME_AUTO : scheme) + "/" +
//                        (Utils.isEmpty(alias) ? FILE_ALIAS_AUTO : alias) + "/" + enFileId + "." + fileType + "');";
                response.setWay(PreviewResponse.WAY_PREVIEW_IMAGE);
                response.setUrl("file/thumbnail/" + (Utils.isEmpty(scheme) ? FILE_SCHEME_AUTO : scheme) + "/" +
                        (Utils.isEmpty(alias) ? FILE_ALIAS_AUTO : alias) + "/" + enFileId + "." + fileType);
            } else {
                String mimeType = getMimeType(fileType);
                String fileParamUrl = "file/download?fileId=" + enFileId + "&scheme=" + scheme + "&alias=" + alias;
                if (Utils.notEmpty(mimeType)) {
                    fileParamUrl += "&inline=1";
                    response.setWay(PreviewResponse.WAY_INLINE);
                }
            }
            return new JsonResponse<>(response);
        } else {
            String error = "附件获取异常@" + System.currentTimeMillis();
            LogUtil.warn(error + "[" + enFileId + "]");
            throw new MarkedException(error);
        }
    }

}
