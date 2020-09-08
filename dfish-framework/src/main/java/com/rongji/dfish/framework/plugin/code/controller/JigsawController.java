package com.rongji.dfish.framework.plugin.code.controller;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.mvc.controller.FrameworkController;
import com.rongji.dfish.framework.mvc.response.JsonResponse;
import com.rongji.dfish.framework.plugin.code.JigsawGenerator;
import com.rongji.dfish.framework.plugin.code.dto.JigsawAuth;
import com.rongji.dfish.framework.plugin.code.dto.JigsawImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 滑动验证码
 *
 * @author lamontYu
 * @since DFish3.2
 */
@RequestMapping("/jigsaw")
public class JigsawController extends FrameworkController {
    /**
     * 滑动图片生成器
     */
    @Autowired
    private JigsawGenerator jigsawGenerator;

    public JigsawGenerator getJigsawGenerator() {
        return jigsawGenerator;
    }

    public void setJigsawGenerator(JigsawGenerator jigsawGenerator) {
        this.jigsawGenerator = jigsawGenerator;
    }

    /**
     * 图片加载地址
     *
     * @param request 请求
     * @return Object 图片加载对象(含地址等信息)
     * @throws Exception 图片生成异常
     */
    @RequestMapping("/img")
    @ResponseBody
    public Object img(HttpServletRequest request) throws Exception {
        JigsawImg response = getJigsawGenerator().generatorJigsaw(request);
        return new JsonResponse<>(response);
    }

    /**
     * 滑动认证方法
     *
     * @param request 请求
     * @return Object 认证信息对象
     */
    @RequestMapping("/auth")
    @ResponseBody
    public Object auth(HttpServletRequest request) {
        Number offset = null;
        String offsetStr = request.getParameter("offset");
        try {
            offset = Double.parseDouble(offsetStr);
        } catch (Exception e) {
            LogUtil.error("", e);
        }
        boolean result = getJigsawGenerator().checkJigsawOffset(request, offset, true);
        return new JsonResponse<>(new JigsawAuth(result));
    }

}
