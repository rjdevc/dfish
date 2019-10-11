package com.rongji.dfish.framework.plugin.file.ui;

import java.util.List;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingManager;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingScheme;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.ui.form.UploadButton;
import com.rongji.dfish.ui.form.UploadFile;
import com.rongji.dfish.ui.form.UploadItem;

/**
 * 默认附件上传组件
 * 
 * @author DFish Team
 *
 */
public class DefaultUploadFile extends UploadFile<DefaultUploadFile> {

    private static final long serialVersionUID = 7489001330729080679L;

	public DefaultUploadFile(String name, String label) {
		this(name, label, null);
	}
	
	public DefaultUploadFile(String name, String label, List<UploadItem> value) {
		this(name, label, value, null);
	}

	public DefaultUploadFile(String name, String label, List<UploadItem> value, String scheme) {
		super(name, label);
		this.setScheme(scheme);
		this.setValue(value);

		this.setUploadsrc("file/uploadFile");
		this.setDownloadsrc("file/download?fileId=$id");
		this.setPreviewsrc("file/preview?fileId=$id");

		FileHandlingManager fileHandlingManager = FrameworkHelper.getBean(FileHandlingManager.class);
		FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);
		String fileTypes = null;
		String sizeLimit = null;
		if (handlingScheme != null) {
			fileTypes = handlingScheme.getHandlingTypes();
			sizeLimit = handlingScheme.getSizeLimit();
		}
		FileService fileService = FrameworkHelper.getBean(FileService.class);
		fileTypes = Utils.isEmpty(fileTypes) ? fileService.getFileTypes() : fileTypes;
		sizeLimit = Utils.isEmpty(sizeLimit) ? fileService.getSizeLimit() : sizeLimit;
		this.setFiletypes(fileTypes);
		this.setMaxfilesize(sizeLimit);

		this.addUploadbutton(new UploadButton("本地上传" + (Utils.notEmpty(sizeLimit) ? "(最大" + sizeLimit + ")" : "")).setIcon(".w-upload-icon-local"));
//		this.addValuebutton(new ValueButton("下载").setOn(ValueButton.EVENT_CLICK, "$.download('file/downloadFile?fileId='+$id);"));
	}
	
	@Override
    public DefaultUploadFile setMaxfilesize(String maxfilesize) {
		if (Utils.notEmpty(getUploadbutton())) {
			// 需要重新修改上传按钮大小名称
			UploadButton uploadButton = getUploadbutton().get(0);
			if (uploadButton != null) {
				uploadButton.setText("本地上传(最大" + maxfilesize + ")");
			}
		}
	    return super.setMaxfilesize(maxfilesize);
    }

}