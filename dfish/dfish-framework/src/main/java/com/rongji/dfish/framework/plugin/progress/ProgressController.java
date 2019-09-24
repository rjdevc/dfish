package com.rongji.dfish.framework.plugin.progress;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.mvc.controller.BaseController;
import com.rongji.dfish.ui.Command;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.widget.Progress;

@Controller
@RequestMapping("/progress")
public class ProgressController extends BaseController {
	@Autowired
	private ProgressManager progressManager;

	@RequestMapping("/reloadProgress")
	@ResponseBody
	public Object reloadProgress(HttpServletRequest request) {
		String enProgressKey = request.getParameter("progressKey");
		if (Utils.isEmpty(enProgressKey)) {
			return getCommand("进度编号为空,无法刷新进度条", false);
		}
		String progressKey = progressManager.decryptKey(enProgressKey);
		ProgressData progressData = progressManager.getProgressData(progressKey);
		
		if (progressData == null) {
			return getCommand(null, true);
		}

		// 进度条结束
		if (progressData.isFinish()) {
			// 将进度条移除记录
			progressManager.removeProgress(progressKey);
			if (progressData.getCompleteNode() != null) {
				return progressData.getCompleteNode();
			}
			// 默认命令做容错
			return new CommandGroup();
		} else {
			List<Progress> progressGroup = progressManager.getProgressGroup(progressKey);
			VerticalLayout vert = new VerticalLayout(null);
			for (Progress progress : progressGroup) {
				vert.add(progress);
			}
			return vert;
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
