package com.rongji.dfish.framework.plugin.code.controller;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.controller.BaseController;
import com.rongji.dfish.framework.plugin.code.CheckCodeGenerator;
import com.rongji.dfish.framework.plugin.code.JigsawGenerator;
import com.rongji.dfish.framework.plugin.code.SimpleJigsawGenerator;
import com.rongji.dfish.framework.plugin.code.dto.JigsawAuthResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/checkCode")
public class CheckCodeController extends BaseController {
	@Autowired(required = false)
	private List<CheckCodeGenerator> generators;
	@Autowired(required = false)
	private JigsawGenerator jigsawGenerator;

	public JigsawGenerator getJigsawGenerator() {
		if (jigsawGenerator == null) {
			jigsawGenerator = new SimpleJigsawGenerator();
		}
		return jigsawGenerator;
	}

	public void setJigsawGenerator(JigsawGenerator jigsawGenerator) {
		this.jigsawGenerator = jigsawGenerator;
	}

	private Map<String, CheckCodeGenerator> generatorMap = new HashMap<>();

	private static final String ALIAS_DEFAULT = "DEFAULT";
	@PostConstruct
	private void init() {
		if (Utils.notEmpty(generators)) {
			for (CheckCodeGenerator generator : generators) {
				if (generator == null || Utils.isEmpty(generator.getAlias())) {
					continue;
				}
				CheckCodeGenerator old = generatorMap.put(generator.getAlias(), generator);
				if (old != null) {
					LogUtil.warn("The CheckCodeGenerator[" + old.getClass().getName() + "] is replaced by [" + generator.getClass().getName() + "]");
				}
			}
		}
		if (generatorMap.isEmpty()) {
			CheckCodeGenerator generator = getDefaultCheckCodeGenerator();
			generatorMap.put(generator.getAlias(), generator);
		}
	}

	private CheckCodeGenerator getDefaultCheckCodeGenerator() {
		CheckCodeGenerator codeGenerator = new CheckCodeGenerator();
		codeGenerator.setAlias(ALIAS_DEFAULT);
		return codeGenerator;
	}

	private CheckCodeGenerator getCheckCodeGenerator(String alias, String defaultAlias) {
		CheckCodeGenerator generator = generatorMap.get(alias);
		if (generator == null) {
			generator = generatorMap.get(defaultAlias);
			if (generator == null) {
				generator = getDefaultCheckCodeGenerator();
				generatorMap.put(generator.getAlias(), generator);
			}
		}
		return generator;
	}

	@RequestMapping("/codeImg.png")
	@ResponseBody
	public void codeImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String alias = request.getParameter("alias");
		CheckCodeGenerator codeGenerator = getCheckCodeGenerator(alias, ALIAS_DEFAULT);
		codeGenerator.drawImage(request, response);
	}

	@RequestMapping("/jigsaw")
	@ResponseBody
	public Object jigsaw(HttpServletRequest request) throws Exception {
		return getJigsawGenerator().generatorJigsaw(request);
	}

	@RequestMapping("/jigsawCheck")
	@ResponseBody
	public Object jigsawCheck(HttpServletRequest request) throws Exception {
	    Double offset = 0.0;
	    try {
			offset = Double.parseDouble(request.getParameter("offset"));
		} catch (Exception e) {
		}
		boolean result = getJigsawGenerator().checkJigsawOffset(request, offset,true);
		return new JigsawAuthResult(result);
	}

}
