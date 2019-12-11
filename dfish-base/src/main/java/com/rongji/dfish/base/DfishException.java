package com.rongji.dfish.base;

/**
 * 带有标识码的异常
 * @author DFish Team
 * @since 2.x
 * @see com.rongji.dfish.base.exception.MarkedException
 */
@Deprecated
public class DfishException extends Exception {

    private static final long serialVersionUID = 7079476050905181817L;
    private String exceptionCode;

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public DfishException() {
        super();
    }

    public DfishException(String msg) {
        super(msg);
    }

    public DfishException(Throwable cause) {
        super(cause);
    }

    public DfishException(String message, Throwable cause) {
        super(message, cause);
    }

    public DfishException(String message, String exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
