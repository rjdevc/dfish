package com.rongji.dfish.framework.plugin.file.config.img;

import com.rongji.dfish.base.context.SystemContext;
import com.rongji.dfish.base.img.ImageProcessConfig;
import com.rongji.dfish.base.img.ImageProcessorGroup;
import com.rongji.dfish.base.img.ImageWatermarkConfig;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.info.ServletInfo;
import com.rongji.dfish.framework.plugin.file.config.FileHandleScheme;
import com.rongji.dfish.framework.plugin.file.dto.PreviewResponse;
import com.rongji.dfish.framework.plugin.file.dto.UploadItem;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;

import java.io.File;
import java.util.List;

/**
 * 图片处理方案
 * @author lamontYu
 * @since DFish5.0
 */
public class ImageHandleScheme extends FileHandleScheme {
    @Override
    public String fileType() {
        return FileService.CONFIG_TYPE_IMAGE;
    }

    @Override
    protected String defaultTypes() {
        return getFileService().getFileTypes(FileService.CONFIG_TYPE_IMAGE, null);
    }

    /**
     * 处理配置
     */
    private List<ImageHandleConfig> handleConfigs;
    /**
     * 是否需要以原图大小压缩
     */
    private boolean handleZoomDefault;

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

    @Override
    public UploadItem uploaded(UploadItem uploadItem) throws Exception {
        if (uploadItem == null) {
            return null;
        }
        PubFileRecord fileRecord = uploadItem.getFileRecord();
        if (fileRecord == null) {
            String errorMsg = "生成缩略图出现异常@" + System.currentTimeMillis();
            LogUtil.warn(errorMsg + ":原记录文件存储记录丢失[" + uploadItem.getId() + "]");
            return uploadItem.setError(new UploadItem.Error(errorMsg));
        }

        File imageFile = getFileService().getFile(fileRecord);
        if (imageFile == null || !imageFile.exists()) {
            String errorMsg = "生成缩略图出现异常@" + System.currentTimeMillis();
            LogUtil.warn(errorMsg + ":原图丢失[" + uploadItem.getId() + "]");
            return uploadItem.setError(new UploadItem.Error(errorMsg));
        }
        ImageProcessorGroup imageProcessorGroup = ImageProcessorGroup.of(imageFile).setDest(imageFile.getParentFile().getAbsolutePath())
                .setAliasPattern("{FILE_NAME}_{ALIAS}.{EXTENSION}");

        // FIXME 默认图片和需要标记点图片放在位置不合理时,导致部分图片未能按标记点图片处理
        if (isHandleZoomDefault()) {
            imageProcessorGroup.process(-1, -1, ImageProcessConfig.WAY_ZOOM, ALIAS_DEFAULT, false);
        }
        List<ImageHandleConfig> handleConfigs = getHandleConfigs();
        if (handleConfigs != null) {
            for (ImageHandleConfig handleConfig : handleConfigs) {
                if (handleConfig.getWatermark() != null) {
                    fixWatermark(handleConfig.getWatermark());
                }
                imageProcessorGroup.process(handleConfig);
            }
        }
        imageProcessorGroup.execute();
        return super.uploaded(uploadItem);
    }

    private void fixWatermark(ImageWatermarkConfig watermark) {
        if (Utils.notEmpty(watermark.getImagePath())) {
            // FIXME 根据水印名称可能做缓存处理
            String servletPath = SystemContext.getInstance().get(ServletInfo.class).getServletRealPath();
            watermark.setImageFile(new File(servletPath + watermark.getImagePath()));
        }
    }

    @Override
    public PreviewResponse getPreviewResponse(String encryptedId, String alias, String fileExtension) {
        PreviewResponse response = new PreviewResponse();
        response.setWay(PreviewResponse.WAY_PREVIEW_IMAGE);
        response.setUrl("file/inline/" + (Utils.isEmpty(alias) ? FILE_ALIAS_AUTO : alias) + "/" + encryptedId + "." + fileExtension);
        return response;
    }

    @Override
    public void fixUploadItem(UploadItem uploadItem) {
        if (isHandleZoomDefault()) {
            uploadItem.setThumbnail("file/inline/" + uploadItem.getId() + "." + uploadItem.getExtension());
        }
    }
}
