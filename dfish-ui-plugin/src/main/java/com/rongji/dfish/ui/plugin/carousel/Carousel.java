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
	private List<Option> value;

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
	public List<Option> getValue() {
		return value;
	}

	/**
	 * 
	 * <p>描述:设置选项值</p>
	 * @param value
	 * @return 本身，这样可以继续设置其他属性
	 * @author DFish Team - lamontYu
	 */
	public Carousel setValue(List<Option> value) {
		this.value = value;
		return this;
	}

	/**
	 * 幻灯片插件中的选项
	 */
	public static class Option {

		private String url;
		private String thumbnail;
		private String text;
		private String href;

		/**
		 *
		 * <p>描述:图片链接</p>
		 * @return String
		 * @author DFish Team - lamontYu
		 */
		public String getUrl() {
			return url;
		}

		/**
		 *
		 * <p>描述:设置图片链接</p>
		 * @param url
		 * @return 本身，这样可以继续设置其他属性
		 * @author DFish Team - lamontYu
		 */
		public Option setUrl(String url) {
			this.url = url;
			return this;
		}

		/**
		 *
		 * <p>描述:缩略图</p>
		 * @return String
		 * @author DFish Team - lamontYu
		 */
		public String getThumbnail() {
			return thumbnail;
		}

		/**
		 * 设置缩略图,默认图片链接
		 * <p>描述:</p>
		 * @param thumbnail
		 * @return 本身，这样可以继续设置其他属性
		 * @author DFish Team - lamontYu
		 */
		public Option setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
			return this;
		}

		/**
		 *
		 * <p>描述:图片文本</p>
		 * @return String
		 * @author DFish Team - lamontYu
		 */
		public String getText() {
			return text;
		}

		/**
		 *
		 * <p>描述:设置图片文本</p>
		 * @param text
		 * @return 本身，这样可以继续设置其他属性
		 * @author DFish Team - lamontYu
		 */
		public Option setText(String text) {
			this.text = text;
			return this;
		}

		/**
		 *
		 * <p>描述:图片链接动作</p>
		 * @return String
		 * @author DFish Team - lamontYu
		 */
		public String getHref() {
			return href;
		}

		/**
		 *
		 * <p>描述:图片链接动作</p>
		 * @param href
		 * @return 本身，这样可以继续设置其他属性
		 * @author DFish Team - lamontYu
		 */
		public Option setHref(String href) {
			this.href = href;
			return this;
		}

	}

}
