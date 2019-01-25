package com.zb.commons.date;


import com.zb.commons.validate.CommonValidateUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期操作工具类
 *
 * @author Administrator
 */
public class DateUtil implements DisposableBean {

    
    public static final String DATE_FORMAT_STR = "yyyy-MM-dd";

    public static final String DATETIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    private static final ThreadLocal<Map<String, SimpleDateFormat>> DATE_FORMAT = new ThreadLocal<Map<String, SimpleDateFormat>>() {
        @Override
        protected Map<String, SimpleDateFormat> initialValue() {
            Map<String, SimpleDateFormat> map = new HashMap<>();
            map.put(DATE_FORMAT_STR, new SimpleDateFormat(DATE_FORMAT_STR));
            map.put(DATETIME_FORMAT_STR, new SimpleDateFormat(DATETIME_FORMAT_STR));
            return map;
        }
    };

    @Override
    public void destroy() throws Exception {
        DATE_FORMAT.remove();
    }
    
    private static void validateDateNotNull(Date date, String exceptionTip) {
        if (date == null) {
            throw new IllegalArgumentException(exceptionTip);
        }
    }

    private static void validateYear(int year) {
        if (year < 1900 || year > 2099) {
            throw new IllegalArgumentException("year is not available");
        }
    }

    /**
     * 根据传入的Date对象获取不包含时部的Calendar对象
     *
     * @param date
     * @return
     */
    public static Calendar getClearedTimePartCalendar(Date date) {
        validateDateNotNull(date, "date is not available");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        clearTimePart(calendar);
        return calendar;
    }

    /**
     * 获取不包含时部的当前Date对象
     *
     * @return
     */
    public static Date getClearedTimePartCurDate() {
        Calendar now = Calendar.getInstance();
        clearTimePart(now);
        return now.getTime();
    }

    /**
     * 是否是同一天
     *
     * @param date
     * @param dateAnother
     * @return
     */
    public static boolean isSameDay(Date date, Date dateAnother) {
        validateDateNotNull(date, "date is not available");
        validateDateNotNull(date, "dateAnother is not available");

        String dateStr = defaultFormatDate(date);
        String dateAnotherStr = defaultFormatDate(dateAnother);
        
        return dateStr.equals(dateAnotherStr);
    }

    /**
     * 是否是周末.即是否是周六或周日
     *
     * @param date
     * @return
     */
    public static boolean isWeekend(Date date) {
        validateDateNotNull(date, "date is not available");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        return calendar.get(Calendar.DAY_OF_WEEK) == 7 || calendar.get(Calendar.DAY_OF_WEEK) == 1;
    }

    /**
     * 获取相差的年数
     * <br>
     * yearDiff = endYear - startYear
     *
     * @param startYear
     * @param endYear
     * @return
     */
    public static int getYearDiff(int startYear, int endYear) {
        validateYear(startYear);
        validateYear(endYear);

        return endYear - startYear;
    }

    /**
     * 获取结束日期和开始日期之间相差的年数
     * <br>
     * yearDiff = monthDiff / 12
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getYearDiff(Date startDate, Date endDate) {

        validateDateNotNull(startDate, "startDate is not available");
        validateDateNotNull(endDate, "endDate is not available");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(endDate);

        int yearEnd = calendar.get(Calendar.YEAR);
        int monthEnd = calendar.get(Calendar.MONTH);
        int dayEnd = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(startDate);

        int yearStart = calendar.get(Calendar.YEAR);
        int monthStart = calendar.get(Calendar.MONTH);
        int dayStart = calendar.get(Calendar.DAY_OF_MONTH);

        //计算
        int yearDiff = yearEnd - yearStart;

        if (monthEnd < monthStart) {
            yearDiff--;
        } else if (monthEnd == monthStart) {
            if (dayEnd < dayStart) {
                yearDiff--;
            }
        } else {
            //do nothing;
        }

        return yearDiff;
    }

    /**
     * 获取结束日期和开始日期之间相差的月数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getMonthDiff(Date startDate, Date endDate) {

        validateDateNotNull(startDate, "startDate is not available");
        validateDateNotNull(endDate, "endDate is not available");

        Calendar startCal = getClearedTimePartCalendar(startDate);
        startCal.set(Calendar.DAY_OF_MONTH, 1); //重置为当月的第一天
        Calendar endCal = getClearedTimePartCalendar(endDate);
        endCal.set(Calendar.DAY_OF_MONTH, 1);   //重置为当月的第一天

        //获取相差的年份
        int yearDiff = getYearDiff(startCal.get(Calendar.YEAR), endCal.get(Calendar.YEAR));

        int monthDiff = endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH);

        //同一年份的情形,直接月份相减
        if (yearDiff == 0) {
            return monthDiff;
        } else {
            return monthDiff + (12 * yearDiff);
        }
    }

    /**
     * 获取结束日期和开始日期之间相差的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDayDiff(Date startDate, Date endDate) {
        validateDateNotNull(startDate, "startDate is not available");
        validateDateNotNull(endDate, "endDate is not available");

        Calendar startCal = getClearedTimePartCalendar(startDate);
        Calendar endCal = getClearedTimePartCalendar(endDate);

        return (int) ((endCal.getTime().getTime() - startCal.getTime().getTime()) / 1000 / 60 / 60 / 24);
    }

    /**
     * 获取结束日期和开始日期之间相差的小时数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getHourDiff(Date startDate, Date endDate) {
		
        validateDateNotNull(startDate, "startDate is not available");
        validateDateNotNull(endDate, "endDate is not available");

        return ((endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60);
    }

    /**
     * 获取结束日期和开始日期之间相差的分钟数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getMinuteDiff(Date startDate, Date endDate) {
        validateDateNotNull(startDate, "startDate is not available");
        validateDateNotNull(endDate, "endDate is not available");

        return ((endDate.getTime() - startDate.getTime()) / 1000 / 60);
    }

    /**
     * adds the specified (signed) amount of time to the given calendar field, based on the calendar's rules.
     * <p>
     * 给指定的Date实例所代表的Calendar实例的field添加指定的时间量,当时间量为负时即代表减少指定的时间量
     *
     * @param date
     * @param field
     * @param amount
     * @return
     */
    public static Date add(Date date, int field, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("date parameter is not available");
        }

        if (field < 0 || field >= Calendar.ZONE_OFFSET) {
            throw new IllegalArgumentException("field parameter is not available");
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * method had been deprecated
     *
     * @param date
     * @param hourAmount
     * @return
     * @see public static Date add(Date date, int field, int amount) {...}
     */
    @Deprecated
    public static Date addHours(Date date, int hourAmount) {
        if (date == null) {
            throw new IllegalArgumentException("date parameter is not available");
        }
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(GregorianCalendar.HOUR_OF_DAY, hourAmount);
        return calendar.getTime();
    }

    /**
     * method had been deprecated
     *
     * @param date
     * @param dayAmount
     * @return
     * @see public static Date add(Date date, int field, int amount) {...}
     */
    @Deprecated
    public static Date addDays(Date date, int dayAmount) {
        if (date == null) {
            throw new IllegalArgumentException("date parameter is not available");
        }
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(GregorianCalendar.DAY_OF_YEAR, dayAmount);
        return calendar.getTime();
    }

    /**
     * method had been deprecated
     *
     * @param date
     * @param monthAmount
     * @return
     * @see public static Date add(Date date, int field, int amount) {...}
     */
    @Deprecated
    public static Date addMonthes(Date date, int monthAmount) {
        if (date == null) {
            throw new IllegalArgumentException("date parameter is not available");
        }
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(GregorianCalendar.MONTH, monthAmount);
        return calendar.getTime();
    }

    /**
     * method had been deprecated
     *
     * @param date
     * @param yearAmount
     * @return
     * @see public static Date add(Date date, int field, int amount) {...}
     */
    @Deprecated
    public static Date addYears(Date date, int yearAmount) {
        if (date == null) {
            throw new IllegalArgumentException("date parameter is not available");
        }
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(GregorianCalendar.YEAR, yearAmount);
        return calendar.getTime();
    }

    /**
     * 获得指定年份下指定月份的最后一天
     *
     * @param year
     * @param monthInRealWorld 现实世界的月份[1-12]
     * @return
     */
    public static Date getLastDayOfMonthInTheSpecifiedYear(int year, int monthInRealWorld) {
        return getLastDayOfCompMonthInTheSpecifiedYear(year, monthInRealWorld - 1);
    }

    /**
     * 获得指定年份下指定计算机月份的最后一天
     *
     * @param year
     * @param monthInComputer 计算机的月份[0-11]
     * @return
     */
    public static Date getLastDayOfCompMonthInTheSpecifiedYear(int year, int monthInComputer) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(year, monthInComputer, 01);
        calendar.set(GregorianCalendar.DAY_OF_MONTH, calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));

        return calendar.getTime();
    }

    /**
     * 获得指定年份下指定月份的第一天
     *
     * @param year
     * @param monthInRealWorld 现实世界的月份[1-12]
     * @return
     */
    public static Date getFirstDayOfMonthInTheSpecifiedYear(int year, int monthInRealWorld) {
        return getFirstDayOfCompMonthInTheSpecifiedYear(year, monthInRealWorld - 1);
    }

    /**
     * 获得指定年份下指定计算机月份的第一天
     *
     * @param year
     * @param monthInComputer 计算机的月份[0-11]
     * @return
     */
    public static Date getFirstDayOfCompMonthInTheSpecifiedYear(int year, int monthInComputer) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(year, monthInComputer, 01);
        return calendar.getTime();
    }

    public static int getYear(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(GregorianCalendar.YEAR);
    }

    public static int getComputerMonth(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(GregorianCalendar.MONTH);
    }

    public static int getRealWorldMonth(Date date) {
        return getComputerMonth(date) + 1;
    }

    public static int getDayForMonth(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(GregorianCalendar.DAY_OF_MONTH);
    }

    public static int getHour(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(GregorianCalendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(GregorianCalendar.MINUTE);
    }

    public static int getSecond(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(GregorianCalendar.SECOND);
    }

	/*----- 日期转换 START -----*/

    /**
     * 将具有缺省日期/时间格式的日期/时间字符串转换为日期对象,可选择在转换时是否加入时部
     * <br>
     * 缺省日期时间格式 格式1:yyyy-MM-dd 格式2:yyyy-MM-dd HH:mm:ss
     * <br>
     *
     * @param dateStr
     * @return
     */
    public static Date defaultParseDate(String dateStr) {
        try {
            return DATE_FORMAT.get().get(DATE_FORMAT_STR).parse(dateStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("parameter date string is not available");
        }
    }
    
    /**
     * 将具有缺省日期/时间格式的日期/时间字符串转换为日期对象,可选择在转换时是否加入时部
     * <br>
     * 缺省日期时间格式 格式1:yyyy-MM-dd 格式2:yyyy-MM-dd HH:mm:ss
     * <br>
     *
     * @param dateStr
     * @return
     */
    public static Date defaultParseDateTime(String dateStr) {
        try {
            return DATE_FORMAT.get().get(DATETIME_FORMAT_STR).parse(dateStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("parameter date string is not available");
        }
    }

    /**
     * 将具有指定格式的日期/时间字符串按照指定的描述日期/时间格式的规则转换为日期对象
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date parse(String dateStr, String format) {
        if (CommonValidateUtil.isEmpty(dateStr)) {
            return null;
        }

        if (CommonValidateUtil.isEmpty(format)) {
            format = DATETIME_FORMAT_STR;
        }

        SimpleDateFormat dateFormat = getDateFormat(format);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("parameter date string is not available");
        }
    }

    private static SimpleDateFormat getDateFormat(String pattern) {
        return DATE_FORMAT.get().computeIfAbsent(pattern, (k) -> new SimpleDateFormat(k));
    }
	
	/*----- 日期转换 END -----*/

	/*----- 日期格式化 START -----*/

    /**
     * 将指定的日期对象按照缺省格式格式化为指定的日期字符串
     * <br />
     * 缺省格式:yyyy-MM-dd
     * <br />
     * 示例:2015-01-01
     *
     * @param date
     * @return
     */
    public static String defaultFormatDate(Date date) {
        return formatDate(date, DATE_FORMAT_STR);
    }

    /**
     * 将指定的日期对象按照缺省格式格式化为指定的日期字符串,可选择在格式化后是否保留时分秒
     *
     * @param date
     * @return
     */
    public static String defaultFormatDatetime(Date date) {
        return formatDate(date, DATETIME_FORMAT_STR);
    }

    /**
     * 将指定的日期对象按照指定的日期时间格式串格式化为指定的日期字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        Assert.notNull(date, "date must not be null");
        return getDateFormat(format).format(date);
    }
	
	/*----- 日期格式化 END -----*/

    /**
     * 清空Calendar对象的时部.即时,分,秒,毫秒
     *
     * @param calendar
     */
    public static void clearTimePart(Calendar calendar) {

        if (calendar == null) {
            throw new RuntimeException("parameter calendar is not available");
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 清空Date对象的时部.即时,分,秒,毫秒
     *
     * @param date
     * @return
     */
    public static Date clearTimePart(Date date) {

        if (date == null) {
            throw new RuntimeException("parameter date is not available");
        }

        Calendar current = Calendar.getInstance();
        current.setTime(date);

        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);

        return current.getTime();
    }

    public static long getSecondsDiff(Date startDate, Date endDate) {
        return (endDate.getTime() - startDate.getTime()) / 1000;
    }

    /**
     * 获取今天剩余的秒数
     * @return
     */
    public static long getRemainSecondsOfToday() {
        Date today = getClearedTimePartCurDate();
        Date tomorrow = add(today, Calendar.DAY_OF_YEAR, 1);
        return getSecondsDiff(new Date(), tomorrow);
    }
}