package com.rongji.dfish.framework.plugin.code.controller;

import com.rongji.dfish.framework.mvc.controller.BaseActionController;
import com.rongji.dfish.framework.mvc.response.JsonResponse;
import com.rongji.dfish.framework.plugin.code.JigsawGenerator;
import com.rongji.dfish.framework.plugin.code.dto.JigsawAuthResult;
import com.rongji.dfish.framework.plugin.code.dto.JigsawImgResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 滑动验证码
 * @author lamontYu
 * @date 2019-12-09
 * @since 3.2
 */
@RequestMapping("/jigsaw")
public class JigsawController extends BaseActionController {
    private JigsawGenerator jigsawGenerator = new JigsawGenerator();

    public JigsawGenerator getJigsawGenerator() {
        return jigsawGenerator;
    }

    public void setJigsawGenerator(JigsawGenerator jigsawGenerator) {
        this.jigsawGenerator = jigsawGenerator;
    }

    @RequestMapping("/img")
    @ResponseBody
    public Object img(HttpServletRequest request) throws Exception {
        JigsawImgResult response = getJigsawGenerator().generatorJigsaw(request);
        return new JsonResponse<>(response);
    }

    @RequestMapping("/auth")
    @ResponseBody
    public Object auth(HttpServletRequest request) {
        boolean result = getJigsawGenerator().checkJigsawOffset(request, request.getParameter("offset"));
        return new JsonResponse<>(new JigsawAuthResult(result));
    }

}
