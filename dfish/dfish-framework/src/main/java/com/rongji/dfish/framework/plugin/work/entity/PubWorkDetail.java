package com.rongji.dfish.framework.plugin.work.entity;
// default package

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * WorkDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PUB_WORK_DETAIL")
public class PubWorkDetail implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7285009842897821637L;
	private String workId;
	private String groupKey;
	private Integer workNo;
	private String implClass;
	private String workConfig;
	private String workKey;
	private String workState;
	private String workOwner;
	private Date createTime;
	private Date beginTime;
	private Date endTime;
	private String errMessage;
	private Integer stepErrCnt;
	private String workStep;

	// Constructors

	/** default constructor */
	public PubWorkDetail() {
	}

	/** minimal constructor */
	public PubWorkDetail(String workId, String workState) {
		this.workId = workId;
		this.workState = workState;
	}

	/** full constructor */
	public PubWorkDetail(String workId, String groupKey, Integer workNo,
			String implClass, String workConfig, String workKey,
			String workState, String workOwner, Date createTime,
			Date beginTime, Date endTime, String errMessage,
			Integer stepErrCnt, String workStep) {
		this.workId = workId;
		this.groupKey = groupKey;
		this.workNo = workNo;
		this.implClass = implClass;
		this.workConfig = workConfig;
		this.workKey = workKey;
		this.workState = workState;
		this.workOwner = workOwner;
		this.createTime = createTime;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.errMessage = errMessage;
		this.stepErrCnt = stepErrCnt;
		this.workStep = workStep;
	}

	// Property accessors
	@Id
	@Column(name = "WORK_ID", unique = true, nullable = false, length = 32)
	public String getWorkId() {
		return this.workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
	}

	@Column(name = "GROUP_KEY", length = 32)
	public String getGroupKey() {
		return this.groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	@Column(name = "WORK_NO", precision = 9, scale = 0)
	public Integer getWorkNo() {
		return this.workNo;
	}

	public void setWorkNo(Integer workNo) {
		this.workNo = workNo;
	}

	@Column(name = "IMPL_CLASS")
	public String getImplClass() {
		return this.implClass;
	}

	public void setImplClass(String implClass) {
		this.implClass = implClass;
	}

	@Column(name = "WORK_CONFIG", length = 2000)
	public String getWorkConfig() {
		return this.workConfig;
	}

	public void setWorkConfig(String workConfig) {
		this.workConfig = workConfig;
	}

	@Column(name = "WORK_KEY", length = 64)
	public String getWorkKey() {
		return this.workKey;
	}

	public void setWorkKey(String workKey) {
		this.workKey = workKey;
	}

	@Column(name = "WORK_STATE", nullable = false, length = 1)
	public String getWorkState() {
		return this.workState;
	}

	public void setWorkState(String workState) {
		this.workState = workState;
	}

	@Column(name = "WORK_OWNER", length = 64)
	public String getWorkOwner() {
		return this.workOwner;
	}

	public void setWorkOwner(String workOwner) {
		this.workOwner = workOwner;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_TIME", length = 7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BEGIN_TIME", length = 7)
	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_TIME", length = 7)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "ERR_MESSAGE", length = 2000)
	public String getErrMessage() {
		return this.errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	@Column(name = "STEP_ERR_CNT", precision = 5, scale = 0)
	public Integer getStepErrCnt() {
		return this.stepErrCnt;
	}

	public void setStepErrCnt(Integer stepErrCnt) {
		this.stepErrCnt = stepErrCnt;
	}

	@Column(name = "WORK_STEP", length = 32)
	public String getWorkStep() {
		return this.workStep;
	}

	public void setWorkStep(String workStep) {
		this.workStep = workStep;
	}

}