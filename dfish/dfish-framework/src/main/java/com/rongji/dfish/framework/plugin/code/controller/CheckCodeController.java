package com.rongji.dfish.framework.plugin.code.controller;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.controller.BaseController;
import com.rongji.dfish.framework.plugin.code.CheckCodeGenerator;

@Controller
@RequestMapping("/checkCode")
public class CheckCodeController extends BaseController {

	private String imgType;
	
	public String getImgType() {
		return imgType;
	}

	public void setImgType(String imgType) {
		this.imgType = imgType;
	}

	@RequestMapping("/codeImg.png")
	@ResponseBody
	public void codeImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CheckCodeGenerator codeGenerator = null;
		try {
			codeGenerator = FrameworkHelper.getBean(CheckCodeGenerator.class);
        } catch (Exception e) {
        }
		if (codeGenerator == null) {
			codeGenerator = new CheckCodeGenerator();
		}
		String randomCode = codeGenerator.getRandomCode();
		request.getSession().setAttribute(CheckCodeGenerator.KEY_CHECKCODE, randomCode);
		BufferedImage image = codeGenerator.generate(randomCode);

		if (Utils.isEmpty(imgType)) {
			imgType = "png";
		}
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/" + imgType);
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			ImageIO.write(image, imgType, output);
        } catch (Exception e) {
	        FrameworkHelper.LOG.error("=====产生验证码图片异常=====", e);
        } finally {
        	if (output != null) {
        		output.close();
        	}
        }
	}
	
}
