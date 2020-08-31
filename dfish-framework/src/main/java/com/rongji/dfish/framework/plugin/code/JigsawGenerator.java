package com.rongji.dfish.framework.plugin.code;

import com.rongji.dfish.framework.plugin.code.dto.JigsawImgResult;

import javax.servlet.http.HttpServletRequest;

public interface JigsawGenerator {
    String KEY_CAPTCHA = "com.rongji.dfish.CAPTCHA.JIGSAW";
    @Deprecated
    public static final String KEY_CHECKCODE = KEY_CAPTCHA;

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

    /**
     * 校验拼图是否正确
     *
     * @param request 请求
     * @param offset 偏移量
     * @return boolean 是否验证通过
     */
    default boolean checkJigsawOffset(HttpServletRequest request, Number offset) {
        return checkJigsawOffset(request, offset, false);
    }
}
