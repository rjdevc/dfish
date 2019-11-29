package com.rongji.dfish.framework.plugin.file.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PubFileRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PUB_FILE_RECORD")
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
	@Column(name = "FILE_ID", unique = true, nullable = false, length = 16)
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

	@Column(name = "FILE_SIZE", scale = 0)
	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	@Column(name = "FILE_CREATOR", length = 32)
	public String getFileCreator() {
		return this.fileCreator;
	}

	public void setFileCreator(String fileCreator) {
		this.fileCreator = fileCreator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", length = 7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "FILE_LINK", length = 64)
	public String getFileLink() {
		return this.fileLink;
	}

	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}

	@Column(name = "FILE_KEY", length = 64)
	public String getFileKey() {
		return this.fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	@Column(name = "FILE_STATUS", length = 1)
	public String getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

}