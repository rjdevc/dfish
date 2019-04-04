package com.rongji.dfish.framework.plugin.file.ui;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
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
		FileService fileService = (FileService) FrameworkHelper.getBean("fileService");
		this.setFiletypes(Utils.isEmpty(fileService.getImageTypes()) ? defaultFileTypes() : fileService.getImageTypes());
		this.setSizelimit(fileService.getSizeLimit());
		this.addUploadbutton(new UploadButton("+"));
		
		this.getPub().setThumbnail(joinScheme("file/thumbnail?fileId=$id"));
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
