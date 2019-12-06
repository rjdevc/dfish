package com.rongji.dfish.framework.plugin.file.dto;

import com.rongji.dfish.framework.dto.QueryParam;

/**
 * 附件查询参数
 *
 * @author lamontYu
 * @version 1.1 继承通用查询参数基类
 * @create 2018-08-03 before
 */
public class FileQueryParam extends QueryParam<FileQueryParam> {

    private static final long serialVersionUID = 321893858528694640L;
    private String fileLink;
    private String fileKey;

    public String getFileLink() {
        return fileLink;
    }

    public FileQueryParam setFileLink(String fileLink) {
        this.fileLink = fileLink;
        return this;
    }

    public String getFileKey() {
        return fileKey;
    }

    public FileQueryParam setFileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

}
