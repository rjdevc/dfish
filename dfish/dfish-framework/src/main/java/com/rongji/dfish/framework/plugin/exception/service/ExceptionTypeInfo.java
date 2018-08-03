package com.rongji.dfish.framework.plugin.exception.service;

import java.util.List;

public class ExceptionTypeInfo {
	private ExceptionTypeInfo cause;
	private String className;
	private List<StackInfo> stackTrace;
	
	public ExceptionTypeInfo getCause() {
		return cause;
	}
	public void setCause(ExceptionTypeInfo cause) {
		this.cause = cause;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<StackInfo> getStackTrace() {
		return stackTrace;
	}
	public void setStackTrace(List<StackInfo> stackTrace) {
		this.stackTrace = stackTrace;
	}
	
}
