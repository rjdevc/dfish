package com.rongji.dfish.base.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.rongji.dfish.base.Utils;

/**
 * DateUtil 为日期工具类,日期格式转换
 * 
 * @author DFish Team 
 * @since dfish-base v1.2
 * @version 0.9
 * @category 工具类(util)
 */
public class DateUtil {

	/**
	 * 到日期的格式
	 */
	public static final String PATTERN_DATE = "yyyy-MM-dd";
	/**
	 * 精确到秒的格式
	 * @deprecated use PATTERN_FULL instead
	 */
	public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 精确到秒的格式
	 */
	public static final String PATTERN_FULL = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 精确到分钟的格式
	 */
	public static final String PATTERN_MINUTE = "yyyy-MM-dd HH:mm";
	/**
	 * 到月份的格式
	 */
	public static final String PATTERN_MONTH = "yyyy-MM";
	/**
	 * 到日期的格式
	 */
	public static final String PATTERN_YEAR = "yyyy";
	

	/**
	 * 时间级别-精确到秒的格式
	 * 该级别可以用来 转化成文本 也可以用来解析成时间
	 */
	public static final int LV_FULL = 1;
	/**
	 * 时间级别-精确到分钟的格式
	 * 该级别可以用来 转化成文本 也可以用来解析成时间
	 */
	public static final int LV_MINUTE = 2;
	/**
	 * 时间级别-月到日的格式
	 * 该级别可以用来 转化成文本 也可以用来解析成时间
	 */
	public static final int LV_MINUTE_SHORT = 3;
	/**
	 * 时间级别-精确到日期的格式
	 * 该级别可以用来 转化成文本 也可以用来解析成时间
	 */
	public static final int LV_DATE = 4;

	/**
	 * 时间级别-通用列表格式， 如果是今年 显示 MM-dd HH:ss
	 * 否则显示 yyyy-MM-dd
	 * 该级别仅可以用来 转化成文本
	 */
	public static final int LV_LIST = 6;
	/**
	 * 时间级别-列表扩展格式， 可以更加友好的根据语言，显示更合适的时间格式
	 * 如今天 13:00等
	 * 该级别仅可以用来 转化成文本
	 */
	public static final int LV_LIST_ADV = 7;
	
	/**
     * 时间级别-列表扩展格式， 可以更加友好的根据语言，显示更合适的时间格式
     * 如今天内显示HH:mm；今年内显示M月d日；往年显示yyyy年M年d日
     * 该级别仅可以用来 转化成文本
     */
	public static final int LV_LIST_ADV_SHORT = 8;

	/**
     * 时间级别-精确到月份的格式yyyy-MM
	 * 该级别可以用来 转化成文本 也可以用来解析成时间
     */
	public static final int LV_MONTH = 9;
	/**
     * 时间级别-精确到年的格式yyyy
	 * 该级别可以用来 转化成文本 也可以用来解析成时间
     */
	public static final int LV_YEAR = 10;

	private static int defaultLevel = LV_MINUTE;
	private static Locale defaultLocale = Locale.getDefault();

	private static final DateFormat SDF_FULL = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final DateFormat SDF_MINUTE_SHORT = new SimpleDateFormat(
			"MM-dd HH:mm");
	private static final DateFormat SDF_MINUTE = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private static final DateFormat SDF_DATE = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final DateFormat SDF_MONTH = new SimpleDateFormat(
			"yyyy-MM");
	private static final DateFormat SDF_YEAR = new SimpleDateFormat(
			"yyyy");

	private static Map<String, DateFormat> dfMap = Collections.synchronizedMap(new HashMap<String, DateFormat>());
	
	private static final int MAP_SIZE = 512;
	
	
	/**
	 * 设置默认字符集。这个字符集是全局的。它会影响显示的文字
	 * 但它并不影响时区。时区还是服务器所设置的时区
	 * @param loc
	 */
	public static void setDefaultLocale(Locale loc) {
		if (loc == null) {
			defaultLocale = Locale.getDefault();
		} else {
			defaultLocale = loc;
		}
	}
	
	/**
	 * 设置默认的显示级别。这个级别只影响没有指定显示级别的调用方法。
	 * @param level 级别
	 * @see #format(Date)
	 * @see #LV_DATE
	 * @see #LV_FULL
	 * @see #LV_LIST
	 * @see #LV_LIST_ADV
	 * @see #LV_MINUTE
	 */
	public static void setDefaultLevel(int level) {
		defaultLevel = level;
	}

	/**
	 * 格式化时间
	 * @param date 时间，如果为空则直接返回 空字符串
	 * @param level 显示级别
	 * @param loc 语言
	 * @return String
	 */
	public static String format(Date date, int level, Locale loc) {
		if (date == null) {
			return "";
		}
		loc = (loc == null) ? defaultLocale : loc;
		switch (level) {
		case LV_FULL:
			synchronized (SDF_FULL) {
				return SDF_FULL.format(date);
			}
		case LV_MINUTE:
			synchronized (SDF_MINUTE) {
				return SDF_MINUTE.format(date);
			}
		case LV_MINUTE_SHORT:
			synchronized (SDF_MINUTE_SHORT) {
				return SDF_MINUTE_SHORT.format(date);
			}
		case LV_DATE:
			synchronized (SDF_DATE) {
				return SDF_DATE.format(date);
			}
		case LV_MONTH:
			synchronized (SDF_MONTH) {
				return SDF_MONTH.format(date);
			}
		case LV_YEAR:
			synchronized (SDF_YEAR) {
				return SDF_YEAR.format(date);
			}
		case LV_LIST:
			return formatForList(date, loc);
		case LV_LIST_ADV:
			return formatForListAdv(date, loc);
		case LV_LIST_ADV_SHORT:
		    return formatForListAdvShort(date, loc);
		default:
			if (level != DateUtil.defaultLevel) {
				return format(date, defaultLevel, loc);
			}
			return null;
		}
	}
	
	/**
	 * 用默认语言 格式化时间
	 * @param date 时间，如果为空则直接返回 空字符串
	 * @param level 显示级别
	 * @return String
	 */
	public static String format(Date date, int level) {
	    return format(date, level, defaultLocale);
	}

	private static String formatForList(Date date, Locale loc) {
		Calendar cal = Calendar.getInstance(loc);
		int currYear = cal.get(Calendar.YEAR);
		cal.setTime(date);
		int dateYear = cal.get(Calendar.YEAR);
		if (currYear == dateYear) {
			return SDF_MINUTE_SHORT.format(date);
		} else {
			return SDF_DATE.format(date);
		}
	}

	private static final int ADV_LV_OTHERS = 0;

	private static final int ADV_LV_YESTERDAY = 1;
	private static final int ADV_LV_TODAY = 2;
	private static final int ADV_LV_TOMORROW = 3;

	// private static final int ADV_LV_LAST_MONTH = 4;
	private static final int ADV_LV_THIS_MONTH = 5;
	// private static final int ADV_LV_NEXT_MONTH = 6;

	private static final int ADV_LV_LAST_YEAR = 7;
	private static final int ADV_LV_THIS_YEAR = 8;
	private static final int ADV_LV_NEXT_YEAR = 9;

	private static String formatForListAdv(Date date, Locale loc) {
		int levelDate = getDateLevelForListAdv(date, loc);

		if ("zh".equals(loc.getLanguage())) {
			DateFormat sdfDay = new SimpleDateFormat(" HH:mm");
			DateFormat sdfMonth = new SimpleDateFormat("d日 HH:mm");
			DateFormat sdfYear = new SimpleDateFormat("M月d日");
			DateFormat sdfOthers = new SimpleDateFormat("yyyy年M月d日");
			switch (levelDate) {
			case ADV_LV_TODAY:
				return sdfDay.format(date);
			case ADV_LV_YESTERDAY:
				return "昨天" + sdfDay.format(date);
			case ADV_LV_TOMORROW:
				return "明天" + sdfDay.format(date);
			case ADV_LV_THIS_MONTH:
				return sdfMonth.format(date);
			case ADV_LV_THIS_YEAR:
				return sdfYear.format(date);
			case ADV_LV_LAST_YEAR:
				return "去年" + sdfYear.format(date);

			case ADV_LV_NEXT_YEAR:
				return "明年" + sdfYear.format(date);
			default:
				return sdfOthers.format(date);
			}

		} else {
			// Default:English
			DateFormat sdfDay = new SimpleDateFormat("hh:mm a");
			DateFormat sdfYear = new SimpleDateFormat("MMM d");
			DateFormat sdfOthers = SDF_DATE;
			switch (levelDate) {
			case ADV_LV_YESTERDAY:
				return "Yesterday, " + sdfDay.format(date);
			case ADV_LV_TODAY:
				return sdfDay.format(date);
			case ADV_LV_TOMORROW:
				return "Tomorrow, " + sdfDay.format(date);
			case ADV_LV_THIS_MONTH:
			case ADV_LV_THIS_YEAR:
				return sdfYear.format(date);
			case ADV_LV_LAST_YEAR:
				return "Last Year" + sdfYear.format(date);
			case ADV_LV_NEXT_YEAR:
				return "Next Year" + sdfYear.format(date);
			default:
				synchronized (sdfOthers) {
					return sdfOthers.format(date);
				}
			}
		}
	}
	
	private static int getDateLevelForListAdvShort(Date date, Locale loc) {
        Calendar cal = Calendar.getInstance(loc);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date todayBegin = cal.getTime();

        long dateMs = date.getTime();
        long currMs = System.currentTimeMillis();
        if (dateMs < currMs) { // 小于目前时间
            // 今天0时
            if (todayBegin.before(date)) { // 今天
                return ADV_LV_TODAY;
            } else {
                // 今年1月1日
                cal.set(Calendar.DAY_OF_YEAR, 1);
                Date yearBegin = cal.getTime();
                if (yearBegin.before(date)) { // 今年
                    return ADV_LV_THIS_YEAR;
                }
            }
        } else { // 超过目前时间
            // 这里结束的时间点都是第二天的0时
            cal.add(Calendar.DAY_OF_YEAR, 1);
            // 第二天0时
            Date todayEnd = cal.getTime();
            if (todayEnd.after(date)) { // 今天
                return ADV_LV_TODAY;
            } else {
                cal.setTime(todayBegin);
                cal.set(Calendar.DAY_OF_YEAR, 1);
                cal.add(Calendar.YEAR, 1);
                Date thisYearEnd = cal.getTime();
                if (thisYearEnd.after(date)) { // 今年
                    return ADV_LV_THIS_YEAR;
                }
            }
        }
        // 其他级别
        return ADV_LV_OTHERS;
	}
	
	private static String formatForListAdvShort(Date date, Locale loc) {
        int levelDate = getDateLevelForListAdvShort(date, loc);
        if ("zh".equals(loc.getLanguage())) {
            DateFormat sdfDay = new SimpleDateFormat("HH:mm");
//            DateFormat sdfYear = new SimpleDateFormat("M-d");
            DateFormat sdfOthers = new SimpleDateFormat("yyyy-M-d");
            switch (levelDate) {
            case ADV_LV_TODAY:
                return sdfDay.format(date);
//            case ADV_LV_THIS_YEAR:
//                return sdfYear.format(date);
            default:
                return sdfOthers.format(date);
            }

        } else {
            // Default:English
            DateFormat sdfDay = new SimpleDateFormat("HH:mm");
            DateFormat sdfYear = new SimpleDateFormat("M-d");
            DateFormat sdfOthers = new SimpleDateFormat("yyyy-M-d");
            switch (levelDate) {
            case ADV_LV_TODAY:
                return sdfDay.format(date);
            case ADV_LV_THIS_YEAR:
                return sdfYear.format(date);
            default:
                return sdfOthers.format(date);
            }
        }
    }

	/**
	 * 取得时间级别
	 * <p>
	 * 级别说明:(0为其他级别)
	 * <ol>
	 * <li>昨天</li>
	 * <li>今天</li>
	 * <li>明天</li>
	 * <li>上月</li>
	 * <li>本月</li>
	 * <li>下月</li>
	 * <li>去年</li>
	 * <li>今年</li>
	 * <li>明年</li>
	 * </ol>
	 * </p>
	 * 
	 * @param date
	 * @param loc
	 */
	private static int getDateLevelForListAdv(Date date, Locale loc) {

		Calendar cal = Calendar.getInstance(loc);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date todayBegin = cal.getTime();

		long dateMs = date.getTime();
		long currMs = System.currentTimeMillis();
		if (dateMs < currMs) { // 小于目前时间
			// 今天0时
			if (todayBegin.before(date)) { // 今天
				return ADV_LV_TODAY;
			} else {
				cal.add(Calendar.DAY_OF_YEAR, -1);
				// 昨天0时
				Date yesterdayBegin = cal.getTime();
				if (yesterdayBegin.before(date)) { // 昨天
					return ADV_LV_YESTERDAY;
				} else {
					// 设置当日时间,防止时间被移到上月
					cal.setTime(todayBegin);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					// 本月1日
					Date thisMonthBegin = cal.getTime();
					if (thisMonthBegin.before(date)) { // 本月
						return ADV_LV_THIS_MONTH;
					} else {
						// 上月1日

						// 设置本月1日,防止时间已经向前移到去年
						cal.setTime(thisMonthBegin);
						// 今年1月1日
						cal.set(Calendar.DAY_OF_YEAR, 1);
						Date yearBegin = cal.getTime();
						if (yearBegin.before(date)) { // 今年
							return ADV_LV_THIS_YEAR;
						} else {
							// 去年1月1日
							cal.add(Calendar.YEAR, -1);
							Date lastYearBegin = cal.getTime();
							if (lastYearBegin.before(date)) { // 去年
								return ADV_LV_LAST_YEAR;
							}
						}
					}
				}
			}
		} else { // 超过目前时间
			// 这里结束的时间点都是第二天的0时

			cal.add(Calendar.DAY_OF_YEAR, 1);
			// 第二天0时
			Date todayEnd = cal.getTime();
			if (todayEnd.after(date)) { // 今天
				return ADV_LV_TODAY;
			} else {
				cal.add(Calendar.DAY_OF_YEAR, 1);
				Date tomorrowEnd = cal.getTime();
				if (tomorrowEnd.after(date)) { // 明天
					return ADV_LV_TOMORROW;
				} else {
					cal.setTime(todayBegin);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					cal.add(Calendar.MONTH, 1);
					Date thisMonthEnd = cal.getTime();
					if (thisMonthEnd.after(date)) { // 本月
						return ADV_LV_THIS_MONTH;
					} else {

						cal.setTime(todayBegin);
						cal.set(Calendar.DAY_OF_YEAR, 1);
						cal.add(Calendar.YEAR, 1);
						Date thisYearEnd = cal.getTime();
						if (thisYearEnd.after(date)) { // 今年
							return ADV_LV_THIS_YEAR;
						} else {
							cal.add(Calendar.YEAR, 1);
							Date nextYearEnd = cal.getTime();
							if (nextYearEnd.after(date)) { // 明年
								return ADV_LV_NEXT_YEAR;
							}
						}
					}
				}
			}
		}
		// 其他级别
		return ADV_LV_OTHERS;
	}
	/**
	 * 用默认语言 默认级别 格式化时间
	 * @param date 时间，如果为空则直接返回 空字符串
	 * @return String
	 */
	public static String format(Date date) {
		return format(date, defaultLevel, defaultLocale);
	}

	/**
	 * 用指定的格式转时间 
	 * @param date Date
	 * @param pattern String 如果yyyy-MM-dd
	 * @return
	 */
	public static String format(Date date, String pattern) {
		if (date == null || Utils.isEmpty(pattern)) {
			return "";
		}
		String str = "";
		try {
			DateFormat format = dfMap.get(pattern);
			if (format == null) {
				if (dfMap.size() > MAP_SIZE) {
					dfMap.clear();
				}
				format = new SimpleDateFormat(pattern);
				dfMap.put(pattern, format);
			}
			synchronized (format) {
				str = format.format(date);
			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return str;
	}

	/**
	 * 解析时间
	 * @param str 时间表达式
	 * @param level 级别
	 * @return Date
	 * @throws ParseException
	 */
	public static Date parse(String str, int level)
			throws ParseException {
		if(str==null||str.equals("")){
			return null;
		}
		switch (level) {
		case LV_FULL:
			synchronized (SDF_FULL) {
				return SDF_FULL.parse(str);
			}
		case LV_MINUTE:
			synchronized (SDF_MINUTE) {
				return SDF_MINUTE.parse(str);
			}
		case LV_DATE:
			synchronized (SDF_DATE) {
				return SDF_DATE.parse(str);
			}
		case LV_MONTH:
			synchronized (SDF_MONTH) {
				return SDF_MONTH.parse(str);
			}
		case LV_YEAR:
			synchronized (SDF_YEAR) {
				return SDF_YEAR.parse(str);
			}
		case LV_LIST:
		case LV_LIST_ADV:
			throw new java.lang.UnsupportedOperationException(
					"can NOT parse date using LIST / LIST_ADVANCE level.");
		default:
			return SDF_DATE.parse(str);
		}
	}

	/**
	 * 解析时间
	 * 该方法只能解析三种格式的时间
	 * <ul>
	 * <li>yyyy-MM-dd HH:mm:ss</li>
	 * <li>yyyy-MM-dd HH:mm</li>
	 * <li>yyyy-MM-dd</li>
	 * </ul>
	 * 如果是yyyy-MM-dd HH:mm:ss.SSS格式也能解析，但是毫秒会被忽略。
	 * 这个方法性能要比精确指定时间格式的低一些。
	 * @param str 时间表达式
	 * @return Date
	 * @throws ParseException
	 */
	public static Date parse(String str) throws ParseException {
	    if (str == null || "".equals(str)) {
	        return null;
	    }
	    String[] strArray = str.split(":");
	    if (strArray.length == 2) {
	        synchronized (SDF_MINUTE) {
	            return SDF_MINUTE.parse(str);
	        }
	    } else if (strArray.length == 3) {
	        synchronized (SDF_FULL) {
	            return SDF_FULL.parse(str);
	        }
	    } else {
	    	strArray = str.split("-");
	    	if (strArray.length == 2) {
	    		 synchronized (SDF_MONTH) {
	                return SDF_MONTH.parse(str);
	            }
	    	}else if (strArray.length == 3) {
	    		 synchronized (SDF_DATE) {
		             return SDF_DATE.parse(str);
		         }
		    }else {
	    		 synchronized (SDF_YEAR) {
		             return SDF_YEAR.parse(str);
		         }
		    }
	    }
	}

	/**
	 * 用指定的格式转时间 
	 * @param str 时间表达式 
	 * @param pattern 时间格式表达式 如 yyyy-MM-dd
	 * @return Date
	 * @throws ParseException
	 */
	public static Date parse(String str, String pattern) throws ParseException {
		DateFormat format = new SimpleDateFormat(pattern);
		return format.parse(str);
	}
	
	/**
	 * 取得某天开始时间。即某天的0点0分
	 * 如果时间为空，则取得今天的开始时间
	 * @param date Date
	 * @return Date
	 */
	public static Date getBeginTimeOfDay(Date date){
		Calendar cal=Calendar.getInstance(DateUtil.defaultLocale);
		if(date!=null){
			cal.setTime(date);
		}
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

}
