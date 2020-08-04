package com.rongji.dfish.ui.form;

import java.io.Serializable;

/**
 * 文件上传数据项,该类既是Widget又是数据对象
 * 
 * @author DFish Team
 *
 */
public class UploadItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7730216981053285569L;

	private String id;
	private String name;
	private String extension;
	private Long size;
	private String url;
	private String thumbnail;
	private Error error;
	private String text;
	private String width;
	private String height;
	private Boolean escape;

	/**
	 * 附件编号
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * 附件编号
	 * @param id String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public UploadItem setId(String id) {
		this.id = id;
		return this;
	}

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
	 * 附件扩展名
	 * @return String 附件扩展名
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * 附件扩展名
	 * @param extension String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public UploadItem setExtension(String extension) {
		this.extension = extension;
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
	 * 如果上传过程出错，返回给JS引擎错误信息
	 * @return  Boolean
	 */
	public Error getError() {
		return error;
	}
	/**
	 * 如果上传过程出错，返回给JS引擎错误信息
	 * @param error Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public UploadItem setError(Error error) {
		this.error = error;
		return this;
	}
	/**
	 * 显示的文本
	 * @return String
	 */
    public String getText() {
		return text;
	}
	/**
	 * 显示的文本
	 * @param text String
	 * @return 本身，这样可以继续设置其他属性 
	 */
    public UploadItem setText(String text) {
		this.text = text;
		return this;
	}

	/**
	 * 宽度
	 * @return String
	 */
    public String getWidth() {
		return width;
	}

	/**
	 * 宽度
	 * @param width String
	 * @return 本身，这样可以继续设置其他属性
	 */
    public UploadItem setWidth(String width) {
		this.width = width;
		return this;
	}

	/**
	 * 高度
	 * @return String
	 */
    public String getHeight() {
		return height;
	}

	/**
	 * 高度
	 * @param height String
	 * @return 本身，这样可以继续设置其他属性
	 */
    public UploadItem setHeight(String height) {
		this.height = height;
		return this;
	}

	/**
	 * 是否转义
	 * @return Boolean
	 */
	public Boolean getEscape() {
		return escape;
	}

	/**
	 * 是否转义
	 * @param escape Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public UploadItem setEscape(Boolean escape) {
		this.escape = escape;
		return this;
	}

	/**
	 * 错误对象信息
	 * @author lamontYu
	 */
    public static class Error {
		private String text;

		public Error(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

		public Error setText(String text) {
			this.text = text;
			return this;
		}
	}

}
