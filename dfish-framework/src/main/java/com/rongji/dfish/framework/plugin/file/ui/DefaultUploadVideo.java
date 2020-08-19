package com.rongji.dfish.framework.plugin.file.ui;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingManager;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingScheme;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.ui.form.UploadButton;
import com.rongji.dfish.ui.form.UploadImage;
import com.rongji.dfish.ui.form.UploadItem;
import com.rongji.dfish.ui.form.UploadVideo;

import java.util.List;

/**
 * 默认视频上传组件
 * 
 * @author DFish Team
 *
 */
public class DefaultUploadVideo extends UploadVideo<DefaultUploadVideo> {


	private static final long serialVersionUID = 7822972212340152961L;

	public DefaultUploadVideo(String name, String label) {
		this(name, label, null);
	}

	public DefaultUploadVideo(String name, String label, List<UploadItem> value) {
		this(name, label, value, null);
	}

	public DefaultUploadVideo(String name, String label, List<UploadItem> value, String scheme) {
		super(name, label);
		this.setScheme(scheme);

		this.setUploadsrc("file/uploadVideo");
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
		fileTypes = Utils.isEmpty(fileTypes) ? (Utils.isEmpty(fileService.getImageTypes()) ? defaultFileTypes() : fileService.getVideoTypes()) : fileTypes;
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
		return "*.mp4";
	}

}
