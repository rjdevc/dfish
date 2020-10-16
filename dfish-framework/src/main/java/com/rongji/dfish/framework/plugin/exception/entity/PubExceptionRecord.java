package com.rongji.dfish.framework.plugin.exception.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_EXCEPTION_RECORD")
public class PubExceptionRecord implements java.io.Serializable{

	private static final long serialVersionUID = -1239029459275581296L;
	
	private String recId;
	private long eventTime;
	private long typeId;
	private String exptMsg;
	private long exptRepetitions;

	@Id
	@Column(name = "REC_ID", unique = true, nullable = false, length = 32)
	public String getRecId() {
		return recId;
	}
	public void setRecId(String recId) {
		this.recId = recId;
	}
	@Column(name = "EVENT_TIME", nullable = false, length = 18)
	public long getEventTime() {
		return eventTime;
	}
	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}
	@Column(name = "TYPE_ID", nullable = false, length = 18)
	public long getTypeId() {
		return typeId;
	}
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}
	@Column(name = "EXPT_MSG", length = 255)
	public String getExptMsg() {
		return exptMsg;
	}
	public void setExptMsg(String exptMsg) {
		this.exptMsg = exptMsg;
	}
	@Column(name = "EXPT_REPETITIONS", length = 255)
	public long getExptRepetitions() {
		return exptRepetitions;
	}
	public void setExptRepetitions(long exptRepetitions) {
		this.exptRepetitions = exptRepetitions;
	}
}
