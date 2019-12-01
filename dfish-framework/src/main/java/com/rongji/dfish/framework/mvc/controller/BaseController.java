package com.rongji.dfish.framework.mvc.controller;

import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.command.Alert;
import com.rongji.dfish.ui.command.Dialog;
import com.rongji.dfish.ui.layout.Vertical;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Html;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketException;

public class BaseController extends BaseActionController {

	/**
	 * 获取分页信息对象
	 *
	 * @param request http请求
	 * @return Page
	 */
	public Page getPage(HttpServletRequest request) {
		int pageSize = 0;
		int cp = 1;
		String cpStr = request.getParameter("cp");
		if (Utils.notEmpty(cpStr)) {
			cp = Integer.parseInt(cpStr);
		}
		if (cp < 1) {
            cp = 0;
        }
		if (isCustomPaginationLimit()) {
			String limit = request.getParameter("limit");
			if (Utils.notEmpty(limit)) {
				pageSize = Integer.parseInt(limit);
			}
			if (pageSize <= 0) {
				pageSize = getPageSize();
			}
		} else {
			pageSize = getPageSize();
		}
		return new Page(cp, pageSize);
	}

	/**
	 * 默认分页大小
	 *
	 * @return int
	 */
	protected int getPageSize() {
		return super.getPaginationLimit();
	}

	@ExceptionHandler
	@ResponseBody
	@Override
    public Object exception(Throwable e) {
		Object obj = null;
		String alertMsg = "";
		if (e != null ) {
			HttpServletRequest request = getRequest();
			if (request != null) {
				request.setAttribute("EXCEPTION_HANDLED", true);
			}

			if (e instanceof SocketException || (e.getCause() != null && e.getCause() instanceof SocketException)) {
				alertMsg = "网络异常@" + System.currentTimeMillis();
				FrameworkHelper.LOG.error(alertMsg + "=====[Network]=====" + e.getClass().getName() + ":["+e.getMessage() + "]");
				return buildWarnAlert(alertMsg);
			}
			DfishException cast = null;
			if (e instanceof DfishException) {
				cast = (DfishException) e;
			} else {
				Throwable t = e;
				// 防止套路深,而往下寻找DfishException 
				while (t.getCause() != null) {
					if (t == t.getCause()) {
						break;
					}
					if (t.getCause() instanceof DfishException) {
						cast = (DfishException) t.getCause();
						break;
					}
					t = t.getCause();
				}
			}
			if (cast != null) { // 有DFish异常,基本上是业务的异常,所以提示相对友好些用alert
				if (Utils.notEmpty(cast.getExceptionCode())) {
//					if (cast.getCode().startsWith("DFISH")) {
//						FrameworkHelper.LOG.error("==========系统异常==========", e);
//					}
					alertMsg += "(" + cast.getExceptionCode() + ")";
				}
				if (Utils.notEmpty(cast.getMessage())) {
					alertMsg += cast.getMessage();
				}
			}
		}
		if (Utils.notEmpty(alertMsg)) {
			obj = buildWarnAlert(alertMsg);
		} else {
			if (FrameworkHelper.LOG.isDebugEnabled()) {
				obj = buildErrorDialog(e);
			} else {
				alertMsg = "系统内部错误@" + System.currentTimeMillis();
				obj = buildWarnAlert(alertMsg);
				FrameworkHelper.LOG.error(alertMsg + "\r\n" + convert2JSON(getRequest()), e);
			}
		}
//		saveLog(loginUser, url, methodName, beginTime, length);
//		saveException(time, logContent, logUser, url, methodName);
		return obj;
	}
	
	protected Alert buildWarnAlert(String alertMsg){
		return new Alert(alertMsg);
	}
	
	protected Dialog buildErrorDialog(Throwable t) {
		View view = buildErrorView(t);
		Dialog dialog = new Dialog("error", "系统提示信息", null).setWidth(Dialog.WIDTH_LARGE).setHeight(Dialog.HEIGHT_LARGE);
		dialog.setNode(view);
		return dialog;
	}
	
	protected View buildErrorView(Throwable t) {
		// errNum = ItaskException.UNKNOWN_EXCEPTION;
		String errType = "系统错误";

		Throwable cause = t;
		while (cause.getCause() != null) {
			if(cause instanceof DfishException){
				DfishException d=(DfishException)cause;
				errType=d.getExceptionCode();
			}
			cause = cause.getCause();
		}
		String errMsg = cause.getMessage();
		String errName = "系统内部错误@" + System.currentTimeMillis();
		String stackTrace = null;
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			errName = cause.getClass().getName();
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			cause.printStackTrace(pw);
			stackTrace = sw.toString();
		} catch (Exception e) {
			FrameworkHelper.LOG.warn("系统异常堆栈输出异常");
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
		FrameworkHelper.LOG.error(errName + "\r\n" + convert2JSON(getRequest()), cause);

		View view = buildDialogView();
		
		Html main = (Html) view.findNodeById(ID_DIALOG_BODY);
		StringBuilder sb = new StringBuilder();

		if (Utils.notEmpty(stackTrace)) {
			sb.append("<div><b>错误类型：</b>").append(errType).append("</div>");
			sb.append("<div><b>错误名称：</b>").append(errName).append("</div>");
			sb.append("<div><b>错误信息：</b>&nbsp;&nbsp;&nbsp;&nbsp;").append(Utils.escapeXMLword(errMsg)).append("<br/>");
			sb.append(stackTrace.replaceAll("\r\n", "<br/>").replaceAll("\r", "<br/>").replaceAll("\n", "<br/>"));
			sb.append("</div>");
		} else {
			sb.append(errName);
		}
		main.setText(sb.toString());
		
		return view;
	}
	
	protected static final String ID_DIALOG_BODY = "dlg_body";
	
	protected View buildDialogView() {
		View view = new View();
		
		Vertical root = new Vertical(null);
		view.add(root);
		
		Html main = new Html(null).setId(ID_DIALOG_BODY).setScroll(true).setStyle("padding:10px 20px;").setHmin(20).setWmin(40);
		root.add(main);
		
		return view;
	}

}