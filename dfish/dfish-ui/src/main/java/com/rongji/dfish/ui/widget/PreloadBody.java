package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;

/**
 * Description: 对话框预装载模板主体
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
public class PreloadBody extends AbstractWidget<PreloadBody>{
	
	private static final long serialVersionUID = 5982252901121247046L;

	@Override
	public String getType() {
		return "preload/body";
	}
	
	public PreloadBody() {
		super();
	}
	
	public PreloadBody(String id) {
		setId(id);
	}

}
