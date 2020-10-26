package com.rongji.dfish.misc.rjd;

import java.io.InputStream;

/**
 * 读取RJD格式的回调。可以取得文件路径，和文件的输入流
 */
@FunctionalInterface
public interface RJDCallback {
    /**
     * 执行动作
     * @param path 文件路径
     * @param is 文件输入流
     */
    void execute(String path, InputStream is);
}
