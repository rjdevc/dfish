package com.rongji.dfish.ui.plugin.carousel;

import java.util.List;

import com.rongji.dfish.ui.AbstractWidget;

/**
 * 
 * Description: 幻灯片插件
 * Copyright:   Copyright (c)2017
 * Company:     rongji
 * @author:     DFish Team - YuLM
 * @version:    1.0
 * Create at:   2017-5-12 上午10:46:02
 * 
 * Modification History:
 * Date			Author				Version		Description
 * ------------------------------------------------------------------
 * 2017-5-12	DFish Team - YuLM	1.0			1.0 Version
 */
public class Carousel extends AbstractWidget<Carousel> {

	private static final long serialVersionUID = -8435402044894707490L;

	@Override
	public String getType() {
		return "carousel";
	}

	/**
	 * 构造方法
	 * @param id
	 */
	public Carousel(String id) {
		super.setId(id);
	}
	
	private Integer thumbwidth;
	private Integer thumbheight;
	private String bigwidth;
	private String bigheight;
	private List<CarouselOption> value;

	/**
	 * 
	 * <p>描述:缩略图宽度</p>
	 * @return Integer
	 * @author DFish Team - YuLM
	 */
	public Integer getThumbwidth() {
		return thumbwidth;
	}

	/**
	 * 
	 * <p>描述:设置缩略图宽度</p>
	 * @param thumbwidth
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - YuLM
	 */
	public Carousel setThumbwidth(Integer thumbwidth) {
		this.thumbwidth = thumbwidth;
		return this;
	}
	
	/**
	 * 
	 * <p>描述:缩略图高度</p>
	 * @return
	 * @author DFish Team - YuLM
	 */
	public Integer getThumbheight() {
		return thumbheight;
	}

	/**
	 * 
	 * <p>描述:设置缩略图高度</p>
	 * @param thumbheight
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - YuLM
	 */
	public Carousel setThumbheight(Integer thumbheight) {
		this.thumbheight = thumbheight;
		return this;
	}

	/**
	 * 
	 * <p>描述:大图宽度</p>
	 * @return
	 * @author DFish Team - YuLM
	 */
	public String getBigwidth() {
		return bigwidth;
	}

	/**
	 * 
	 * <p>描述:设置大图宽度</p>
	 * @param bigwidth
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - YuLM
	 */
	public Carousel setBigwidth(Integer bigwidth) {
    	this.bigwidth = String.valueOf(bigwidth);
		return this;
	}

	/**
	 * 
	 * <p>描述:大图高度</p>
	 * @return
	 * @author DFish Team - YuLM
	 */
	public String getBigheight() {
		return bigheight;
	}

	/**
	 * 
	 * <p>描述:设置大图高度</p>
	 * @param bigheight
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - YuLM
	 */
	public Carousel setBigheight(Integer bigheight) {
    	this.bigheight = String.valueOf(bigheight);
		return this;
	}
	/**
	 * 
	 * <p>描述:设置大图宽度</p>
	 * @param bigwidth
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - YuLM
	 */
	public Carousel setBigwidth(String bigwidth) {
		this.bigwidth = bigwidth;
		return this;
	}
	
	
	/**
	 * 
	 * <p>描述:设置大图高度</p>
	 * @param bigheight
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - YuLM
	 */
	public Carousel setBigheight(String bigheight) {
		this.bigheight = bigheight;
		return this;
	}

	/**
	 * 
	 * <p>描述:选项值</p>
	 * @return
	 * @author DFish Team - YuLM
	 */
	public List<CarouselOption> getValue() {
		return value;
	}

	/**
	 * 
	 * <p>描述:设置选项值</p>
	 * @param value
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - YuLM
	 */
	public Carousel setValue(List<CarouselOption> value) {
		this.value = value;
		return this;
	}

}
