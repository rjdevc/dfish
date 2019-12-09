package com.rongji.dfish.demo.controller;

import com.rongji.dfish.framework.mvc.controller.BaseActionController;
import com.rongji.dfish.framework.mvc.response.JsonResponse;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.framework.plugin.lob.service.LobService;
import com.rongji.dfish.framework.util.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lamontYu
 * @create 2019-12-09
 */
@RequestMapping("/demo")
@Controller
public class DemoController extends BaseActionController {
    @Autowired
    private FileService fileService;
    @Autowired
    private LobService lobService;

    @RequestMapping("/index")
    @ResponseBody
    public Object index(HttpServletRequest request) {
        return null;
    }

    @RequestMapping("/submit")
    @ResponseBody
    public Object submit(HttpServletRequest request) throws Exception {
        String fileJson = ServletUtil.getParameter(request, "fileJson");

        fileService.updateFileLink(fileJson, "DEMO_TEST", "TEST01");
        String lobContent = ServletUtil.getParameter(request, "lobContent");
        lobService.saveLob(lobContent);
        return new JsonResponse<>(true);
    }

    @RequestMapping("/test")
    @ResponseBody
    public Object test(HttpServletRequest request) {
        String id = request.getParameter("id");
        int count = lobService.archive(id);
        return new JsonResponse<>(count);
    }

}
