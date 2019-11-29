package com.rongji.dfish.framework.plugin.file.controller;

import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.ThreadUtil;
import com.rongji.dfish.framework.FrameworkContext;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.mvc.controller.BaseController;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingDefine;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingManager;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingScheme;
import com.rongji.dfish.framework.plugin.file.controller.config.ImageHandlingDefine;
import com.rongji.dfish.framework.plugin.file.controller.plugin.FileUploadPlugin;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.misc.util.ImageUtil;
import com.rongji.dfish.ui.command.JS;
import com.rongji.dfish.ui.form.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping("/file")
@Controller
public class FileController extends BaseController {

    @Autowired
    private FileService fileService;
    @Autowired(required = false)
    private List<FileUploadPlugin> uploadPlugins;
    @Autowired
    private FileHandlingManager fileHandlingManager;

    private Map<String, FileUploadPlugin> uploadPluginMap = new HashMap<>();

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

    public static UploadItem saveFile(HttpServletRequest request, FileService fileService, String acceptTypes) {
        UploadItem uploadItem = null;

        boolean accept = false;
        try {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
                MultipartFile fileData = mRequest.getFile("Filedata");

                String extName = FileUtil.getFileExtName(fileData.getOriginalFilename());
                accept = accept(extName, acceptTypes);
                if (accept) {
                    String loginUserId = FrameworkHelper.getLoginUser(mRequest);
                    uploadItem = fileService.saveFile(fileData, loginUserId);
                } else {
                    LogUtil.debug("上传文件失败：extName=" + extName + "&acceptTypes=" + acceptTypes);
                }
            }
        } catch (Exception e) {
            String error = "上传失败,系统内部异常@" + System.currentTimeMillis();
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
     * 判断扩展名是否支持
     *
     * @param extName     拓展名(不管有没.都支持;即doc和.doc)
     * @param acceptTypes 可接受的类型;格式如:*.doc;*.png;*.jpg;
     * @return
     */
    static boolean accept(String extName, String acceptTypes) {
        if (acceptTypes == null || "".equals(acceptTypes)) {
            return true;
        }
        if (Utils.isEmpty(extName)) {
            return false;
        }
        // 这里的extName是包含.

        String[] accepts = acceptTypes.split("[,;]");
//		extName=extName.toLowerCase();
        // 类型是否含.
        int extDot = extName.lastIndexOf(".");
        // 统一去掉.
        String realExtName = (extDot >= 0) ? extName.substring(extDot + 1) : extName;
        for (String s : accepts) {
            if (Utils.isEmpty(s)) {
                continue;
            }
            int dotIndex = s.lastIndexOf(".");
            if (dotIndex < 0) {
                continue;
            }
            String acc = s.substring(dotIndex + 1);
            if (acc.equalsIgnoreCase(realExtName)) {
                return true;
            }
        }
        return false;
    }

    @RequestMapping("/uploadFile")
    @ResponseBody
    public UploadItem uploadFile(HttpServletRequest request) {
        String scheme = request.getParameter("scheme");
        FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);
        // 其实这里根据不同业务模块的判断限制意义不大,根据全局的设置即可
        String acceptTypes = handlingScheme != null && Utils.notEmpty(handlingScheme.getHandlingTypes()) ? handlingScheme.getHandlingTypes() : fileService.getFileTypes();
        return saveFile(request, fileService, acceptTypes);
    }

    private static final ExecutorService EXECUTOR_IMAGE = ThreadUtil.getCachedThreadPool();

    @RequestMapping("/uploadImage")
    @ResponseBody
    public UploadItem uploadImage(HttpServletRequest request) {
        final UploadItem uploadItem = uploadFile(request);
        if (uploadItem == null || Utils.isEmpty(uploadItem.getId())) {
            // 这样异常结果返回可能导致前端显示异常
            return uploadItem;
        }

        // 这里先统一补thumbnail缩略图地址
//		FilterParam param = getFileParam(request);
//        uploadItem.setThumbnail("file/thumbnail?fileId=" + uploadItem.getId() + param);

        String scheme = request.getParameter("scheme");
        final FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);
        if (handlingScheme == null || Utils.isEmpty(handlingScheme.getDefines())) {
            // 无需进行图片压缩
            return uploadItem;
        }
        final AtomicInteger doneFileCount = new AtomicInteger(0);
        EXECUTOR_IMAGE.execute(new Runnable() {
            @Override
            public void run() {
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
                    InputStream input = null;
                    OutputStream output = null;
                    try {
                        FileHandlingDefine handlingDefine = fileHandlingManager.getDefine(defineAlias);
                        if (handlingDefine == null || !(handlingDefine instanceof ImageHandlingDefine)) {
                            continue;
                        }
                        ImageHandlingDefine realDefine = (ImageHandlingDefine) handlingDefine;

                        input = new FileInputStream(imageFile);
                        File outputFile = fileService.getFile(fileRecord, defineAlias, false);
                        if (!outputFile.exists()) {
                            outputFile.createNewFile();
                        }
                        output = new FileOutputStream(outputFile);
                        if (ImageHandlingDefine.WAY_ZOOM.equals(realDefine.getWay())) {
                            ImageUtil.zoom(input, output, fileExtName, realDefine.getWidth(), realDefine.getHeight());
                        } else if (ImageHandlingDefine.WAY_CUT.equals(realDefine.getWay())) {
                            ImageUtil.cut(input, output, fileExtName, realDefine.getWidth(), realDefine.getHeight());
                        } else if (ImageHandlingDefine.WAY_RESIZE.equals(realDefine.getWay())) {
                            ImageUtil.resize(input, output, fileExtName, realDefine.getWidth(), realDefine.getHeight());
                        }
                        doneFileCount.incrementAndGet();
                    } catch (Exception e) {
                        LogUtil.error("生成缩略图出现异常:详见信息", e);
                    } finally {
                        if (input != null) {
                            try {
                                input.close();
                            } catch (IOException e) {
                                LogUtil.error("生成缩略图出现异常:输入流关闭异常", e);
                            }
                        }
                        if (output != null) {
                            try {
                                output.close();
                            } catch (IOException e) {
                                LogUtil.error("生成缩略图出现异常:输出流关闭异常", e);
                            }
                        }
                    }
                }
            }
        });
        int waitCount = 0;
        // 至少保证第1个缩略图生成才返回结果
        while (doneFileCount.get() < 1 && waitCount++ < 30) {
            // 最多等待3秒
            try {
                // 缩略图未生成休眠等待
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LogUtil.error("生成缩略图等待异常", e);
            }
        }
        return uploadItem;
    }

    @RequestMapping("/upload4Plugin")
    @ResponseBody
    public Object upload4Plugin(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        PubFileRecord fileRecord = fileService.getFileRecord(fileId);
        boolean inline = "1".equals(request.getParameter("inline"));
        // 目前文件下载统一默认都是原件下载
        downloadFileData(response, inline, fileRecord, null);
    }

    protected static final Map<String, String> MIME_MAP = new HashMap<>();

    protected Class<?> getMimeClass() {
        return getClass();
    }

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
    @RequestMapping("/downloadFile")
    @ResponseBody
    @Deprecated
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 下载附件方法统一使用download
        download(request, response);
    }

    /**
     * 附件下载
     *
     * @param response
     * @param fileRecord
     * @throws Exception
     */
    private void downloadFileData(HttpServletResponse response, boolean inline, PubFileRecord fileRecord, String alias) throws Exception {
        InputStream input = null;
        String errorMsg = null;
        try {
            if (fileRecord != null) {
                String fileName = fileRecord.getFileName();
                input = fileService.getFileInputStream(fileRecord, alias);
                long fileSize = 0L;
                if (input != null) {
                    fileSize = fileService.getFileSize(fileRecord, alias);
                } else { // 当别名附件不存在时,使用原附件
                    input = fileService.getFileInputStream(fileRecord);
                    fileSize = fileService.getFileSize(fileRecord);
                }
                if (input != null) {
                    downloadFileData(response, inline, input, fileName, fileSize);
                } else {
                    errorMsg = "下载的附件不存在";
                }
            } else {
                errorMsg = "下载的附件不存在";
            }

        } catch (Exception e) {
            errorMsg = "下载附件异常@" + System.currentTimeMillis();
            LogUtil.error(errorMsg + "[" + fileRecord.getFileId() + "]", e);
        } finally {
            if (input != null) {
                input.close();
            }
            if (Utils.notEmpty(errorMsg)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, errorMsg);
            }
        }
    }

    /**
     * 附件下载
     *
     * @param response
     * @throws Exception
     */
    private void downloadFileData(HttpServletResponse response, boolean inline, InputStream input, String fileName, long fileSize) throws Exception {
        String encoding = "UTF-8";
        try {
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Accept-Charset", encoding);
            String contentType = null;
            if (inline) {
                String extName = FileUtil.getFileExtName(fileName);
                if (Utils.notEmpty(extName)) {
                    contentType = getMimeType(extName);
                }
            }
            if (Utils.isEmpty(contentType)) {
                contentType = "application/octet-stream";
            }
            response.setHeader("Content-type", contentType);
            response.setHeader("Content-Disposition", (inline ? "inline" : "attachment") + "; filename=" + URLEncoder.encode(fileName, encoding));
            response.setHeader("Content-Length", String.valueOf(fileSize));
            response.setStatus(HttpServletResponse.SC_OK);
            if (input != null) {
                FileUtil.downLoadData(response, input);
            }
        } catch (Exception e) {
            String error = "下载附件异常@" + System.currentTimeMillis();
            LogUtil.error(error, e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, error);
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    private static final String FILE_SCHEME_AUTO = "AUTO";
    private static final String FILE_ALIAS_AUTO = "AUTO";

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
        thumbnail(response, scheme, alias, fileId);
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
    public void thumbnail(HttpServletResponse response, @PathVariable String scheme,
                          @PathVariable String alias, @PathVariable String fileId) throws Exception {
        String decFileId = fileService.decrypt(fileId);
        PubFileRecord fileRecord = fileService.getFileRecord(decFileId);
        // 获取附件别名
        String realAlias = getFileAlias(alias, scheme);
        if (fileRecord != null) {
            InputStream input = null;
            try {
                String fileName = fileRecord.getFileName();
                input = fileService.getFileInputStream(fileRecord, realAlias);
                long fileSize = 0L;
                if (input != null) {
                    fileSize = fileService.getFileSize(fileRecord, realAlias);
                } else if (Utils.notEmpty(realAlias)) {
                    // 当别名附件不存在时,使用原附件
                    input = fileService.getFileInputStream(fileRecord);
                    fileSize = fileService.getFileSize(fileRecord);
                }
                if (input != null) {
                    downloadFileData(response, true, input, fileName, fileSize);
                    return;
                }
            } catch (Exception e) {
                String error = "下载附件异常@" + System.currentTimeMillis();
                LogUtil.error(error, e);
            } finally {
                if (input != null) {
                    input.close();
                }
            }
        }

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
        File defaultImageFile = new File(FrameworkContext.getInstance().getServletInfo().getServletRealPath() + "m/default/img/" + defaultIcon);
        if (defaultImageFile.exists()) {
            downloadFileData(response, true, new FileInputStream(defaultImageFile), defaultImageFile.getName(), defaultImageFile.length());
            return;
        } else {
            String error = "附件记录不存在@" + System.currentTimeMillis();
            LogUtil.warn(error + "[" + fileId + "->" + decFileId + "]");
            response.sendError(HttpServletResponse.SC_NOT_FOUND, error);
        }
    }

    private String getFileAlias(String alias, String scheme) {
        if (Utils.isEmpty(alias) || FILE_ALIAS_AUTO.equals(alias)) {
            FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);
            if (handlingScheme != null && Utils.notEmpty(handlingScheme.getDefines())) {
                alias = handlingScheme.getDefines().get(0);
            } else {
                alias = "";
            }
        }
        return alias;
    }

    /**
     * 预览文件方法
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/preview")
    @ResponseBody
    public Object preview(HttpServletRequest request) throws Exception {
        // FIXME 目前仅图片预览方法,如果是文件预览需做处理,不支持预览可能直接下载
        String enFileId = request.getParameter("fileId");
        String fileId = fileService.decrypt(enFileId);
        PubFileRecord fileRecord = fileService.getFileRecord(fileId);

        if (fileRecord != null) {
            String fileType = FileUtil.getFileExtName(fileRecord.getFileUrl());
            boolean isImage = accept(fileType, fileService.getImageTypes());
            String scheme = request.getParameter("scheme");
            scheme = scheme == null ? "" : scheme;
            String alias = request.getParameter("alias");
            alias = alias == null ? "" : alias;

            if (isImage) {
                // 图片形式使用框架的预览方式
                return new JS("$.previewImage('file/thumbnail/" + (Utils.isEmpty(scheme) ? FILE_SCHEME_AUTO : scheme) + "/" +
                        (Utils.isEmpty(alias) ? FILE_ALIAS_AUTO : alias) + "/" + enFileId + "." + fileType + "');");
            } else {
                String mimeType = getMimeType(fileType);
                String fileParamUrl = "?fileId=" + enFileId + "&scheme=" + scheme + "&alias=" + alias;
                if (Utils.notEmpty(mimeType)) {
                    // 如果是浏览器支持可查看的内联类型,新窗口打开
                    return new JS("window.open('file/download" + fileParamUrl + "&inline=1');");
                } else { // 其他情况都是直接下载
                    return new JS("$.download('file/download" + fileParamUrl + "');");
                }
            }
        } else {
            String error = "附件获取异常@" + System.currentTimeMillis();
            LogUtil.warn(error + "[" + fileRecord.getFileId() + "]");
            throw new DfishException(error);
        }
    }

}
