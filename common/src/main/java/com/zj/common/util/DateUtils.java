package com.zj.common.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ZHUHEJIN615 on 2019/6/12.
 * <p>
 * 关于LocalDate & LocalDateTime
 * 详细参见
 * https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
 * https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html
 *
 */
@Slf4j
public class DateUtils {

    static String DEFAULT_ZONE_NAME = "Asia/Shanghai";
    static ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_ZONE_NAME);

    /**
     * 日期格式
     */
    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYYMMDD = "yyyyMMdd";

    /**
     * 日期时间格式
     */
    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD);

    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);

    /**
     * 获取当前时间的前后几个月
     *
     * @param sourceDate
     * @param month
     * @return
     */
    public static Date stepMonth(Date sourceDate, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(sourceDate);
        c.add(Calendar.MONTH, month);
        return c.getTime();
    }


    /**
     * 日期格式化
     *
     * @return
     */
    public static String now() {
        return dateTimeFormat(new Date());
    }

    /**
     * 日期格式化
     *
     * @param date
     * @return
     */
    public static String dateFormat(LocalDate date) {
        return date.format(DEFAULT_DATE_FORMATTER);
    }

    public static String dateFormat(Date date) {
        return date2LocalDate(date).format(DEFAULT_DATE_FORMATTER);
    }

    public static String dateFormat(LocalDate date, String formatPattern) {
        return date.format(DateTimeFormatter.ofPattern(formatPattern));
    }

    public static String dateFormat(Date date, String formatPattern) {
        return date2LocalDate(date).format(DateTimeFormatter.ofPattern(formatPattern));
    }

    /**
     * 日期时间格式化
     *
     * @param date
     * @return
     */
    public static String dateTimeFormat(LocalDateTime date) {
        return date.format(DEFAULT_DATETIME_FORMATTER);
    }

    public static String dateTimeFormat(Date date) {
        return date2LocalDateTime(date).format(DEFAULT_DATETIME_FORMATTER);
    }

    public static String dateTimeFormat(LocalDateTime date, String formatPattern) {
        return date.format(DateTimeFormatter.ofPattern(formatPattern));
    }

    public static String dateTimeFormat(Date date, String formatPattern) {
        return date2LocalDateTime(date).format(DateTimeFormatter.ofPattern(formatPattern));
    }

    /**
     * 下面四个方法是Date和LocalDate的相关转换
     */
    public static LocalDate date2LocalDate(Date date) {
        return date.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDate();
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        return date.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDateTime();
    }

    public static Date localDate2Date(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(DEFAULT_ZONE_ID).toInstant());
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * Date转时间字符串
     *
     * @param date
     * @param formatter
     * @return
     */
    public static String formatDateToString(Date date, String formatter) {
        if (StringUtils.isEmptyOrNull(formatter) || date == null) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        LocalDateTime dateTime = date2LocalDateTime(date);
        String formattedDateTime = dateTime.format(dateTimeFormatter);
        return formattedDateTime;
    }

    /**
     * 字符串转日期
     *
     * @param formatter
     * @param dateString
     * @return
     */
    public static Date formatStringToDate(String formatter, String dateString) {
        if (StringUtils.isEmptyOrNull(formatter) || StringUtils.isEmptyOrNull(dateString)) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, dateTimeFormatter);
        return localDateTime2Date(dateTime);
    }

    public static Date formatStringToDateByDefault(String str) {
        if (StringUtils.isEmptyOrNull(str)) {
            return null;
        }
        return formatStringToDate(YYYY_MM_DD_HH_MM_SS, str);
    }

    /**
     * 获取指定日期的零点
     *
     * @return
     */
    public static Date getTheDayZero(Date date) {
        String strDate = DateUtils.formatDateToString(date, DateUtils.YYYY_MM_DD_HH_MM_SS);
        String splitStrDate = strDate.substring(0, 10);
        String zeroStrDate = new StringBuffer().append(splitStrDate).append(" 00:00:00").toString();
        return DateUtils.formatStringToDateByDefault(zeroStrDate);
    }

    /**
     * 获取当前月月初的时间戳
     *
     * @return
     */
    public static long getCurrentMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * @param dateString 日期字符串
     * @param dateFormat 日期格式
     * @return {@link Date }
     * @doc 解析字符串日期为Date类型
     * @author cz
     * @date 2023/04/20
     */
    public static Date parseStringToDate(String dateString, String dateFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            return format.parse(dateString);
        } catch (ParseException e) {
            log.error("parseStringToDate error", e);
            throw new RuntimeException(e.getMessage());
        }
    }


//    public static void main(String[] args) {
//
//        Date  dateNow =new Date();
//
//        LocalDate localDate = LocalDate.now();
//        LocalDateTime localDateTime = LocalDateTime.now();
//
//        System.out.println("========开始 老Date=======");
//
//        //日期格式化
//        System.out.println(DateUtils.now());
//        System.out.println(DateUtils.dateFormat(dateNow));
//        System.out.println(DateUtils.dateFormat(dateNow, DateUtils.YYYY_MM_DD));
//
//        //日期时间格式化
//        System.out.println(DateUtils.dateTimeFormat(dateNow));
//        System.out.println(DateUtils.dateTimeFormat(dateNow, DateUtils.YYYY_MM_DD_HH_MM_SS));
//
//        System.out.println("========= 新LocalDate========");
//
//        //日期格式化
//        System.out.println(DateUtils.dateFormat(localDate));
//        System.out.println(DateUtils.dateFormat(localDate, DateUtils.YYYY_MM_DD));
//
//        //日期时间格式化
//        System.out.println(DateUtils.dateTimeFormat(localDateTime));
//        System.out.println(DateUtils.dateTimeFormat(localDateTime, DateUtils.YYYY_MM_DD_HH_MM_SS));
//
//        System.out.println("=========String&Date的相互转换========");
//        //String&Date的相互转换
//        String dateTimeS = "2019-06-12 14:56:00";
//        Date date = formatStringToDateByDefault(dateTimeS);
//        System.out.println(date);
//        String dateTimeSs = formatDateToString(new Date(),DateUtils.YYYY_MM_DD_HH_MM_SS);
//        System.out.println(dateTimeSs);
//    }

//    ========开始 老Date=======
//    2019-06-12 15:09:33
//    2019-06-12
//    2019-06-12
//    2019-06-12 15:09:33
//    2019-06-12 15:09:33
//    ========= 新LocalDate========
//    2019-06-12
//    2019-06-12
//    2019-06-12 15:09:33
//    2019-06-12 15:09:33
//    =========String&Date的相互转换========
//    Wed Jun 12 14:56:00 CST 2019
//    2019-06-12 15:09:40

}
