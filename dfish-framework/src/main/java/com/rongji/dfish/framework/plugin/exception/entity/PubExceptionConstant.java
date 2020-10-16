package com.rongji.dfish.framework.plugin.exception.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_EXCEPTION_CONSTANT")
public class PubExceptionConstant  implements java.io.Serializable{

	private static final long serialVersionUID = 4045234936861293428L;
	
	private int conId;
	private String conName;
	
	@Id
	@Column(name = "CON_ID", unique = true, nullable = false, length = 9)
	public int getConId() {
		return conId;
	}
	public void setConId(int conId) {
		this.conId = conId;
	}
	@Column(name = "CON_NAME",nullable = false, length = 255)
	public String getConName() {
		return conName;
	}
	public void setConName(String conName) {
		this.conName = conName;
	}
	
	

}
