package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.AbstractView;
import com.rongji.dfish.ui.LazyLoad;


/**
 * 视图对象。
 * @author DFish Team
 *
 */
public class Xsrc extends AbstractView<Xsrc> implements LazyLoad<Xsrc>{

	private static final long serialVersionUID = 4639236345195733523L;

	/**
	 * 构造函数
	 * @param id String
	 */
	public Xsrc(String id) {
		super(id);
	}
	
	/**
	 * 默认构造函数
	 */
	public Xsrc() {
		super(null);
	}
	
	private String template;
	private String src;

	@Override
	public String getType() {
		return "xsrc";
	}


	public String getTemplate() {
		return template;
	}

	public Xsrc setTemplate(String template) {
		this.template = template;
		return this;
	}

	public String getSrc() {
		return src;
	}

	public Xsrc setSrc(String src) {
		this.src = src;
		return this;
	}
	
}
