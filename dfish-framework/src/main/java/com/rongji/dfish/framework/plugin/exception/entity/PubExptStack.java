package com.rongji.dfish.framework.plugin.exception.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_EXPT_STACK")
public class PubExptStack  implements java.io.Serializable{

	private static final long serialVersionUID = -6181757035218588593L;
	
	private long stackId;
	private long typeId;
	private int stackOrder;
	private int className;
	private int methodName;
	private int fileName;
	private int lineNumber;
	
	@Id
	@Column(name = "STACK_ID", unique = true, nullable = false, length = 18)
	public long getStackId() {
		return stackId;
	}
	public void setStackId(long stackId) {
		this.stackId = stackId;
	}
	@Column(name = "TYPE_ID", nullable = false, length = 18)
	public long getTypeId() {
		return typeId;
	}
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}
	@Column(name = "STACK_ORDER",  nullable = false, length = 9)
	public int getStackOrder() {
		return stackOrder;
	}
	public void setStackOrder(int stackOrder) {
		this.stackOrder = stackOrder;
	}
	@Column(name = "CLASS_NAME",  nullable = false, length = 9)
	public int getClassName() {
		return className;
	}
	public void setClassName(int className) {
		this.className = className;
	}
	@Column(name = "METHOD_NAME",  length = 9)
	public int getMethodName() {
		return methodName;
	}
	public void setMethodName(int methodName) {
		this.methodName = methodName;
	}
	@Column(name = "FILE_NAME",  length = 9)
	public int getFileName() {
		return fileName;
	}
	public void setFileName(int fileName) {
		this.fileName = fileName;
	}
	@Column(name = "LINE_NUMBER",  length = 9)
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==this) {
            return true;
        }
		if(obj==null) {
            return false;
        }
		if(!(obj instanceof PubExptStack)){
			return false;
		}
		PubExptStack cast=(PubExptStack)obj;
		return lineNumber==cast.getLineNumber() && fileName==cast.getFileName() && className==cast.getClassName() && methodName==cast.getMethodName();
	}
}
