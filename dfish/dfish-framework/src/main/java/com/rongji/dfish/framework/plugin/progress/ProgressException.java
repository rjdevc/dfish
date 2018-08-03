package com.rongji.dfish.framework.plugin.progress;

public class ProgressException extends RuntimeException {

	private static final long serialVersionUID = -5010323366896210348L;
	
	private String exceptionCode;

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public ProgressException(){
		super();
	}
	public ProgressException(String message){
		super(message);
	}
	
	public ProgressException(Throwable cause) {
		super(cause);
	}
	
	public ProgressException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public ProgressException(String message, String exceptionCode){
		super(message);
		this.exceptionCode = exceptionCode;
	}

}
