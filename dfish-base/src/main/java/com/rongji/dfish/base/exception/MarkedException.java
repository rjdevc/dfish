package com.rongji.dfish.base.exception;

/**
 * 带有异常标识码的异常
 * @author lamontYu
 * @date 2019-12-11
 * @since 5.0
 */
public class MarkedException extends Exception implements Marked {

    private static final long serialVersionUID = -6835365153909461845L;
    /**
     * 异常标识码
     */
    private String code;

    @Override
    public String getCode() {
        return code;
    }

    /**
     * 设置异常标识码
     * @param code String 异常标识码
     */
    public void setCode(String code) {
        this.code = code;
    }

    public MarkedException() {
        super();
    }

    public MarkedException(String message) {
        super(message);
    }

    public MarkedException(Throwable cause) {
        super(cause);
    }

    public MarkedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MarkedException(String message, String code) {
        super(message);
        this.code = code;
    }

    public MarkedException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }
    
}
