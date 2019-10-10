package com.rongji.dfish.base;

import com.rongji.dfish.base.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 由于JDK里面没有封装周的运算。
 * 为了方便周的计算封装此类
 *
 * @author DFish Team
 */
public class Week implements Cloneable, java.lang.Comparable<Week> {
    /**
     * 按国际惯例，一周的开始时间为星期天
     */
    public static final int DEFAULT_FIRST_DAY_OF_WEEK = Calendar.SUNDAY;
    /**
     * 第一周有4天或4天以上在今年，则他属于今年否则属于去年。
     * 即这周超过一半的时间在今年则算今年的。
     * 由于以SUNDAY为周的第一天，所以，这个也表示，一周的工作日更多的在哪一年，则它就是那一年的。
     */
    public static final int DEFAULT_MINIMAL_DAYS_IN_FIRST_WEEK = 4;
    private Date beginDate;

    private Date endDate;

    private int firstDayOfWeek;

    private int minimalDaysInFirstWeek;

    private int weekIndex;

    private int year;

    private Week() {
    }

    /**
     * 构建一个周对象
     *
     * @param firstDayOfWeek         一周第一天是星期几
     * @param minimalDaysInFirstWeek 一年的第一周至少要有多少天
     * @param year                   年份
     * @param weekIndex              周数
     */
    public Week(int firstDayOfWeek, int minimalDaysInFirstWeek, int year, int weekIndex) {
        init(firstDayOfWeek, minimalDaysInFirstWeek, year, weekIndex);
    }

    /**
     * 构建一个周对象
     *
     * @param firstDayOfWeek         一周第一天是星期几
     * @param minimalDaysInFirstWeek 一年的第一周至少要有多少天
     * @param weekCode               周编号 如2008-02
     */
    public Week(int firstDayOfWeek, int minimalDaysInFirstWeek, String weekCode) {
        String[] s = weekCode.split("-");
        init(firstDayOfWeek, minimalDaysInFirstWeek, Integer.parseInt(s[0]), Integer.parseInt(s[1]));
    }

    /**
     * 构建一个周对象
     *
     * @param year      年份
     * @param weekIndex 周数
     */
    public Week(int year, int weekIndex) {
        init(DEFAULT_FIRST_DAY_OF_WEEK, DEFAULT_MINIMAL_DAYS_IN_FIRST_WEEK, year, weekIndex);
    }

    /**
     * 构建一个周对象
     *
     * @param weekCode String 周编号 如2008-02
     */
    public Week(String weekCode) {
        String[] s = weekCode.split("-");
        init(DEFAULT_FIRST_DAY_OF_WEEK, DEFAULT_MINIMAL_DAYS_IN_FIRST_WEEK, Integer.parseInt(s[0]), Integer.parseInt(s[1]));
    }

    /**
     * 构建一个周对象
     *
     * @param firstDayOfWeek         每周一第一天是星期几
     * @param minimalDaysInFirstWeek 第一周至少要有多少天
     * @param aDateInTheWeek         这个周里面的某一个时间点
     */
    public Week(int firstDayOfWeek, int minimalDaysInFirstWeek,
                Date aDateInTheWeek) {
        init(firstDayOfWeek, minimalDaysInFirstWeek, aDateInTheWeek);
    }

    /**
     * 构建一个周对象
     *
     * @param aDateInTheWeek 这个周里面的某一个时间点
     */
    public Week(Date aDateInTheWeek) {
        init(DEFAULT_FIRST_DAY_OF_WEEK, DEFAULT_MINIMAL_DAYS_IN_FIRST_WEEK, aDateInTheWeek);
    }

    /**
     * 取得一年的第一周
     * new Week(year,1);
     *
     * @param year 年份
     * @return Week
     */
    public static Week firstWeekOfYear(int year) {
        return new Week(year, 1);
    }

    /**
     * 取得一年的最后一周
     *
     * @param year 年份
     * @return Week
     */
    public static Week lastWeekOfYear(int year) {
        try {
            Week week = new Week(SHORT_FORMAT.parse(year + "1231"));
            if (week.getYear() > year) {
                return week.lastWeek();
            }
            return week;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 取得一年的第一周
     * new Week(firstDayOfWeek, minimalDaysInFirstWeek, year, 1);
     *
     * @param firstDayOfWeek         每周一第一天是星期几
     * @param minimalDaysInFirstWeek 第一周至少要有多少天
     * @param year                   年份
     * @return Week
     */
    public static Week firstWeekOfYear(int firstDayOfWeek, int minimalDaysInFirstWeek, int year) {
        return new Week(firstDayOfWeek, minimalDaysInFirstWeek, year, 1);
    }

    /**
     * 取得一年中的最后一个周
     *
     * @param firstDayOfWeek         每周一第一天是星期几
     * @param minimalDaysInFirstWeek 第一周至少要有多少天
     * @param year                   年份
     * @return Week
     */
    public static Week lastWeekOfYear(int firstDayOfWeek, int minimalDaysInFirstWeek, int year) {
        try {
            Week week = new Week(firstDayOfWeek, minimalDaysInFirstWeek, SHORT_FORMAT.parse(year + "1231"));
            if (week.getYear() > year) {
                return week.lastWeek();
            }
            return week;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 取得这个周的开始时间(含)，这个时间点，是属于这一周的0点0分
     *
     * @return beginDate
     */
    public Date getBeginDate() {
        return beginDate;
    }

    /**
     * 取得这个周的结束时间(不含)，这个时间也是下一周的开始。而这个时间点，是属于下一周的0点0分
     *
     * @return endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 设置一周开始时间是星期几。
     * 中国是周一，国际是周日
     *
     * @return 一周开始时间是星期几
     * @see java.util.Calendar#getFirstDayOfWeek()
     * @see java.util.Calendar#SUNDAY
     * @see java.util.Calendar#MONDAY
     * @see java.util.Calendar#TUESDAY
     * @see java.util.Calendar#WEDNESDAY
     * @see java.util.Calendar#THURSDAY
     * @see java.util.Calendar#FRIDAY
     * @see java.util.Calendar#SATURDAY
     */
    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    /**
     * 取得这个周在一年中是第几周
     *
     * @return 第几周
     */
    public int getWeekIndex() {
        return weekIndex;
    }

    /**
     * 取得这个周属于哪一年，特别是在周跨年的时候值得注意。
     *
     * @return 这个周属于哪一年
     */
    public int getYear() {
        return year;
    }

    /**
     * 设置，一年的第一周，要求有多少天是这年的，这周才算这年。
     * (1 到 7)
     * <p>碰到有一个周跨越了年分，一般有以下3种方式</p>
     * <ol>
     * <li>这周的周一是哪一年的,这周就是那一年的。这个时候这个值设置为7,即要7天都在下一年的才算是下一年的。</li>
     * <li>(默认)这周有更多的日子是在哪一年的，这周就算哪一年。这时候这个值可以设置为4</li>
     * <li>这周，只要在下一年出现过，那这周就是属于下一年的，这时候，这个值为1，JDK默认为这个值</li>
     * </ol>
     *
     * @return minimalDaysInFirstWeek
     */
    public int getMinimalDaysInFirstWeek() {
        return minimalDaysInFirstWeek;
    }

    private final static SimpleDateFormat SHORT_FORMAT = new SimpleDateFormat("yyyyMMdd");

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('W').append(year).append('-');
        if (weekIndex < 10) sb.append('0');
        sb.append(weekIndex);
        sb.append('[');
        sb.append(SHORT_FORMAT.format(beginDate));
        sb.append(',');
        sb.append(SHORT_FORMAT.format(endDate));
        sb.append(')');
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Week)) return false;
        Week cast = (Week) obj;
        return cast.firstDayOfWeek == firstDayOfWeek
                && cast.minimalDaysInFirstWeek == minimalDaysInFirstWeek
                && cast.year == year
                && cast.weekIndex == weekIndex;
    }

    @Override
    public int hashCode() {
        return (firstDayOfWeek << 12) ^ (minimalDaysInFirstWeek << 8) ^ (year << 4) ^ weekIndex;
    }

    /**
     * 周编码
     *
     * @return 周编码
     */
    public String weekCode() {
        return year + "-" + (weekIndex >= 10 ? "" : "0") + weekIndex;
    }

    private void init(int firstDayOfWeek, int minimalDaysInFirstWeek, int year, int weekIndex) {
        this.firstDayOfWeek = firstDayOfWeek;
        this.minimalDaysInFirstWeek = minimalDaysInFirstWeek;
        this.year = year;
        this.weekIndex = weekIndex;
        Calendar cal = Calendar.getInstance();
        DateUtil.resetTime(cal);
        cal.setMinimalDaysInFirstWeek(minimalDaysInFirstWeek);
        cal.setFirstDayOfWeek(firstDayOfWeek);

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, weekIndex);
        cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        beginDate = cal.getTime();
        cal.add(Calendar.DATE, 7);
        endDate = cal.getTime();
    }

    private void init(int firstDayOfWeek, int minimalDaysInFirstWeek,
                      Date aDateInTheWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
        this.minimalDaysInFirstWeek = minimalDaysInFirstWeek;
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDateInTheWeek);
        DateUtil.resetTime(cal);
        cal.setMinimalDaysInFirstWeek(minimalDaysInFirstWeek);
        cal.setFirstDayOfWeek(firstDayOfWeek);
        cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        beginDate = cal.getTime();
        weekIndex = cal.get(Calendar.WEEK_OF_YEAR);
        year = cal.get(Calendar.YEAR);

        cal.add(Calendar.DATE, 7);
        endDate = cal.getTime();
        int checkYear = cal.get(Calendar.YEAR);
        if (checkYear != year && weekIndex < 5) {
            year = checkYear;
        }

    }

    @Override
    public Week clone() {
        Week week = new Week();
        week.beginDate = (Date) beginDate.clone();
        week.endDate = (Date) endDate.clone();
        week.firstDayOfWeek = firstDayOfWeek;
        week.minimalDaysInFirstWeek = minimalDaysInFirstWeek;
        week.weekIndex = weekIndex;
        week.year = year;
        return week;
    }

    /**
     * 取得后一个星期
     *
     * @return Week
     */
    public Week nextWeek() {
        if (weekIndex < 52) {
            Week week = new Week();
            week.beginDate = (Date) endDate.clone();
            week.endDate = new Date(endDate.getTime() + 604800000L);
            week.firstDayOfWeek = firstDayOfWeek;
            week.minimalDaysInFirstWeek = minimalDaysInFirstWeek;
            week.weekIndex = weekIndex + 1;
            week.year = year;
            return week;
        } else {
            return new Week(endDate);
        }
    }

    /**
     * 取得前一个星期
     *
     * @return Week
     */
    public Week lastWeek() {
        if (weekIndex > 1) {
            Week week = new Week();
            week.beginDate = new Date(beginDate.getTime() - 604800000L);
            week.endDate = (Date) beginDate.clone();
            week.firstDayOfWeek = firstDayOfWeek;
            week.minimalDaysInFirstWeek = minimalDaysInFirstWeek;
            week.weekIndex = weekIndex - 1;
            week.year = year;
            return week;
        } else {
            return new Week(new Date(beginDate.getTime() - 604800000L));
        }
    }

    public int compareTo(Week o) {
        return beginDate.compareTo(o.beginDate);
    }
}
