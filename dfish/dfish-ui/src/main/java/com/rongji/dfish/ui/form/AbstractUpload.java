package com.rongji.dfish.ui.form;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Iterator;
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
public abstract class AbstractUpload<T extends AbstractUpload<T>> extends AbstractFormElement<T, List<UploadItem>> implements Directional<T> {

	/**
	 * 方向:横向
	 */
	public static final String DIR_HORIZONTAL = "h";
	/**
	 * 方向:纵向
	 */
	public static final String DIR_VERTICAL = "v";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4779985030349561966L;
	protected String uploadsrc;
	protected String downloadsrc;
	protected String previewsrc;
	protected String removesrc;
	protected String filetypes;
	protected String sizelimit;
	protected Integer uploadlimit;
	protected List<ValueButton> valuebutton;
	protected List<UploadButton> uploadbutton;
	protected String dir;
	protected String scheme;
	protected UploadItem pub;

	/**
	 * 上传地址,最终返回结果会拼上scheme的参数值
	 * @return String
	 */
	public String getUploadsrc() {
		return joinScheme(this.uploadsrc);
	}

	/**
	 * 上传地址
	 * @param uploadsrc String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setUploadsrc(String uploadsrc) {
		this.uploadsrc = uploadsrc;
		return (T) this;
	}

	/**
	 * 下载地址,最终返回结果会拼上scheme的参数值
	 * @return String
	 */
	public String getDownloadsrc() {
		return joinScheme(this.downloadsrc);
	}

	/**
	 * 下载地址
	 * @param downloadsrc String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setDownloadsrc(String downloadsrc) {
		this.downloadsrc = downloadsrc;
		return (T) this;
	}

	/**
	 * 预览地址,最终返回结果会拼上scheme的参数值
	 * @return String
	 */
	public String getPreviewsrc() {
		return joinScheme(this.previewsrc);
	}

	/**
	 * 预览地址
	 * @param previewsrc String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setPreviewsrc(String previewsrc) {
		this.previewsrc = previewsrc;
		return (T) this;
	}

	/**
	 * 移除地址,最终返回结果会拼上scheme的参数值
	 * @return String
	 */
	public String getRemovesrc() {
		return joinScheme(this.removesrc);
	}

	/**
	 * 移除地址,默认无需设置该属性
	 * @param removesrc String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setRemovesrc(String removesrc) {
		this.removesrc = removesrc;
		return (T) this;
	}

	/**
	 * 允许的文件类型。例如只允许上传图片: "*.jpg;*.gif;*.png"
	 * @return String
	 */
	public String getFiletypes() {
		return filetypes;
	}

	/**
	 * 允许的文件类型。例如只允许上传图片: "*.jpg;*.gif;*.png"
	 * @param filetypes String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setFiletypes(String filetypes) {
		this.filetypes = filetypes;
		return (T) this;
	}

	/**
	 * 单个附件文件大小。如 "50M"。
	 * @return String
	 */
	public String getSizelimit() {
		return sizelimit;
	}

	/**
	 * 单个附件文件大小。如 "50M"。
	 * @param sizelimit String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setSizelimit(String sizelimit) {
		this.sizelimit = sizelimit;
		return (T) this;
	}

	/**
	 * 最大允许上传文件数
	 * @return Integer
	 */
	public Integer getUploadlimit() {
		return uploadlimit;
	}

	/**
	 * 最大允许上传文件数
	 * @param uploadlimit 最大允许上传文件数
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setUploadlimit(Integer uploadlimit) {
		this.uploadlimit = uploadlimit;
		return (T) this;
	}

	/**
	 * 附件项的"更多"选项 button 数组。点击附件项的"更多"生成一个 menu。
	 * @return List&lt;{@link ValueButton}&gt;
	 */
	public List<ValueButton> getValuebutton() {
		return valuebutton;
	}

	/**
	 * 附件项的"更多"选项 button 数组。点击附件项的"更多"生成一个 menu。
	 * @param valuebutton "更多"选项 button 数组
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setValuebutton(List<ValueButton> valuebutton) {
		this.valuebutton = valuebutton;
		return (T) this;
	}

	/**
	 * 添加附件项的"更多"选项按钮。点击附件项的"更多"生成一个 menu。
	 * @param button "更多"选项按钮
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T addValuebutton(ValueButton button) {
		if (this.valuebutton == null) {
			this.valuebutton = new ArrayList<>();
		}
		valuebutton.add(button);
		return (T) this;
	}

	/**
	 * 上传按钮
	 * @return List&lt;{@link UploadButton}&gt;
	 */
	public List<UploadButton> getUploadbutton() {
		return uploadbutton;
	}

	/**
	 * 上传按钮
	 * @param uploadbutton 上传按钮
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setUploadbutton(List<UploadButton> uploadbutton) {
		this.uploadbutton = uploadbutton;
		return (T) this;
	}

	/**
	 * 添加上传按钮
	 * @param button 上传按钮
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T addUploadbutton(UploadButton button) {
		if (this.uploadbutton == null) {
			this.uploadbutton = new ArrayList<>();
		}
		this.uploadbutton.add(button);
		return (T) this;
	}

	/**
	 * 上传地址。
	 * @return  String
	 * @see #getUploadsrc()
	 */
	@Deprecated
	@Transient
	public String getUpload_url() {
		return getUploadsrc();
	}
	/**
	 * 上传地址。
	 * @param upload_url 上传地址
	 * @return 本身，这样可以继续设置其他属性
	 * @see #setUploadsrc(String)
	 */
	@Deprecated
	public T setUpload_url(String upload_url) {
		return setUploadsrc(upload_url);
	}
	/**
	 * 下载地址。支持以 "javascript:" 的语句模式。支持 $xxx 变量
	 * @return down_url
	 * @see #getDown_url()
	 */
	@Deprecated
	@Transient
	public String getDown_url() {
		return getDownloadsrc();
	}
	/**
	 * 下载地址。支持以 "javascript:" 的语句模式。支持 $xxx 变量
	 * @param down_url 下载地址
	 * @return 本身，这样可以继续设置其他属性
	 * @see #setDownloadsrc(String)
	 */
	@Deprecated
    public T setDown_url(String down_url) {
		return setDownloadsrc(down_url);
	}

    /**
     * 文件预览地址。支持以 "javascript:" 的语句模式。支持 $xxx 变量
     * @return 文件预览地址
	 * @see #getPreviewsrc()
     */
    @Deprecated
	@Transient
    public String getPreview_url() {
        return getPreviewsrc();
    }

    /**
     * 文件预览地址。支持以 "javascript:" 的语句模式。支持 $xxx 变量
     * @param preview_url 文件预览地址
     * @return 本身，这样可以继续设置其他属性
	 * @see #setPreviewsrc(String)
     */
    @Deprecated
    public T setPreview_url(String preview_url) {
        return setPreviewsrc(preview_url);
    }

    /**
	 * 单个附件最大体积。如 "50M"。
	 * @return file_size_limit
	 * @see #getSizelimit()
	 */
    @Deprecated
	@Transient
	public String getFile_size_limit() {
		return getSizelimit();
	}
	/**
	 * 单个附件最大体积。如 "50M"。
	 * @param file_size_limit  单个附件最大体积。如 "50M"。
	 * @return 本身，这样可以继续设置其他属性
	 * @see #setSizelimit(String)
	 */
	@Deprecated
    public T setFile_size_limit(String file_size_limit) {
		return setSizelimit(file_size_limit);
	}
	/**
	 * 允许的文件类型。例如只允许上传图片: "*.jpg;*.gif;*.png"
	 * @return file_types
	 * @see #getFiletypes()
	 */
	@Deprecated
	@Transient
	public String getFile_types() {
		return getFiletypes();
	}
	/**
	 * 允许的文件类型。例如只允许上传图片: "*.jpg;*.gif;*.png"
	 * @param file_types 文件类型
	 * @return 本身，这样可以继续设置其他属性
	 * @see #setFiletypes(String)
	 */
	@Deprecated
    public T setFile_types(String file_types) {
		return setFiletypes(file_types);
	}
	/**
	 * 最多可上传数量。
	 * @return file_upload_limit
	 * @see #getUploadlimit()
	 */
	@Deprecated
	@Transient
	public Integer getFile_upload_limit() {
		return getUploadlimit();
	}
	/**
	 * 最大允许上传文件数
	 * @param file_upload_limit 最大允许上传文件数
	 * @return 本身，这样可以继续设置其他属性
	 * @see #setUploadlimit(Integer)
	 */
	@Deprecated
    public T setFile_upload_limit(Integer file_upload_limit) {
		return setUploadlimit(file_upload_limit);
	}
	/**
	 * 附件项的"更多"选项 button 数组。点击附件项的"更多"生成一个 menu。
	 * @return value_button
	 * @see #getValuebutton()
	 */
	@Deprecated
	@Transient
	public List<ValueButton> getValue_button() {
		return getValuebutton();
	}
	/**
	 * 附件项的"更多"选项 button 数组。点击附件项的"更多"生成一个 menu。
	 * @param value_button "更多"选项 button 数组
	 * @return 本身，这样可以继续设置其他属性
	 * @see #addValuebutton(ValueButton)
	 */
	@Deprecated
    public T addValue_button(ValueButton value_button) {
		return addValuebutton(value_button);
	}
	/**
	 * 上传按钮的数组。
	 * @return upload_button
	 * @see #getUploadbutton()
	 */
	@Deprecated
	@Transient
	public List<UploadButton> getUpload_button() {
		return getUploadbutton();
	}
	/**
	 * 增加上传按钮
	 * @param upload_button 上传按钮
	 * @return 本身，这样可以继续设置其他属性
	 * @see #addUploadbutton(UploadButton)
	 */
	@Deprecated
	public T addUpload_button(UploadButton upload_button) {
		return addUploadbutton(upload_button);
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

	/**
	 * 附件排列方向。可选值: h(横向,默认), v(纵向)
	 * @return String
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * 附件排列方向。可选值: h(横向,默认), v(纵向)
	 * @param dir String
	 * @return 本身，这样可以继续设置其他属性
	 */
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

	/**
	 * 附件上传方案
	 * @return String
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * 附件上传方案
	 * @param scheme String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setScheme(String scheme) {
		this.scheme = scheme;
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

	@Override
	public T setValue(Object value) {
		if (value == null) {
			this.value = null;
			return (T) this;
		}
		if (!(value instanceof List)) {
			// 具体项如果不是UploadItem如何判断不清楚怎么写
			throw new IllegalArgumentException("The value must be List<UploadItem>.");
		}
		return setValue((List<UploadItem>) value);
	}

	/**
	 * 设置表单的元素的值
	 * <p>在HTML协议中提交表单元素的时候，以键值对的方式提交。
	 * 一般情况下。其中值就是value. 某些多选项的元素可能这些键值对会以复数形式出现。
	 * 比如select 多行形式时。</p>
	 * <p>v2.3以后改方法返回自身以便可以继续对这个表单操作</p>
	 * @param value {@link List}&lt;{@link UploadItem}&gt;
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setValue(List<UploadItem> value) {
		this.value = value;
		return (T) this;
	}

}
