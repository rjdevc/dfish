package com.rongji.dfish.base.exception;

import com.rongji.dfish.base.util.Utils;

/**
 * 接口，异常编码
 * @author lamontYu
 * @since DFish5.0
 */
public interface MarkedCause {

    /**
     * 标识码
     * @return String
     */
    String getCode();

    /**
     * 异常信息
     * @return String
     */
    String getMessage();

    /**
     * 本地化异常信息
     * @return String
     */
    String getLocalMessage();

    /**
     * 显示信息
     * @return String
     */
    default String message() {
        return Utils.notEmpty(getLocalMessage()) ? getLocalMessage() : getMessage();
    }

}
