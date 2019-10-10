package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.AbstractSrc;
import com.rongji.dfish.ui.LazyLoad;


/**
 * 视图对象。
 * @author DFish Team
 *
 */
public class Xsrc extends AbstractSrc<Xsrc> implements LazyLoad<Xsrc>{

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
	


	@Override
	public String getType() {
		return "xsrc";
	}
	
}
