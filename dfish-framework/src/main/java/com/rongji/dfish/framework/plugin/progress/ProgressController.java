package com.rongji.dfish.framework.plugin.progress;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.mvc.controller.BaseActionController;
import com.rongji.dfish.framework.mvc.response.JsonResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 进度条controller,主要用于刷新进度的方法
 *
 * @author lamontYu
 * @version 1.2 去掉注解,项目中可自定义加载 lamontYu 2019-12-05
 * @date 2018-08-03 before
 * @since 3.0
 */
@RequestMapping("/progress")
public class ProgressController extends BaseActionController {
    @Resource(name = "progressManager")
    private ProgressManager progressManager;

    public ProgressManager getProgressManager() {
        return progressManager;
    }

    public void setProgressManager(ProgressManager progressManager) {
        this.progressManager = progressManager;
    }

    @RequestMapping("/reload/{progressKey}")
    @ResponseBody
    public Object reload(@PathVariable String progressKey) {
        JsonResponse jsonReponse = new JsonResponse<>();
        ProgressData progressData = null;
        if (Utils.notEmpty(progressKey)) {
            progressKey = progressManager.decrypt(progressKey);
            progressData = progressManager.reloadProgressData(progressKey);
            if (progressData != null) {
                if (progressData.isFinish() || progressData.getError() != null) {
                    progressManager.removeProgress(progressKey);
                }
            }
        }
        if (progressData != null) {
            if (progressData.getError() != null) {
                jsonReponse.setErrCode(progressData.getError().getCode());
                jsonReponse.setErrMsg(progressData.getError().getMsg());
            } else {
                jsonReponse.setData(progressData);
            }
        } else {
            ProgressData finishData = new ProgressData();
            finishData.setProgressKey(progressKey);
            finishData.setFinish(true);
            jsonReponse.setData(finishData);
        }

        return jsonReponse;
    }

//    @RequestMapping("/reloadProgress")
//    @ResponseBody
//    public Object reloadProgress(HttpServletRequest request) {
//        String enProgressKey = request.getParameter("progressKey");
//        if (Utils.isEmpty(enProgressKey)) {
//            return getCommand("进度编号为空,无法刷新进度条", false);
//        }
//        String progressKey = progressManager.decrypt(enProgressKey);
//        ProgressData progressData = progressManager.reloadProgressData(progressKey);
//
//        if (progressData == null) {
//            return getCommand(null, true);
//        }
//
//        if (progressData.getError() != null) {
//            String alertMsg = "";
//            if (Utils.notEmpty(progressData.getError().getCode())) {
//                alertMsg = "[" + progressData.getError().getCode() + "]";
//            }
//            alertMsg += progressData.getError().getMsg();
//            // 进度条异常,将进度条移除记录
//            progressManager.removeProgress(progressKey);
//            return new Alert(alertMsg);
//        } else if (progressData.isFinish()) {
//            // 进度条结束,将进度条移除记录
//            progressManager.removeProgress(progressKey);
//            if (progressData.getComplete() != null) {
//                return progressData.getComplete();
//            }
//            // 默认命令做容错
//            return new Cmd();
//        } else {
//            return progressManager.getProgress(progressData);
//        }
//    }
//
//    private Command<?> getCommand(String errorMsg, boolean closeLoading) {
//        StringBuilder js = new StringBuilder();
//        if (Utils.notEmpty(errorMsg)) {
//            js.append("$.alert('").append(errorMsg).append("');");
//        }
//        if (closeLoading) {
//            js.append("$.close('").append(ProgressManager.ID_LOADING).append("');");
//        }
//        return new JS(js.toString());
//    }

}
