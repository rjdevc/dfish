package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;

/**
 * Split 为分割线(栏)。可用于 vert, horz, menu, buttonbar 中。
 * @author DFish Team
 *
 */
public class Split extends AbstractWidget<Split> implements HasText<Split>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4972016491477561395L;
	/**
	 * 前节点
	 */
	public static final String TARGET_PREV = "prev";
	/**
	 * 后节点
	 */
	public static final String TARGET_NEXT = "next";
	
	private String text;
	private Boolean escape;
	private String icon;
	private String openicon;
	private String range;
	private String target;

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
	public String getText() {
		return text;
	}

	/**
	 * 显示文本
	 * @param text 显示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Split setText(String text) {
		this.text = text;
		return this;
	}
	
	/**
	 * 收拢图标。图片地址url，或是以点 "." 开头的样式名
	 * @return icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * 收拢图标。图片地址url，或是以点 "." 开头的样式名
	 * @param icon 图标 收拢图标
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Split setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	/**
	 * 展开图标。图片地址url，或是以点 "." 开头的样式名。
	 * @return openicon String 展开图标
	 */
	public String getOpenicon() {
		return openicon;
	}

	/**
	 * 展开图标。图片地址url，或是以点 "." 开头的样式名。
	 * @param openicon 展开图标
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Split setOpenicon(String openicon) {
		this.openicon = openicon;
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
	public String getTarget() {
		return target;
	}

	/**
	 * 指定展开收拢的节点位置。可选值有两个: "prev"(前节点), "next"(后节点)。
	 * @param target 展开收拢的节点位置
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Split setTarget(String target) {
		this.target = target;
		return this;
	}

	@Override
	public String getType() {
		return "split";
	}

	public Split setEscape(Boolean escape){
		this.escape=escape;
		return this;
	}
	public Boolean getEscape(){
		return escape;
	}
}
