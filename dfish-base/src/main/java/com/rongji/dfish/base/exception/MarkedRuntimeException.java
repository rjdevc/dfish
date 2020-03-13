package com.rongji.dfish.base.exception;

/**
 * 带有异常编码的运行时异常
 *
 * @author lamontYu
 * @date 2019-12-11
 * @since 5.0
 */
public class MarkedRuntimeException extends RuntimeException implements Marked {

    private static final long serialVersionUID = -7624791248986463899L;
    private String code;

    @Override
    public String getCode() {
        return code;
    }

    /**
     * 获取标识码
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 构造函数
     */
    public MarkedRuntimeException() {
        super();
    }

    /**
     * 构造函数
     * @param message
     */
    public MarkedRuntimeException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param cause
     */
    public MarkedRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数
     * @param message
     * @param cause
     */
    public MarkedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     * @param message
     * @param code
     */
    public MarkedRuntimeException(String message, String code) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数
     * @param message
     * @param cause
     * @param code
     */
    public MarkedRuntimeException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

}
