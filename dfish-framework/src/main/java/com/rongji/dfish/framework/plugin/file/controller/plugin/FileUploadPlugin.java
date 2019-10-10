package com.rongji.dfish.framework.plugin.file.controller.plugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 附件上传插件
 */
public interface FileUploadPlugin {

    /**
     * 插件名称
     *
     * @return String
     */
    String name();

    /**
     * 插件附件请求方法
     *
     * @param request
     * @return Object
     * @throws Exception
     */
    Object doRequest(HttpServletRequest request) throws Exception;
}
