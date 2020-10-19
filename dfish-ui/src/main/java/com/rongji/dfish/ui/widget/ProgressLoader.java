package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractPubNodeContainer;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.LazyLoad;
import com.rongji.dfish.ui.auxiliary.Progress;

/**
 * 进度条加载组件
 *
 * @author DFish team
 */
public class ProgressLoader extends AbstractPubNodeContainer<ProgressLoader, Progress, Progress> implements HasText<ProgressLoader>, LazyLoad<ProgressLoader> {

    private static final long serialVersionUID = -5027456322715352343L;

    private Long delay;
    private String text;
    private Boolean escape;
    private String format;
    private String guide;
    private String src;
    private Boolean sync;
    private String complete;
    private String error;
    private String success;
    private String filter;

    /**
     * 构造函数
     *
     * @param id String
     */
    public ProgressLoader(String id) {
        super(id);
    }

    /**
     * 构造函数
     */
    public ProgressLoader() {
        super(null);
    }

    @Override
    protected Progress newPub() {
        return new Progress(null);
    }

    /**
     * 延迟访问 src 。单位:毫秒。
     *
     * @return Long
     */
    public Long getDelay() {
        return delay;
    }

    /**
     * 延迟访问 src 。单位:毫秒。
     *
     * @param delay Long
     * @return 本身，这样可以继续设置其他属性
     */
    public ProgressLoader setDelay(Long delay) {
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
     *
     * @return String
     */
    @Override
    public String getSrc() {
        return src;
    }

    /**
     * 访问这个地址返回一个命令json。如果返回的是 progress json，当前实例将被替换。
     *
     * @param src String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public ProgressLoader setSrc(String src) {
        this.src = src;
        return this;
    }

    /**
     * 首次访问的地址。比src优先，且只访问一次。
     * @return String
     */
    public String getGuide() {
        return guide;
    }

    /**
     * 首次访问的地址。比src优先，且只访问一次。
     * @param guide String
     * @return 本身，这样可以继续设置其他属性
     */
    public ProgressLoader setGuide(String guide) {
        this.guide = guide;
        return this;
    }

    /**
     * 显示文本。
     *
     * @return String
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * 显示文本。
     *
     * @param text String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public ProgressLoader setText(String text) {
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

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public ProgressLoader setFormat(String format) {
        this.format = format;
        return this;
    }

    @Override
    public String getComplete() {
        return complete;
    }

    @Override
    public ProgressLoader setComplete(String complete) {
        this.complete = complete;
        return this;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public ProgressLoader setError(String error) {
        this.error = error;
        return this;
    }

    @Override
    public String getSuccess() {
        return success;
    }

    @Override
    public ProgressLoader setSuccess(String success) {
        this.success = success;
        return this;
    }

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    public ProgressLoader setFilter(String filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public ProgressLoader setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    @Override
    public Boolean getEscape() {
        return escape;
    }

    @Override
    public ProgressLoader setSync(Boolean sync) {
        this.sync = sync;
        return this;
    }

    @Override
    public Boolean getSync() {
        return sync;
    }

}
