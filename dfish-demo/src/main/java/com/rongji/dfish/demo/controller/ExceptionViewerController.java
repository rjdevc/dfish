package com.rongji.dfish.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/exceptionViewer")
@Controller
public class ExceptionViewerController extends com.rongji.dfish.framework.plugin.exception.AbstractExceptionViewerController {
    @Override
    protected boolean accept(HttpServletRequest request) {
        //任何人都可以查看异常日志。
        return true;
    }
}
