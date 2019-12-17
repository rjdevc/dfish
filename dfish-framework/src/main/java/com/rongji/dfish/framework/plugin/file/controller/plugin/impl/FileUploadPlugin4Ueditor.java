package com.rongji.dfish.framework.plugin.file.controller.plugin.impl;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.plugin.file.controller.FileController;
import com.rongji.dfish.framework.plugin.file.config.FileHandleScheme;
import com.rongji.dfish.framework.plugin.file.dto.UploadItem;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 百度编辑器中图片上传处理
 * @author lamontYu
 */
@Component
public class FileUploadPlugin4Ueditor extends AbstractFileUploadPlugin {

    @Override
    public String name() {
        return "UEDITOR";
    }

    @Override
    public Object doRequest(HttpServletRequest request) {
        String action = request.getParameter("action");
        if ("config".equals(action)) {
            byte[] buff = new byte[8192];
            String readJson = "";
            try(InputStream is = FileController.class.getClassLoader().getResourceAsStream("com/rongji/dfish/framework/plugin/file/config/ueditor_config.json")) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int readLength = -1;
                while ((readLength = is.read(buff)) > 0) {
                    baos.write(buff, 0, readLength);
                }
                readJson = new String(baos.toByteArray(), "UTF-8");
            } catch (IOException e1) {
                LogUtil.error("上传过程出现异常", e1);
            }
            return readJson;
        }
        // 百度编辑器的附件上传应该来说只有图片
        UploadItem uploadItem = saveFile(request);
        String resultJson;
        if (uploadItem.getError() != null && uploadItem.getError()) {
            resultJson = "{\"state\":\"FAIL\",\"text\":\"" + uploadItem.getText() + "\"}";
        } else {
            String scheme = request.getParameter("scheme");
            FileHandleScheme handlingScheme = fileHandleManager.getScheme(scheme);
            String fileUrl = null;
            if (handlingScheme != null) {
                fileUrl = handlingScheme.getFileUrl();
            }
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
