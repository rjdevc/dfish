package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractPubNodeContainer;
import com.rongji.dfish.ui.auxiliary.CalendarItem;

/**
 * 用于表示日历的控件
 * 它提供4种选择，
 * <ol>
 * <li>列出某个月的所有天的情况</li>
 * <li>列出某年的所有周的情况</li>
 * <li>列出某年的所有月的情况</li>
 * <li>列出年度列表的情况</li>
 * </ol>
 *
 * @author DFish Team
 * @version 2.0
 * @since xmltmpl 2.0
 */
public class Calendar extends AbstractPubNodeContainer<Calendar, CalendarItem, CalendarItem> {
    private static final long serialVersionUID = -7016518294135279513L;

    /**
     * 列出某个月的所有天
     */
    public static final String FACE_DATE = "date";
    /**
     * 列出某年的所有周
     */
    public static final String FACE_WEEK = "week";
    /**
     * 列出某年的所有月
     */
    public static final String FACE_MONTH = "month";
    /**
     * 年度列表
     */
    public static final String FACE_YEAR = "year";

    private String face = FACE_DATE;
//    private Map<String, Item> body;
    private String date;
    private String focusDate;
    private String src;
    private Boolean fillBlank;
    private Integer start;

    /**
     * 构造函数
     *
     * @param id 编号
     * @see #FACE_DATE
     * @see #FACE_MONTH
     * @see #FACE_WEEK
     * @see #FACE_YEAR
     */
    public Calendar(String id) {
        super(id);
    }
    public Calendar() {
        super(null);
    }

    @Override
    protected CalendarItem newPub() {
        return new CalendarItem(null);
    }

    public String getFace() {
        return face;
    }

    public Calendar setFace(String face) {
        this.face = face;
        return this;
    }

    /**
     * 以此日期为基准显示一个月的日期。
     * 格式 yyyy-mm-dd  当 TYPE_DATE
     * 格式 yyyy-mm  当 TYPE_MONTH
     * 格式 yyyy-ww  当 TYPE_WEEK
     * 格式 yyyy  当 TYPE_YEAR
     *
     * @return String
     */
    public String getDate() {
        return date;
    }

    /**
     * 以此日期为基准显示一个月的日期。
     * 格式 yyyy-mm-dd  当 TYPE_DATE
     * 格式 yyyy-mm  当 TYPE_MONTH
     * 格式 yyyy-ww  当 TYPE_WEEK
     * 格式 yyyy  当 TYPE_YEAR
     *
     * @param date String
     * @return 本身，这样可以继续设置其他属性
     */
    public Calendar setDate(String date) {
        this.date = date;
        return this;
    }

    /**
     * 高亮显示的某一日期。
     * 格式 yyyy-mm-dd 当 TYPE_DATE
     * 格式 yyyy-mm  当 TYPE_MONTH
     * 格式 yyyy-ww  当 TYPE_WEEK
     * 格式 yyyy  当 TYPE_YEAR
     *
     * @return String
     */
    public String getFocusDate() {
        return focusDate;
    }

    /**
     * 高亮显示的某一日期。
     * 格式 yyyy-mm-dd 当 TYPE_DATE
     * 格式 yyyy-mm  当 TYPE_MONTH
     * 格式 yyyy-ww  当 TYPE_WEEK
     * 格式 yyyy  当 TYPE_YEAR
     *
     * @param focusDate String
     * @return 本身，这样可以继续设置其他属性
     */
    public Calendar setFocusDate(String focusDate) {
        this.focusDate = focusDate;
        return this;
    }

    /**
     * 点击日期将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表日期。
     *
     * @return String
     */
    public String getSrc() {
        return src;
    }

    /**
     * 点击日期将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表日期。
     *
     * @param src String
     * @return 本身，这样可以继续设置其他属性
     */
    public Calendar setSrc(String src) {
        this.src = src;
        return this;
    }

    /**
     * 设置为true，当日历不满6行时填补一行空白
     *
     * @return Boolean
     */
    public Boolean getFillBlank() {
        return this.fillBlank;
    }

    /**
     * 设置为true，当日历不满6行时填补一行空白
     *
     * @param fillBlank Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Calendar setFillBlank(Boolean fillBlank) {
        this.fillBlank = fillBlank;
        return this;
    }

    /**
     * 一周的第一天是星期几。可选值从1到7。仅当mode为week时本参数有效。
     * @return Integer
     */
    public Integer getStart() {
        return start;
    }

    /**
     * 一周的第一天是星期几。可选值从1到7。仅当mode为week时本参数有效。
     * @param start Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public Calendar setStart(Integer start) {
        this.start = start;
        return this;
    }

}
