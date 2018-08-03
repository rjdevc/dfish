package com.rongji.dfish.framework.plugin.file.ui;

import java.util.Iterator;
import java.util.List;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.misc.util.JsonUtil;
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

	@Deprecated
	public DefaultUploadFile() {
		this(null, null);
	}
	
	@Deprecated
	public DefaultUploadFile(String name, String label) {
//		this.setName(name);
//		this.setLabel(label);
//		
//		this.setUpload_url("file/uploadFile");
//		this.setDown_url("file/downloadFile?fileId=$id");
//		this.setRemove_url("file/removeFile?fileId=$id");
//		this.setFile_types(FileService.getTypesFile());
//		String sizeLimit = FileService.getSizeLimit();
//		this.setFile_size_limit(FileService.getSizeLimit());
//		this.addUpload_button(new UploadButton("本地上传(最大" + sizeLimit + ")").setIcon(".w-upload-icon-local").setCls("linkbtn"));
//		this.addValue_button(new ValueButton("下载").setOn(ValueButton.EVENT_CLICK, "$.download('file/downloadFile?fileId='+$id);"));
		this(name, label, null);
	}
	
	public DefaultUploadFile(String name, String label, List<UploadItem> value) {
		this.setName(name);
		this.setLabel(label);
		
		this.setUpload_url("file/uploadFile");
		this.setDown_url("file/downloadFile?fileId=$id");
		this.setRemove_url("file/removeFile?fileId=$id");
		FileService fileService = (FileService) FrameworkHelper.getBean("fileService");
		this.setFile_types(fileService.getTypesFile());
		String sizeLimit = fileService.getSizeLimit();
		this.setFile_size_limit(sizeLimit);
		this.addUpload_button(new UploadButton("本地上传(最大" + sizeLimit + ")").setIcon(".w-upload-icon-local").setCls("linkbtn"));
//		this.addValue_button(new ValueButton("下载").setOn(ValueButton.EVENT_CLICK, "$.download('file/downloadFile?fileId='+$id);"));
		this.setValue(value);
	}
	
	@Override
    public DefaultUploadFile setFile_size_limit(String file_size_limit) {
		if (Utils.notEmpty(upload_button)) {
			// 需要重新修改上传按钮大小名称
			UploadButton uploadButton = upload_button.get(0);
			if (uploadButton != null) {
				uploadButton.setText("本地上传(最大" + file_size_limit + ")");
			}
		}
	    return super.setFile_size_limit(file_size_limit);
    }

	@Deprecated
	public DefaultUploadFile setUploadItems(List<UploadItem> itemList) {
		return setValue(itemList);
	}
	
	public DefaultUploadFile setValue(Object value) {
		return super.setValue(value);
	}
	
	public DefaultUploadFile setValue(List<UploadItem> value) {
		if (Utils.notEmpty(value)) {
			for (Iterator<UploadItem> iter=value.iterator(); iter.hasNext();) {
				UploadItem item = iter.next();
				if (item == null) {
					iter.remove();
				}
			}
			if (value.isEmpty()) {
				super.setValue(null);
			} else {
				// 有值时,默认不删除附件
				this.setRemove_url(null);
				super.setValue(JsonUtil.toJson(value));
			}
		} else {
			super.setValue(null);
		}
		return this;
	}
	
}
