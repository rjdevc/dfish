package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.Valignable;

/**
 * 上传图片。
 * @author DFish Team
 * @param <T> 类型
 *
 */
@SuppressWarnings("unchecked")
public class UploadImage<T extends UploadImage<T>> extends AbstractUpload<T> implements Alignable<T>,Valignable<T>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2689106571559435242L;
	private Boolean transparent;
	private String placeholder;
	private String thumbnail_url;
	private String align;
	private String valign;
	
	/**
	 * 
	 */
	public UploadImage(){
		
	}
	/**
	 * @param name
	 * @param label
	 */
	public UploadImage(String name,String label){
		this.name=name;
		this.label=label;
	}
	@Override
	public String getType() {
		return "upload/image";
	}

	/**
	 * 设置为true，表单将成为无边框无背景的状态。
	 * @return  transparent
	 */
	public Boolean getTransparent() {
		return transparent;
	}
	/**
	 * 设置为true，表单将成为无边框无背景的状态。
	 * @param transparent
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T setTransparent(Boolean transparent) {
		this.transparent = transparent;
		return (T) this;
	}
	/**
	 * 占位符 。当表单没有值时显示的提示文本。
	 * @return  placeholder 
	 */
	public String getPlaceholder() {
		return placeholder;
	}
	/**
	 * 占位符。当表单没有值时显示的提示文本。
	 * @param placeholder
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
		return (T) this;
	}
	
	/**
	 * 缩略图地址
	 * @return String
	 */
	public String getThumbnail_url() {
		return thumbnail_url;
	}
	
	/**
	 * 缩略图地址
	 * @param thumbnail_url String
	 * @return this
	 */
	public T setThumbnail_url(String thumbnail_url) {
		this.thumbnail_url = joinScheme(thumbnail_url);
		return (T) this;
	}
	
	public String getAlign() {
		return align;
	}
	
    public T setAlign(String align) {
		this.align = align;
		return (T) this;
	}
	
	public String getValign() {
		return valign;
	}
	

    public T setValign(String valign) {
		this.valign = valign;
		return (T) this;
	}
	@Override
	public T setValue(Object value) {
		this.value=toString(value);
		return (T)this;
	}
}
