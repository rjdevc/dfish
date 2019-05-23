package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractSrc;

/**
 * 对话框模板视图
 * @author DFish Team
 * @deprecated 3.2 模式变换后，该组件已经不存在了
 */
@Deprecated
public class TemplateView extends AbstractSrc<TemplateView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6600717230736265510L;

	public TemplateView() {
		super(null);
	}
	
	/**
	 * 构造函数
	 * @param id String
	 */
	public TemplateView(String id){
		super(id);
	}

	public String getType() {
		return "template/view";
	}
	
}
