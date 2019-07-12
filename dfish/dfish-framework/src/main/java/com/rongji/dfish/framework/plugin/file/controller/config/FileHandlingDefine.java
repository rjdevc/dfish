package com.rongji.dfish.framework.plugin.file.controller.config;

/**
 * 文件处理定义
 * 初步规划为对上传的文件进行格式转换等功能（因默认文件如何转换未定方案，这里接口未定出来）
 * @author YuLM
 */
public class FileHandlingDefine {

    /**
     * 方案别名
     */
    private String alias;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
