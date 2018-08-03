package com.rongji.dfish.ui.plugin.carousel;
/**
 * 
 * Description: 幻灯片选项
 * Copyright:   Copyright (c)2017
 * Company:     rongji
 * @author:     DFish Team - YuLM
 * @version:    1.0
 * Create at:   2017-5-12 上午10:38:14
 * 
 * Modification History:
 * Date			Author				Version		Description
 * ------------------------------------------------------------------
 * 2017-5-12	DFish Team - YuLM	1.0			1.0 Version
 */
public class CarouselOption {

	private String url;
	private String thumbnail;
	private String text;
	private String href;

	/**
	 * 
	 * <p>描述:图片链接</p>
	 * @return String
	 * @author DFish Team - YuLM
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * <p>描述:设置图片链接</p>
	 * @param url
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - YuLM
	 */
	public CarouselOption setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * 
	 * <p>描述:缩略图</p>
	 * @return String
	 * @author DFish Team - YuLM
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * 设置缩略图,默认图片链接
	 * <p>描述:</p>
	 * @param thumbnail
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - YuLM
	 */
	public CarouselOption setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
		return this;
	}

	/**
	 * 
	 * <p>描述:图片文本</p>
	 * @return String
	 * @author DFish Team - YuLM
	 */
	public String getText() {
		return text;
	}

	/**
	 * 
	 * <p>描述:设置图片文本</p>
	 * @param text
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - YuLM
	 */
	public CarouselOption setText(String text) {
		this.text = text;
		return this;
	}

	/**
	 * 
	 * <p>描述:图片链接动作</p>
	 * @return String
	 * @author DFish Team - YuLM
	 */
	public String getHref() {
		return href;
	}

	/**
	 * 
	 * <p>描述:图片链接动作</p>
	 * @param href
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - YuLM
	 */
	public CarouselOption setHref(String href) {
		this.href = href;
		return this;
	}

}
