package com.rongji.dfish.framework.plugin.lob.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * LobData entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PUB_LOB")
public class PubLob implements java.io.Serializable {

	private static final long serialVersionUID = -3653424051971393087L;
	private String lobId;
	private String lobContent;
	private Date operTime;
	private String archiveFlag;
	private Date archiveTime;

	// Constructors

	/** default constructor */
	public PubLob() {
	}

	/** minimal constructor */
	public PubLob(String lobId) {
		this.lobId = lobId;
	}

	/** full constructor */
	public PubLob(String lobId, String lobContent, Date operTime, String archiveFlag, Date archiveTime) {
		this.lobId = lobId;
		this.lobContent = lobContent;
		this.operTime = operTime;
		this.archiveFlag = archiveFlag;
		this.archiveTime = archiveTime;
	}

	// Property accessors
	@Id
	@Column(name = "LOB_ID", unique = true, nullable = false, length = 32)
	public String getLobId() {
		return this.lobId;
	}

	public void setLobId(String lobId) {
		this.lobId = lobId;
	}

	@Column(name = "LOB_CONTENT")
	public String getLobContent() {
		return this.lobContent;
	}

	public void setLobContent(String lobContent) {
		this.lobContent = lobContent;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OPER_TIME", length = 7)
	public Date getOperTime() {
		return this.operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	@Column(name = "ARCHIVE_FLAG", length = 1)
	public String getArchiveFlag() {
		return this.archiveFlag;
	}

	public void setArchiveFlag(String archiveFlag) {
		this.archiveFlag = archiveFlag;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ARCHIVE_TIME", length = 7)
	public Date getArchiveTime() {
		return this.archiveTime;
	}

	public void setArchiveTime(Date archiveTime) {
		this.archiveTime = archiveTime;
	}

}