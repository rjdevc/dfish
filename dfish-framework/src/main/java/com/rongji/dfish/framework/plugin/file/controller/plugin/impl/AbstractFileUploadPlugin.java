package com.rongji.dfish.framework.plugin.file.controller.plugin.impl;

import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandleManager;
import com.rongji.dfish.framework.plugin.file.controller.plugin.FileUploadPlugin;
import com.rongji.dfish.framework.plugin.file.dto.UploadItem;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lamontYu
 * @create 2019-12-10
 */
public abstract class AbstractFileUploadPlugin implements FileUploadPlugin {

    @Resource(name = "fileService")
    protected FileService fileService;
    @Autowired
    protected FileHandleManager fileHandleManager;

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public FileHandleManager getFileHandleManager() {
        return fileHandleManager;
    }

    public void setFileHandleManager(FileHandleManager fileHandleManager) {
        this.fileHandleManager = fileHandleManager;
    }

    /**
     * 附件保存方法,根据定义情况对上传附件类型进行限制
     *
     * @param request
     * @return
     */
    public UploadItem saveFile(HttpServletRequest request) {
        UploadItem uploadItem = null;

        boolean accept = false;
        try {
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            MultipartFile fileData = mRequest.getFile("Filedata");

            String extName = FileUtil.getFileExtName(fileData.getOriginalFilename());
            String acceptTypes = fileService.getFileTypes();
            accept = fileService.accept(extName, acceptTypes);
            if (accept) {
                String loginUserId = FrameworkHelper.getLoginUser(mRequest);
                uploadItem = fileService.saveFile(fileData.getInputStream(), fileData.getOriginalFilename(), fileData.getSize(), loginUserId);

                if (uploadItem != null && (uploadItem.getError() == null || !uploadItem.getError())) {
                    String fileId = fileService.decrypt(uploadItem.getId());
                    // FIXME 哪些附件未被启用还需进一步判断
                    fileService.updateFileLink(fileId, name(), fileId);
                }
            }
        } catch (Exception e) {
            String error = "上传失败,系统内部异常@" + System.currentTimeMillis();
            LogUtil.error(error, e);
            uploadItem = new UploadItem();
            uploadItem.setError(true);
            uploadItem.setText(error);
        }

        if (uploadItem == null) {
            uploadItem = new UploadItem();
            uploadItem.setError(true);
            uploadItem.setText("上传文件失败" + (!accept ? "：当前文件类型不符合系统规范" : ""));
        }
        return uploadItem;
    }

}
