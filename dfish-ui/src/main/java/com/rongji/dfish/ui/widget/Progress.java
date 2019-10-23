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
	private Boolean hidepercent;
	private String src;
	private String text;
	private String dataFormat;
	private Boolean escape;
	private String format;

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
	 * @return 本身，这样可以继续设置其他属性
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
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Progress setPercent(Number percent) {
		this.percent = percent;
		return this;
	}

	/**
	 * 隐藏进度数字
	 * @return Boolean
	 */
	public Boolean getHidepercent() {
		return hidepercent;
	}

	/**
	 * 隐藏进度数字
	 * @param hidepercent Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Progress setHidepercent(Boolean hidepercent) {
		this.hidepercent = hidepercent;
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
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Progress setSrc(String src) {
		this.src = src;
		return this;
	}
	/**
	 * 显示文本。
	 * @return String
	 */
	@Override
    public String getText() {
		return text;
	}
	/**
	 * 显示文本。
	 * @param text String
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
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
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Progress setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
		return this;
	}

	/**
	 * 用于显示文本是否需要转义,不设置默认是true
	 * @return Boolean
	 */
	public Boolean getEscape() {
		return escape;
	}

	/**
	 * 用于显示文本是否需要转义,不设置默认是true
	 * @param escape Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Progress setEscape(Boolean escape) {
		this.escape = escape;
		return this;
	}

	/**
	 * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
	 * @return String 格式化内容
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
	 * @param format String 格式化内容
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Progress setFormat(String format) {
		this.format = format;
		return this;
	}

}
