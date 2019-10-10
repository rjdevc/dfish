package com.rongji.dfish.framework.plugin.progress;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.framework.mvc.response.JsonResponse;
import com.rongji.dfish.ui.command.AlertCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.mvc.controller.BaseController;
import com.rongji.dfish.ui.Command;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.JSCommand;

@Controller
@RequestMapping("/progress")
public class ProgressController extends BaseController {
	@Autowired
	private ProgressManager progressManager;

	@RequestMapping("/reload/{progressKey}")
	@ResponseBody
	public Object reload(@PathVariable String progressKey) {
		progressKey = progressManager.decrypt(progressKey);

		ProgressData progressData = progressManager.reloadProgressData(progressKey);
		JsonResponse jsonReponse = new JsonResponse<>(progressData);
		if (progressData != null) {
			if(progressData.getError() != null) {
				jsonReponse.setData(null);
				jsonReponse.setErrCode(progressData.getError().getCode());
				jsonReponse.setErrMsg(progressData.getError().getMsg());
				progressManager.removeProgress(progressKey);
			} else if (progressData.isFinish()) {
				progressManager.removeProgress(progressKey);
			}
		}
		return jsonReponse;
	}

	@RequestMapping("/reloadProgress")
	@ResponseBody
	public Object reloadProgress(HttpServletRequest request) {
		String enProgressKey = request.getParameter("progressKey");
		if (Utils.isEmpty(enProgressKey)) {
			return getCommand("进度编号为空,无法刷新进度条", false);
		}
		String progressKey = progressManager.decrypt(enProgressKey);
		ProgressData progressData = progressManager.reloadProgressData(progressKey);
		
		if (progressData == null) {
			return getCommand(null, true);
		}

		if (progressData.getError() != null) {
			String alertMsg = "";
			if (Utils.notEmpty(progressData.getError().getCode())) {
				alertMsg = "[" + progressData.getError().getCode() + "]";
			}
			alertMsg += progressData.getError().getMsg();
			// 进度条异常,将进度条移除记录
			progressManager.removeProgress(progressKey);
			return new AlertCommand(alertMsg);
		} else if (progressData.isFinish()) {
			// 进度条结束,将进度条移除记录
			progressManager.removeProgress(progressKey);
			if (progressData.getComplete() != null) {
				return progressData.getComplete();
			}
			// 默认命令做容错
			return new CommandGroup();
		} else {
			return progressManager.getProgress(progressData);
		}
	}
	
	private Command<?> getCommand(String errorMsg, boolean closeLoading) {
		StringBuilder js = new StringBuilder();
		if (Utils.notEmpty(errorMsg)) {
			js.append("$.alert('").append(errorMsg).append("');");
		}
		if (closeLoading) {
			js.append("$.close('").append(ProgressManager.ID_LOADING).append("');");
		}
		return new JSCommand(js.toString());
	}
	
}
