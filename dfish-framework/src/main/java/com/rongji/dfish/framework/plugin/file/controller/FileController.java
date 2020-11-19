package com.rongji.dfish.framework.plugin.file.controller;

import com.rongji.dfish.base.exception.MarkedException;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.mvc.controller.FrameworkController;
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
public class FileController extends FrameworkController {

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
        if (request instanceof MultipartHttpServletRequest) {
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
    public Object upload(HttpServletRequest request, @PathVariable String fileType) throws Exception {
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
            uploadItem = new UploadItem();
            uploadItem.setError(new UploadItem.Error("附件上传失败@" + System.currentTimeMillis()));
            // 错误格式按照DFish前端格式
//            return uploadItem;
            jsonResponse.setErrorMessage(uploadItem.getError().getMessage());
        } else {
            PubFileRecord fileRecord = uploadItem.getFileRecord();
            // FIXME 这里先简单处理全部记录更新
            if (fileRecord != null && fileRecord.isChange()) {
                fileService.update(fileRecord);
            }

            uploadItem.setFileRecord(null);
            if (uploadItem.getError() != null) {
                // 错误格式按照DFish前端格式
//                return uploadItem;
                jsonResponse.setErrorMessage(uploadItem.getError().getMessage());
            } else {
                jsonResponse.setData(uploadItem);
            }
        }
        return jsonResponse;
    }

    /**
     * 第三方插件附件上传
     *
     * @param request
     * @param plugin
     * @return
     * @throws Exception
     */
    @RequestMapping("/upload/plugin/{plugin}")
    @ResponseBody
    public Object uploadPlugin(HttpServletRequest request, @PathVariable String plugin) throws Exception {
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
        String decFileId = fileService.decrypt(fileId);
        PubFileRecord fileRecord = fileService.get(decFileId);
        if (fileRecord == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            LogUtil.warn("the record is not found.[decryptId:" + decFileId + ",encryptId:" + fileId + "]");
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


    /**
     * 附件下载
     *
     * @param response
     * @param fileRecord
     * @throws Exception
     */
    private DownloadStatus downloadFileData(HttpServletRequest request, HttpServletResponse response, PubFileRecord fileRecord, DownloadParam downloadParam) throws Exception {
        try {
            DownloadResource resource = new DownloadResource(
                    downloadParam.getFileName(), downloadParam.getFileSize(), downloadParam.getLastModified(),
                    () -> fileService.getFileInputStream(fileRecord, downloadParam.getAlias(), downloadParam.getExtension()));
            return ServletUtil.download(request, response, resource, downloadParam.isInline());
        } catch (Exception e) {
            if (!"ClientAbortException".equals(e.getClass().getSimpleName())) {
                String error = "下载附件异常@" + System.currentTimeMillis();
                LogUtil.error(error + "[" + fileRecord.getFileId() + "]", e);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, error);
            }
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

    /**
     * 显示缩略图方法，该方法暂时保留，兼容旧版本数据
     *
     * @param response
     * @throws Exception
     * @see #inline(HttpServletRequest, HttpServletResponse, String)
     */
    @RequestMapping("/thumbnail")
    @Deprecated
    public void thumbnail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileId = request.getParameter("fileId");
        inline(request, response, null, fileId);
    }

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
            LogUtil.warn("the record is not found.[decryptId:" + decFileId + ",encryptId:" + fileId + "]");
            return;
        }
        // 获取附件别名
        String realAlias = getFileAlias(alias, fileRecord.getFileType(), fileRecord.getFileScheme());

        DownloadParam downloadParam = getDownloadParam(fileRecord, realAlias);
        // 目前文件下载统一默认都是原件下载
        downloadParam.setInline(true).setEncryptedFileId(fileId);

        downloadFileData(request, response, fileRecord, downloadParam);
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
            return new JsonResponse<>(response);
        } else {
            String error = "附件获取异常@" + System.currentTimeMillis();
            LogUtil.warn(error + "[" + fileId + "]");
            throw new MarkedException(error);
        }
    }

}
