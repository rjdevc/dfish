package com.rongji.dfish.framework.plugin.file.controller.config;

import java.util.List;

/**
 * 图片缩放支持的模块
 */
public class FileHandlingScheme {

    private String name;

    private List<String> defines;

    private String handlingTypes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDefines() {
        return defines;
    }

    public void setDefines(List<String> defines) {
        this.defines = defines;
    }

    public String getHandlingTypes() {
        return handlingTypes;
    }

    public void setHandlingTypes(String handlingTypes) {
        this.handlingTypes = handlingTypes;
    }
}
