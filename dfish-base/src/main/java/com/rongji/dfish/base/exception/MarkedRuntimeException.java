package com.rongji.dfish.base.exception;

/**
 * 带有异常编码的运行时异常
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class MarkedRuntimeException extends RuntimeException implements MarkedCause {

    private static final long serialVersionUID = -7624791248986463899L;
    private String code;
    private String localMessage;

    /**
     * 构造函数
     */
    public MarkedRuntimeException() {
        super();
    }

    /**
     * 构造函数
     * @param message 信息
     */
    public MarkedRuntimeException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param cause 起因
     */
    public MarkedRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数
     * @param message 信息
     * @param cause 起因
     */
    public MarkedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     * @param message 信息
     * @param code 标识码
     */
    public MarkedRuntimeException(String message, String code) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数
     * @param message 信息
     * @param cause 起因
     * @param code 标识码
     */
    public MarkedRuntimeException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 获取异常标识码
     * @return 标识码
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 设置异常标识码
     * @param code 标识码
     * @return 本身，这样可以继续设置属性
     */
    public MarkedRuntimeException setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String getLocalMessage() {
        return localMessage;
    }

    /**
     * 设置本地化信息
     * @param localMessage String 本地化信息
     * @return 本身，这样可以继续设置属性
     */
    public MarkedRuntimeException setLocalMessage(String localMessage) {
        this.localMessage = localMessage;
        return this;
    }
}
