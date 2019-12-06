package com.rongji.dfish.framework.plugin.qrcode.controller;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.mvc.controller.BaseUIController;
import com.rongji.dfish.framework.util.ServletUtil;
import com.rongji.dfish.misc.qrcode.MatrixToImageWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 二维码图标图片生成
 * @author lamontYu
 * @create 2018-08-03 before
 * @since 3.0
 */
public class QRCodeController extends BaseUIController {

	@RequestMapping("/image")
	public void image(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String content = ServletUtil.getParameter(request, "content");
		String size = request.getParameter("size");
		String format = request.getParameter("format");
		format = Utils.isEmpty(format) ? "png" : format;
		MatrixToImageWriter.writeToStream(content, format, Integer.parseInt(size), response.getOutputStream());
	}
	
}
