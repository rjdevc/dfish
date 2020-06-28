package com.rongji.dfish.framework.plugin.code;

import com.rongji.dfish.framework.plugin.code.dto.JigsawImgResult;

import javax.servlet.http.HttpServletRequest;

/**
 * 滑动验证码图片工具类
 * @author LinLW
 * @since 3.2
 */
public interface JigsawGenerator {
    /**
     * 验证码
     */
    String KEY_CAPTCHA = "com.rongji.dfish.CAPTCHA.JIGSAW";
    @Deprecated
    String KEY_CHECKCODE = KEY_CAPTCHA;

    /**
     * 生成拼图
     *
     * @param request 请求
     * @return
     * @throws Exception
     */
    JigsawImgResult generatorJigsaw(HttpServletRequest request) throws Exception;

    /**
     * 校验拼图是否正确
     *
     * @param request 请求
     * @param offset 偏移量
     * @param retainCaptcha 保留验证码(一般情况是组件的验证使用)
     * @return boolean 是否验证通过
     */
    boolean checkJigsawOffset(HttpServletRequest request, Number offset, boolean retainCaptcha);
}
