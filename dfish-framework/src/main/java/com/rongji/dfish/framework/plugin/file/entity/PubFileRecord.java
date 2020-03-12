package com.rongji.dfish.framework.plugin.file.entity;

import javax.persistence.Column;
import javax.persistence.Id;
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
	private String fileType;
	private String fileUrl;
	private Long fileSize;
	private String fileCreator;
	private Date createTime;
	private Date updateTime;
	private String fileLink;
	private String fileKey;
	private String fileStatus;

	// Constructors

	/** default constructor */
	public PubFileRecord() {
	}

	/** minimal constructor */
	public PubFileRecord(String fileId) {
		this.fileId = fileId;
	}

	// Property accessors
	@Id
	@Column(name = "FILE_ID")
	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "FILE_TYPE")
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@Column(name = "FILE_URL")
	public String getFileUrl() {
		return this.fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	@Column(name = "FILE_SIZE")
	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	@Column(name = "FILE_CREATOR")
	public String getFileCreator() {
		return this.fileCreator;
	}

	public void setFileCreator(String fileCreator) {
		this.fileCreator = fileCreator;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "FILE_LINK")
	public String getFileLink() {
		return this.fileLink;
	}

	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}

	@Column(name = "FILE_KEY")
	public String getFileKey() {
		return this.fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	@Column(name = "FILE_STATUS")
	public String getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

}