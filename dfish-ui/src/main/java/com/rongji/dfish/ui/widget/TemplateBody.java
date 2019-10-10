package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;

/**
 * Description: 对话模板主体
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		YuLM
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018年5月4日 下午6:06:23		YuLM			1.0				1.0 Version
 */
public class TemplateBody extends AbstractWidget<TemplateBody>{

	private static final long serialVersionUID = 9096485147445138199L;

	@Override
	public String getType() {
		return "template/body";
	}
	
	public TemplateBody() {
		super();
	}
	
	public TemplateBody(String id) {
		setId(id);
	}

}
