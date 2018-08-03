package com.rongji.dfish.framework.plugin.qrcode.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.controller.BaseController;
import com.rongji.dfish.misc.qrcode.MatrixToImageWriter;

@RequestMapping("/qrCode")
@Controller
public class QRCodeController extends BaseController {

	@RequestMapping("/image")
	public void image(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String content = Utils.getParameter(request, "content");
		String size = request.getParameter("size");
		String format = request.getParameter("format");
		format = Utils.isEmpty(format) ? "png" : format;
		MatrixToImageWriter.writeToStream(content, format, Integer.parseInt(size), response.getOutputStream());
	}
	
}
