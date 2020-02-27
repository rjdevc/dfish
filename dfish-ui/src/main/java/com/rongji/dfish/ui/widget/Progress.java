package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.*;

/**
 * 进度条
 *
 * @author DFish team
 */
public class Progress extends AbstractPubNodeContainer<Progress, Progress.Item> implements HasText<Progress>, LazyLoad<Progress> {

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
    public Progress(String id) {
        super(id);
    }
    public Progress() {
        super(null);
    }

    @Override
    protected Item newPub() {
        return new Item(null);
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
    public Progress setSrc(String src) {
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
    public Progress setGuide(String guide) {
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

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public Progress setFormat(String format) {
        this.format = format;
        return this;
    }

    @Override
    public String getComplete() {
        return complete;
    }

    @Override
    public Progress setComplete(String complete) {
        this.complete = complete;
        return this;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public Progress setError(String error) {
        this.error = error;
        return this;
    }

    @Override
    public String getSuccess() {
        return success;
    }

    @Override
    public Progress setSuccess(String success) {
        this.success = success;
        return this;
    }

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    public Progress setFilter(String filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public Progress setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    @Override
    public Boolean getEscape() {
        return escape;
    }

    @Override
    public Progress setSync(Boolean sync) {
        this.sync = sync;
        return this;
    }

    @Override
    public Boolean getSync() {
        return sync;
    }

    /**
     * 进度项,用于展示进度情况
     *
     * @author lamontYu
     * @date 2019-09-30 14:31
     * @since 5.0
     */
    public static class Item extends AbstractWidget<Item> implements HasText<Item> {

        private Number percent;
        private String text;
        private Boolean escape;
        private String format;
        private String range;

        public Item(String id) {
            setId(id);
        }

        public Item(String id, Number percent) {
            setId(id);
            setPercent(percent);
        }

        @Override
        public String getType() {
            return "ProgressItem";
        }

        /**
         * 进度值。范围从 0 到 100。
         *
         * @return Number
         */
        public Number getPercent() {
            return percent;
        }

        /**
         * 进度值。范围从 0 到 100。
         *
         * @param percent Number
         * @return 本身，这样可以继续设置其他属性
         */
        public Item setPercent(Number percent) {
            this.percent = percent;
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
        public Item setText(String text) {
            this.text = text;
            return this;
        }

        /**
         * 是否对html内容转义。默认值为true。
         *
         * @return Boolean
         */
        @Override
        public Boolean getEscape() {
            return escape;
        }

        /**
         * 是否对html内容转义。默认值为true。
         *
         * @param escape Boolean
         * @return 本身，这样可以继续设置其他属性
         */
        @Override
        public Item setEscape(Boolean escape) {
            this.escape = escape;
            return this;
        }

        @Override
        public String getFormat() {
            return format;
        }

        @Override
        public Item setFormat(String format) {
            this.format = format;
            return this;
        }

        /**
         * 划分进度阶段的数值，用逗号隔开。每个数字都会生成该阶段的样式 "z-数值"，数值范围从 0 到 100。
         * 例如设置 range: "60,100"，那么进度在 (>=60 && <100) 范围内会存在样式 "z-60"，进度在 100 时会存在样式 "z-100"。
         * @return String
         */
        public String getRange() {
            return range;
        }

        /**
         * 划分进度阶段的数值，用逗号隔开。每个数字都会生成该阶段的样式 "z-数值"，数值范围从 0 到 100。
         * 例如设置 range: "60,100"，那么进度在 (>=60 && <100) 范围内会存在样式 "z-60"，进度在 100 时会存在样式 "z-100"。
         * @param range String
         * @return 本身，这样可以继续设置其他属性
         */
        public Item setRange(String range) {
            this.range = range;
            return this;
        }
    }
}
