package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;

/**
 * Split 为分割线(栏)。可用于 vert, horz, menu, buttonbar 中。
 * @author DFish Team
 *
 */
public class Split extends AbstractWidget<Split> implements HasText<Split>{

	private static final long serialVersionUID = 4972016491477561395L;
	/**
	 * 前节点
	 */
	public static final String HIDE_PREV = "prev";
	/**
	 * 后节点
	 */
	public static final String HIDE_NEXT = "next";
	
	private String text;
	private String format;
	private Boolean escape;
	private String collapsedIcon;
	private String expandedIcon;
	private String range;
	private String hide;
	private Boolean movable;

	/**
	 * 构造函数
	 */
	public Split() {
	}
	
	/**
	 * 构造函数
	 * @param text 显示文本
	 */
	public Split(String text) {
		this.text = text;
	}
	
	/**
	 * 显示文本
	 * @return text
	 */
	@Override
    public String getText() {
		return text;
	}

	/**
	 * 显示文本
	 * @param text 显示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public Split setText(String text) {
		this.text = text;
		return this;
	}
	@Override
	public String getFormat() {
		return format;
	}

	@Override
	public Split setFormat(String format) {
		this.format = format;
		return this;
	}

	@Override
	public Split setEscape(Boolean escape){
		this.escape=escape;
		return this;
	}
	@Override
	public Boolean getEscape(){
		return escape;
	}

	/**
	 * 收拢图标。图片地址url，或是以点 "." 开头的样式名
	 * @return icon
	 */
	public String getCollapsedIcon() {
		return collapsedIcon;
	}

	/**
	 * 收拢图标。图片地址url，或是以点 "." 开头的样式名
	 * @param collapsedIcon 图标 收拢图标
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Split setCollapsedIcon(String collapsedIcon) {
		this.collapsedIcon = collapsedIcon;
		return this;
	}

	/**
	 * 展开图标。图片地址url，或是以点 "." 开头的样式名。
	 * @return openicon String 展开图标
	 */
	public String getExpandedIcon() {
		return expandedIcon;
	}

	/**
	 * 展开图标。图片地址url，或是以点 "." 开头的样式名。
	 * @param expandedIcon 展开图标
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Split setExpandedIcon(String expandedIcon) {
		this.expandedIcon = expandedIcon;
		return this;
	}

	/**
	 * 设置拖动调整大小的前后范围
	 * @return range
	 */
	public String getRange() {
		return range;
	}

	/**
	 * 设置拖动调整大小的前后范围
	 * @param range 设置拖动调整大小的前后范围
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Split setRange(String range) {
		this.range = range;
		return this;
	}

	/**
	 * 指定展开收拢的节点位置。可选值有两个: "prev"(前节点), "next"(后节点)。
	 * @return target
	 */
	public String getHide() {
		return hide;
	}

	/**
	 * 指定展开收拢的节点位置。可选值有两个: "prev"(前节点), "next"(后节点)。
	 * @param hide 展开收拢的节点位置
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Split setHide(String hide) {
		this.hide = hide;
		return this;
	}

	/**
	 * 是否可拖动改变布局大小
	 * @return Boolean
	 */
	public Boolean getMovable() {
		return movable;
	}

	/**
	 * 是否可拖动改变布局大小
	 * @param movable Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Split setMovable(Boolean movable) {
		this.movable = movable;
		return this;
	}
}
