package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.AbstractNode;
import com.rongji.dfish.ui.HasText;

/**
 * 文件上传数据项,该类既是Widget又是数据对象
 * 
 * @author DFish Team
 *
 */
public class UploadItem extends AbstractNode<UploadItem> implements HasText<UploadItem> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7730216981053285569L;

	private String name;
	private Long size;
	private String url;
	private String thumbnail;
	private Boolean error;
	private String text;

	/**
	 * 显示的名称
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * 显示的名称
	 * @param name String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public UploadItem setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * 文件大小，单位字节
	 * @return long
	 */
	public Long getSize() {
		return size;
	}
	/**
	 * 文件大小，单位字节
	 * @param size long
	 * @return 本身，这样可以继续设置其他属性
	 */
	public UploadItem setSize(Long size) {
		this.size = size;
		return this;
	}

	/**
	 * 下载地址
	 * @return String
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 下载地址
	 * @param url String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public UploadItem setUrl(String url) {
		this.url = url;
		return this;
	}
	/**
	 * 缩略图地址
	 * @return String
	 */
	public String getThumbnail() {
		return thumbnail;
	}
	/**
	 * 缩略图地址
	 * @param thumbnail String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public UploadItem setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
		return this;
	}
	/**
	 * 如果上传过程出错，返回给JS引擎true
	 * @return  Boolean
	 */
	public Boolean getError() {
		return error;
	}
	/**
	 * 如果上传过程出错，返回给JS引擎true
	 * @param error Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public UploadItem setError(Boolean error) {
		this.error = error;
		return this;
	}
	/**
	 * 显示的文本
	 * @return String
	 */
	@Override
    public String getText() {
		return text;
	}
	/**
	 * 显示的文本
	 * @param text String
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public UploadItem setText(String text) {
		this.text = text;
		return this;
	}

	@Override
    public String getType() {
	    return null;
    }

}
