package com.rongji.dfish.framework.plugin.file.controller;

import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.SystemData;
import com.rongji.dfish.framework.controller.BaseController;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingDefine;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingManager;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingScheme;
import com.rongji.dfish.framework.plugin.file.controller.config.ImageHandlingDefine;
import com.rongji.dfish.framework.plugin.file.controller.plugin.FileUploadPlugin;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.misc.util.ImageUtil;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.form.UploadItem;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 附件处理类
 *
 * @author lamontYu
 */
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

    public static UploadItem saveFile(HttpServletRequest request, FileService fileService, String acceptTypes) {
        UploadItem uploadItem = null;

        boolean accept = false;
        try {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
                MultipartFile fileData = mRequest.getFile("Filedata");

                String fileExtension = FileUtil.getExtension(fileData.getOriginalFilename());
                accept = accept(fileExtension, acceptTypes);
                if (accept) {
                    String loginUserId = FrameworkHelper.getLoginUser(mRequest);
                    String fileName = Utils.getParameter(request, "fileName");
                    uploadItem = fileService.saveFile(fileData, fileName, loginUserId);
                } else {
                    LogUtil.warn("上传文件失败：fileExtension=" + fileExtension + "&acceptTypes=" + acceptTypes);
                }
            }
        } catch (Exception e) {
            String error = "上传失败,系统内部异常@" + System.currentTimeMillis();
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
     * 判断扩展名是否支持
     *
     * @param fileExtension 拓展名(不管有没.都支持;即doc和.doc)
     * @param acceptTypes   可接受的类型;格式如:*.doc;*.png;*.jpg;
     * @return
     */
    static boolean accept(String fileExtension, String acceptTypes) {
        if (acceptTypes == null || acceptTypes.equals("")) {
            return true;
        }
        if (Utils.isEmpty(fileExtension)) {
            return false;
        }
        String[] accepts = acceptTypes.split("[,;]");
        // 类型是否含.
        int extDot = fileExtension.lastIndexOf(".");
        // 统一去掉.
        String realFileExtension = (extDot >= 0) ? fileExtension.substring(extDot + 1) : fileExtension;
        for (String s : accepts) {
            if (Utils.isEmpty(s)) {
                continue;
            }
            int dotIndex = s.lastIndexOf(".");
            if (dotIndex < 0) {
                continue;
            }
            String acc = s.substring(dotIndex + 1);
            if (acc.equalsIgnoreCase(realFileExtension)) {
                return true;
            }
        }
        return false;
    }

    @RequestMapping("/uploadFile")
    @ResponseBody
    public UploadItem uploadFile(HttpServletRequest request) {
        return uploadFile(request, null);
    }

    /**
     * 上传附件
     * @param request
     * @param fileTypes
     * @return
     */
    public UploadItem uploadFile(HttpServletRequest request, String fileTypes) {
        String scheme = request.getParameter("scheme");
        FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);

        String acceptTypes = handlingScheme != null && Utils.notEmpty(handlingScheme.getHandlingTypes()) ? handlingScheme.getHandlingTypes() : fileTypes;
        if (Utils.isEmpty(acceptTypes)) {
            acceptTypes = fileService.getFileTypes();
        }
        return saveFile(request, fileService, acceptTypes);
    }

    private static final ExecutorService EXECUTOR_IMAGE = Executors.newFixedThreadPool(5);


    /**
     * 上传图片
     *
     * @param request 请求
     * @return 上传的附件信息
     */
    @RequestMapping("/uploadImage")
    @ResponseBody
    public Object uploadImage(HttpServletRequest request) {
        final UploadItem uploadItem = uploadFile(request, fileService.getImageTypes());
        if (uploadItem == null || Utils.isEmpty(uploadItem.getId())) {
            // 这样异常结果返回可能导致前端显示异常
            return uploadItem;
        }

        // 这里先统一补thumbnail缩略图地址
//		FilterParam param = getFileParam(request);
//        uploadItem.setThumbnail("file/thumbnail?fileId=" + uploadItem.getId() + param);

        String scheme = request.getParameter("scheme");
        final FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);
        final String extName = Utils.notEmpty(uploadItem.getExtension()) ? ("." + uploadItem.getExtension()) : "";

        if (handlingScheme == null || Utils.isEmpty(handlingScheme.getDefines())) {
            uploadItem.setThumbnail("file/inline/" + uploadItem.getId() + extName);
            // 无需进行图片压缩
            return uploadItem;
        }
        final AtomicInteger doneFileCount = new AtomicInteger(0);
        EXECUTOR_IMAGE.execute(new Runnable() {
            @Override
            public void run() {
                // 本应该先提前判断再进行获取压缩,暂时不考虑这些个别情况(多损耗性能)
                String fileId = fileService.decId(uploadItem.getId());
                PubFileRecord fileRecord = fileService.get(fileId);
                if (fileRecord == null || Utils.isEmpty(fileRecord.getFileUrl())) {
                    LogUtil.warn("生成缩略图出现异常:原记录文件存储记录丢失[" + fileId + "]");
                    return;
                }
                String fileExtension = fileRecord.getFileExtension();
                if (Utils.isEmpty(fileExtension)) {
                    LogUtil.warn("生成缩略图出现异常:原记录文件扩展名未知[" + fileId + "]");
                    return;
                }

                File imageFile = fileService.getFile(fileRecord);
                if (imageFile == null || !imageFile.exists()) {
                    LogUtil.warn("生成缩略图出现异常:原图丢失[" + fileId + "]");
                    return;
                }
                boolean isFirst = true;
                for (String defineAlias : handlingScheme.getDefines()) {
                    InputStream input = null;
                    OutputStream output = null;
                    try {
                        FileHandlingDefine handlingDefine = fileHandlingManager.getDefine(defineAlias);
                        if (handlingDefine == null || !(handlingDefine instanceof ImageHandlingDefine)) {
                            LogUtil.warn("图片处理方式未定义[" + defineAlias + "]");
                            continue;
                        }
                        ImageHandlingDefine realDefine = (ImageHandlingDefine) handlingDefine;

                        input = new FileInputStream(imageFile);
                        File outputFile = fileService.getFile(fileRecord, defineAlias, null, false);
                        if (!outputFile.exists()) {
                            outputFile.createNewFile();
                        }
                        output = new FileOutputStream(outputFile);
                        if (ImageHandlingDefine.WAY_ZOOM.equals(realDefine.getWay())) {
                            ImageUtil.zoom(input, output, fileExtension, realDefine.getWidth(), realDefine.getHeight());
                        } else if (ImageHandlingDefine.WAY_CUT.equals(realDefine.getWay())) {
                            ImageUtil.cut(input, output, fileExtension, realDefine.getWidth(), realDefine.getHeight());
                        } else if (ImageHandlingDefine.WAY_RESIZE.equals(realDefine.getWay())) {
                            ImageUtil.resize(input, output, fileExtension, realDefine.getWidth(), realDefine.getHeight());
                        }
                        doneFileCount.incrementAndGet();
                        if (isFirst) {
                            uploadItem.setThumbnail("file/inline/" + defineAlias + "/" + uploadItem.getId() + extName);
                        } else {
                            isFirst = false;
                        }
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
        while (doneFileCount.get() < 1 && waitCount++ < 30) { // 最多等待3秒
            try {
                // 缩略图未生成休眠等待
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LogUtil.error("生成缩略图等待异常", e);
            }
        }
        if (Utils.isEmpty(uploadItem.getThumbnail())) {
            uploadItem.setThumbnail("file/inline/" + uploadItem.getId() + extName);
        }
        return uploadItem;
    }

    /**
     * 上传媒体类文件
     *
     * @param request 请求
     * @return 上传的附件信息
     */
    @RequestMapping("/uploadVideo")
    @ResponseBody
    public Object uploadVideo(HttpServletRequest request) {
        final UploadItem uploadItem = uploadFile(request, fileService.getVideoTypes());
        if (uploadItem == null || Utils.isEmpty(uploadItem.getId())) {
            // 这样异常结果返回可能导致前端显示异常
            return uploadItem;
        }
        // 抓取视频帧作为封面缩略图
        grabVideoFramer(uploadItem);

        return uploadItem;
    }

    /**
     * 将视频文件帧处理并以“jpg”格式进行存储。
     * 依赖FrameToBufferedImage方法：将frame转换为bufferedImage对象
     *
     * @param uploadItem
     */
    private void grabVideoFramer(UploadItem uploadItem) {
        if (uploadItem == null) {
            return;
        }
        String fileId = fileService.decId(uploadItem.getId());
        PubFileRecord fileRecord = fileService.get(fileId);
        if (fileRecord == null) {
            return;
        }
        FFmpegFrameGrabber fFmpegFrameGrabber = null;
        try {
			 /*
            获取视频文件
            */
            fFmpegFrameGrabber = new FFmpegFrameGrabber(fileService.getFileInputStream(fileRecord));
            fFmpegFrameGrabber.start();

            //获取视频总帧数
            int frameLength = fFmpegFrameGrabber.getLengthInFrames();
            // 这里播放时长暂时用秒
            int duration = (int) (frameLength / fFmpegFrameGrabber.getFrameRate());
            uploadItem.setDuration(duration);
            // 帧下标
            int frameIndex = 0;
            while (frameIndex++ <= frameLength) {
                Frame frame = fFmpegFrameGrabber.grabImage();
                // 对目标帧进行处理
                if (frame != null && frameIndex == getVideoThumbnailPosition()) {
                    String saveFileName = fileId + "_" + getVideoThumbnailAlias() + "." + getVideoThumbnailExtension();
                    //文件绝对路径+名字
                    String fileName = fileService.getFileDir(fileRecord) + saveFileName;

                    //文件储存对象
                    File output = new File(fileName);

                    // 将目标帧转换成图片
                    Java2DFrameConverter converter = new Java2DFrameConverter();
                    BufferedImage bufferedImage = converter.getBufferedImage(frame);
                    ImageIO.write(bufferedImage, getVideoThumbnailExtension(), output);

                    uploadItem.setThumbnail("file/inline/" + getVideoThumbnailAlias() + "/" + uploadItem.getId() + "." + getVideoThumbnailExtension());

                    break;
                }
            }
            fFmpegFrameGrabber.stop();

        } catch (Exception e) {
            LogUtil.error("截取视频缩略图异常", e);
        } finally {
            try {
                fFmpegFrameGrabber.close();
            } catch (FrameGrabber.Exception e) {
                LogUtil.error("截取视频缩略图工具类关闭异常", e);
            }
        }
    }

    /**
     * 获取视频缩略图的位置
     *
     * @return int
     */
    private int getVideoThumbnailPosition() {
        return 5;
    }

    /**
     * 获取视频缩略图的别名
     *
     * @return String
     */
    private String getVideoThumbnailAlias() {
        return "POSTER";
    }

    /**
     * 获取视频截取帧的扩展名
     *
     * @return String
     */
    private String getVideoThumbnailExtension() {
        return "jpg";
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
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String enFileId = request.getParameter("fileId");
        String fileId = fileService.decId(enFileId);
        PubFileRecord fileRecord = fileService.getFileRecord(fileId);
        boolean inline = "1".equals(request.getParameter("inline"));
        DownloadParam downloadParam = getDownloadParam(fileRecord, null);
        // 目前文件下载统一默认都是原件下载
        downloadFileData(response, fileRecord, downloadParam.setInline(inline));
    }

    /**
     * 内联方式下载附件方法(默认文件)
     *
     * @param response
     * @param fileId   附件编号
     * @return
     * @throws Exception
     */
    @RequestMapping("/inline/{fileId}")
    public void inline(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileId) throws Exception {
        inline(request, response, null, fileId);
    }

    /**
     * 内联方式下载附件方法(指定别名文件)
     *
     * @param response
     * @param fileAlias 附件别名
     * @param fileId    附件编号
     * @return
     * @throws Exception
     */
    @RequestMapping("/inline/{fileAlias}/{fileId}")
    public void inline(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileAlias, @PathVariable String fileId) throws Exception {
        String realFileId = fileService.decId(fileId);

        PubFileRecord fileRecord = fileService.getFileRecord(realFileId);

        DownloadParam downloadParam = getDownloadParam(fileRecord, fileAlias);
        if (!checkIfModifiedSince(request, response, downloadParam)) {
            return;
        }

        downloadFileData(response, fileRecord, downloadParam.setInline(true));
    }

//    /**
//     * 内联方式下载附件的名称(目前仅inline和thumbnail方法的名称调用这个方法)
//     *
//     * @param fileRecord 附件记录
//     * @return String 附件名称
//     */
//    private String getInlineFileName(PubFileRecord fileRecord) {
//        if (fileRecord == null) {
//            return "";
//        }
//        String fileExtension = fileRecord.getFileExtension();
//        // FIXME 这里先使用附件原始名字看下缓存是否正常,如果不正常将使用附件编号和访问的地址保持一致
////        return fileService.encId(fileRecord.getFileId()) + (Utils.notEmpty(fileExtension) ? ("." + fileExtension) : "");
//        return fileRecord.getFileName() + (Utils.notEmpty(fileExtension) ? ("." + fileExtension) : "");
//    }

    protected static final Map<String, String> MIME_MAP = new HashMap<>();

    protected Class<?> getMimeClass() {
        return getClass();
    }

    protected String getMimeType(String fileExtension) {
        if (Utils.isEmpty(fileExtension)) {
            return null;
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
        return MIME_MAP.get(fileExtension);
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
    private void downloadFileData(HttpServletResponse response, PubFileRecord fileRecord, DownloadParam downloadParam) throws Exception {
        if (fileRecord == null) {
            LogUtil.warn("下载的附件不存在");
            return;
        }
        InputStream input = null;
        try {
            input = fileService.getFileInputStream(fileRecord, downloadParam.getAlias(), downloadParam.getExtension());
//            long fileSize;
//            if (input != null) {
//                fileSize = fileService.getFileSize(fileRecord, downloadParam.getAlias(), downloadParam.getExtension());
//            } else { // 当别名附件不存在时,使用原附件
//                input = fileService.getFileInputStream(fileRecord);
//                fileSize = fileService.getFileSize(fileRecord);
//            }
            downloadFileData(response, input, downloadParam);
        } catch (Exception e) {
            String error = "下载附件异常@" + System.currentTimeMillis();
            LogUtil.error(error + "[" + fileRecord.getFileId() + "]", e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, error);
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    private static class DownloadParam {
        String fileName;
        long fileSize;
        long lastModified;
        String alias;
        boolean inline;
        String extension;
//        String encryptedFileId;

        public DownloadParam() {
        }

        public DownloadParam(String fileName, long fileSize, long lastModified) {
            this.fileName = fileName;
            this.fileSize = fileSize;
            this.lastModified = lastModified;
        }

        public DownloadParam(String fileName, long fileSize, long lastModified, String alias, boolean inline) {
            this.fileName = fileName;
            this.fileSize = fileSize;
            this.lastModified = lastModified;
            this.alias = alias;
            this.inline = inline;
        }

        public boolean isInline() {
            return inline;
        }

        public DownloadParam setInline(boolean inline) {
            this.inline = inline;
            return this;
        }

//        public String getEncryptedFileId() {
//            return encryptedFileId;
//        }
//
//        public DownloadParam setEncryptedFileId(String encryptedFileId) {
//            this.encryptedFileId = encryptedFileId;
//            return this;
//        }

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

        public long getLastModified() {
            return lastModified;
        }

        public DownloadParam setLastModified(long lastModified) {
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

        public String getExtension() {
            return extension;
        }

        public DownloadParam setExtension(String extension) {
            this.extension = extension;
            return this;
        }
    }

    private DownloadParam getDownloadParam(PubFileRecord fileRecord, String alias) {
        DownloadParam downloadParam = new DownloadParam();

        downloadParam.setLastModified(fileRecord.getUpdateTime() != null ? fileRecord.getUpdateTime().getTime() :
                (fileRecord.getCreateTime() != null ? fileRecord.getCreateTime().getTime() : 0L));
        downloadParam.setAlias(alias);

        String fileName = fileRecord.getFileName();
        // 视频类型
        if (accept(fileRecord.getFileExtension(), fileService.getVideoTypes()) && getVideoThumbnailAlias().equals(alias)) {
            downloadParam.setExtension(getVideoThumbnailExtension());
            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex >= 0) {
                fileName = fileName.substring(0, dotIndex + 1) + downloadParam.getExtension();
            } else {
                fileName += "." + downloadParam.getExtension();
            }
        } else {
            downloadParam.setExtension(fileRecord.getFileExtension());
        }
        downloadParam.setFileName(fileName);
        long fileSize = fileService.getFileSize(fileRecord, downloadParam.getAlias(), downloadParam.getExtension());
        downloadParam.setFileSize(fileSize);

        return downloadParam;
    }

    /**
     * 附件下载
     *
     * @param response
     * @throws Exception
     */
    private void downloadFileData(HttpServletResponse response, InputStream input, DownloadParam downloadParam) throws Exception {
        String encoding = "UTF-8";
        try {
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Accept-Charset", encoding);
            String contentType = null;
            String disposition;
            if (downloadParam.isInline()) {
                String fileExtension = downloadParam.getExtension();
                if (Utils.notEmpty(fileExtension)) {
                    contentType = getMimeType(fileExtension);
                }
                disposition = "inline;";
            } else {
                disposition = "attachment;";
            }
            disposition += " filename=" + URLEncoder.encode(downloadParam.getFileName(), encoding);

            if (Utils.isEmpty(contentType)) {
                contentType = "application/octet-stream";
            }
            response.setHeader("Content-type", contentType);

            response.setHeader("Content-Disposition", disposition);
            response.setHeader("Content-Length", String.valueOf(downloadParam.getFileSize()));
            if (downloadParam.getLastModified() > 0L) {
                synchronized (DF_GMT) {
                    response.setHeader("Last-Modified", DF_GMT.format(new Date(downloadParam.getLastModified())));
                }
                response.setHeader("ETag", getEtag(downloadParam));
            }
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

    private static String getEtag(DownloadParam downloadParam) {
        long lastModified = downloadParam.getLastModified();
        String etag = getIntHex(downloadParam.getFileSize()) + getIntHex(lastModified);
        return etag;
    }

    private static String getIntHex(long l) {
        l = (l & 0xFFFFFFFFL) | 0x100000000L;
        String s = Long.toHexString(l);
        return s.substring(1);
    }

    protected boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, DownloadParam downloadParam) {
        try {
            long headerValue = request.getDateHeader("If-Modified-Since");
            long lastModified = downloadParam.getLastModified();
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

    /**
     * 显示缩略图方法
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/thumbnail")
    @ResponseBody
    public void thumbnail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String enFileId = request.getParameter("fileId");
        String fileId = fileService.decId(enFileId);
        PubFileRecord fileRecord = fileService.getFileRecord(fileId);
//		FrameworkHelper.LOG.debug("file.thumbnail[" + enFileId + "->" + fileId + "]");
        // 获取参数
        FilterParam param = getFileParam(request);
        if (fileRecord != null) {
            // 获取附件别名
            String fileAlias = getFileAlias(param);

            InputStream input = null;
            try {
                String fileName = fileRecord.getFileName();
                input = fileService.getFileInputStream(fileRecord, fileAlias);
                long fileSize = 0L;
                if (input != null) {
                    fileSize = fileService.getFileSize(fileRecord, fileAlias);
                } else if (Utils.notEmpty(fileAlias)) { // 当别名附件不存在时,使用原附件
                    input = fileService.getFileInputStream(fileRecord);
                    fileSize = fileService.getFileSize(fileRecord);
                }

                DownloadParam downloadParam = getDownloadParam(fileRecord, fileAlias);
                if (!checkIfModifiedSince(request, response, downloadParam)) {
                    return;
                }

                if (input != null) {
                    downloadFileData(response, input, downloadParam.setInline(true));
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

        String scheme = param.getValueAsString("scheme");
        FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);
        String defaultIcon = null;
        if (handlingScheme != null && Utils.notEmpty(handlingScheme.getDefaultIcon())) {
            defaultIcon = handlingScheme.getDefaultIcon();
        } else {
            defaultIcon = "default.png";
        }
        String fileAlias = request.getParameter("fileAlias");
        if (Utils.notEmpty(fileAlias)) {
            int lastDot = defaultIcon.lastIndexOf(".");
            if (lastDot >= 0) {
                defaultIcon = defaultIcon.substring(0, lastDot) + "_" + fileAlias + defaultIcon.substring(lastDot);
            } else {
                defaultIcon += "_" + fileAlias;
            }
        }

        // 这里可能考虑重定向到具体文件目录去
        File defaultImageFile = new File(SystemData.getInstance().getServletInfo().getServletRealPath() + "m/default/img/" + defaultIcon);
        if (defaultImageFile.exists()) {
            downloadFileData(response, new FileInputStream(defaultImageFile), new DownloadParam(defaultImageFile.getName(), defaultImageFile.length(), defaultImageFile.lastModified()).setInline(true));
            return;
        } else {
            String error = "附件记录不存在@" + System.currentTimeMillis();
            LogUtil.warn(error + "[" + enFileId + "->" + fileId + "]");
            response.sendError(HttpServletResponse.SC_NOT_FOUND, error);
        }
    }

    private String getFileAlias(FilterParam param) {
        String fileAlias = param.getValueAsString("fileAlias");
        if (Utils.isEmpty(fileAlias)) {
            String scheme = param.getValueAsString("scheme");
            FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);
            if (handlingScheme != null && Utils.notEmpty(handlingScheme.getDefines())) {
                fileAlias = handlingScheme.getDefines().get(0);
                // 缩略图还没生成需要处理
            }
        }
        return fileAlias;
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
        String fileId = fileService.decId(enFileId);
        PubFileRecord fileRecord = fileService.getFileRecord(fileId);

        if (fileRecord != null) {
            String fileExtension = FileUtil.getExtension(fileRecord.getFileUrl());
            boolean isImage = accept(fileExtension, fileService.getImageTypes());
            FilterParam param = getFileParam(request);
            String fileParamUrl = "?fileId=" + enFileId + param;
            if (isImage) {
                // 图片形式使用框架的预览方式
                return new JSCommand("$.previewImage('file/thumbnail" + fileParamUrl + "');");
            } else {
                String mimeType = getMimeType(fileExtension);
                if (Utils.notEmpty(mimeType)) {
                    // 如果是浏览器支持可查看的内联类型,新窗口打开
                    return new JSCommand("window.open('file/download" + fileParamUrl + "&inline=1');");
                } else { // 其他情况都是直接下载
                    return new JSCommand("$.download('file/download" + fileParamUrl + "');");
                }
            }
        } else {
            String error = "附件获取异常@" + System.currentTimeMillis();
            LogUtil.warn(error + "[" + fileRecord.getFileId() + "]");
            throw new DfishException(error);
        }
    }

    private FilterParam getFileParam(HttpServletRequest request) {
        FilterParam param = new FilterParam();
        param.registerKey("fileAlias");
        param.registerKey("scheme");

        param.bindRequest(request);
        return param;
    }
}
