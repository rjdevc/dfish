package com.rongji.dfish.framework.plugin.file.config.img;

import com.rongji.dfish.base.img.ImageWatermarkConfig;
import com.rongji.dfish.framework.plugin.file.config.FileHandleScheme;

import java.util.List;

/**
 * @author lamontYu
 * @date 2019-12-18
 * @since 5.0
 */
public class ImageHandleScheme extends FileHandleScheme {

    private List<ImageHandleConfig> handleConfigs;
    /**
     * 默认图片(图片不存在时显示默认图片,暂没想到文件有什么类似需求,暂时以这个命名)
     */
    private String defaultIcon;
    private ImageWatermarkConfig watermark;

    @Override
    public List<ImageHandleConfig> getHandleConfigs() {
        return handleConfigs;
    }

    public void setHandleConfigs(List<ImageHandleConfig> handleConfigs) {
        this.handleConfigs = handleConfigs;
    }

    public String getDefaultIcon() {
        return defaultIcon;
    }

    public void setDefaultIcon(String defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

    public ImageWatermarkConfig getWatermark() {
        return watermark;
    }

    public void setWatermark(ImageWatermarkConfig watermark) {
        this.watermark = watermark;
    }

}
