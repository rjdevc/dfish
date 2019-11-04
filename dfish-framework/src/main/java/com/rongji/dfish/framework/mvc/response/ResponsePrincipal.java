package com.rongji.dfish.framework.mvc.response;

import java.security.Principal;

/**
 * @author lamontYu
 * @create 2019-11-04 15:32
 */
public interface ResponsePrincipal extends Principal {

    String getNatureName();

    String getFullName();

}
