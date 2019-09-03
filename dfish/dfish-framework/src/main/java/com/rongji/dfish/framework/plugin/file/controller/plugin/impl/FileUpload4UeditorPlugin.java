package com.rongji.dfish.framework.plugin.file.controller.plugin.impl;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.plugin.file.controller.FileController;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingManager;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingScheme;
import com.rongji.dfish.framework.plugin.file.controller.plugin.FileUploadPlugin;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.ui.form.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 百度编辑器中图片上传处理
 * @author YuLM
 */
@Component
public class FileUpload4UeditorPlugin implements FileUploadPlugin {
    @Autowired
    private FileService fileService;

    @Autowired
    private FileHandlingManager fileHandlingManager;

    @Override
    public String name() {
        return "ueditor";
    }

    @Override
    public Object doRequest(HttpServletRequest request) {
        String action = request.getParameter("action");
        if ("config".equals(action)) {
            InputStream is = FileController.class.getClassLoader().getResourceAsStream("com/rongji/dfish/framework/plugin/file/controller/ueditor_config.json");
            byte[] buff = new byte[8192];
            String readJson = "";
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int readLength = -1;
                while ((readLength = is.read(buff)) > 0) {
                    baos.write(buff, 0, readLength);
                }
                readJson = new String(baos.toByteArray(), "UTF-8");
            } catch (IOException e1) {
                LogUtil.error("上传过程出现异常", e1);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    LogUtil.error("文件流关闭异常", e);
                }
            }
            return readJson;
        }
        // 百度编辑器的附件上传应该来说只有图片
        UploadItem uploadItem = FileController.saveFile(request, fileService, fileService.getImageTypes());
        String resultJson;
        if (uploadItem == null || (uploadItem.getError() != null && uploadItem.getError())) {
            resultJson = "{\"state\":\"FAIL\",\"text\":\"" + uploadItem.getText() + "\"}";
        } else {
            String scheme = request.getParameter("scheme");
            FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);
            String fileUrl = handlingScheme.getFileUrl();
            if (Utils.isEmpty(fileUrl)) {
                fileUrl = "file/thumbnail?fileId=$fileId";
            }
            resultJson = "{\"state\":\"SUCCESS\"," +
                    "\"url\":\""+fileUrl.replace("$fileId", uploadItem.getId())+"\"," +
                    "\"title\":\""+uploadItem.getName()+"\"," +
                    "\"original\":\""+uploadItem.getName()+"\"," +
                    "\"type\":\""+FileUtil.getFileExtName(uploadItem.getName())+"\"," +
                    "\"size\":"+uploadItem.getSize()+"" +
                    "}";
        }
        return resultJson;

    }

}
