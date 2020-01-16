package com.rongji.dfish.ui.plugin.player;

import com.rongji.dfish.ui.AbstractWidget;

/**
 * 流播放器
 */
public class FlowPlayer extends AbstractWidget<FlowPlayer> {

	private static final long serialVersionUID = 201444129291547600L;

	/**
	 * 构造方法
	 * @param id
	 */
	public FlowPlayer(String id) {
		super.setId(id);
	}
	/**
	 * 构造方法
	 * @param id
	 * @param src
	 */
	public FlowPlayer(String id, String src) {
		super.setId(id);
		this.src = src;
	}

	private String src;

	/**
	 * 流文件地址
	 * @return String
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * 流文件地址
	 * @param src String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public FlowPlayer setSrc(String src) {
		this.src = src;
		return this;
	}
}
