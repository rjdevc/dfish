package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.HasSrc;
import com.rongji.dfish.ui.layout.AbstractLayout;

/**
 * 进度条
 * @author DFish team
 *
 */
public class Progress extends AbstractLayout<Progress, ProgressItem> implements HasSrc<Progress> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5027456322715352343L;
	private Long delay;
	private String text;
	//	private String dataFormat;
	private String format;
	private ProgressItem pub;
	private String src;
	private String template;//http 格式的路径。
	private String complete;//js语句，在获取服务器的响应数据后调用(不论成功失败都会执行)。
	private String error;//	js语句，在获取服务器的响应数据失败后调用。
	private String success;//	js语句，在成功获取服务器的响应数据后调用。
	private String filter;//	js语句，执行该语句而不是执行命令

	/**
	 * 构造函数
	 * @param id String
	 */
	public Progress(String id){
		super(id);
	}
	
	@Override
	public String getType() {
		return "progress";
	}
	
	/**
	 * 延迟访问 src 。单位:毫秒。
	 * @return Long
	 */
	public Long getDelay() {
		return delay;
	}
	/**
	 * 延迟访问 src 。单位:毫秒。
	 * @param delay Long
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Progress setDelay(Long delay) {
		this.delay = delay;
		return this;
	}

//	private static Map<String, DecimalFormat> formatMap = Collections.synchronizedMap(new HashMap<>());

//	/**
//	 * 进度值。范围从 0 到 100。
//	 * @return Number
//	 */
//	public Number getPercent() {
//		if (percent != null && Utils.notEmpty(dataFormat)) {
//			try {
//				DecimalFormat format = formatMap.get(dataFormat);
//				if (format == null) {
//					format = new DecimalFormat(dataFormat);
//					formatMap.put(dataFormat, format);
//				}
//				format.format(percent);
//				percent = Double.parseDouble(format.format(percent));
//			} catch(Exception e) {
//			}
//		}
//		return percent;
//	}
//	/**
//	 * 进度值。范围从 0 到 100。
//	 * @param percent Number
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public Progress setPercent(Number percent) {
//		this.percent = percent;
//		return this;
//	}
//
//	/**
//	 * 隐藏进度数字
//	 * @return Boolean
//	 */
//	public Boolean getHidepercent() {
//		return hidepercent;
//	}
//
//	/**
//	 * 隐藏进度数字
//	 * @param hidepercent Boolean
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public Progress setHidepercent(Boolean hidepercent) {
//		this.hidepercent = hidepercent;
//		return this;
//	}

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
	public String getText() {
		return text;
	}
	/**
	 * 显示文本。
	 * @param text String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Progress setText(String text) {
		this.text = text;
		return this;
	}

//	/**
//	 * 百分比数据格式化
//	 * @return String
//	 */
//	@Transient
//	public String getDataFormat() {
//		return dataFormat;
//	}
//
//	/**
//	 * 百分比数据格式化
//	 * @param dataFormat 数据格式化
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public Progress setDataFormat(String dataFormat) {
//		this.dataFormat = dataFormat;
//		return this;
//	}

//	/**
//	 * 用于显示文本是否需要转义,不设置默认是true
//	 * @return Boolean
//	 */
//	public Boolean getEscape() {
//		return escape;
//	}
//
//	/**
//	 * 用于显示文本是否需要转义,不设置默认是true
//	 * @param escape Boolean
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public Progress setEscape(Boolean escape) {
//		this.escape = escape;
//		return this;
//	}

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

	public ProgressItem getPub() {
		if (pub == null) {
			pub = new ProgressItem(null);
		}
		return pub;
	}

	public Progress setPub(ProgressItem pub) {
		this.pub = pub;
		return this;
	}


	public String getTemplate() {
		return template;
	}
	public Progress setTemplate(String template) {
		this.template = template;
		return this;
	}
	public String getComplete() {
		return complete;
	}
	public Progress setComplete(String complete) {
		this.complete = complete;
		return this;
	}
	public String getError() {
		return error;
	}
	public Progress setError(String error) {
		this.error = error;
		return this;
	}
	public String getSuccess() {
		return success;
	}
	public Progress setSuccess(String success) {
		this.success = success;
		return this;
	}
	public String getFilter() {
		return filter;
	}
	public Progress setFilter(String filter) {
		this.filter = filter;
		return this;
	}
}
