package com.rongji.dfish.framework.plugin.file.ui;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingManager;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingScheme;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.ui.form.UploadButton;
import com.rongji.dfish.ui.form.UploadImage;
import com.rongji.dfish.ui.form.UploadItem;

import java.util.List;

/**
 * 默认图片上传组件
 * 
 * @author DFish Team
 *
 */
public class DefaultUploadImage extends UploadImage<DefaultUploadImage> {

    private static final long serialVersionUID = -3371412187081962648L;

	public DefaultUploadImage(String name, String label) {
		this(name, label, null);
	}
	
	public DefaultUploadImage(String name, String label, List<UploadItem> value) {
		this(name, label, value, null);
	}

	public DefaultUploadImage(String name, String label, List<UploadItem> value, String scheme) {
		super(name, label);
		this.setScheme(scheme);

		this.setUploadsrc("file/uploadImage");
		this.setDownloadsrc("file/download?fileId=$id");
		this.setPreviewsrc("file/preview?fileId=$id");
		this.setThumbnailsrc("file/thumbnail?fileId=$id");

		FileHandlingManager fileHandlingManager = FrameworkHelper.getBean(FileHandlingManager.class);
		FileHandlingScheme handlingScheme = fileHandlingManager.getScheme(scheme);
		String fileTypes = null;
		String sizeLimit = null;
		if (handlingScheme != null) {
			fileTypes = handlingScheme.getHandlingTypes();
			sizeLimit = handlingScheme.getSizeLimit();
		}
		FileService fileService = FrameworkHelper.getBean(FileService.class);
		fileTypes = Utils.isEmpty(fileTypes) ? (Utils.isEmpty(fileService.getImageTypes()) ? defaultFileTypes() : fileService.getImageTypes()) : fileTypes;
		sizeLimit = Utils.isEmpty(sizeLimit) ? fileService.getSizeLimit() : sizeLimit;
		this.setFiletypes(fileTypes);
		this.setMaxfilesize(sizeLimit);

		this.addUploadbutton(new UploadButton("+"));
		
		this.setValue(value);
	}
	
	/**
	 * 默认文件类型,
	 * @return
	 */
	protected String defaultFileTypes() {
		return "*.jpg;*.gif;*.png";
	}

}
