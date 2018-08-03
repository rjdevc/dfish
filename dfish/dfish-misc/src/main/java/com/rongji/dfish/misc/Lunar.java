package com.rongji.dfish.misc;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 负责当前日期转化为农历日期
 * <p>
 * Title: Dfish-Platform
 * </p>
 * <p>
 * Description: Dfish-Platform
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: rjsoft
 * </p>
 * 
 * @author DFish Team
 * @version 2.0
 */
public class Lunar {
    
    final static String[] CHINESE_NUMBER = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二" };
    private final static String[] CHINESE_MONTHS = { "正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬", "腊" };
    private final static String[] STEM_NAMES = { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };
    private final static String[] BRANCH_NAMES = { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" };
    private final static String[] ANIMAL_NAMES = { "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪" };
    static SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    final static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554,
            0x056a0, 0x09ad0, 0x055d2, 0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0,
            0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,
            0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550,
            0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0,
            0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263,
            0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0,
            0x195a6, 0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5,
            0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960, 0x0d954, 0x0d4a0,
            0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
            0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0,
            0x0d260, 0x0ea65, 0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520,
            0x0dd45, 0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };
    private int year;
    private int month;
    private int day;
    private boolean isLeapMonth;

    private final static int MAX_YEAR = 2050;
    public final static DateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private final static Date BASE_DATE = new Date();
//    private static int lunarYear;
//    private static int lunarMonth;
//    private static int lunarDay;
    private final static int TIME_ZONE = TimeZone.getDefault().getRawOffset() / 3600000;

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(1900, 0, 31, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        BASE_DATE.setTime(cal.getTimeInMillis());
    }

    /**
     * 创建农历对象，将新历转成农历，目前仅支持1900-2050{@link #MAX_YEAR}年之间的日期转换
     * 
     * @param cal
     * @return
     */
    public Lunar(Calendar cal) {
        if (cal != null) {
            if (cal.get(Calendar.YEAR) >= MAX_YEAR) {
                throw new RuntimeException("仅支持年份在1900-" + MAX_YEAR + "之间的日期");
            }
            Object[] result = calcLunarDate(cal);
            year = (Integer) result[0];
            month = (Integer) result[1];
            day = (Integer) result[2];
            isLeapMonth = (Boolean) result[3];
        }
    }
    
    /**
     * 新历日期,计算出农历的日期
     * @param cal 新历
     * @return 日期数组；Object[0]-农历年份，Object[1]-农历月份，Object[2]-农历日子，Object[3]-是否闰月（true是，false否），
     */
    private static Object[] calcLunarDate(Calendar cal) {
        if (cal == null) {
            cal = Calendar.getInstance();
        }
//      int yearCyl;
//        int monCyl;
//        int dayCyl;
        int leapMonth = 0;

        initZoneDateTime(cal);
        // 求出和1900年1月31日相差的天数
        int offset = (int) ((cal.getTimeInMillis() - BASE_DATE.getTime()) / 86400000L);
//        dayCyl = offset + 40;
//        monCyl = 14;

        // 用offset减去每农历年的天数
        // 计算当天是农历第几天
        // i最终结果是农历的年份
        // offset是当年的第几天
        int iYear;

        // 用offset减去每农历年的天数
        // 计算当天是农历第几天
        // i最终结果是农历的年份
        // offset是当年的第几天
        int daysOfYear = 0;

        for (iYear = 1900; (iYear < MAX_YEAR) && (offset > 0); iYear++) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
//            monCyl += 12;
        }

        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
//            monCyl -= 12;
        }

        // 农历年份
        int lunarYear = iYear;

//        yearCyl = iYear - 1864;
        leapMonth = leapMonth(iYear); // 闰哪个月,1-12
        boolean isLeapMonth = false;

        // 用当年的天数offset,逐个减去每月(农历)的天数，求出当天是本月的第几天
        int iMonth;

        // 用当年的天数offset,逐个减去每月(农历)的天数，求出当天是本月的第几天
        int daysOfMonth = 0;

        for (iMonth = 1; (iMonth < 13) && (offset > 0); iMonth++) {
            // 闰月
            if ((leapMonth > 0) && (iMonth == (leapMonth + 1)) && !isLeapMonth) {
                --iMonth;
                isLeapMonth = true;
                daysOfMonth = leapDays(lunarYear);
            } else {
                daysOfMonth = monthDays(lunarYear, iMonth);
            }

            offset -= daysOfMonth;

            // 解除闰月
            if (isLeapMonth && (iMonth == (leapMonth + 1))) {
                isLeapMonth = false;
            }

//            if (!isLeapMonth) {
//                monCyl++;
//            }
        }

        // offset为0时，并且刚才计算的月份是闰月，要校正
        if ((offset == 0) && (leapMonth > 0) && (iMonth == (leapMonth + 1))) {
            if (isLeapMonth) {
                isLeapMonth = false;
            } else {
                isLeapMonth = true;
                --iMonth;
//                --monCyl;
            }
        }

        // offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
//            --monCyl;
        }

        int lunarMonth = iMonth;
        int lunarDay = offset + 1;
        return new Object[]{lunarYear, lunarMonth, lunarDay, isLeapMonth};
    }

    /**
     * 传回农历 y年的总天数
     * 
     * @param y
     * @return
     */
    private static int yearDays(int y) {
        int i;
        int sum = 348;

        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((lunarInfo[y - 1900] & i) != 0) {
                sum += 1;
            }
        }

        return (sum + leapDays(y));
    }

    /**
     * 传回农历 y年闰月的天数
     * 
     * @param y
     * @return
     */
    private static int leapDays(int y) {
        if (leapMonth(y) != 0) {
            if ((lunarInfo[y - 1900] & 0x10000) != 0) {
                return 30;
            } else {
                return 29;
            }
        } else {
            return 0;
        }
    }

    /**
     * 传回农历 y年闰哪个月 1-12 , 没闰传回 0
     * 
     * @param y
     * @return
     */
    private static int leapMonth(int y) {
        return (int) (lunarInfo[y - 1900] & 0xf);
    }

    /**
     * 传回农历 y年m月的总天数
     * 
     * @param y
     * @param m
     * @return
     */
    private static int monthDays(int y, int m) {
        if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0) {
            return 29;
        } else {
            return 30;
        }
    }

    /**
     * 转换中文表达方式
     * 
     * @param day
     * @return
     */
    public static String getChineseDayString(int day) {
        String[] chineseTen = { "初", "十", "廿", "卅" };
        int n = ((day % 10) == 0) ? 9 : ((day % 10) - 1);

        if (day > 30) {
            return "";
        }

        if (day == 10) {
            return "初十";
        }
        // if (day == 20) {
        // return "二十";
        // }
        // if (day == 30) {
        // return "三十";
        // }
        else {
            return chineseTen[day / 10] + CHINESE_NUMBER[n];
        }
    }

    /**
     * toString
     * 
     * @return
     */
    public String toString() {
        // return // year + "年" +
        // (isLeapMonth ? "闰" : "") + (month == 1 ? "正" : chineseNumber[month - 1]) + "月" + getChinaDayString(day);
        return getChineseYearString(year) + "(" + year + ")" + getChineseMonthString(month, isLeapMonth)
                + getChineseDayString(day);
    }

    /**
     * 获得Year+Month+Day农历字符串 不用考虑闰年情况，其中已经处理了。
     * 
     * @return
     */
    public String getStrLunarDate() {
        return year + "-" + month + "-" + day;
    }

    /**
     * 获取实际农历日期<br/> String[0]-是否闰月,闰月String[0]为"1",否则为"0"<br/> String[1]-农历日期,以字符串形式来显示,格式为yyyy-MM-dd<br/>
     * 
     * @return
     */
    public String[] getActualLunarDateStr() {
        return new String[] { (isLeapMonth ? "1" : "0"), getStrLunarDate() };
    }

    /**
     * 返回农历日期CST-Date型
     * 
     * @return
     */
    public Date getLunarDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date date = sdf.parse(getStrLunarDate(), pos);

        return date;
    }

    /**
     * 返回sql-Date型
     * 
     * @return
     */
    public Date getLunarSqlDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(getLunarDate());

        return java.sql.Date.valueOf(str);
    }

    /**
     * 获取农历年的中文名称(天干地支)
     * 
     * @return
     */
    public static String getChineseYearString(int year) {
        return STEM_NAMES[(year - 4) % 10] + BRANCH_NAMES[(year - 4) % 12] + "年";
    }

    /**
     * 获取农历年所属生肖年
     * 
     * @param year
     *            农历年份
     * @return 生肖名称
     */
    public static String getChineseAnimalYearString(int year) {
        return ANIMAL_NAMES[(year - 4) % 12];
    }

    /**
     * 获取农历月的中文名称
     * 
     * @return
     */
    public static String getChineseMonthString(int month, boolean isLeapMonth) {
        return (isLeapMonth ? "闰" : "") + CHINESE_MONTHS[month - 1] + "月";
    }

    /**
     * 获取一些年份农历年份数据集合
     * 
     * @param beginLunarYear
     *            开始的农历年份(包含此年)
     * @param endLunarYear
     *            结束的农历年份(不包含此年)
     * @return 农历年份数据的集合,Object[0]-农历年份,Object[1]-农历年份中文名称
     */
    public static List<Object[]> getLunarYearList(int beginLunarYear, int endLunarYear) {
        List<Object[]> yearDataList = new ArrayList<Object[]>();
        for (int y = beginLunarYear; y < endLunarYear; y++) {
            yearDataList.add(new Object[] { y, getChineseYearString(y) + "(" + y + ")" });
        }
        return yearDataList;
    }

    /**
     * 根据农历年份获取农历月份数据的集合
     * 
     * @param lunarYear
     *            农历年份
     * @param isBirthday
     *            是否生日
     * @return 农历月份数据的集合,Object[0]-农历月份(负值表示为闰月,其余为正值),Object[1]-农历月份的中文名称
     */
    public static List<Object[]> getLunarMonthList(int lunarYear, boolean isBirthday) {
        List<Object[]> monthDataList = new ArrayList<Object[]>();
        int leapMonth = leapMonth(lunarYear);
        Calendar cal = Calendar.getInstance();
        Object[] result = calcLunarDate(cal);
        int currLunarYear = (Integer) result[0];
        int currLunarMonth = (Integer) result[1];
        boolean currMonthLeap = (Boolean) result[3];
        boolean isCurrYear = lunarYear == currLunarYear;
        for (int month = 1; month <= 12; month++) {
            monthDataList.add(new Object[] { month, getChineseMonthString(month, false) });
            if (leapMonth == month) {
                // 当前月不是闰月月份  或者当前月属于闰月
                if (!isBirthday) {
                    monthDataList.add(new Object[] { -leapMonth, getChineseMonthString(month, true) });
                } else if (!isCurrYear || currLunarMonth != leapMonth || currMonthLeap) {
                    monthDataList.add(new Object[] { -leapMonth, getChineseMonthString(month, true) });
                }
            }
            if (isBirthday && isCurrYear && month >= currLunarMonth) {
                break;
            }
        }
        return monthDataList;
    }

    /**
     * 根据农历年份和月份获取农历日数据集合
     * 
     * @param lunarYear
     *            农历年份
     * @param lunarMonth
     *            农历月份
     * @param isBirthday
     *            是否生日
     * @return 获取农历日数据集合,Object[0]-在月的第几天,Object[1]-农历日的中文名称
     */
    public static List<Object[]> getLunarDayList(int lunarYear, int lunarMonth, boolean isBirthday) {
        List<Object[]> dayDataList = new ArrayList<Object[]>();
        int monthDays = 0;
        if (lunarMonth > 0) {
            monthDays = monthDays(lunarYear, lunarMonth);
        } else if (lunarMonth < 0 && lunarMonth == -leapMonth(lunarYear)) {
            monthDays = leapDays(lunarYear);
        }
        Calendar cal = Calendar.getInstance();
        Object[] result = calcLunarDate(cal);
        int currLunarYear = (Integer) result[0];
        int currLunarMonth = (Integer) result[1];
        boolean currMonthLeap = (Boolean) result[3];
        if (currMonthLeap) {
            currLunarMonth = -currLunarMonth;
        }
        boolean isCurrMonth = (currLunarYear == lunarYear) && (currLunarMonth == lunarMonth);
        int currLunarDay = (Integer) result[2];
        for (int d = 1; d <= monthDays; d++) {
            dayDataList.add(new Object[] { d, getChineseDayString(d) });
            if (isBirthday && isCurrMonth && d >= currLunarDay) {
                break;
            }
        }
        return dayDataList;
    }

    /**
     * 根据农历生日获取几年后的生日对应的公历的日期
     * 
     * @param lunarDateStr
     *            农历生日字符串(格式如:yyyy-MM-dd)
     * @param afterYears
     *            几年后
     * @return 几年后生日的公历日期
     */
    public static Date getBirthday(String lunarDateStr, boolean isLeapMonth, int afterYears) {
        Date birthdayDate = getGregorianDate(lunarDateStr, isLeapMonth);
        return getBirthday(birthdayDate, isLeapMonth, afterYears);
    }

    /**
     * 将农历日期转换成公历日期(农历日期格式为:yyyy-MM-dd)
     * 
     * @param lunarDateStr
     *            农历日期字符串
     * @param isLeapMonth
     *            是否闰月
     * @return 公历的日期
     */
    public static Date getGregorianDate(String lunarDateStr, boolean isLeapMonth) {
        LunarDateInfo lunarDateInfo = parseLunarDateInfo(lunarDateStr, isLeapMonth);
        return getGregorianDate(lunarDateInfo);
    }

    /**
     * 将农历字符串转换成农历日期信息类
     * 
     * @param lunarDateStr
     *            农历日期字符串,格式如:yyyy-MM-dd
     * @param isLeapMonth
     *            是否闰月
     * @return
     */
    private static LunarDateInfo parseLunarDateInfo(String lunarDateStr, boolean isLeapMonth) {
        LunarDateInfo lunarDateInfo = null;
        try {
            String[] strArray = lunarDateStr.split("-");
            int year = Integer.parseInt(strArray[0]);
            int month = Integer.parseInt(strArray[1]);
            if (isLeapMonth) {
                month = -month;
            }
            int day = Integer.parseInt(strArray[2]);
            lunarDateInfo = new LunarDateInfo();
            lunarDateInfo.setYear(year);
            lunarDateInfo.setMonth(month);
            lunarDateInfo.setDay(day);
        } catch (Exception e) {
            throw new RuntimeException("仅支持日期格式yyyy-MM-dd");
        }
        return lunarDateInfo;
    }

    /**
     * 将农历日期信息转换成公历日期
     * 
     * @param lunarDateInfo
     *            农历日期信息
     * @return 公历的日期
     */
    public static Date getGregorianDate(LunarDateInfo lunarDateInfo) {
        if (lunarDateInfo == null) {
            return null;
        }
        boolean isLeapMonth = false;
        int lunarYear = lunarDateInfo.getYear();
        int lunarMonth = lunarDateInfo.getMonth();
        if (lunarMonth < 0) {
            lunarMonth = -lunarMonth;
            isLeapMonth = true;
        }
        int lunarDay = lunarDateInfo.getDay();

        int afterDays = 0;
        for (int i = 1900; i < lunarYear; i++) {
            afterDays += yearDays(i);
        }

        int leapMonth = leapMonth(lunarYear);
        if (leapMonth > 0 && leapMonth < lunarMonth) {
            afterDays += leapDays(lunarYear);
        }
        for (int i = 1; i < lunarMonth; i++) {
            afterDays += monthDays(lunarYear, i);
        }

        int lastMonthDays = 0;
        if (isLeapMonth) {
            afterDays += monthDays(lunarYear, lunarMonth);
            lastMonthDays = leapDays(lunarYear);
        } else {
            lastMonthDays = monthDays(lunarYear, lunarMonth);
        }
        afterDays += (lunarDay < lastMonthDays ? lunarDay : lastMonthDays) - 1;

        Calendar cal = Calendar.getInstance();
        cal.setTime(BASE_DATE);
        cal.add(Calendar.DAY_OF_YEAR, afterDays);

        return cal.getTime();
    }

    public static LunarDateInfo getLunarDateInfo(Date date) {
        LunarDateInfo lunarDate = new LunarDateInfo();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        initZoneDateTime(cal);
        // 求出和1900年1月31日相差的天数
        int offset = (int) ((cal.getTimeInMillis() - BASE_DATE.getTime()) / 86400000L);
        int iYear;
        int daysOfYear = 0;
        for (iYear = 1900; (iYear < MAX_YEAR) && (offset > 0); iYear++) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
        }

        int leapMonth = leapMonth(iYear); // 闰哪个月,1-12
        boolean isLeapMonth = false;
        int iMonth;
        int daysOfMonth = 0;

        for (iMonth = 1; (iMonth < 13) && (offset > 0); iMonth++) {
            if ((leapMonth > 0) && (iMonth == (leapMonth + 1)) && !isLeapMonth) {
                --iMonth;
                isLeapMonth = true;
                daysOfMonth = leapDays(iYear);
            } else {
                daysOfMonth = monthDays(iYear, iMonth);
            }
            offset -= daysOfMonth;
            if (isLeapMonth && (iMonth == (leapMonth + 1))) {
                isLeapMonth = false;
            }
        }
        if ((offset == 0) && (leapMonth > 0) && (iMonth == (leapMonth + 1))) {
            if (isLeapMonth) {
                isLeapMonth = false;
            } else {
                isLeapMonth = true;
                --iMonth;
            }
        }
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
        }
        lunarDate.setYear(iYear);
        if (isLeapMonth) {
            lunarDate.setMonth(-iMonth);
        } else {
            lunarDate.setMonth(iMonth);
        }
        lunarDate.setDay(offset + 1);
        lunarDate.setLunarDateStr(getChineseYearString(iYear) + "(" + iYear + ")", getChineseMonthString(iMonth,
                isLeapMonth), getChineseDayString(lunarDate.getDay()));
        return lunarDate;
    }

    /**
     * 根据农历生日对应的公历日期获取N年后的生日的公历日期
     * 
     * @param birthdayDate
     *            农历生日对应的公历日期
     * @param afterYears
     *            几年后
     * @return 几年后生日的公历日期
     */
    public static Date getBirthday(Date birthdayDate, boolean isLeapMonth, int afterYears) {
        if (birthdayDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(birthdayDate);
        initDateTime(cal);
        Date result = null;
        Object[] lunarData = calcLunarDate(cal);
        int lunarYear = (Integer) lunarData[0];
        int lunarMonth = (Integer) lunarData[1];
        int lunarDay = (Integer) lunarData[2];
        if (afterYears < 0) {
            result = null;
        } else if (afterYears > 0) {
            int afterDays = 0;
            afterDays += getLunarYearLeftDays(cal.getTime());
            for (int i = 1; i < afterYears; i++) {
                afterDays += yearDays(lunarYear + i);
            }
            int calcYear = lunarYear + afterYears;
            int leapMonth = leapMonth(calcYear);
            if (leapMonth == lunarMonth) { // 生日在当年闰月时
                for (int i = 1; i < lunarMonth; i++) {
                    afterDays += monthDays(calcYear, i);
                }
                int lastMonthDays = 0;
                if (isLeapMonth) {
                    afterDays += monthDays(calcYear, lunarMonth);
                    lastMonthDays = leapDays(calcYear);
                } else {
                    lastMonthDays = monthDays(calcYear, lunarMonth);
                }
                afterDays += (lunarDay < lastMonthDays ? lunarDay : lastMonthDays) - 1;
                cal.add(Calendar.DAY_OF_YEAR, afterDays);
                result = cal.getTime();
            } else {
                if (leapMonth > 0 && leapMonth < lunarMonth) { // 闰月在生日月之前
                    afterDays += leapDays(calcYear);
                }
                for (int i = 1; i < lunarMonth; i++) {
                    afterDays += monthDays(calcYear, i);
                }
                int monthDays = monthDays(calcYear, lunarMonth);
                int dateNum = 0;
                if (monthDays < lunarDay) {
                    dateNum = monthDays;
                } else {
                    dateNum = lunarDay;
                }
                afterDays += dateNum - 1;
                cal.add(Calendar.DAY_OF_YEAR, afterDays);
                result = cal.getTime();
            }
        } else {
            result = cal.getTime();
        }
        return result;
    }

    /**
     * 求出当年农历剩余的天数
     * 
     * @param date
     * @return
     */
    private static int getLunarYearLeftDays(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        initZoneDateTime(cal);
        int offset = (int) ((cal.getTimeInMillis() - BASE_DATE.getTime()) / 86400000);
        int iYear;
        int days = 0;
        for (iYear = 1900; (iYear < 2050) && (days < offset); iYear++) {
            days += yearDays(iYear);
        }
        return (days - offset);
    }

    /**
     * 初始化日期时间值为0
     * 
     * @param cal
     *            公历日期
     * @return
     */
    private static void initDateTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private static void initZoneDateTime(Calendar cal) {
        initDateTime(cal);
        cal.add(Calendar.HOUR_OF_DAY, TIME_ZONE);
    }

    public boolean isLeapMonth() {
        return isLeapMonth;
    }

    public void setLeapMonth(boolean isLeapMonth) {
        this.isLeapMonth = isLeapMonth;
    }

    public static class SolarTerms {
        // 2000年-2030年间所有节气日期
        private static final String[][] SOLAR_TERM_DATA = new String[][] {
                { "2000-01-06", "2000-01-21", "2000-02-04", "2000-02-19", "2000-03-05", "2000-03-20", "2000-04-04",
                        "2000-04-20", "2000-05-05", "2000-05-21", "2000-06-05", "2000-06-21", "2000-07-07",
                        "2000-07-22", "2000-08-07", "2000-08-23", "2000-09-07", "2000-09-23", "2000-10-08",
                        "2000-10-23", "2000-11-07", "2000-11-22", "2000-12-07", "2000-12-21" },
                { "2001-01-05", "2001-01-20", "2001-02-04", "2001-02-18", "2001-03-05", "2001-03-20", "2001-04-05",
                        "2001-04-20", "2001-05-05", "2001-05-21", "2001-06-05", "2001-06-21", "2001-07-07",
                        "2001-07-23", "2001-08-07", "2001-08-23", "2001-09-07", "2001-09-23", "2001-10-08",
                        "2001-10-23", "2001-11-07", "2001-11-22", "2001-12-07", "2001-12-22" },
                { "2002-01-05", "2002-01-20", "2002-02-04", "2002-02-19", "2002-03-06", "2002-03-21", "2002-04-05",
                        "2002-04-20", "2002-05-06", "2002-05-21", "2002-06-06", "2002-06-21", "2002-07-07",
                        "2002-07-23", "2002-08-08", "2002-08-23", "2002-09-08", "2002-09-23", "2002-10-08",
                        "2002-10-23", "2002-11-07", "2002-11-22", "2002-12-07", "2002-12-22" },
                { "2003-01-06", "2003-01-20", "2003-02-04", "2003-02-19", "2003-03-06", "2003-03-21", "2003-04-05",
                        "2003-04-20", "2003-05-06", "2003-05-21", "2003-06-06", "2003-06-22", "2003-07-07",
                        "2003-07-23", "2003-08-08", "2003-08-23", "2003-09-08", "2003-09-23", "2003-10-09",
                        "2003-10-24", "2003-11-08", "2003-11-23", "2003-12-07", "2003-12-22" },
                { "2004-01-06", "2004-01-21", "2004-02-04", "2004-02-19", "2004-03-05", "2004-03-20", "2004-04-04",
                        "2004-04-20", "2004-05-05", "2004-05-21", "2004-06-05", "2004-06-21", "2004-07-07",
                        "2004-07-22", "2004-08-07", "2004-08-23", "2004-09-07", "2004-09-23", "2004-10-08",
                        "2004-10-23", "2004-11-07", "2004-11-22", "2004-12-07", "2004-12-21" },
                { "2005-01-05", "2005-01-20", "2005-02-04", "2005-02-18", "2005-03-05", "2005-03-20", "2005-04-05",
                        "2005-04-20", "2005-05-05", "2005-05-21", "2005-06-05", "2005-06-21", "2005-07-07",
                        "2005-07-23", "2005-08-07", "2005-08-23", "2005-09-07", "2005-09-23", "2005-10-08",
                        "2005-10-23", "2005-11-07", "2005-11-22", "2005-12-07", "2005-12-22" },
                { "2006-01-05", "2006-01-20", "2006-02-04", "2006-02-19", "2006-03-06", "2006-03-21", "2006-04-05",
                        "2006-04-20", "2006-05-05", "2006-05-21", "2006-06-06", "2006-06-21", "2006-07-07",
                        "2006-07-23", "2006-08-07", "2006-08-23", "2006-09-08", "2006-09-23", "2006-10-08",
                        "2006-10-23", "2006-11-07", "2006-11-22", "2006-12-07", "2006-12-22" },
                { "2007-01-06", "2007-01-20", "2007-02-04", "2007-02-19", "2007-03-06", "2007-03-21", "2007-04-05",
                        "2007-04-20", "2007-05-06", "2007-05-21", "2007-06-06", "2007-06-22", "2007-07-07",
                        "2007-07-23", "2007-08-08", "2007-08-23", "2007-09-08", "2007-09-23", "2007-10-09",
                        "2007-10-24", "2007-11-08", "2007-11-23", "2007-12-07", "2007-12-22" },
                { "2008-01-06", "2008-01-21", "2008-02-04", "2008-02-19", "2008-03-05", "2008-03-20", "2008-04-04",
                        "2008-04-20", "2008-05-05", "2008-05-21", "2008-06-05", "2008-06-21", "2008-07-07",
                        "2008-07-22", "2008-08-07", "2008-08-23", "2008-09-07", "2008-09-22", "2008-10-08",
                        "2008-10-23", "2008-11-07", "2008-11-22", "2008-12-07", "2008-12-21" },
                { "2009-01-05", "2009-01-20", "2009-02-04", "2009-02-18", "2009-03-05", "2009-03-20", "2009-04-04",
                        "2009-04-20", "2009-05-05", "2009-05-21", "2009-06-05", "2009-06-21", "2009-07-07",
                        "2009-07-23", "2009-08-07", "2009-08-23", "2009-09-07", "2009-09-23", "2009-10-08",
                        "2009-10-23", "2009-11-07", "2009-11-22", "2009-12-07", "2009-12-22" },
                { "2010-01-05", "2010-01-20", "2010-02-04", "2010-02-19", "2010-03-06", "2010-03-21", "2010-04-05",
                        "2010-04-20", "2010-05-05", "2010-05-21", "2010-06-06", "2010-06-21", "2010-07-07",
                        "2010-07-23", "2010-08-07", "2010-08-23", "2010-09-08", "2010-09-23", "2010-10-08",
                        "2010-10-23", "2010-11-07", "2010-11-22", "2010-12-07", "2010-12-22" },
                { "2011-01-06", "2011-01-20", "2011-02-04", "2011-02-19", "2011-03-06", "2011-03-21", "2011-04-05",
                        "2011-04-20", "2011-05-06", "2011-05-21", "2011-06-06", "2011-06-22", "2011-07-07",
                        "2011-07-23", "2011-08-08", "2011-08-23", "2011-09-08", "2011-09-23", "2011-10-08",
                        "2011-10-24", "2011-11-08", "2011-11-23", "2011-12-07", "2011-12-22" },
                { "2012-01-06", "2012-01-21", "2012-02-04", "2012-02-19", "2012-03-05", "2012-03-20", "2012-04-04",
                        "2012-04-20", "2012-05-05", "2012-05-20", "2012-06-05", "2012-06-21", "2012-07-07",
                        "2012-07-22", "2012-08-07", "2012-08-23", "2012-09-07", "2012-09-22", "2012-10-08",
                        "2012-10-23", "2012-11-07", "2012-11-22", "2012-12-07", "2012-12-21" },
                { "2013-01-05", "2013-01-20", "2013-02-04", "2013-02-18", "2013-03-05", "2013-03-20", "2013-04-04",
                        "2013-04-20", "2013-05-05", "2013-05-21", "2013-06-05", "2013-06-21", "2013-07-07",
                        "2013-07-22", "2013-08-07", "2013-08-23", "2013-09-07", "2013-09-23", "2013-10-08",
                        "2013-10-23", "2013-11-07", "2013-11-22", "2013-12-07", "2013-12-22" },
                { "2014-01-05", "2014-01-20", "2014-02-04", "2014-02-19", "2014-03-06", "2014-03-21", "2014-04-05",
                        "2014-04-20", "2014-05-05", "2014-05-21", "2014-06-06", "2014-06-21", "2014-07-07",
                        "2014-07-23", "2014-08-07", "2014-08-23", "2014-09-08", "2014-09-23", "2014-10-08",
                        "2014-10-23", "2014-11-07", "2014-11-22", "2014-12-07", "2014-12-22" },
                { "2015-01-06", "2015-01-20", "2015-02-04", "2015-02-19", "2015-03-06", "2015-03-21", "2015-04-05",
                        "2015-04-20", "2015-05-06", "2015-05-21", "2015-06-06", "2015-06-22", "2015-07-07",
                        "2015-07-23", "2015-08-08", "2015-08-23", "2015-09-08", "2015-09-23", "2015-10-08",
                        "2015-10-24", "2015-11-08", "2015-11-22", "2015-12-07", "2015-12-22" },
                { "2016-01-06", "2016-01-20", "2016-02-04", "2016-02-19", "2016-03-05", "2016-03-20", "2016-04-04",
                        "2016-04-19", "2016-05-05", "2016-05-20", "2016-06-05", "2016-06-21", "2016-07-06",
                        "2016-07-22", "2016-08-07", "2016-08-23", "2016-09-07", "2016-09-22", "2016-10-08",
                        "2016-10-23", "2016-11-07", "2016-11-22", "2016-12-07", "2016-12-21" },
                { "2017-01-05", "2017-01-20", "2017-02-03", "2017-02-18", "2017-03-05", "2017-03-20", "2017-04-04",
                        "2017-04-20", "2017-05-05", "2017-05-21", "2017-06-05", "2017-06-21", "2017-07-07",
                        "2017-07-22", "2017-08-07", "2017-08-23", "2017-09-07", "2017-09-23", "2017-10-08",
                        "2017-10-23", "2017-11-07", "2017-11-22", "2017-12-07", "2017-12-22" },
                { "2018-01-05", "2018-01-20", "2018-02-04", "2018-02-19", "2018-03-05", "2018-03-21", "2018-04-05",
                        "2018-04-20", "2018-05-05", "2018-05-21", "2018-06-06", "2018-06-21", "2018-07-07",
                        "2018-07-23", "2018-08-07", "2018-08-23", "2018-09-08", "2018-09-23", "2018-10-08",
                        "2018-10-23", "2018-11-07", "2018-11-22", "2018-12-07", "2018-12-22" },
                { "2019-01-05", "2019-01-20", "2019-02-04", "2019-02-19", "2019-03-06", "2019-03-21", "2019-04-05",
                        "2019-04-20", "2019-05-06", "2019-05-21", "2019-06-06", "2019-06-21", "2019-07-07",
                        "2019-07-23", "2019-08-08", "2019-08-23", "2019-09-08", "2019-09-23", "2019-10-08",
                        "2019-10-24", "2019-11-08", "2019-11-22", "2019-12-07", "2019-12-22" },
                { "2020-01-06", "2020-01-20", "2020-02-04", "2020-02-19", "2020-03-05", "2020-03-20", "2020-04-04",
                        "2020-04-19", "2020-05-05", "2020-05-20", "2020-06-05", "2020-06-21", "2020-07-06",
                        "2020-07-22", "2020-08-07", "2020-08-22", "2020-09-07", "2020-09-22", "2020-10-08",
                        "2020-10-23", "2020-11-07", "2020-11-22", "2020-12-06", "2020-12-21" },
                { "2021-01-05", "2021-01-20", "2021-02-03", "2021-02-18", "2021-03-05", "2021-03-20", "2021-04-04",
                        "2021-04-20", "2021-05-05", "2021-05-21", "2021-06-05", "2021-06-21", "2021-07-07",
                        "2021-07-22", "2021-08-07", "2021-08-23", "2021-09-07", "2021-09-23", "2021-10-08",
                        "2021-10-23", "2021-11-07", "2021-11-22", "2021-12-07", "2021-12-21" },
                { "2022-01-05", "2022-01-20", "2022-02-04", "2022-02-19", "2022-03-05", "2022-03-20", "2022-04-05",
                        "2022-04-20", "2022-05-05", "2022-05-21", "2022-06-06", "2022-06-21", "2022-07-07",
                        "2022-07-23", "2022-08-07", "2022-08-23", "2022-09-07", "2022-09-23", "2022-10-08",
                        "2022-10-23", "2022-11-07", "2022-11-22", "2022-12-07", "2022-12-22" },
                { "2023-01-05", "2023-01-20", "2023-02-04", "2023-02-19", "2023-03-06", "2023-03-21", "2023-04-05",
                        "2023-04-20", "2023-05-06", "2023-05-21", "2023-06-06", "2023-06-21", "2023-07-07",
                        "2023-07-23", "2023-08-08", "2023-08-23", "2023-09-08", "2023-09-23", "2023-10-08",
                        "2023-10-24", "2023-11-08", "2023-11-22", "2023-12-07", "2023-12-22" },
                { "2024-01-06", "2024-01-20", "2024-02-04", "2024-02-19", "2024-03-05", "2024-03-20", "2024-04-04",
                        "2024-04-19", "2024-05-05", "2024-05-20", "2024-06-05", "2024-06-21", "2024-07-06",
                        "2024-07-22", "2024-08-07", "2024-08-22", "2024-09-07", "2024-09-22", "2024-10-08",
                        "2024-10-23", "2024-11-07", "2024-11-22", "2024-12-06", "2024-12-21" },
                { "2025-01-05", "2025-01-20", "2025-02-03", "2025-02-18", "2025-03-05", "2025-03-20", "2025-04-04",
                        "2025-04-20", "2025-05-05", "2025-05-21", "2025-06-05", "2025-06-21", "2025-07-07",
                        "2025-07-22", "2025-08-07", "2025-08-23", "2025-09-07", "2025-09-23", "2025-10-08",
                        "2025-10-23", "2025-11-07", "2025-11-22", "2025-12-07", "2025-12-21" },
                { "2026-01-05", "2026-01-20", "2026-02-04", "2026-02-18", "2026-03-05", "2026-03-20", "2026-04-05",
                        "2026-04-20", "2026-05-05", "2026-05-21", "2026-06-05", "2026-06-21", "2026-07-07",
                        "2026-07-23", "2026-08-07", "2026-08-23", "2026-09-07", "2026-09-23", "2026-10-08",
                        "2026-10-23", "2026-11-07", "2026-11-22", "2026-12-07", "2026-12-22" },
                { "2027-01-05", "2027-01-20", "2027-02-04", "2027-02-19", "2027-03-06", "2027-03-21", "2027-04-05",
                        "2027-04-20", "2027-05-06", "2027-05-21", "2027-06-06", "2027-06-21", "2027-07-07",
                        "2027-07-23", "2027-08-08", "2027-08-23", "2027-09-08", "2027-09-23", "2027-10-08",
                        "2027-10-23", "2027-11-07", "2027-11-22", "2027-12-07", "2027-12-22" },
                { "2028-01-06", "2028-01-20", "2028-02-04", "2028-02-19", "2028-03-05", "2028-03-20", "2028-04-04",
                        "2028-04-19", "2028-05-05", "2028-05-20", "2028-06-05", "2028-06-21", "2028-07-06",
                        "2028-07-22", "2028-08-07", "2028-08-22", "2028-09-07", "2028-09-22", "2028-10-08",
                        "2028-10-23", "2028-11-07", "2028-11-22", "2028-12-06", "2028-12-21" },
                { "2029-01-05", "2029-01-20", "2029-02-03", "2029-02-18", "2029-03-05", "2029-03-20", "2029-04-04",
                        "2029-04-20", "2029-05-05", "2029-05-21", "2029-06-05", "2029-06-21", "2029-07-07",
                        "2029-07-22", "2029-08-07", "2029-08-23", "2029-09-07", "2029-09-23", "2029-10-08",
                        "2029-10-23", "2029-11-07", "2029-11-22", "2029-12-07", "2029-12-21" },
                { "2030-01-05", "2030-01-20", "2030-02-04", "2030-02-18", "2030-03-05", "2030-03-20", "2030-04-05",
                        "2030-04-20", "2030-05-05", "2030-05-21", "2030-06-05", "2030-06-21", "2030-07-07",
                        "2030-07-23", "2030-08-07", "2030-08-23", "2030-09-07", "2030-09-23", "2030-10-08",
                        "2030-10-23", "2030-11-07", "2030-11-22", "2030-12-07", "2030-12-22" } };
        private static final String[] SOLAR_TERMS = new String[] { "小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨",
                "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至" };

        private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        private static final int TIME_OFFSET = TimeZone.getDefault().getRawOffset();
        private static int[] cacheDays;
        private static int defaultDays;

        static {
            long begin = System.currentTimeMillis();
            Calendar cal = Calendar.getInstance();
            cal.set(2031, 0, 1);

            Date endDate = cal.getTime();
            cal.set(2000, 0, 1);
            Date date = cal.getTime();
            defaultDays = getDays(date);
            int days = (int) ((endDate.getTime() - date.getTime()) / 86400000L);
            cacheDays = new int[days];

            for (int i = 0; i < days; i++) {
                int year = cal.get(Calendar.YEAR);
                String[] data = SOLAR_TERM_DATA[year - 2000];
                cacheDays[i] = Arrays.binarySearch(data, DATE_FORMAT.format(cal.getTime()));
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            long end = System.currentTimeMillis();
            System.out.println("===========初始化节气数据,用时" + (end - begin) + "ms");
        }

        public static String getSolarTermName(Date date) {
            String name = "";
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (cal.get(Calendar.YEAR) >= 2000 || cal.get(Calendar.YEAR) <= 2030) {
                int index = cacheDays[getDays(cal.getTime()) - defaultDays];
                if (index >= 0) {
                    name = SOLAR_TERMS[index];
                }
            }
            return name;
        }

        // 获取从1970.1.1到现在的天数
        private static int getDays(Date date) {
            if (date == null) {
                return -1;
            }
            return (int) ((date.getTime() + TIME_OFFSET) / 86400000L);
        }
    }

    /**
     * 农历日期信息
     * 
     * @author DFish Team
     * 
     */
    public static class LunarDateInfo {
        // 农历年份
        private int year;
        // 农历月份,负值表示闰月,正值表示非闰月
        private int month;
        // 农历日
        private int day;
        // 农历年份中文名称
        private String yearStr;
        // 农历月份中文名称
        private String monthStr;
        // 农历日中文名称
        private String dayStr;
        // 农历日期完整名称
        private String lunarDateStr;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public String getYearStr() {
            return yearStr;
        }

        public void setYearStr(String yearStr) {
            this.yearStr = yearStr;
        }

        public String getMonthStr() {
            return monthStr;
        }

        public void setMonthStr(String monthStr) {
            this.monthStr = monthStr;
        }

        public String getDayStr() {
            return dayStr;
        }

        public void setDayStr(String dayStr) {
            this.dayStr = dayStr;
        }

        public String getLunarDateStr() {
            return lunarDateStr;
        }

        public void setLunarDateStr(String lunarDateStr) {
            this.lunarDateStr = lunarDateStr;
        }

        void setLunarDateStr(String yearStr, String monthStr, String dayStr) {
            setYearStr(yearStr);
            setMonthStr(monthStr);
            setDayStr(dayStr);
            setLunarDateStr(yearStr + monthStr + dayStr);
        }

    }

}
