package com.rongji.dfish.ui.form;

import java.util.List;

/**
 * 上传附件。
 *
 * @author DFish Team
 * @date 2018-08-03 before
 */
public class FileUpload extends AbstractUpload<FileUpload> {

    private static final long serialVersionUID = 5921590784801725804L;

	/**
     * 构造函数
     *
     * @param name  String 提交属性的名字
     * @param label String 显示的标题
     */
    public FileUpload(String name, String label, List<UploadItem> value) {
        this.name = name;
        setLabel(label);
        this.setValue(value);
    }
    /**
     * 构造函数
     *
     * @param name  String 提交属性的名字
     * @param label String 显示的标题
     */
    public FileUpload(String name, Label label, List<UploadItem> value) {
        this.name = name;
        setLabel(label);
        this.setValue(value);
    }
}
