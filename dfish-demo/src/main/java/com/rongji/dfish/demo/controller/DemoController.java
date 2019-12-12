package com.rongji.dfish.demo.controller;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.mvc.controller.BaseActionController;
import com.rongji.dfish.framework.mvc.response.JsonResponse;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.framework.plugin.lob.service.LobService;
import com.rongji.dfish.framework.plugin.progress.ProgressData;
import com.rongji.dfish.framework.plugin.progress.ProgressManager;
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
    @Autowired
    private ProgressManager progressManager;

    @RequestMapping("/index")
    @ResponseBody
    public Object index(HttpServletRequest request) {
        return null;
    }

    @RequestMapping("/submit")
    @ResponseBody
    public Object submit(HttpServletRequest request) throws Exception {
        String fileJson = ServletUtil.getParameter(request, "fileJson");
        fileService.updateFileLinks(fileJson, "FILE_TEST", "FILE01");
        String imageJson = ServletUtil.getParameter(request, "imageJson");
        fileService.updateFileLinks(imageJson, "IMAGE_TEST", "IMAGE01");
        String lobContent = ServletUtil.getParameter(request, "lobContent");
        if (Utils.notEmpty(lobContent)) {
            lobService.saveLob(lobContent);
        }
        return new JsonResponse<>(true);
    }

    @RequestMapping("/test")
    @ResponseBody
    public Object test(HttpServletRequest request) {
        String id = request.getParameter("id");
        int count = lobService.archive(id);
        return new JsonResponse<>(count);
    }

    @RequestMapping("/loading")
    @ResponseBody
    public Object loading(HttpServletRequest request) {
        String progressKey = String.valueOf(System.currentTimeMillis());
        ProgressData progressData = progressManager.register(() -> {
            try {
                int steps = 1;
                progressManager.resetSteps(progressKey, steps);
                for (int i = 0; i < steps * 10; i++) {
                    progressManager.addStepPercent(progressKey, 10.0);
                    Thread.sleep(500L);
                    if ((i+1)%10==0) {
                        progressManager.nextStep(progressKey);
                    }
                }
            } catch (Exception e) {
                LogUtil.error("", e);
            }
        }, progressKey, "测试文本");
        return new JsonResponse<>(progressData);
    }

}
