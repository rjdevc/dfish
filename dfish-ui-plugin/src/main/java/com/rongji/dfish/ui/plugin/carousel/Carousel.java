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

	/**
	 * 动画效果-渐变 (默认正常切换效果,暂时不需要选项)
	 */
	public static final String ANIMATE_FADE = "fade";

	/**
	 * 构造方法
	 * @param id
	 */
	public Carousel(String id) {
		super.setId(id);
	}

	@Override
	public String getType() {
		return "carousel";
	}

	private Integer thumbwidth;
	private Integer thumbheight;
	private String bigwidth;
	private String bigheight;
	private List<CarouselOption> value;
	/**
	 * 皮肤,目前候选项是0,1,2,3,4(由于现在没有正规名称,暂时不设置常量)
	 */
	private String face;
	private String animate;
	private Boolean cover;
	private Boolean arrow;
	private Long time;

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

	/**
	 * 样式。目前候选项有0,1,2,3,4(由于现在没有正规名称,暂时不设置常量)
	 * @return String
	 */
	public String getFace() {
		return face;
	}

	/**
	 * 样式。目前候选项有0,1,2,3,4(由于现在没有正规名称,暂时不设置常量)
	 * @param face String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Carousel setFace(String face) {
		this.face = face;
		return this;
	}

	/**
	 * 动画效果
	 * @return String
	 */
	public String getAnimate() {
		return animate;
	}

	/**
	 * 动画效果
	 * @param animate String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Carousel setAnimate(String animate) {
		this.animate = animate;
		return this;
	}

	/**
	 * 是否铺满整个页面
	 * @return Boolean
	 */
	public Boolean getCover() {
		return cover;
	}

	/**
	 * 是否铺满整个页面
	 * @param cover Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Carousel setCover(Boolean cover) {
		this.cover = cover;
		return this;
	}

	/**
	 * 是否需要翻页箭头
	 * @return Boolean
	 */
	public Boolean getArrow() {
		return arrow;
	}

	/**
	 * 是否需要翻页箭头
	 * @param arrow Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Carousel setArrow(Boolean arrow) {
		this.arrow = arrow;
		return this;
	}

	/**
	 * 轮播间隔时间,单位:秒
	 * @return Long
	 */
	public Long getTime() {
		return time;
	}

	/**
	 * 轮播间隔时间,单位:秒
	 * @param time Long
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Carousel setTime(Long time) {
		this.time = time;
		return this;
	}
}
