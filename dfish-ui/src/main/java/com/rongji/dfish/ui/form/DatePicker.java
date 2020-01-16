package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.util.DateUtil;

import java.util.Collection;

/**
 * DateTag 相对应前端的时间选择按钮，格式yyyy-MM-dd
 * 因为3.0自动代码生成需要改为更易理解的格式。
 *
 * @author DFish Team
 * @version 3.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 1.0
 */
public class DatePicker extends AbstractInput<DatePicker, String> {

    private static final long serialVersionUID = -3382217461451186149L;

    /**
     * 仅日期类型(年月日)
     */
    public static final String FORMAT_DATE = "yyyy-mm-dd";
    /**
     * 日期和时间，精确到分钟
     */
    public static final String FORMAT_DATE_TIME = "yyyy-mm-dd hh:ii";
    /**
     * 日期，仅精确到月
     */
    public static final String FORMAT_MONTH = "yyyy-mm";
    /**
     * 日期，仅精确到年
     */
    public static final String FORMAT_YEAR = "yyyy";
    /**
     * 时间，仅小时和分钟
     */
    public static final String FORMAT_TIME = "hh:ii";
    /**
     * 日期，只有月和日。
     */
    public static final String FORMAT_MONTH_DAY = "mm-dd";
    /**
     * 日期完整，包括日期时间，精确到秒
     */
    public static final String FORMAT_DATE_TIME_FULL = "yyyy-mm-dd hh:ii:ss";

    static final String[][] DATE_FORMATS = {
            {FORMAT_DATE, "yyyy-MM-dd"},
            {FORMAT_DATE_TIME, "yyyy-MM-dd HH:mm"},
            {FORMAT_MONTH, "yyyy-MM"},
            {FORMAT_YEAR, "yyyy"},
            {FORMAT_TIME, "HH:mm"},
            {FORMAT_MONTH_DAY, "MM-dd"},
            {FORMAT_DATE_TIME_FULL, "yyyy-MM-dd HH:mm:ss"}
    };
    private String format;
    private String javaFormat;
    private Boolean multiple;

    /**
     * 构造函数
     *
     * @param name  String
     * @param label String
     * @param value Object
     */
    public DatePicker(String name, String label, Object value) {
        this.setName(name);
        this.setLabel(label);
        calFormat(FORMAT_DATE);
        this.setValue(value);
        // 默认日期
    }

    /**
     * 构造函数
     *
     * @param name   String
     * @param label  String
     * @param value  Object
     * @param format String
     */
    public DatePicker(String name, String label, Object value, String format) {
        this.setName(name);
        this.setLabel(label);
        calFormat(format);
        this.setValue(value);
    }

    private void calFormat(String format) {
        for (String[] row : DATE_FORMATS) {
            if (row[0].equals(format)) {
                this.format = row[0];
                this.javaFormat = row[1];
                break;
            }
        }
        if (this.format == null) {
            String[] row = DATE_FORMATS[0];
            this.format = row[0];
            this.javaFormat = row[1];
        }
    }


    /**
     * 设置格式。该格式为JS的格式。如 yyyy-mm-dd hh:ii:ss
     *
     * @return the date Format
     * @see #FORMAT_DATE_TIME_FULL
     */
    public String getFormat() {
        return format;
    }

    /**
     * 设置格式。该格式为JS的格式。如 yyyy-mm-dd hh:ii:ss
     *
     * @param format String
     * @return this
     * @see #FORMAT_DATE_TIME_FULL
     */
    public DatePicker setFormat(String format) {
        calFormat(format);
        return this;
    }

    /**
     * 是否多选模式
     *
     * @return Boolean
     */
    public Boolean getMultiple() {
        return multiple;
    }

    /**
     * 是否多选模式
     *
     * @param multiple Boolean
     * @return this
     */
    public DatePicker setMultiple(Boolean multiple) {
        this.multiple = multiple;
        return this;
    }

    @Override
    public DatePicker setValue(Object value) {
        if (value == null) {
            this.value = null;
        } else if (value instanceof java.util.Date) {
            this.value = DateUtil.format((java.util.Date) value, javaFormat);
        } else if (value.getClass().isArray()) {
            Object[] arr = (Object[]) value;
            this.value = toString(arr);
        } else if (value instanceof Collection) {
            Collection coll = (Collection) value;
            this.value = toString(coll);
        } else {
            this.value = value.toString();
        }
        return this;
    }

    protected String toString(Iterable<?> iter) {

        StringBuilder sb = new StringBuilder();
        boolean begin = true;
        for (Object o : iter) {
            if (o == null) {
                continue;
            } else {
                if (begin) {
                    begin = false;
                } else {
                    sb.append(',');
                }
                if (o instanceof java.util.Date) {
                    sb.append(DateUtil.format((java.util.Date) o, javaFormat));
                } else {
                    sb.append(o);
                }
            }
        }
        return sb.toString();
    }

    protected String toString(Object[] iter) {
        StringBuilder sb = new StringBuilder();
        boolean begin = true;
        for (Object o : iter) {
            if (o == null) {
                continue;
            } else {
                if (begin) {
                    begin = false;
                } else {
                    sb.append(',');
                }
                if (o instanceof java.util.Date) {
                    sb.append(DateUtil.format((java.util.Date) o, javaFormat));
                } else {
                    sb.append(o);
                }
            }
        }
        return sb.toString();
    }


}
