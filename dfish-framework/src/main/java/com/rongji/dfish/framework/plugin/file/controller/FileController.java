package com.rongji.dfish.framework.plugin.file.controller;

import com.rongji.dfish.base.exception.MarkedException;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.mvc.controller.BaseActionController;
import com.rongji.dfish.framework.mvc.response.JsonResponse;
import com.rongji.dfish.framework.plugin.file.config.DownloadParam;
import com.rongji.dfish.framework.plugin.file.config.FileHandleManager;
import com.rongji.dfish.framework.plugin.file.config.FileHandleScheme;
import com.rongji.dfish.framework.plugin.file.controller.plugin.FileUploadPlugin;
import com.rongji.dfish.framework.plugin.file.dto.PreviewResponse;
import com.rongji.dfish.framework.plugin.file.dto.UploadItem;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.framework.util.DownloadResource;
import com.rongji.dfish.framework.util.DownloadStatus;
import com.rongji.dfish.framework.util.ServletUtil;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 附件相关控制层
 *
 * @author lamontYu
 * @version 1.3 去掉类注解,采用配置加载模式,有利于项目自定义配置 lamontYu 2019-12-5
 * @since DFish3.0
 */
@RequestMapping("/file")
public class FileController extends BaseActionController {

    @Resource(name = "fileService")
    private FileService fileService;
    @Autowired(required = false)
    private List<FileUploadPlugin> uploadPlugins;
    @Resource(name = "fileHandleManager")
    private FileHandleManager fileHandleManager;

    private String defaultImageFolder = "x/default/img/";

    /**
     * 获取fileService
     *
     * @return
     */
    public FileService getFileService() {
        return fileService;
    }

    /**
     * 设置fileService
     *
     * @param fileService
     */
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 获取FileHandleManager
     *
     * @return
     */
    public FileHandleManager getFileHandleManager() {
        return fileHandleManager;
    }

    /**
     * 设置 FileHandleManager
     */
    public void setFileHandleManager(FileHandleManager fileHandleManager) {
        this.fileHandleManager = fileHandleManager;
    }

    /**
     * 默认图片路径
     *
     * @return
     */
    public String getDefaultImageFolder() {
        return defaultImageFolder;
    }

    /**
     * 默认图片路径
     *
     * @param defaultImageFolder
     */
    public void setDefaultImageFolder(String defaultImageFolder) {
        if (defaultImageFolder != null && !defaultImageFolder.endsWith("/")) {
            defaultImageFolder += "/";
        }
        this.defaultImageFolder = defaultImageFolder;
    }

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
            LogUtil.warn("The plugin name is empty.[" + uploadPlugin.getClass().getName() + "]");
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
    public UploadItem saveFile(HttpServletRequest request, String acceptTypes, String fileType, String scheme) {
        boolean accept = false;
        UploadItem uploadItem = null;
        try {
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            MultipartFile fileData = mRequest.getFile("Filedata");

            String extName = FileUtil.getFileExtName(fileData.getOriginalFilename());
            accept = FileUtil.accept(extName, acceptTypes);
            if (accept) {
                String loginUserId = FrameworkHelper.getLoginUser(request);
                PubFileRecord fileRecord = new PubFileRecord();
                fileRecord.setFileName(fileData.getOriginalFilename());
                fileRecord.setFileSize(fileData.getSize());
                fileRecord.setFileCreator(loginUserId);
                fileRecord.setFileType(fileType);
                fileRecord.setFileScheme(scheme);
                uploadItem = fileService.saveFile(fileData.getInputStream(), fileRecord);
            }
        } catch (Exception e) {
            String error = "上传失败,系统异常@" + System.currentTimeMillis();
            LogUtil.error(error, e);
            uploadItem = new UploadItem();
            uploadItem.setError(new UploadItem.Error(error));
        }

        if (uploadItem == null) {
            uploadItem = new UploadItem();
            uploadItem.setError(new UploadItem.Error("上传文件失败" + (!accept ? "：当前文件类型不符合系统规范" : "")));
        }
        return uploadItem;
    }

    /**
     * 上传附件
     *
     * @param request
     * @return
     */
    @RequestMapping("/upload/{fileType}")
    @ResponseBody
    public JsonResponse upload(HttpServletRequest request, @PathVariable String fileType) throws Exception {
        String scheme = request.getParameter("scheme");
        FileHandleScheme handleScheme = fileHandleManager.getScheme(fileType, scheme);

        // 其实这里根据不同业务模块的判断限制意义不大,根据全局的设置即可
        String acceptTypes = handleScheme != null ? handleScheme.acceptTypes() : fileService.getFileTypes(fileType, null);
        UploadItem uploadItem = saveFile(request, acceptTypes, fileType, scheme);
        if (handleScheme != null) {
            uploadItem = handleScheme.uploaded(uploadItem);
        }
        JsonResponse jsonResponse = new JsonResponse();
        if (uploadItem == null) {
            // FIXME 这里做容错,但这里很可能不会出现这种情况
            jsonResponse.setErrMsg("附件上传失败@" + System.currentTimeMillis());
        } else {
            PubFileRecord fileRecord = uploadItem.getFileRecord();
            // FIXME 这里先简单处理全部记录更新
            if (fileRecord != null && fileRecord.isChange()) {
                fileService.update(fileRecord);
            }

            uploadItem.setFileRecord(null);
            if (uploadItem.getError() != null) {
                jsonResponse.setErrMsg(uploadItem.getError().getText());
            } else {
                jsonResponse.setData(uploadItem);
            }
        }
        return jsonResponse;
    }

//    /**
//     * 上传附件
//     *
//     * @param request
//     * @return
//     */
//    @RequestMapping("/upload/file")
//    @ResponseBody
//    public JsonResponse uploadFile(HttpServletRequest request) {
//        return uploadFile(request,null);
//    }

//    /**
//     * 上传附件
//     *
//     * @param request
//     * @return
//     */
//
//    public JsonResponse uploadFile(HttpServletRequest request,String filTypes) {
//        String scheme = request.getParameter("scheme");
//        FileHandleScheme handlingScheme = fileHandleManager.getScheme(scheme);
//        // 其实这里根据不同业务模块的判断限制意义不大,根据全局的设置即可
//        String acceptTypes = handlingScheme != null && Utils.notEmpty(handlingScheme.getHandleTypes()) ? handlingScheme.getHandleTypes() : fileService.getFileTypes();
//        UploadItem uploadItem = saveFile(request, acceptTypes);
//        return new JsonResponse<>(uploadItem);
//    }

//    /**
//     * 上传图片
//     *
//     * @param request
//     * @return
//     */
//    @RequestMapping("/upload/image")
//    @ResponseBody
//    public JsonResponse uploadImage(HttpServletRequest request) throws Exception {
//        final JsonResponse jsonResponse = uploadFile(request);
//        UploadItem uploadItem = (UploadItem) jsonResponse.getData();
//        if (uploadItem == null || Utils.isEmpty(uploadItem.getId())) {
//            // 这样异常结果返回可能导致前端显示异常
//            return jsonResponse;
//        }
//
//        String scheme = request.getParameter("scheme");
//        ImageHandleScheme handleScheme = fileHandleManager.getScheme(scheme);
//        if (handleScheme == null || Utils.isEmpty(handleScheme.getHandleConfigs())) {
//            // 无需进行图片压缩
//            return jsonResponse;
//        }
//        String fileId = fileService.decrypt(uploadItem.getId());
//        PubFileRecord fileRecord = fileService.get(fileId);
//        if (fileRecord == null) {
//            String errorMsg = "生成缩略图出现异常@" + System.currentTimeMillis();
//            LogUtil.warn(errorMsg + ":原记录文件存储记录丢失[" + fileId + "]");
//            return jsonResponse.setErrMsg(errorMsg);
//        }
//
//        File imageFile = fileService.getFile(fileRecord);
//        if (imageFile == null || !imageFile.exists()) {
//            String errorMsg = "生成缩略图出现异常@" + System.currentTimeMillis();
//            LogUtil.warn(errorMsg + ":原图丢失[" + fileId + "]");
//            return jsonResponse.setErrMsg(errorMsg);
//        }
//        ImageProcessorGroup imageProcessorGroup = ImageProcessorGroup.of(imageFile).setDest(imageFile.getParentFile().getAbsolutePath())
//                .setAliasPattern("{FILE_NAME}_{ALIAS}.{EXTENSION}");
//
//        // FIXME 默认图片和需要标记点图片放在位置不合理时,导致部分图片未能按标记点图片处理
//        if (handleScheme.isHandleZoomDefault()) {
//            imageProcessorGroup.process(-1, -1, ImageProcessConfig.WAY_ZOOM, ALIAS_DEFAULT, false);
//        }
//        for (ImageHandleConfig handleConfig : handleScheme.getHandleConfigs()) {
//            if (handleConfig.getWatermark() != null) {
//                fixWatermark(handleConfig.getWatermark());
//            }
//            imageProcessorGroup.process(handleConfig);
//        }
//        imageProcessorGroup.execute();
//
//        return jsonResponse;
//    }
//
//    private static final String ALIAS_DEFAULT = "DEFAULT";
//
//    private void fixWatermark(ImageWatermarkConfig watermark) {
//        if (Utils.notEmpty(watermark.getImagePath())) {
//            // FIXME 根据水印名称可能做缓存处理
//            String servletPath = SystemContext.getInstance().get(ServletInfo.class).getServletRealPath();
//            watermark.setImageFile(new File(servletPath + watermark.getImagePath()));
//        }
//    }

//    /**
//     * 上传媒体类文件
//     *
//     * @param request 请求
//     * @return 上传的附件信息
//     */
//    @RequestMapping("/uploadVideo")
//    @ResponseBody
//    public Object uploadVideo(HttpServletRequest request) {
//        final JsonResponse<UploadItem> uploadItem = uploadFile(request, fileService.getFileTypes(FileService.CONFIG_TYPE_VIDEO, "*.mp4;"));
//        if (uploadItem == null || Utils.isEmpty(uploadItem.getData().getId())) {
//            // 这样异常结果返回可能导致前端显示异常
//            return uploadItem;
//        }
//        // 抓取视频帧作为封面缩略图
//        grabVideoFramer(uploadItem.getData());
//
//        return uploadItem;
//    }

    /**
     * 第三方插件附件上传
     *
     * @param request
     * @param plugin
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadx/{plugin}")
    @ResponseBody
    public Object uploadx(HttpServletRequest request, @PathVariable String plugin) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/download/{fileId}")
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileId) throws Exception {
        if (Utils.isEmpty(fileId)) {
            fileId = request.getParameter("fileId");
        }
        String rawFileId = fileService.decrypt(fileId);
        PubFileRecord fileRecord = fileService.get(rawFileId);
        if (fileRecord == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        boolean inline = "1".equals(request.getParameter("inline"));
        DownloadParam downloadParam = getDownloadParam(fileRecord, null);
        downloadParam.setInline(inline).setEncryptedFileId(fileId);
        downloadFileData(request, response, fileRecord, downloadParam);
    }

    protected static final Map<String, String> MIME_MAP = new HashMap<>();

    protected Class getMimeClass() {
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

//    private static class DownloadParam {
//        boolean inline;
//        String fileName;
//        String extension;
//        long fileSize;
//        long lastModified;
//        String alias;
//        String encryptedFileId;
//
//
//        public String getExtension() {
//            return extension;
//        }
//
//        public void setExtension(String extension) {
//            this.extension = extension;
//        }
//
//        public DownloadParam() {
//        }
//
//        public DownloadParam(String fileName, long fileSize, long lastModified) {
//            this.fileName = fileName;
//            this.fileSize = fileSize;
//            this.lastModified = lastModified;
//        }
//
//        public DownloadParam(String fileName, long fileSize, long lastModified, String alias, boolean inline) {
//            this.fileName = fileName;
//            this.fileSize = fileSize;
//            this.lastModified = lastModified;
//            this.alias = alias;
//            this.inline = inline;
//        }
//
//        public boolean isInline() {
//            return inline;
//        }
//
//        public DownloadParam setInline(boolean inline) {
//            this.inline = inline;
//            return this;
//        }
//
//        public String getFileName() {
//            return fileName;
//        }
//
//        public DownloadParam setFileName(String fileName) {
//            this.fileName = fileName;
//            return this;
//        }
//
//        public long getFileSize() {
//            return fileSize;
//        }
//
//        public DownloadParam setFileSize(long fileSize) {
//            this.fileSize = fileSize;
//            return this;
//        }
//
//        public long getLastModified() {
//            return lastModified;
//        }
//
//        public DownloadParam setLastModified(long lastModified) {
//            this.lastModified = lastModified;
//            return this;
//        }
//
//        public String getAlias() {
//            return alias;
//        }
//
//        public DownloadParam setAlias(String alias) {
//            this.alias = alias;
//            return this;
//        }
//
//        public String getEncryptedFileId() {
//            return encryptedFileId;
//        }
//
//        public DownloadParam setEncryptedFileId(String encryptedFileId) {
//            this.encryptedFileId = encryptedFileId;
//            return this;
//        }
//    }

//    /**
//     * 下载附件数据方法
//     * @param response
//     * @param input
//     * @param downloadParam
//     * @return
//     * @throws Exception
//     */
//    private boolean downloadFileData(HttpServletResponse response, InputStream input, DownloadParam downloadParam) throws Exception {
//        if (input == null) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND);
//            return false;
//        }
//        String encoding = "UTF-8";
//        try {
//            response.setHeader("Accept-Ranges", "bytes");
//            response.setHeader("Accept-Charset", encoding);
//            String contentType = null;
//
//            String disposition;
//            if (downloadParam.isInline()) {
//                String extName = FileUtil.getFileExtName(downloadParam.getFileName());
//                if (Utils.notEmpty(extName)) {
//                    contentType = getMimeType(extName);
//                }
//                disposition = "inline; filename=" + (Utils.notEmpty(downloadParam.getEncryptedFileId()) ? (downloadParam.getEncryptedFileId() + extName) : URLEncoder.encode(downloadParam.getFileName(), encoding));
//            } else {
//                disposition = "attachment; filename=" + URLEncoder.encode(downloadParam.getFileName(), encoding);
//            }
//            if (Utils.isEmpty(contentType)) {
//                contentType = "application/octet-stream";
//            }
//
//            response.setHeader("Content-type", contentType);
//            response.setHeader("Content-Disposition", disposition);
//            response.setHeader("Content-Length", String.valueOf(downloadParam.getFileSize()));
//            if (downloadParam.getLastModified() > 0L) {
//                synchronized (DF_GMT) {
//                    response.setHeader("Last-Modified", DF_GMT.format(downloadParam.getLastModified()));
//                }
//                response.setHeader("ETag", getEtag(downloadParam));
//            }
//            response.setStatus(HttpServletResponse.SC_OK);
//            ServletUtil.downLoadData(response, input);
//            return true;
//        } catch (Exception e) {
//            String error = "下载附件异常@" + System.currentTimeMillis();
//            LogUtil.error(error, e);
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, error);
//            return false;
//        } finally {
//            if (input != null) {
//                input.close();
//            }
//        }
//    }

    /**
     * 附件下载
     *
     * @param response
     * @param fileRecord
     * @throws Exception
     */
    private DownloadStatus downloadFileData(HttpServletRequest request, HttpServletResponse response, PubFileRecord fileRecord, DownloadParam downloadParam) throws Exception {
        if (fileRecord == null) {
            LogUtil.warn("下载的附件不存在");
            return null;
        }
        try {
//            long fileSize;
//            if (input != null) {
//                fileSize = fileService.getFileSize(fileRecord, downloadParam.getAlias(), downloadParam.getExtension());
//            } else { // 当别名附件不存在时,使用原附件
//                input = fileService.getFileInputStream(fileRecord);
//                fileSize = fileService.getFileSize(fileRecord);
//            }
//            downloadFileData(response, input, downloadParam);
            DownloadResource resource = new DownloadResource(
                    downloadParam.getFileName(), downloadParam.getFileSize(), downloadParam.getLastModified(),
                    () -> {
                        try {
                            return fileService.getFileInputStream(fileRecord, downloadParam.getAlias(), downloadParam.getExtension());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    });

            return ServletUtil.download(request, response, resource, downloadParam.isInline());
        } catch (Exception e) {
            String error = "下载附件异常@" + System.currentTimeMillis();
            LogUtil.error(error + "[" + fileRecord.getFileId() + "]", e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, error);
        }
        return null;
    }

    private DownloadParam getDownloadParam(PubFileRecord fileRecord, String alias) {
        DownloadParam downloadParam = new DownloadParam();

        downloadParam.setLastModified(fileRecord.getUpdateTime() != null ? fileRecord.getUpdateTime().getTime() :
                (fileRecord.getCreateTime() != null ? fileRecord.getCreateTime().getTime() : 0L));
        downloadParam.setAlias(alias);
        String fileName = fileRecord.getFileName();

        FileHandleScheme handleScheme = fileHandleManager.getScheme(fileRecord.getFileType(), fileRecord.getFileScheme());
        // 视频类型
        if (handleScheme != null) {
            handleScheme.fixDownloadParam(downloadParam, fileRecord, alias);
        } else {
            downloadParam.setExtension(fileRecord.getFileExtension());
            downloadParam.setFileName(fileName);
        }
        long fileSize = fileService.getFileSize(fileRecord, downloadParam.getAlias(), downloadParam.getExtension());
        downloadParam.setFileSize(fileSize);
        return downloadParam;
    }

//    /**
//     * 判断扩展名是否支持
//     *
//     * @param fileExtension 拓展名(不管有没.都支持;即doc和.doc)
//     * @param acceptTypes   可接受的类型;格式如:*.doc;*.png;*.jpg;
//     * @return
//     */
//    static boolean accept(String fileExtension, String acceptTypes) {
//        if (acceptTypes == null || acceptTypes.equals("")) {
//            return true;
//        }
//        if (Utils.isEmpty(fileExtension)) {
//            return false;
//        }
//        String[] accepts = acceptTypes.split("[,;]");
//        // 类型是否含.
//        int extDot = fileExtension.lastIndexOf(".");
//        // 统一去掉.
//        String realFileExtension = (extDot >= 0) ? fileExtension.substring(extDot + 1) : fileExtension;
//        for (String s : accepts) {
//            if (Utils.isEmpty(s)) {
//                continue;
//            }
//            int dotIndex = s.lastIndexOf(".");
//            if (dotIndex < 0) {
//                continue;
//            }
//            String acc = s.substring(dotIndex + 1);
//            if (acc.equalsIgnoreCase(realFileExtension)) {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * 显示缩略图方法
     *
     * @param response
     * @param fileId
     * @throws Exception
     */
    @RequestMapping("/inline/{fileId}")
    public void inline(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileId) throws Exception {
        inline(request, response, null, fileId);
    }

    /**
     * 显示缩略图方法
     *
     * @param response
     * @param alias
     * @param fileId
     * @throws Exception
     */
    @RequestMapping("/inline/{alias}/{fileId}")
    public void inline(HttpServletRequest request, HttpServletResponse response, @PathVariable String alias, @PathVariable String fileId) throws Exception {
        String decFileId = fileService.decrypt(fileId);
        PubFileRecord fileRecord = fileService.get(decFileId);

        if (fileRecord == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 获取附件别名
        String realAlias = getFileAlias(alias, fileRecord.getFileType(), fileRecord.getFileScheme());

        DownloadParam downloadParam = getDownloadParam(fileRecord, realAlias);
        // 目前文件下载统一默认都是原件下载
        downloadParam.setInline(true).setEncryptedFileId(fileId);

        DownloadStatus ds = downloadFileData(request, response, fileRecord, downloadParam);
//        boolean success = false;
//        if (ds != null) {
//            long checkPoint = ds.getResourceLength() * 3 / 4;
//            success = ds.getRangeBegin() <= checkPoint && ds.getRangeBegin() + ds.getCompleteLength() >= checkPoint;
//        }
        // 下载不成功,用默认图片代替
//        if (!success) {
//            ImageHandleScheme handlingScheme = fileHandleManager.getScheme(fileRecord.getFileType(), scheme);
//            String defaultIcon = null;
//            if (handlingScheme != null) {
//                if (Utils.notEmpty(handlingScheme.getDefaultIcon())) {
//                    defaultIcon = handlingScheme.getDefaultIcon();
//                }
//            }
//            defaultIcon = Utils.isEmpty(defaultIcon) ? "default.png" : defaultIcon;
//            if (Utils.notEmpty(realAlias)) {
//                int lastDot = defaultIcon.lastIndexOf(".");
//                if (lastDot >= 0) {
//                    defaultIcon = defaultIcon.substring(0, lastDot) + "_" + realAlias + defaultIcon.substring(lastDot);
//                } else {
//                    defaultIcon += "_" + realAlias;
//                }
//            }
//
//            // 这里可能考虑重定向到具体文件目录去
//            File defaultImageFile = new File(SystemContext.getInstance().get(ServletInfo.class).getServletRealPath() + defaultImageFolder + defaultIcon);
//            if (defaultImageFile.exists()) {
////                downloadParam.setFileName(defaultImageFile.getName()).setFileSize(defaultImageFile.length()).setLastModified(defaultImageFile.lastModified()).setEncryptedFileId(null);
//
//                ServletUtil.download(request, response, defaultImageFile, true);
//                return;
//            } else {
//                String error = "附件不存在@" + System.currentTimeMillis();
//                LogUtil.warn(error + "[" + fileId + "->" + decFileId + "]");
//                response.sendError(HttpServletResponse.SC_NOT_FOUND, error);
//            }
//        }
    }

    private String getFileAlias(String alias, String fileType, String scheme) {
        if (Utils.isEmpty(scheme) || FileHandleScheme.FILE_SCHEME_AUTO.equals(scheme)) {
            if (FileHandleScheme.FILE_ALIAS_AUTO.equals(alias)) {
                return null;
            }
            return alias;
        }
        if (Utils.isEmpty(alias) || FileHandleScheme.FILE_ALIAS_AUTO.equals(alias)) {
            FileHandleScheme handleScheme = fileHandleManager.getScheme(fileType, scheme);
            if (handleScheme != null) {
                alias = handleScheme.getDefaultAlias();
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
    @RequestMapping("/preview/{fileId}")
    @ResponseBody
    public Object preview(HttpServletRequest request, @PathVariable String fileId) throws Exception {
        String rawFileId = fileService.decrypt(fileId);
        PubFileRecord fileRecord = fileService.get(rawFileId);

        if (fileRecord != null) {
            String alias = request.getParameter("alias");
            alias = alias == null ? "" : alias;
            String scheme = fileRecord.getFileScheme();
            scheme = scheme == null ? "" : scheme;
            PreviewResponse response;

            FileHandleScheme handleScheme = fileHandleManager.getScheme(fileRecord.getFileType(), scheme);
            String fileExtension = fileRecord.getFileExtension();
            if (handleScheme == null) {
                String mimeType = getMimeType(fileExtension);
                String fileParamUrl = "file/download?fileId=" + fileId + "&scheme=" + scheme + "&alias=" + alias;
                response = new PreviewResponse();
                if (Utils.notEmpty(mimeType)) {
                    fileParamUrl += "&inline=1";
                    response.setWay(PreviewResponse.WAY_PREVIEW_INLINE);
                } else {
                    response.setWay(PreviewResponse.WAY_PREVIEW_DOWNLOAD);
                }
                response.setUrl(fileParamUrl);
            } else {
                response = handleScheme.getPreviewResponse(fileId, alias, fileExtension);
            }

//            boolean isImage = fileService.accept(fileExtension, fileService.getFileTypes(FileService.CONFIG_TYPE_IMAGE, "*.jpg;"));
//
//            if (isImage) {
//                // 图片形式使用框架的预览方式
////                previewJs = "$.previewImage('file/thumbnail/" + (Utils.isEmpty(scheme) ? FILE_SCHEME_AUTO : scheme) + "/" +
////                        (Utils.isEmpty(alias) ? FILE_ALIAS_AUTO : alias) + "/" + enFileId + "." + fileType + "');";
//                response.setWay(PreviewResponse.WAY_PREVIEW_IMAGE);
//                response.setUrl("file/thumbnail/" + (Utils.isEmpty(scheme) ? FILE_SCHEME_AUTO : scheme) + "/" +
//                        (Utils.isEmpty(alias) ? FILE_ALIAS_AUTO : alias) + "/" + enFileId + "." + fileExtension);
//            } else {
//                String mimeType = getMimeType(fileExtension);
//                String fileParamUrl = "file/download?fileId=" + enFileId + "&scheme=" + scheme + "&alias=" + alias;
//                if (Utils.notEmpty(mimeType)) {
//                    fileParamUrl += "&inline=1";
//                    response.setWay(PreviewResponse.WAY_PREVIEW_INLINE);
//                }
//                response.setUrl(fileParamUrl);
//            }
            return new JsonResponse<>(response);
        } else {
            String error = "附件获取异常@" + System.currentTimeMillis();
            LogUtil.warn(error + "[" + fileId + "]");
            throw new MarkedException(error);
        }
    }

}
