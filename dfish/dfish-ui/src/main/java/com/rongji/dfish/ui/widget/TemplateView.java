package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractView;

/**
 * 对话框模板视图
 * @author DFish Team
 *
 */
public class TemplateView extends AbstractView<TemplateView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6600717230736265510L;

	public TemplateView() {
		super(null);
	}
	
	/**
	 * @param id
	 */
	public TemplateView(String id){
		super(id);
	}

	public String getType() {
		return "template/view";
	}
	
}
