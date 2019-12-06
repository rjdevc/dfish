package com.rongji.dfish.framework.plugin.file.controller.config;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件处理管理类,主要管理注册的文件处理方案和文件处理定义
 * @author lamontYu
 * @create 2019-08-07
 * @since 3.2
 */
@Component
public class FileHandlingManager {

    @Autowired(required = false)
    private List<FileHandlingDefine> fileHandlingDefines;
    @Autowired(required = false)
    private List<FileHandlingScheme> fileHandlingSchemes;

    private Map<String, FileHandlingDefine> fileHandlingDefineMap = new HashMap<>();
    private Map<String, FileHandlingScheme> fileHandlingSchemeMap = new HashMap<>();

    @PostConstruct
    private void init() {
        if (Utils.notEmpty(fileHandlingDefines)) {
            for (FileHandlingDefine handlingDefine : fileHandlingDefines) {
                registerDefine(handlingDefine);
            }
        }
        if (Utils.notEmpty(fileHandlingSchemes)) {
            for (FileHandlingScheme handlingScheme : fileHandlingSchemes) {
                registerScheme(handlingScheme);
            }
        }
    }

    private void registerDefine(FileHandlingDefine handlingDefine) {
        if (handlingDefine == null) {
            return;
        }
        if (Utils.isEmpty(handlingDefine.getAlias())) {
            LogUtil.warn("The alias is empty.[" + handlingDefine.getClass().getName() + "]");
        }
        FileHandlingDefine old = fileHandlingDefineMap.put(handlingDefine.getAlias(), handlingDefine);
        if (old != null) {
            LogUtil.warn("The system exists same name of the FileHandlingDefine.[" + handlingDefine.getAlias() + "]");
        }
    }

    private void registerScheme(FileHandlingScheme handlingScheme) {
        if (handlingScheme == null) {
            return;
        }
        if (Utils.isEmpty(handlingScheme.getName())) {
            LogUtil.warn("The name is empty.[" + handlingScheme.getClass().getName() + "]");
        }
        FileHandlingScheme old = fileHandlingSchemeMap.put(handlingScheme.getName(), handlingScheme);
        if (old != null) {
            LogUtil.warn("The system exists same name of the FileHandlingScheme.[" + handlingScheme.getName() + "]");
        }
    }

    /**
     * 根据名称获取文件处理方案对象
     * @param scheme 方案名称
     * @return FileHandlingDefine
     */
    public FileHandlingScheme getScheme(String scheme) {
        if (Utils.isEmpty(scheme)) {
            return null;
        }
        return fileHandlingSchemeMap.get(scheme);
    }

    /**
     * 根据名称获取文件处理定义对象
     * @param defineAlias 文件处理定义名称
     * @return FileHandlingDefine
     */
    public FileHandlingDefine getDefine(String defineAlias) {
        if (Utils.isEmpty(defineAlias)) {
            return null;
        }
        return fileHandlingDefineMap.get(defineAlias);
    }

}
