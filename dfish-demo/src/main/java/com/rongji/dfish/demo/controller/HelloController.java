package com.rongji.dfish.demo.controller;

import com.rongji.dfish.framework.mvc.controller.BaseUiController;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Html;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lamontYu
 * @since DFish5.0
 */
@Controller
@RequestMapping("/hello")
public class HelloController extends BaseUiController {

    @RequestMapping("/index")
    @ResponseBody
    public Object index(HttpServletRequest request) {
        View view = new View();
        view.setNode(new Html("这是您的第1个页面"));
        return view;
    }

}
