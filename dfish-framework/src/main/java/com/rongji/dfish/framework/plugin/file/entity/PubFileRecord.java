package com.rongji.dfish.framework.plugin.file.entity;

import java.util.Date;

/**
 * 关于附件的实体类
 */
public class PubFileRecord implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
    private static final long serialVersionUID = 3466920245217817895L;
	private String fileId;
	private String fileName;
	private String fileExtension;
	private String fileType;
	private String fileUrl;
	private Long fileSize;
	private String fileCreator;
	private Date createTime;
	private Date updateTime;
	private String fileLink;
	private String fileKey;
	private String fileStatus;
	private Integer fileDuration;
	private String fileScheme;

	/**
	 * 记录是否改变
	 */
	private boolean change;

	/**
	 * 构造函数
	 */
	public PubFileRecord() {
	}

	/**
	 * 构造函数
	 * @param fileId String 附件编号
	 */
	public PubFileRecord(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * 附件编号
	 * @return String
	 */
	public String getFileId() {
		return this.fileId;
	}

	/**
	 * 附件编号
	 * @param fileId String 附件编号
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * 附件名称
	 * @return String
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * 附件名称
	 * @param fileName String 附件名称
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 扩展名
	 * @return String
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * 扩展名
	 * @param fileExtension
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	/**
	 * 附件类型
	 * @return String
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * 附件类型
	 * @param fileType String 附件类型
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * 附件存储地址
	 * @return String
	 */
	public String getFileUrl() {
		return this.fileUrl;
	}
	/**
	 * 附件存储地址
	 * @param fileUrl String 附件地址
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	/**
	 * 文件大小
	 * @return Long
	 */
	public Long getFileSize() {
		return this.fileSize;
	}

	/**
	 * 文件大小
	 * @param fileSize Long
	 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * 文件创建人
	 * @return String
	 */
	public String getFileCreator() {
		return this.fileCreator;
	}

	/**
	 * 文件创建人
	 * @param fileCreator String
	 */
	public void setFileCreator(String fileCreator) {
		this.fileCreator = fileCreator;
	}

	/**
	 * 创建时间
	 * @return Date 创建时间
	 */
	public Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * 创建时间
	 * @param createTime Date 创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 更新时间
	 * @return Date
	 */
	public Date getUpdateTime() {
		return this.updateTime;
	}

	/**
	 * 更新时间
	 * @param updateTime Date 更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 附件链接模块
	 * @return String 链接模块
	 */
	public String getFileLink() {
		return this.fileLink;
	}

	/**
	 * 附件链接模块
	 * @param fileLink String 链接模块
	 */
	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}

	/**
	 * 附件链接的关键字
	 * @return String
	 */
	public String getFileKey() {
		return this.fileKey;
	}

	/**
	 * 附件链接的关键字
	 * @param fileKey String 链接关键字
	 */
	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	/**
	 * 附件状态#0:初始状态,1:已关联,9:删除
	 * @return String
	 */
	public String getFileStatus() {
		return fileStatus;
	}

	/**
	 * 附件状态#0:正常,1:删除
	 * @param fileStatus String 附件状态
	 */
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

	/**
	 * 附件时长(单位:秒)
	 * @return Integer
	 */
	public Integer getFileDuration() {
		return fileDuration;
	}

	/**
	 * 附件时长(单位:秒)
	 * @param fileDuration Integer
	 */
	public void setFileDuration(Integer fileDuration) {
		this.fileDuration = fileDuration;
	}

	/**
	 * 处理方案
	 * @return String
	 */
	public String getFileScheme() {
		return fileScheme;
	}

	/**
	 * 处理方案
	 * @param fileScheme String
	 */
	public void setFileScheme(String fileScheme) {
		this.fileScheme = fileScheme;
	}

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}
}