package com.rongji.dfish.framework.plugin.file.controller.plugin.impl;

import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.config.FileHandleManager;
import com.rongji.dfish.framework.plugin.file.controller.plugin.FileUploadPlugin;
import com.rongji.dfish.framework.plugin.file.dto.UploadItem;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 抽象类，用于引入外部插件需要用到附件上传的处理方式
 * @author lamontYu
 * @since DFish5.0
 */
public abstract class AbstractFileUploadPlugin implements FileUploadPlugin {

    @Resource(name = "fileService")
    protected FileService fileService;
    @Autowired
    protected FileHandleManager fileHandleManager;

    /**
     * 获取fileService
     * @return FileService
     */
    public FileService getFileService() {
        return fileService;
    }

    /**
     * 设置fileService
     * @param fileService 文件服务
     */
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 获取 fileHandleManager
     * @return FileHandleManager
     */
    public FileHandleManager getFileHandleManager() {
        return fileHandleManager;
    }

    /**
     * 设置 fileHandleManager
     * @param fileHandleManager 文件处理管理器
     */
    public void setFileHandleManager(FileHandleManager fileHandleManager) {
        this.fileHandleManager = fileHandleManager;
    }

    /**
     * 附件保存方法,根据定义情况对上传附件类型进行限制
     *
     * @param request 请求
     * @return 上传数据项
     */
    public UploadItem saveFile(HttpServletRequest request) {
        UploadItem uploadItem = null;

        boolean accept = false;
        try {
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            MultipartFile fileData = mRequest.getFile("Filedata");

            String extName = FileUtil.getFileExtName(fileData.getOriginalFilename());
            String acceptTypes = fileService.getFileTypes(FileService.CONFIG_TYPE_FILE, null);
            accept = FileUtil.accept(extName, acceptTypes);
            if (accept) {
                String loginUserId = FrameworkHelper.getLoginUser(mRequest);
                PubFileRecord fileRecord = new PubFileRecord();
                fileRecord.setFileName(fileData.getOriginalFilename());
                fileRecord.setFileSize(fileData.getSize());
                fileRecord.setFileCreator(loginUserId);
                String fileType = request.getParameter("fileType");
                String fileScheme = request.getParameter("scheme");
                fileRecord.setFileType(fileType);
                fileRecord.setFileScheme(fileScheme);
                uploadItem = fileService.saveFile(fileData.getInputStream(), fileRecord);

                if (uploadItem != null && (uploadItem.getError() == null || Utils.isEmpty(uploadItem.getError().getText()))) {
                    String fileId = fileService.decrypt(uploadItem.getId());
                    // FIXME 哪些附件未被启用还需进一步判断
                    fileService.updateFileLink(fileId, name(), fileId,loginUserId);
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

}
