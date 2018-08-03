package com.rongji.dfish.ui.widget;

import java.beans.Transient;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.PubHolder;

/**
 * 用于表示日历的控件
 * 它提供4种选择，
 * <ol>
 * <li>列出某个月的所有天的情况</li>
 * <li>列出某年的所有周的情况</li>
 * <li>列出某年的所有月的情况</li>
 * <li>列出年度列表的情况</li>
 * </ol>
 * @author DFish Team
 * @version 2.0
 * @since xmltmpl 2.0
 */
public class CalendarPanel extends AbstractWidget<CalendarPanel>implements PubHolder<CalendarPanel,CalendarItem> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7016518294135279513L;

	/**
	 * 列出某个月的所有天
	 */
	public static final String TYPE_DATE="calendar/date";
	/**
	 * 列出某年的所有周
	 */
	public static final String TYPE_WEEK="calendar/week";
	/**
	 * 列出某年的所有月
	 */
	public static final String TYPE_MONTH="calendar/month";
	/**
	 * 年度列表
	 */
	public static final String TYPE_YEAR="calendar/year";
	/**
	 * 构造函数
	 * @param id 编号
	 * @param type 类型
	 * @see #TYPE_DATE
	 * @see #TYPE_MONTH
	 * @see #TYPE_WEEK
	 * @see #TYPE_YEAR
	 */
	public CalendarPanel(String id,String type){
		this.id=id;
		this.type=type;
	}
	private String type;
	private String date;
	private String focusdate;
	private String src;
	private CalendarCss css=new CalendarCss();
	private CalendarItem pub;
	
	/**
	 * 以此日期为基准显示一个月的日期。
	 * 格式 yyyy-mm-dd  当 TYPE_DATE
	 * 格式 yyyy-mm  当 TYPE_MONTH
	 * 格式 yyyy-ww  当 TYPE_WEEK
	 * 格式 yyyy  当 TYPE_YEAR
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
	 * @param date String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public CalendarPanel setDate(String date) {
		this.date = date;
		return this;
	}
	/**
	 * 高亮显示的某一日期。
	 * 格式 yyyy-mm-dd 当 TYPE_DATE
	 * 格式 yyyy-mm  当 TYPE_MONTH
	 * 格式 yyyy-ww  当 TYPE_WEEK
	 * 格式 yyyy  当 TYPE_YEAR
	 * @return String
	 */
	public String getFocusdate() {
		return focusdate;
	}
	/**
	 * 高亮显示的某一日期。
	 * 格式 yyyy-mm-dd 当 TYPE_DATE
	 * 格式 yyyy-mm  当 TYPE_MONTH
	 * 格式 yyyy-ww  当 TYPE_WEEK
	 * 格式 yyyy  当 TYPE_YEAR
	 * @param focusdate String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public CalendarPanel setFocusdate(String focusdate) {
		this.focusdate = focusdate;
		return this;
	}
	/**
	 * 点击日期将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表日期。
	 * @return String
	 */
	public String getSrc() {
		return src;
	}
	/**
	 * 点击日期将通过ajax访问此地址。后台应返回一个 command。支持 $0 变量代表日期。
	 * @param src String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public CalendarPanel setSrc(String src) {
		this.src = src;
		return this;
	}
	/**
	 * 设置css的值
	 * @param value String
	 * @return 本身，这样可以继续设置其他属性
	 * @see CalendarCss#setValue(String)
	 */
	public CalendarPanel setCssValue(String value){
		css.setValue(value);
		return this;
	}
	/**
	 * 取得css的值
	 * @return 本身，这样可以继续设置其他属性
	 * @see CalendarCss#setValue(String)
	 */
	@Transient
	public String getCssValue(){
		return css.getPrototype().get("value");
	}
	/**
	 * 设置css的关键字的值
	 * @param key char
	 * @param style String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public CalendarPanel setCss(char key,String style){
		css.set(key, style);
		return this;
	}
	/**
	 * 取得css
	 * @return CalendarCss
	 */
	public CalendarCss getCss(){
		return css;
	}

	/**
	 * 日期按钮的公共设置。
	 * 范例: 点击日期按钮显示日期值。
	 * var opt = { type: 'canlendar/date', pub: { on: { click: 'alert(this.val())' } } }
	 * @return CalendarItem
	 */
	public CalendarItem getPub() {
		if (pub == null) {
			pub = new CalendarItem();
		}
		return pub;
	}
	/**
	 * 日期按钮的公共设置。
	 * 范例: 点击日期按钮显示日期值。
	 * var opt = { type: 'canlendar/date', pub: { on: { click: 'alert(this.val())' } } }
	 * @param pub CalendarItem
	 * @return 本身，这样可以继续设置其他属性
	 */
	public CalendarPanel setPub(CalendarItem pub) {
		this.pub = pub;
		return this;
	}

//	"type": "calendar/date", "date": "2015-01-01", "focusdate": "2015-01-15", "src": "webapp/test/demo.json.php?act=calendar&date=$0", "css": { "value": "NNNNNNNNNYNNNNNNNNYYYYYYYNNNNNN", "Y" : "color:green;font-weight:bold" }, "defaults": { "on": { "click": "alert(this.val())" } }
	public String getType() {
		return type;
	}
	
}
