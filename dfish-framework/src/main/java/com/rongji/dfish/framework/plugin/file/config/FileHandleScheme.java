package com.rongji.dfish.framework.plugin.file.config;

import java.util.List;

/**
 * 文件处理方案,定义了上传
 * @author lamontYu
 * @create 2019-08-07
 * @since 3.2
 */
public class FileHandleScheme {
    /**
     * 方案名称
     */
    private String name;
    /**
     * 默认图片(图片不存在时显示默认图片,暂没想到文件有什么类似需求,暂时以这个命名)
     */
    private String defaultIcon;
    /**
     * 方案定义
     */
    private List<FileHandleDefine> defines;
    /**
     * 需处理的文件类型
     */
    private String handlingTypes;
    /**
     * 最大允许上传的文件大小
     */
    private String sizeLimit;
    /**
     * 附件链接的地址
     */
    private String fileUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultIcon() {
        return defaultIcon;
    }

    public void setDefaultIcon(String defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

    public List<FileHandleDefine> getDefines() {
        return defines;
    }

    public void setDefines(List<FileHandleDefine> defines) {
        this.defines = defines;
    }

    public String getHandlingTypes() {
        return handlingTypes;
    }

    public void setHandlingTypes(String handlingTypes) {
        this.handlingTypes = handlingTypes;
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
}
