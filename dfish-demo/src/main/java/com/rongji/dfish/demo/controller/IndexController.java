package com.rongji.dfish.demo.controller;

import com.rongji.dfish.framework.mvc.controller.BaseActionController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lamontYu
 * @create 2019-12-09
 */
@RequestMapping("/index")
@Controller
public class IndexController extends BaseActionController {

    @RequestMapping("/index")
    @ResponseBody
    public Object index(HttpServletRequest request) {
        return null;
    }

}
