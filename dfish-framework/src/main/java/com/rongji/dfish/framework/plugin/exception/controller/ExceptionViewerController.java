package com.rongji.dfish.framework.plugin.exception.controller;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.controller.BaseController;
import com.rongji.dfish.framework.plugin.exception.entity.PubExptRecord;
import com.rongji.dfish.framework.plugin.exception.service.ExceptionManager;
import com.rongji.dfish.framework.plugin.exception.service.ExceptionTypeInfo;
import com.rongji.dfish.framework.plugin.exception.service.ExceptionViewerService;
import com.rongji.dfish.framework.plugin.exception.service.StackInfo;
import com.rongji.dfish.framework.plugin.exception.view.ExceptionViewerView;
import com.rongji.dfish.ui.command.AlertCommand;
import com.rongji.dfish.ui.command.CommandGroup;
import com.rongji.dfish.ui.command.ReplaceCommand;
import com.rongji.dfish.ui.json.J;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.layout.grid.Td;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.Html;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@RequestMapping("/exceptionViewer")
@Controller
public class ExceptionViewerController extends BaseController {

	@Autowired
	private ExceptionViewerService exceptionViewerService;
	@Autowired
	private ExceptionViewerView exceptionViewerView;

	@Override
	protected int getPageSize() {
		return 50;
	}

	@RequestMapping("/index")
	@ResponseBody
	public Object index(HttpServletRequest request) throws Exception {
		// FIXME
		// 临时补一个方法,作为入口地址,以免其他系统已经调用了还要改入口地址,此方法与showAsLog相同,下次做改造的时候需要验证测试,并将showAsLog方法去除
		Page page = getPage(request);
		String typeId = request.getParameter("typeId");

		List<PubExptRecord> recs = exceptionViewerService.findRecords(typeId, page);
		View view = exceptionViewerView.buildListView(recs, page, typeId);

		return view;
	}

	@RequestMapping("/showAsLog")
	@ResponseBody
	public Object showAsLog(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Page page = getPage(request);
		String typeId = request.getParameter("typeId");

		List<PubExptRecord> recs = exceptionViewerService.findRecords(typeId, page);
		View view = exceptionViewerView.buildListView(recs, page, typeId);

		return view;
	}

	@RequestMapping("/logTurnPage")
	@ResponseBody
	public Object logTurnPage(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Page page = getPage(request);
		String typeId = request.getParameter("typeId");

		List<PubExptRecord> recs = exceptionViewerService.findRecords(typeId, page);
		CommandGroup cg = new CommandGroup();
		cg.add(new ReplaceCommand().setNode(exceptionViewerView.buildGrid(recs)));
		cg.add(new ReplaceCommand().setNode(exceptionViewerView.buildPageBar(page, typeId)));
		return cg;
	}

	@RequestMapping("/showStackTrace")
	@ResponseBody
	public Object showStackTrace(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// View view =new View();
		Tr tr = new Tr();
		StringBuilder sb = new StringBuilder();
		long typeId = Long.parseLong(request.getParameter("typeId"));
		FrameworkHelper.getDAO().queryAsAnObject("FROM PubExptType t WHERE t.typeId=?", typeId);
		ExceptionTypeInfo et = ExceptionManager.getInstance().getExceptionType(typeId);
		ExceptionTypeInfo cur = et;
		sb.append("[<a href='javascript:;' onclick=\"VM(this).reload('exceptionViewer/showAsLog?typeId=" + typeId
		        + "')\">点此查询类似的异常</a>]<br/>");
		while (true) {
			sb.append(cur.getClassName());
			sb.append("<br/>");
			for (StackInfo info : cur.getStackTrace()) {
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;at ");
				sb.append(info.getClassName());
				sb.append('.');
				Utils.escapeXMLword( info.getMethodName(),sb);
				if (info.getFileName() != null) {
					sb.append('(');
					sb.append(info.getFileName());
					if (info.getLineNumber() > 0) {
						sb.append(' ');
						sb.append(info.getLineNumber());
					}
					sb.append(')');
				}
				sb.append("<br/>");
			}
			cur = cur.getCause();
			if (cur == null) {
				break;
			}
			sb.append("Cause by ");
		}
		// view.add(new Html(sb.toString()));
		tr.setData("C1", new Td().setColspan(4).setNode(new Html(sb.toString())));

		return J.toJson(Collections.singletonList(tr));
	}

	/**
	 * 删除所有记录（四张表全部清空）
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteAllExpt")
	@ResponseBody
	public Object deleteAllExpt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		exceptionViewerService.deleteAllExpt();
		ExceptionManager.getInstance().clear(true);
		return showAsLog(request, response);
	}

	/**
	 * stack去重
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/distinct")
	@ResponseBody
	public Object distinct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		exceptionViewerService.distinct();
		ExceptionManager.getInstance().clear(false);
		return showAsLog(request, response);
	}

	/**
	 * 去重2
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/removeRepit")
	@ResponseBody
	public Object removeRepit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ExceptionManager exceptionManager = ExceptionManager.getInstance();
		exceptionManager.removerepit();
		return showAsLog(request, response);
	}

	@RequestMapping("/changeLock")
	@ResponseBody
	public Object changeLock(HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean lock = ExceptionManager.getInstance().toggleEnable();
		return new AlertCommand("异常记录开关" + (lock ? "已开启" : "已关闭")).setPosition(AlertCommand.POSITION_SOUTHEAST)
		        .setTimeout(5);
		// return showAsLog(request, response);
	}

}
