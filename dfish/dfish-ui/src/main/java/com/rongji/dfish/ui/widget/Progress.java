package com.rongji.dfish.ui.widget;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;

import java.beans.Transient;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
	private Number delay;
	private Number percent;
	private String src;
	private String text;
	private String dataFormat;
	/**
	 * 构造函数
	 * @param id String
	 * @param percent Number 0.0-100.0
	 */
	public Progress(String id, Number percent){
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
	 * @return Number
	 */
	public Number getDelay() {
		return delay;
	}
	/**
	 * 延迟访问 src 。单位:秒。
	 * @param delay Number
	 * @return this
	 */
	public Progress setDelay(Number delay) {
		this.delay = delay;
		return this;
	}

	private static Map<String, DecimalFormat> formatMap = Collections.synchronizedMap(new HashMap<String, DecimalFormat>());

	/**
	 * 进度值。范围从 0 到 100。
	 * @return Number
	 */
	public Number getPercent() {
		if (percent != null && Utils.notEmpty(dataFormat)) {
			try {
				DecimalFormat format = formatMap.get(dataFormat);
				if (format == null) {
					format = new DecimalFormat(dataFormat);
					formatMap.put(dataFormat, format);
				}
				format.format(percent);
				percent = Double.parseDouble(format.format(percent));
			} catch(Exception e) {
			}
		}
		return percent;
	}
	/**
	 * 进度值。范围从 0 到 100。
	 * @param percent Number
	 * @return this
	 */
	public Progress setPercent(Number percent) {
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

	/**
	 * 百分比数据格式化
	 * @return String
	 */
	@Transient
	public String getDataFormat() {
		return dataFormat;
	}

	/**
	 * 百分比数据格式化
	 * @param dataFormat 数据格式化
	 * @return this
	 */
	public Progress setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
		return this;
	}
}
