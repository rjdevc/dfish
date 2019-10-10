package com.rongji.dfish.framework.plugin.work.entity;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * WorkGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PUB_WORK_GROUP")
public class PubWorkGroup implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3663682268147863965L;
	private String groupKey;
	private String groupName;
	private Integer maxThread;
	private Integer errCount;
	private String errStrategy;

	// Constructors

	/** default constructor */
	public PubWorkGroup() {
	}

	/** minimal constructor */
	public PubWorkGroup(String groupKey, String groupName) {
		this.groupKey = groupKey;
		this.groupName = groupName;
	}

	/** full constructor */
	public PubWorkGroup(String groupKey, String groupName, Integer maxThread,
			Integer errCount, String errStrategy) {
		this.groupKey = groupKey;
		this.groupName = groupName;
		this.maxThread = maxThread;
		this.errCount = errCount;
		this.errStrategy = errStrategy;
	}

	// Property accessors
	@Id
	@Column(name = "GROUP_KEY", unique = true, nullable = false, length = 32)
	public String getGroupKey() {
		return this.groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	@Column(name = "GROUP_NAME", nullable = false)
	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column(name = "MAX_THREAD", precision = 5, scale = 0)
	public Integer getMaxThread() {
		return this.maxThread;
	}

	public void setMaxThread(Integer maxThread) {
		this.maxThread = maxThread;
	}

	@Column(name = "ERR_COUNT", precision = 5, scale = 0)
	public Integer getErrCount() {
		return this.errCount;
	}

	public void setErrCount(Integer errCount) {
		this.errCount = errCount;
	}

	@Column(name = "ERR_STRATEGY", length = 1)
	public String getErrStrategy() {
		return this.errStrategy;
	}

	public void setErrStrategy(String errStrategy) {
		this.errStrategy = errStrategy;
	}

}