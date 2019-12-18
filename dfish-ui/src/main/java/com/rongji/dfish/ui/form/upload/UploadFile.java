package com.rongji.dfish.ui.form.upload;

/**
 * 上传附件。
 * @author DFish Team
 * @date 2018-08-03 before
 */
public class UploadFile extends AbstractUpload<UploadFile> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5921590784801725804L;
	/**
	 *
	 */
	public UploadFile(){

	}
	/**
	 * 构造函数
	 * @param name String 提交属性的名字
	 * @param label String 显示的标题
	 */
	public UploadFile(String name, String label){
		this.name=name;
		setLabel(label);
	}
	
	@Override
	public String getType() {
		return "upload/file";
	}

}
