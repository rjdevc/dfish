package com.rongji.dfish.ui.form;

/**
 * 上传附件。
 * @author DFish Team
 * @param <T> 类型
 *
 */
public class UploadFile<T extends UploadFile<T>> extends AbstractUpload<T> {

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
	 * @param name
	 * @param label
	 */
	public UploadFile(String name,String label){
		this.name=name;
		this.label=label;
	}
	
	@Override
	public String getType() {
		return "upload/file";
	}
	@SuppressWarnings("unchecked")
	@Override
	public T setValue(Object value) {
		this.value=toString(value);
		return (T)this;
	}
}
