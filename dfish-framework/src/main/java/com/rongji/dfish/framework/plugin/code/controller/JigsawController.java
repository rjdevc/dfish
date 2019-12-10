package com.rongji.dfish.framework.plugin.code.controller;

import com.rongji.dfish.framework.mvc.controller.BaseActionController;
import com.rongji.dfish.framework.plugin.code.JigsawGenerator;
import com.rongji.dfish.framework.plugin.code.dto.JigsawCheckResult;
import com.rongji.dfish.framework.plugin.code.dto.JigsawResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lamontYu
 * @create 2019-12-09
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
        JigsawResponse response = getJigsawGenerator().generatorJigsaw(request);
        return response;
    }

    @RequestMapping("/auth")
    @ResponseBody
    public Object auth(HttpServletRequest request) {
        boolean result = getJigsawGenerator().checkJigsawOffset(request, request.getParameter("offset"));
        return new JigsawCheckResult(result);
    }

}
