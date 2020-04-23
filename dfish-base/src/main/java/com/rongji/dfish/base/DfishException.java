package com.rongji.dfish.base;

import com.rongji.dfish.base.exception.MarkedException;

/**
 * 带有标识码的异常
 * @author DFish Team
 * @since DFish2.x
 * @see com.rongji.dfish.base.exception.MarkedException
 */
@Deprecated
public class DfishException extends MarkedException {

    private static final long serialVersionUID = 7079476050905181817L;

    public String getExceptionCode() {
        return getCode();
    }

    public void setExceptionCode(String exceptionCode) {
        setCode(exceptionCode);
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
        setExceptionCode(exceptionCode);
    }
}
