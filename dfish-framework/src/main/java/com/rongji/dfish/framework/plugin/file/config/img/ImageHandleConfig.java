package com.rongji.dfish.framework.plugin.file.config.img;

import com.rongji.dfish.base.img.ImageProcessConfig;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.plugin.file.config.FileHandleConfig;

/**
 * 图片处理定义
 *
 * @author lamontYu
 * @date 2019-08-07
 * @since 5.0
 */
public class ImageHandleConfig extends ImageProcessConfig implements FileHandleConfig {
    /**
     * 构造函数
     */
    public ImageHandleConfig() {
    }

    /**
     * 构造函数
     * @param width
     * @param height
     */
    public ImageHandleConfig(int width, int height) {
        super(width, height);
    }

    /**
     * 构造函数
     * @param width
     * @param height
     * @param way
     */
    public ImageHandleConfig(int width, int height, String way) {
        super(width, height, way);
    }

    /**
     * 构造函数
     * @param width
     * @param height
     * @param way
     * @param alias
     */
    public ImageHandleConfig(int width, int height, String way, String alias) {
        super(width, height, way, alias);
    }

    /**
     * 构造函数
     * @param width
     * @param height
     * @param way
     * @param alias
     * @param lazy
     */
    public ImageHandleConfig(int width, int height, String way, String alias, boolean lazy) {
        super(width, height, way, alias, lazy);
    }

    @Override
    public String getAlias() {
        if (Utils.isEmpty(super.getAlias())) {
            super.setAlias(getWidth() + "x" + getHeight());
        }
        return super.getAlias();
    }

}
