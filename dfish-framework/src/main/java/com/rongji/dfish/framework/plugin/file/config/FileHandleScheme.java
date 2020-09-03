package com.rongji.dfish.framework.plugin.file.config;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.dto.PreviewResponse;
import com.rongji.dfish.framework.plugin.file.dto.UploadItem;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;

import java.io.File;
import java.util.List;

/**
 * 文件处理方案,定义了上传
 * @author lamontYu
 * @since DFish5.0
 */
public class FileHandleScheme {

    public static final String ALIAS_DEFAULT = "DEFAULT";
    public static final String FILE_SCHEME_AUTO = "AUTO";
    public static final String FILE_ALIAS_AUTO = "AUTO";

    protected FileService getFileService() {
        // FIXME 暂不关心文件服务获取不到的情况,当做使用该方案默认就有文件服务
        return FrameworkHelper.getBean(FileService.class);
    }

    /**
     * 附件类型,默认为F(ile)
     * @return String
     */
    public String fileType() {
        return FileService.CONFIG_TYPE_FILE;
    }

    /**
     * 该方案默认文件类型
     * @return String
     */
    protected String defaultTypes() {
        return getFileService().getFileTypes(FileService.CONFIG_TYPE_FILE, null);
    }

    /**
     * 该方案能接受的文件类型
     * @return String
     */
    public String acceptTypes() {
        return Utils.isEmpty(handleTypes) ? defaultTypes() : handleTypes;
    }
    /**
     * 方案名称
     */
    protected String name;
    /**
     * 方案定义
     */
    protected List<? extends FileHandleConfig> handleConfigs;
    /**
     * 需处理的文件类型
     */
    protected String handleTypes;
    /**
     * 最大允许上传的文件大小
     */
    protected String sizeLimit;
    /**
     * 附件链接的地址
     */
    protected String fileUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<? extends FileHandleConfig> getHandleConfigs() {
        return handleConfigs;
    }

    public void setDefines(List<? extends FileHandleConfig> handleConfigs) {
        this.handleConfigs = handleConfigs;
    }

    public String getHandleTypes() {
        return handleTypes;
    }

    public void setHandleTypes(String handleTypes) {
        this.handleTypes = handleTypes;
    }

    public String getSizeLimit() {
        return sizeLimit;
    }

    public void setSizeLimit(String sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public UploadItem uploaded(UploadItem uploadItem) throws Exception {
//        PubFileRecord fileRecord = uploadItem.getFileRecord();
//        fileRecord.setFileType(fileType());
//        fileRecord.setFileScheme(getName());
        return uploadItem;
    }

    /**
     * 修正下载文件参数(主要是下载的文件名和扩展名)
     * @param downloadParam DownloadParam
     * @param fileRecord PubFileRecord
     * @param alias String
     */
    public void fixDownloadParam(DownloadParam downloadParam, PubFileRecord fileRecord, String alias) {
        downloadParam.setExtension(fileRecord.getFileExtension());
        downloadParam.setFileName(fileRecord.getFileName());
    }

    public String getDefaultAlias() {
        return ALIAS_DEFAULT;
    }

    public PreviewResponse getPreviewResponse(String encryptedId, String alias, String fileExtension) {
        PreviewResponse response = new PreviewResponse();
        String fileParamUrl = "file/download?fileId=" + encryptedId + "&scheme=" + getName() + "&alias=" + alias;
        response.setWay(PreviewResponse.WAY_PREVIEW_DOWNLOAD);
        response.setUrl(fileParamUrl);
        return response;
    }

    public void fixUploadItem(UploadItem uploadItem) {

    }

}
