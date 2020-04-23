package com.rongji.dfish.base.exception;

/**
 * 带有异常标识码的异常
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class MarkedException extends Exception implements Marked {

    private static final long serialVersionUID = -6835365153909461845L;
    /**
     * 异常标识码
     */
    private String code;

    /**
     * 构造函数
     */
    public MarkedException() {
        super();
    }

    /**
     * 构造函数
     *
     * @param message 信息
     */
    public MarkedException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param cause 起因
     */
    public MarkedException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数
     *
     * @param message 信息
     * @param cause   起因
     */
    public MarkedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     *
     * @param message 信息
     * @param code    标识码
     */
    public MarkedException(String message, String code) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数
     *
     * @param message 信息
     * @param cause   起因
     * @param code    标识码
     */
    public MarkedException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 获取异常标识码
     *
     * @return 标识码
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 设置异常标识码
     *
     * @param code String 标识码
     * @return 本身，这样可以继续设置属性
     */
    public MarkedException setCode(String code) {
        this.code = code;
        return this;
    }


}
