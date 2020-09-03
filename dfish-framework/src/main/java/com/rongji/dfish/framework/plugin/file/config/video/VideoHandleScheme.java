package com.rongji.dfish.framework.plugin.file.config.video;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.plugin.file.config.DownloadParam;
import com.rongji.dfish.framework.plugin.file.config.FileHandleScheme;
import com.rongji.dfish.framework.plugin.file.dto.PreviewResponse;
import com.rongji.dfish.framework.plugin.file.dto.UploadItem;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 视频处理方案
 * @author lamontYu
 * @since DFish5.0
 */
public class VideoHandleScheme extends FileHandleScheme {
    @Override
    public String fileType() {
        return FileService.CONFIG_TYPE_VIDEO;
    }

    @Override
    protected String defaultTypes() {
        return getFileService().getFileTypes(FileService.CONFIG_TYPE_VIDEO, null);
    }

    private String posterAlias = "POSTER";
    private int posterPosition = 5;
    private String posterExtension = "jpg";

    public String getPosterAlias() {
        return posterAlias;
    }

    public void setPosterAlias(String posterAlias) {
        this.posterAlias = posterAlias;
    }

    public int getPosterPosition() {
        return posterPosition;
    }

    public void setPosterPosition(int posterPosition) {
        this.posterPosition = posterPosition;
    }

    public String getPosterExtension() {
        return posterExtension;
    }

    public void setPosterExtension(String posterExtension) {
        this.posterExtension = posterExtension;
    }

    @Override
    public UploadItem uploaded(UploadItem uploadItem) throws Exception {
        if (uploadItem == null) {
            return null;
        }
        grabVideoFramer(uploadItem);

        return super.uploaded(uploadItem);
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
        String fileId = getFileService().decrypt(uploadItem.getId());
        PubFileRecord fileRecord = getFileService().get(fileId);
        if (fileRecord == null) {
            return;
        }
        FFmpegFrameGrabber fFmpegFrameGrabber = null;
        try {
			 /*
            获取视频文件
            */
            fFmpegFrameGrabber = new FFmpegFrameGrabber(getFileService().getFileInputStream(fileRecord));
            fFmpegFrameGrabber.start();

            //获取视频总帧数
            int frameLength = fFmpegFrameGrabber.getLengthInFrames();
            // 这里播放时长暂时用秒
            int duration = (int) (frameLength / fFmpegFrameGrabber.getFrameRate());
            uploadItem.setDuration(duration);
            // 帧下标
            int frameIndex = 0;
            while (frameIndex++ <= frameLength) {
                // 对目标帧进行处理
                if (frameIndex == getPosterPosition()) {
                    Frame frame = fFmpegFrameGrabber.grabImage();
                    if (frame != null) {
                        String saveFileName = fileId + "_" + getPosterAlias() + "." + getPosterExtension();
                        //文件绝对路径+名字
                        String fileName = getFileService().getFileDir(fileRecord) + saveFileName;

                        //文件储存对象
                        File output = new File(fileName);

                        // 将目标帧转换成图片
                        Java2DFrameConverter converter = new Java2DFrameConverter();
                        BufferedImage bufferedImage = converter.getBufferedImage(frame);
                        ImageIO.write(bufferedImage, getPosterExtension(), output);

                        uploadItem.setThumbnail("file/inline/" + getPosterAlias() + "/" + uploadItem.getId() + "." + getPosterExtension());
                    }
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

    @Override
    public void fixDownloadParam(DownloadParam downloadParam, PubFileRecord fileRecord, String alias) {
        if (downloadParam == null || fileRecord == null) {
            return;
        }
        String fileName = fileRecord.getFileName();
        if (getPosterAlias().equals(alias)) {
            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex >= 0) {
                fileName = fileName.substring(0, dotIndex + 1) + downloadParam.getExtension();
            } else {
                fileName += "." + downloadParam.getExtension();
            }
            downloadParam.setExtension(getPosterExtension());
        } else {
            downloadParam.setExtension(fileRecord.getFileExtension());
        }
        downloadParam.setFileName(fileName);
    }

    @Override
    public PreviewResponse getPreviewResponse(String encryptedId, String alias, String fileExtension) {
        PreviewResponse response = new PreviewResponse();
        response.setWay(PreviewResponse.WAY_PREVIEW_VIDEO);
        response.setUrl("file/inline/" + FILE_ALIAS_AUTO + "/" + encryptedId + "." + fileExtension);
        return response;
    }

    @Override
    public void fixUploadItem(UploadItem uploadItem) {
        uploadItem.setThumbnail("file/inline/" + getPosterAlias() + "/" + uploadItem.getId() + "." + uploadItem.getExtension());
    }
}
