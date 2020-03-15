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

	/**
	 * 附件编号
	 * @return
	 */
	@Id
	@Column(name = "FILE_ID")
	public String getFileId() {
		return this.fileId;
	}

	/**
	 * 附件编号
	 * @param fileId
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * 附件名称
	 * @return
	 */
	@Column(name = "FILE_NAME")
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * 附件名称
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 附件类型
	 * @return
	 */
	@Column(name = "FILE_TYPE")
	public String getFileType() {
		return fileType;
	}

	/**
	 * 附件类型
	 * @param fileType
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * 附件存储地址
	 * @return
	 */
	@Column(name = "FILE_URL")
	public String getFileUrl() {
		return this.fileUrl;
	}
	/**
	 * 附件存储地址
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	/**
	 * 文件大小
	 * @return
	 */
	@Column(name = "FILE_SIZE")
	public Long getFileSize() {
		return this.fileSize;
	}

	/**
	 * 文件大小
	 * @param fileSize
	 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * 文件创建人
	 * @return
	 */
	@Column(name = "FILE_CREATOR")
	public String getFileCreator() {
		return this.fileCreator;
	}

	/**
	 * 文件创建人
	 * @param fileCreator
	 */
	public void setFileCreator(String fileCreator) {
		this.fileCreator = fileCreator;
	}

	/**
	 * 创建时间
	 * @return
	 */
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * 创建时间
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 更新时间
	 * @return
	 */
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	/**
	 * 更新时间
	 * @param updateTime
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 附件链接模块
	 * @return
	 */
	@Column(name = "FILE_LINK")
	public String getFileLink() {
		return this.fileLink;
	}

	/**
	 * 附件链接模块
	 * @param fileLink
	 */
	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}

	/**
	 * 附件链接的关键字
	 * @return
	 */
	@Column(name = "FILE_KEY")
	public String getFileKey() {
		return this.fileKey;
	}

	/**
	 * 附件链接的关键字
	 * @param fileKey
	 */
	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	/**
	 * 附件状态#0:正常,1:删除
	 * @return
	 */
	@Column(name = "FILE_STATUS")
	public String getFileStatus() {
		return fileStatus;
	}

	/**
	 * 附件状态#0:正常,1:删除
	 * @param fileStatus
	 */
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

}