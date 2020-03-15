package com.rongji.dfish.framework.mvc.controller;

import com.rongji.dfish.base.exception.Marked;
import com.rongji.dfish.base.exception.MarkedException;
import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.command.Alert;
import com.rongji.dfish.ui.command.Dialog;
import com.rongji.dfish.ui.json.JsonFormat;
import com.rongji.dfish.ui.layout.Vertical;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Html;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketException;

public class BaseUiController extends BaseActionController {

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
		if (isCustomizedLimit()) {
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
				LogUtil.error(alertMsg + "=====[Network]=====" + e.getClass().getName() + ":["+e.getMessage() + "]");
				return buildWarnAlert(alertMsg);
			}
			Throwable cause = getCause(e);
			if (cause instanceof Marked) {
				// 有DFish异常,基本上是业务的异常,所以提示相对友好些用alert
				String code = ((Marked) cause).getCode();
				if (Utils.notEmpty(code)) {
//					if (cast.getCode().startsWith("DFISH")) {
//						LogUtil.error("==========系统异常==========", e);
//					}
					alertMsg += "(" + code + ")";
				}
				if (Utils.notEmpty(cause.getMessage())) {
					alertMsg += cause.getMessage();
				}
			}
		}
		if (Utils.notEmpty(alertMsg)) {
			obj = buildWarnAlert(alertMsg);
		} else {
			if (LogUtil.getLog(this.getClass()).isDebugEnabled()) {
				obj = buildErrorDialog(e);
			} else {
				alertMsg = "系统内部错误@" + System.currentTimeMillis();
				obj = buildWarnAlert(alertMsg);
				LogUtil.error(alertMsg + "\r\n" + convert2JSON(getRequest()), e);
			}
		}
//		saveLog(loginUser, url, methodName, beginTime, length);
//		saveException(time, logContent, logUser, url, methodName);
		return obj;
	}

	/**
	 * 构建警告框窗口
	 * @param alertMsg
	 * @return
	 */
	protected Alert buildWarnAlert(String alertMsg){
		return new Alert(alertMsg);
	}

	/**
	 * 构建错误信息提示窗口
	 * @param t
	 * @return
	 */
	protected Dialog buildErrorDialog(Throwable t) {
		View view = buildErrorView(t);
		Dialog dialog = new Dialog("error", "系统提示信息", null);
		dialog.setNode(view);
		return dialog;
	}

	/**
	 * 构建错误信息的详细内容
	 * @param t
	 * @return
	 */
	protected View buildErrorView(Throwable t) {
		// errNum = ItaskException.UNKNOWN_EXCEPTION;
		String errType = "系统错误";

		Throwable cause = t;
		while (cause.getCause() != null) {
			if(cause instanceof MarkedException){
				MarkedException d=(MarkedException)cause;
				errType=d.getCode();
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
			LogUtil.warn("系统异常堆栈输出异常");
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
		LogUtil.error(errName + "\r\n" + convert2JSON(getRequest()), cause);

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

	/**
	 * 构建对话窗口
	 * @return
	 */
	protected View buildDialogView() {
		View view = new View();
		
		Vertical root = new Vertical(null);
		view.setNode(root);
		
		Html main = new Html(null).setId(ID_DIALOG_BODY).setScroll(true).setStyle("padding:10px 20px;").setHeightMinus(20).setWidthMinus(40);
		root.add(main);
		
		return view;
	}

	protected void outputJson(HttpServletResponse response, Node jsonObject) {
		outputJson(response, jsonObject.asJson());
	}

	/**
	 * 以json格式输出
	 * @param response
	 * @param content
	 */
	public static void outputJson(HttpServletResponse response, final String content) {
		FrameworkHelper.outputContent(response, content, "text/json");
		LogUtil.lazyDebug(()->"\r\n" + JsonFormat.formatJson(content));
	}

}
