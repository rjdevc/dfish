package com.rongji.dfish.ui.form;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.Directional;

/**
 * AbstractUpload为抽象的上传组件，上传组件在dfish3中默认有两种，提供文件上传和图片上传
 * 图片上传同时还有预览功能。
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
@SuppressWarnings("unchecked")
public abstract class AbstractUpload<T extends AbstractUpload<T>> extends AbstractFormElement<T,String> implements Directional<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4779985030349561966L;
	protected String upload_url;
	protected String down_url;
	//50M
	protected String file_size_limit ;
	protected String file_types ;
	protected Integer file_upload_limit ;
	protected String value_width;
	protected List<ValueButton> value_button;
	protected List<UploadButton> upload_button;
//	protected String remove_url;
	
	protected UploadItem pub;
	protected String dir;
	protected String scheme;

	/**
	 * 上传地址。
	 * @return  upload_url
	 */
	public String getUpload_url() {
		return upload_url;
	}
	/**
	 * 上传地址。
	 * @param upload_url 上传地址
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setUpload_url(String upload_url) {
		this.upload_url = joinScheme(upload_url);
		return (T) this;
	}
	/**
	 * 下载地址。支持以 "javascript:" 的语句模式。支持 $xxx 变量
	 * @return down_url
	 */
	public String getDown_url() {
		return down_url;
	}
	/**
	 * 下载地址。支持以 "javascript:" 的语句模式。支持 $xxx 变量
	 * @param down_url 下载地址
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T setDown_url(String down_url) {
		this.down_url = joinScheme(down_url);
		return (T) this;
	}
	/**
	 * 单个附件最大体积。如 "50M"。
	 * @return file_size_limit
	 */
	public String getFile_size_limit() {
		return file_size_limit;
	}
	/**
	 * 单个附件最大体积。如 "50M"。
	 * @param file_size_limit  单个附件最大体积。如 "50M"。
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T setFile_size_limit(String file_size_limit) {
		this.file_size_limit = file_size_limit;
		return (T) this;
	}
	/**
	 * 允许的文件类型。例如只允许上传图片: "*.jpg;*.gif;*.png"
	 * @return file_types
	 */
	public String getFile_types() {
		return file_types;
	}
	/**
	 * 允许的文件类型。例如只允许上传图片: "*.jpg;*.gif;*.png"
	 * @param file_types 文件类型
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T setFile_types(String file_types) {
		this.file_types = file_types;
		return (T) this;
	}
	/**
	 * 最多可上传数量。
	 * @return file_upload_limit
	 */
	public Integer getFile_upload_limit() {
		return file_upload_limit;
	}
	/**
	 * 最大允许上传文件数
	 * @param file_upload_limit 最大允许上传文件数
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T setFile_upload_limit(Integer file_upload_limit) {
		this.file_upload_limit = file_upload_limit;
		return (T) this;
	}
	/**
	 * 附件项的宽度。
	 * @return value_width
	 */
	public String getValue_width() {
		return value_width;
	}
	/**
	 * 附件项的宽度。 
	 * @param value_width 附件项的宽度
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T setValue_width(String value_width) {
		this.value_width = value_width;
		return (T) this;
	}
	/**
	 * 附件项的"更多"选项 button 数组。点击附件项的"更多"生成一个 menu。
	 * @return value_button
	 */
	public List<ValueButton> getValue_button() {
		return value_button;
	}
	/**
	 * 附件项的"更多"选项 button 数组。点击附件项的"更多"生成一个 menu。
	 * @param value_button "更多"选项 button 数组
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T addValue_button(ValueButton value_button) {
		if(this.value_button==null){
			this.value_button=new ArrayList<ValueButton>();
		}
		this.value_button.add(value_button);
		return (T) this;
	}
	/**
	 * 上传按钮的数组。
	 * @return upload_button
	 */
	
	public List<UploadButton> getUpload_button() {
		return upload_button;
	}
	/**
	 * 增加上传按钮
	 * @param upload_button 上传按钮
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T addUpload_button(UploadButton upload_button) {
		if(this.upload_button==null){
			this.upload_button=new ArrayList<>();
		}
		this.upload_button.add(upload_button);
		return (T) this;
	}
	
//	/**
//	 * 移除附件的地址
//	 * @return String
//	 * @deprecated 删除操作在保存时触发,界面点击删除是伪删除,这里的值无论怎么设置都是空值
//	 */
//	@Deprecated
//	public String getRemove_url() {
//		return remove_url;
//	}
	
//	/**
//	 * 移除附件的地址
//	 * @param remove_url String
//	 * @return 本身，这样可以继续设置其他属性
//	 * @deprecated 删除操作在保存时触发,界面点击删除是伪删除,这里的值无论怎么设置都是空值
//	 */
//	@Deprecated
//    public T setRemove_url(String remove_url) {
//    	// 不做任何操作
//		this.remove_url = remove_url;
//		return (T) this;
//	}
	
	/**
	 * 取得默认值
	 * @return UploadItem
	 */
	public UploadItem getPub() {
		if (pub == null) {
			pub = new UploadItem();
		}
		return pub;
	}
	
	/**
	 * 设置默认值
	 * @param pub UploadItem
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T setPub(UploadItem pub) {
		this.pub = pub;
		return (T) this;
	}
    
	public String getDir() {
		return dir;
	}
	
	public T setDir(String dir) {
		this.dir = dir;
		return (T)this;
	}
	
	@Deprecated
	public T setTip(Boolean tip) {
		return (T) this;
	}
	
	@Deprecated
	public T setTip(String tip) {
		return (T) this;
	}

	protected String joinScheme(String url) {
		if (Utils.isEmpty(url)) {
			return url;
		}
		if (Utils.notEmpty(scheme)) {
			url += (url.indexOf("?") >= 0) ? "&" : "?";
			url += "scheme=" + scheme;
		}
		return url;
	}

}
