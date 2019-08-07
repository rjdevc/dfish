package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;

/**
 * 展开收拢的工具条
 * @author DFish Team
 *
 */
public class Toggle extends AbstractWidget<Toggle> implements HasText<Toggle>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4964103955404250558L;

	@Override
	public String getType() {
		return "toggle";
	}
	
	/**
	 * 构造函数
	 */
	public Toggle() {
		
	}
	
	/**
	 * 构造函数
	 * @param text 文本
	 */
	public Toggle(String text) {
		this.setText(text);
	}
	
	private Boolean hr;
	private Boolean open;
	private String target;
	private String text;
	private String icon;
	private String openicon;

	/**
	 * 是否显示一条水平线
	 * @return hr
	 */
	public Boolean getHr() {
		return hr;
	}
	
	/**
	 * 是否显示一条水平线
	 * @param hr Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Toggle setHr(Boolean hr) {
		this.hr = hr;
		return this;
	}

	/**
	 * 设置初始状态为展开或收拢。如果设置了此参数，会产生一个toggle图标
	 * @return open
	 */
	public Boolean getOpen() {
		return open;
	}

	/**
	 * 设置初始状态为展开或收拢。如果设置了此参数，会产生一个toggle图标
	 * @param open 设置初始状态是否展开
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Toggle setOpen(Boolean open) {
		this.open = open;
		return this;
	}
	/**
	 * 绑定要展开收拢的 widget ID。多个用逗号隔开。
	 * @return target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * 绑定要展开收拢的 widget ID。多个用逗号隔开。
	 * @param target 绑定要展开收拢的 widget ID
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Toggle setTarget(String target) {
		this.target = target;
		return this;
	}
	/**
	 * 显示文本
	 * @return texts
	 */
	public String getText() {
		return text;
	}
	/**
	 * 显示文本
	 * @param text 显示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Toggle setText(String text) {
		this.text = text;
		return this;
	}
	/**
	 * 收缩图标
	 * @return String
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * 收缩图标
	 * @param icon  String icon
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Toggle setIcon(String icon) {
		this.icon = icon;
		return this;
	}
	/**
	 * 展开图标
	 * @return String
	 */
	public String getOpenicon() {
		return openicon;
	}
	/**
	 * 展开图标
	 * @param openicon String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Toggle setOpenicon(String openicon) {
		this.openicon = openicon;
		return this;
	}

}
