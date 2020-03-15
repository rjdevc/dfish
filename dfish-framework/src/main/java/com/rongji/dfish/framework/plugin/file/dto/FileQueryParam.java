package com.rongji.dfish.framework.plugin.file.dto;

import com.rongji.dfish.framework.dto.QueryParam;

/**
 * 附件查询参数
 *
 * @author lamontYu
 * @version 1.1 继承通用查询参数基类
 * @date 2018-08-03 before
 */
public class FileQueryParam extends QueryParam<FileQueryParam> {

    private static final long serialVersionUID = 321893858528694640L;
    private String fileLink;
    private String fileKey;

    /**
     * 链接名
     * @return String
     */
    public String getFileLink() {
        return fileLink;
    }

    /**
     * 链接名
     * @param fileLink
     * @return 本身，这样可以继续设置其他属性
     */
    public FileQueryParam setFileLink(String fileLink) {
        this.fileLink = fileLink;
        return this;
    }

    /**
     * 关联数据
     * @return
     */
    public String getFileKey() {
        return fileKey;
    }

    /**
     * 关联数据
     * @param fileKey
     * @return 本身，这样可以继续设置其他属性
     */
    public FileQueryParam setFileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

}
