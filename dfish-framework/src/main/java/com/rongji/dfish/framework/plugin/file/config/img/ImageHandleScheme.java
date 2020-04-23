package com.rongji.dfish.framework.plugin.file.config.img;

import com.rongji.dfish.framework.plugin.file.config.FileHandleScheme;

import java.util.List;

/**
 * 图片处理方案
 * @author lamontYu
 * @since DFish5.0
 */
public class ImageHandleScheme extends FileHandleScheme {
    /**
     * 处理配置
     */
    private List<ImageHandleConfig> handleConfigs;
    /**
     * 是否需要以原图大小压缩
     */
    private boolean handleZoomDefault;
    /**
     * 默认图片(图片不存在时显示默认图片,暂没想到文件有什么类似需求,暂时以这个命名)
     */
    private String defaultIcon;

    /**
     * 处理配置
     * @return 处理配置
     */
    @Override
    public List<ImageHandleConfig> getHandleConfigs() {
        return handleConfigs;
    }

    /**
     * 处理配置
     * @param handleConfigs 处理配置
     */
    public void setHandleConfigs(List<ImageHandleConfig> handleConfigs) {
        this.handleConfigs = handleConfigs;
    }

    /**
     * 是否需要以原图大小压缩
     * @return 是否以原图大小压缩
     */
    public boolean isHandleZoomDefault() {
        return handleZoomDefault;
    }

    /**
     * 是否需要以原图大小压缩
     * @param handleZoomDefault
     */
    public void setHandleZoomDefault(boolean handleZoomDefault) {
        this.handleZoomDefault = handleZoomDefault;
    }

    /**
     * 默认图片(图片不存在时显示默认图片,暂没想到文件有什么类似需求,暂时以这个命名)
     * @return 默认图片
     */
    public String getDefaultIcon() {
        return defaultIcon;
    }

    /**
     * 默认图片(图片不存在时显示默认图片,暂没想到文件有什么类似需求,暂时以这个命名)
     * @param defaultIcon 默认图片
     */
    public void setDefaultIcon(String defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

}
