package com.rongji.dfish.framework.plugin.file.config;

/**
 * 文件处理定义
 * 初步规划为对上传的文件进行格式转换等功能（因默认文件如何转换未定方案，这里接口未定出来）
 * @author lamontYu
 * @create 2019-08-07
 * @since 3.2
 */
public interface FileHandleDefine {

    /**
     * 方案别名
     * @return String
     */
    String getAlias();

}
