package com.rongji.dfish.framework.plugin.file.ui;

import java.util.Iterator;
import java.util.List;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.misc.util.JsonUtil;
import com.rongji.dfish.ui.form.UploadButton;
import com.rongji.dfish.ui.form.UploadImage;
import com.rongji.dfish.ui.form.UploadItem;

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

		this.setUpload_url("file/uploadImage");
		this.setDown_url("file/download?fileId=$id");
//		this.setRemove_url("file/removeFile?fileId=$id");
		this.setThumbnail_url("file/thumbnail?fileId=$id");
		this.setPreview_url("file/preview?fileId=$id");
		FileService fileService = (FileService) FrameworkHelper.getBean("fileService");
		this.setFile_types(fileService.getImageTypes());
		this.setFile_size_limit(fileService.getSizeLimit());
		this.addUpload_button(new UploadButton("+"));
		
		this.getPub().setOn(UploadItem.EVENT_CLICK, "this.preview();");
		this.setValue(value);
	}
	
	/**
	 * 默认文件类型,
	 * @return
	 */
	protected String defaultFileTypes() {
		return "*.jpg;*.gif;*.png";
	}

	/**
     *
     * @param itemList
     * @return
     * @see #setValue(List)
     */
	@Deprecated
	public DefaultUploadImage setUploadItems(List<UploadItem> itemList) {
		return setValue(itemList);
	}
	
	public DefaultUploadImage setValue(Object value) {
		return super.setValue(value);
	}
	
	public DefaultUploadImage setValue(List<UploadItem> value) {
		if (Utils.notEmpty(value)) {
			for (Iterator<UploadItem> iter=value.iterator(); iter.hasNext();) {
				UploadItem item = iter.next();
				if (item == null) {
					iter.remove();
					continue;
				}
			}
			if (value.isEmpty()) {
				super.setValue(null);
			} else {
//				// 有值时,默认不删除图片
//				this.setRemove_url(null);
				super.setValue(JsonUtil.toJson(value));
			}
		} else {
			super.setValue(null);
		}
		return this;
	}
	
}
