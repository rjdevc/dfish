package com.rongji.dfish.framework.plugin.file.config;

import java.util.List;

/**
 * 文件处理方案,定义了上传
 * @author lamontYu
 * @since DFish5.0
 */
public class FileHandleScheme {
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
}
