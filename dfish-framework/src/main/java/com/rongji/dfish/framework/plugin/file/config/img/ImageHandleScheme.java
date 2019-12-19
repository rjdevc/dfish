package com.rongji.dfish.framework.plugin.file.config.img;

import com.rongji.dfish.framework.plugin.file.config.FileHandleScheme;

import java.util.List;

/**
 * 图片处理方案
 * @author lamontYu
 * @date 2019-12-18
 * @since 5.0
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

    @Override
    public List<ImageHandleConfig> getHandleConfigs() {
        return handleConfigs;
    }

    public void setHandleConfigs(List<ImageHandleConfig> handleConfigs) {
        this.handleConfigs = handleConfigs;
    }

    public boolean isHandleZoomDefault() {
        return handleZoomDefault;
    }

    public void setHandleZoomDefault(boolean handleZoomDefault) {
        this.handleZoomDefault = handleZoomDefault;
    }

    public String getDefaultIcon() {
        return defaultIcon;
    }

    public void setDefaultIcon(String defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

}
