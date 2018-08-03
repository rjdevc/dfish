package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;

/**
 * 进度条
 * @author DFish team
 *
 */
public class Progress extends AbstractWidget<Progress> implements HasText<Progress>{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = -5027456322715352343L;
	private Double delay;
	private Double percent;
	private String src;
	private String text;
	/**
	 * 构造函数
	 * @param id
	 * @param percent
	 */
	public Progress(String id,Double percent){
		super();
		setId(id);
		setPercent(percent);
	}
	
	@Override
	public String getType() {
		return "progress";
	}
	
	/**
	 * 延迟访问 src 。单位:秒。
	 * @return Double
	 */
	public Double getDelay() {
		return delay;
	}
	/**
	 * 延迟访问 src 。单位:秒。
	 * @param delay Double
	 * @return this
	 */
	public Progress setDelay(Double delay) {
		this.delay = delay;
		return this;
	}
	/**
	 * 进度值。范围从 0 到 100。
	 * @return Double
	 */
	public Double getPercent() {
		return percent;
	}
	/**
	 * 进度值。范围从 0 到 100。
	 * @param percent Double
	 * @return this
	 */
	public Progress setPercent(Double percent) {
		this.percent = percent;
		return this;
	}
	/**
	 * 访问这个地址返回一个命令json。如果返回的是 progress json，当前实例将被替换。
	 * @return String
	 */
	public String getSrc() {
		return src;
	}
	/**
	 * 访问这个地址返回一个命令json。如果返回的是 progress json，当前实例将被替换。
	 * @param src String
	 * @return this
	 */
	public Progress setSrc(String src) {
		this.src = src;
		return this;
	}
	/**
	 * 显示文本。
	 * @return String
	 */
	public String getText() {
		return text;
	}
	/**
	 * 显示文本。
	 * @param text String
	 * @return this
	 */
	public Progress setText(String text) {
		this.text = text;
		return this;
	}



}
