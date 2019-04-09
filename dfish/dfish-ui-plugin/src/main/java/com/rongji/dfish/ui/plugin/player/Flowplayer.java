package com.rongji.dfish.ui.plugin.player;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.plugin.carousel.CarouselOption;

import java.util.List;

/**
 * 流播放器
 */
public class Flowplayer extends AbstractWidget<Flowplayer> {

	@Override
	public String getType() {
		return "flowplayer";
	}

	/**
	 * 构造方法
	 * @param id
	 */
	public Flowplayer(String id) {
		super.setId(id);
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
	public Flowplayer setSrc(String src) {
		this.src = src;
		return this;
	}
}
