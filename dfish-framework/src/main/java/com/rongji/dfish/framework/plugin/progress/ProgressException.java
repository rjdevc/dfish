package com.rongji.dfish.framework.plugin.progress;

/**
 * 进度条运行异常
 * @author lamontYu
 * @create 2019-12-05
 * @since 3.0
 */
public class ProgressException extends RuntimeException {

	private static final long serialVersionUID = -5010323366896210348L;
	
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
	
	public ProgressException(String message, String code){
		super(message);
		this.code = code;
	}

}
