package com.rongji.dfish.framework.plugin.file.config;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件处理管理类,主要管理注册的文件处理方案和文件处理定义
 * @author lamontYu
 * @since DFish5.0
 */
public class FileHandleManager {
    @Autowired(required = false)
    private List<FileHandleScheme> fileHandleSchemes;

    private Map<String, FileHandleScheme> fileHandleSchemeMap = new HashMap<>();

    @PostConstruct
    private void init() {
        if (Utils.notEmpty(fileHandleSchemes)) {
            for (FileHandleScheme handlingScheme : fileHandleSchemes) {
                registerScheme(handlingScheme);
            }
        }
    }

    private void registerScheme(FileHandleScheme handlingScheme) {
        if (handlingScheme == null) {
            return;
        }
        if (Utils.isEmpty(handlingScheme.getName())) {
            LogUtil.warn("The name is empty.[" + handlingScheme.getClass().getName() + "]");
        }
        FileHandleScheme old = fileHandleSchemeMap.put(handlingScheme.getName(), handlingScheme);
        if (old != null) {
            LogUtil.warn("The system exists same name of the FileHandlingScheme.[" + handlingScheme.getName() + "]");
        }
    }

    /**
     * 根据名称获取文件处理方案对象
     * @param scheme 方案名称
     * @param <T> 方案对象泛型
     * @return FileHandlingDefine
     */
    public <T> T getScheme(String scheme) {
        if (Utils.isEmpty(scheme)) {
            return null;
        }
        return (T) fileHandleSchemeMap.get(scheme);
    }

}
