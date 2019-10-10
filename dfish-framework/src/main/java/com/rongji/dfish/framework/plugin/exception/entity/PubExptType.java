package com.rongji.dfish.framework.plugin.exception.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_EXPT_TYPE")
public class PubExptType  implements java.io.Serializable{

	private static final long serialVersionUID = 6195762226692019505L;
	
	private long typeId;
	private int className;
	private long causeId;
	
	@Id
	@Column(name = "TYPE_ID", unique = true, nullable = false, length = 18)
	public long getTypeId() {
		return typeId;
	}
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}
	@Column(name = "CLASS_NAME", nullable = false, length = 9)
	public int getClassName() {
		return className;
	}
	public void setClassName(int className) {
		this.className = className;
	}

	@Column(name = "CAUSE_ID", nullable = false, length = 18)
	public long getCauseId() {
		return causeId;
	}
	public void setCauseId(long causeId) {
		this.causeId = causeId;
	}
	
}
