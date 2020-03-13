package com.rongji.dfish.misc.docpreview;

/**
 * 图片URL转换
 */
public interface ImageURLConverter {
    /**
     * 文档解析的时候，图片会存在到一个相对路径下。
     * 但如果支持下载的话，可能要么指定这个图片文件夹的相对路径。
     * 要么提供一个下载地址。
     * @param relativeURL 相对路径
     * @return 下载路径
     */
    String getDownloadURL(String relativeURL);
}
