package com.rongji.dfish.framework.mvc.response;

import java.security.Principal;

/**
 * 接口响应的调用者信息
 *
 * @author lamontYu
 * @since DFish5.0
 */
public interface ResponsePrincipal extends Principal {

    /**
     * 调用者的名称
     * @return String
     */
    String getNatureName();

    /**
     * 调用者的完整名称
     * @return String
     */
    String getFullName();

}
