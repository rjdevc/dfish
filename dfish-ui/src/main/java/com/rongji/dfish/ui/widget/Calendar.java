package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.PubHolder;

import java.util.LinkedHashMap;
import java.util.Map;

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
public class Calendar extends AbstractWidget<Calendar> implements PubHolder<Calendar, Calendar.Item> {
    /**
     *
     */
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
    private Map<String, Item> body;
    private String date;
    private String focusDate;
    private String src;
    private Item pub;
    private Boolean fillBlank;

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
        this.id = id;
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

//	/**
//	 * 设置css的值
//	 * @param value String
//	 * @return 本身，这样可以继续设置其他属性
//	 * @see CalendarCss#setValue(String)
//	 */
//	public CalendarPanel setCssValue(String value){
//		css.setValue(value);
//		return this;
//	}
//	/**
//	 * 取得css的值
//	 * @return 本身，这样可以继续设置其他属性
//	 * @see CalendarCss#setValue(String)
//	 */
//	@Transient
//	public String getCssValue(){
//		return css.getPrototype().get("value");
//	}
//	/**
//	 * 设置css的关键字的值
//	 * @param key char
//	 * @param style String
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public CalendarPanel setCss(char key,String style){
//		css.set(key, style);
//		return this;
//	}
//	/**
//	 * 取得css
//	 * @return CalendarCss
//	 */
//	public CalendarCss getCss(){
//		return css;
//	}

    /**
     * 日期按钮的公共设置。
     * 范例: 点击日期按钮显示日期值。
     * var opt = { type: 'canlendar/date', pub: { on: { click: 'alert(this.val())' } } }
     *
     * @return CalendarItem
     */
    @Override
    public Item getPub() {
        if (pub == null) {
            pub = new Item(null);
        }
        return pub;
    }

    /**
     * 日期按钮的公共设置。
     * 范例: 点击日期按钮显示日期值。
     * var opt = { type: 'canlendar/date', pub: { on: { click: 'alert(this.val())' } } }
     *
     * @param pub CalendarItem
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Calendar setPub(Item pub) {
        this.pub = pub;
        return this;
    }

    public Map<String, Item> getBody() {
        return body;
    }

    public Calendar setBody(Map<String, Item> body) {
        this.body = body;
        return this;
    }

    public Calendar add(String name, Item td) {
        if (body == null) {
            body = new LinkedHashMap<String, Item>();
        }
        body.put(name, td);
        return this;
    }

    public Calendar add(int name, Item td) {
        return add(String.valueOf(name), td);
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
     * Calendar里面每个元素按钮的默认值
     *
     * @author DFish Team
     */
    public static class Item extends AbstractWidget<Item> {

        private static final long serialVersionUID = 6176298189403402501L;

        private String text;
        private Boolean focus;
        private Boolean focusable;

        public Item(String text) {
            this.setText(text);
        }


        @Override
        public String getType() {
            return "CalendarItem";
        }

        /**
         * 显示内容
         *
         * @return String
         */
        public String getText() {
            return text;
        }

        /**
         * 显示内容
         *
         * @param text String
         * @return 本身，这样可以继续设置其他属性
         */
        public Item setText(String text) {
            this.text = text;
            return this;
        }

        /**
         * 是否焦点
         *
         * @return Boolean
         */
        public Boolean getFocus() {
            return focus;
        }

        /**
         * 是否聚焦
         *
         * @param focus Boolean
         * @return 本身，这样可以继续设置其他属性
         */
        public Item setFocus(Boolean focus) {
            this.focus = focus;
            return this;
        }

        /**
         * 是否可聚焦模式
         *
         * @return
         */
        public Boolean getFocusable() {
            return focusable;
        }

        /**
         * 是否可聚焦模式
         *
         * @param focusable Boolean
         * @return 本身，这样可以继续设置其他属性
         */
        public Item setFocusable(Boolean focusable) {
            this.focusable = focusable;
            return this;
        }

    }

}
