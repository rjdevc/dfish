package com.rongji.dfish.framework.plugin.code.controller;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 验证码controller
 * @author lamontYu
 *
 * @since DFish3.0
 * @deprecated {@link CaptchaController}
 */
@RequestMapping("/checkCode")
@Deprecated
public class CheckCodeController extends CaptchaController{
}
