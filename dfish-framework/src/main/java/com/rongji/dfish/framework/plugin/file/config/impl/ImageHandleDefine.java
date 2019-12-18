package com.rongji.dfish.framework.plugin.file.config.impl;

import com.rongji.dfish.base.img.ImageHandler;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.plugin.file.config.FileHandleDefine;

/**
 * 图片处理定义
 *
 * @author lamontYu
 * @date 2019-08-07
 * @since 3.2
 */
public class ImageHandleDefine extends ImageHandler implements FileHandleDefine {
    public ImageHandleDefine() {
    }

    public ImageHandleDefine(int width, int height) {
        super(width, height);
    }

    public ImageHandleDefine(int width, int height, String way) {
        super(width, height, way);
    }

    public ImageHandleDefine(int width, int height, String way, String alias) {
        super(width, height, way, alias);
    }

    public ImageHandleDefine(int width, int height, String way, String alias, boolean lazy) {
        super(width, height, way, alias, lazy);
    }

    @Override
    public String getAlias() {
        if (Utils.isEmpty(super.getAlias())) {
            super.setAlias(getWay() + "x" + getHeight());
        }
        return super.getAlias();
    }
}
