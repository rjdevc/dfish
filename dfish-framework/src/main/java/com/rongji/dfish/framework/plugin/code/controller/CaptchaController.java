package com.rongji.dfish.framework.plugin.code.controller;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.mvc.controller.BaseActionController;
import com.rongji.dfish.framework.plugin.code.CaptchaGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证码controller
 * @author lamontYu
 * @date 2019-12-28
 * @since 5.0
 */
@RequestMapping("/captcha")
public class CaptchaController extends BaseActionController {
    @Autowired(required = false)
    private List<CaptchaGenerator> generators;
    private Map<String, CaptchaGenerator> generatorMap = new HashMap<>();

    private static final String ALIAS_DEFAULT = "DEFAULT";

    @PostConstruct
    private void init() {
        if (Utils.notEmpty(generators)) {
            for (CaptchaGenerator generator : generators) {
                if (generator == null || Utils.isEmpty(generator.getAlias())) {
                    continue;
                }
                CaptchaGenerator old = generatorMap.put(generator.getAlias(), generator);
                if (old != null) {
                    LogUtil.warn("The CaptchaGenerator[" + old.getClass().getName() + "] is replaced by [" + generator.getClass().getName() + "]");
                }
            }
        }
        if (generatorMap.isEmpty()) {
            CaptchaGenerator generator = getDefaultCaptchaGenerator();
            generatorMap.put(generator.getAlias(), generator);
        }
    }

    private CaptchaGenerator getDefaultCaptchaGenerator() {
        CaptchaGenerator codeGenerator = new CaptchaGenerator();
        codeGenerator.setAlias(ALIAS_DEFAULT);
        return codeGenerator;
    }

    private CaptchaGenerator getCaptchaGenerator(String alias) {
        CaptchaGenerator generator = generatorMap.get(alias);
        if (generator == null) {
            LogUtil.warn("can not find the CaptchaGenerator[" + alias + "], use default instead.");
            generator = getDefaultCaptchaGenerator();
            generatorMap.put(generator.getAlias(), generator);
        }
        return generator;
    }

    /**
     * 生成校验码图片
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/codeImg.png")
    @ResponseBody
    public void codeImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String alias = request.getParameter("alias");
        alias = alias == null ? ALIAS_DEFAULT : alias;
        CaptchaGenerator codeGenerator = getCaptchaGenerator(alias);
        codeGenerator.drawImage(request, response);
    }

}
