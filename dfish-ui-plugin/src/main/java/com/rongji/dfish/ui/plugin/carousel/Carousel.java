package com.rongji.dfish.ui.plugin.carousel;

import java.util.List;

import com.rongji.dfish.ui.AbstractWidget;

/**
 * 
 * Description: 幻灯片插件
 * Copyright:   Copyright (c)2017
 * Company:     rongji
 * @author:     DFish Team - lamontYu
 * @version:    1.0
 * Create at:   2017-5-12 上午10:46:02
 * 
 * Modification History:
 * Date			Author				Version		Description
 * ------------------------------------------------------------------
 * 2017-5-12	DFish Team - lamontYu	1.0			1.0 Version
 */
public class Carousel extends AbstractWidget<Carousel> {

	private static final long serialVersionUID = -8435402044894707490L;

	/**
	 * 构造方法
	 * @param id
	 */
	public Carousel(String id) {
		super.setId(id);
	}
	
	private Integer thumbWidth;
	private Integer thumbHeight;
	private String bigWidth;
	private String bigHeight;
	private List<CarouselOption> value;

	/**
	 * 
	 * <p>描述:缩略图宽度</p>
	 * @return Integer
	 * @author DFish Team - lamontYu
	 */
	public Integer getThumbWidth() {
		return thumbWidth;
	}

	/**
	 * 
	 * <p>描述:设置缩略图宽度</p>
	 * @param thumbWidth
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - lamontYu
	 */
	public Carousel setThumbWidth(Integer thumbWidth) {
		this.thumbWidth = thumbWidth;
		return this;
	}
	
	/**
	 * 
	 * <p>描述:缩略图高度</p>
	 * @return
	 * @author DFish Team - lamontYu
	 */
	public Integer getThumbHeight() {
		return thumbHeight;
	}

	/**
	 * 
	 * <p>描述:设置缩略图高度</p>
	 * @param thumbHeight
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - lamontYu
	 */
	public Carousel setThumbHeight(Integer thumbHeight) {
		this.thumbHeight = thumbHeight;
		return this;
	}

	/**
	 * 
	 * <p>描述:大图宽度</p>
	 * @return
	 * @author DFish Team - lamontYu
	 */
	public String getBigWidth() {
		return bigWidth;
	}

	/**
	 * 
	 * <p>描述:设置大图宽度</p>
	 * @param bigWidth
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - lamontYu
	 */
	public Carousel setBigWidth(Integer bigWidth) {
    	this.bigWidth = String.valueOf(bigWidth);
		return this;
	}

	/**
	 * 
	 * <p>描述:大图高度</p>
	 * @return
	 * @author DFish Team - lamontYu
	 */
	public String getBigHeight() {
		return bigHeight;
	}

	/**
	 * 
	 * <p>描述:设置大图高度</p>
	 * @param bigHeight
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - lamontYu
	 */
	public Carousel setBigHeight(Integer bigHeight) {
    	this.bigHeight = String.valueOf(bigHeight);
		return this;
	}
	/**
	 * 
	 * <p>描述:设置大图宽度</p>
	 * @param bigWidth
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - lamontYu
	 */
	public Carousel setBigWidth(String bigWidth) {
		this.bigWidth = bigWidth;
		return this;
	}
	
	
	/**
	 * 
	 * <p>描述:设置大图高度</p>
	 * @param bigHeight
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - lamontYu
	 */
	public Carousel setBigHeight(String bigHeight) {
		this.bigHeight = bigHeight;
		return this;
	}

	/**
	 * 
	 * <p>描述:选项值</p>
	 * @return
	 * @author DFish Team - lamontYu
	 */
	public List<CarouselOption> getValue() {
		return value;
	}

	/**
	 * 
	 * <p>描述:设置选项值</p>
	 * @param value
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - lamontYu
	 */
	public Carousel setValue(List<CarouselOption> value) {
		this.value = value;
		return this;
	}

}
