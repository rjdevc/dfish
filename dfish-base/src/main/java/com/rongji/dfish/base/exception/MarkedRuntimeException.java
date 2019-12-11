package com.rongji.dfish.base.exception;

/**
 * 带有异常编码的运行时异常
 *
 * @author lamontYu
 * @create 2019-12-11
 * @since 5.0
 */
public class MarkedRuntimeException extends RuntimeException implements Marked {

    private static final long serialVersionUID = -7624791248986463899L;
    private String code;

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MarkedRuntimeException() {
        super();
    }

    public MarkedRuntimeException(String message) {
        super(message);
    }

    public MarkedRuntimeException(Throwable cause) {
        super(cause);
    }

    public MarkedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MarkedRuntimeException(String message, String code) {
        super(message);
        this.code = code;
    }

    public MarkedRuntimeException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

}
