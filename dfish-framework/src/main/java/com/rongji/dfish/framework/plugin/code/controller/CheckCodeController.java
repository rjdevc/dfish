package com.rongji.dfish.framework.plugin.code.controller;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.mvc.controller.BaseActionController;
import com.rongji.dfish.framework.plugin.code.CheckCodeGenerator;
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
 * 验证码生成controller
 */
public class CheckCodeController extends BaseActionController {
    @Autowired(required = false)
    private List<CheckCodeGenerator> generators;
    private Map<String, CheckCodeGenerator> generatorMap = new HashMap<>();

    private static final String ALIAS_DEFAULT = "DEFAULT";

    @PostConstruct
    private void init() {
        if (Utils.notEmpty(generators)) {
            for (CheckCodeGenerator generator : generators) {
                if (generator == null || Utils.isEmpty(generator.getAlias())) {
                    continue;
                }
                CheckCodeGenerator old = generatorMap.put(generator.getAlias(), generator);
                if (old != null) {
                    LogUtil.warn("The CheckCodeGenerator[" + old.getClass().getName() + "] is replaced by [" + generator.getClass().getName() + "]");
                }
            }
        }
        if (generatorMap.isEmpty()) {
            CheckCodeGenerator generator = getDefaultCheckCodeGenerator();
            generatorMap.put(generator.getAlias(), generator);
        }
    }

    private CheckCodeGenerator getDefaultCheckCodeGenerator() {
        CheckCodeGenerator codeGenerator = new CheckCodeGenerator();
        codeGenerator.setAlias(ALIAS_DEFAULT);
        return codeGenerator;
    }

    private CheckCodeGenerator getCheckCodeGenerator(String alias) {
        CheckCodeGenerator generator = generatorMap.get(alias);
        if (generator == null) {
            LogUtil.warn("can not find the CheckCodeGenerator[" + alias + "], use default instead.");
            generator = getDefaultCheckCodeGenerator();
            generatorMap.put(generator.getAlias(), generator);
        }
        return generator;
    }

    @RequestMapping("/codeImg.png")
    @ResponseBody
    public void codeImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String alias = request.getParameter("alias");
        alias = alias == null ? ALIAS_DEFAULT : alias;
        CheckCodeGenerator codeGenerator = getCheckCodeGenerator(alias);
        codeGenerator.drawImage(request, response);
    }

}
