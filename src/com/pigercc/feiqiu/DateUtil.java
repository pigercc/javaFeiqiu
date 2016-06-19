package com.pigercc.feiqiu;

/**
 * Created by pigercc on 2016/6/19.
 */
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期转换工具
 */
public class DateUtil
{
    public static final String C_DATE_DIVISION = "-";

    public static final String C_TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String C_DATE_PATTON_DEFAULT = "yyyy-MM-dd";
    public static final String C_DATA_PATTON_YYYYMMDD = "yyyyMMdd";
    public static final String C_TIME_PATTON_HHMMSS = "HH:mm:ss";

    public static final int  C_ONE_SECOND = 1000;
    public static final int  C_ONE_MINUTE = 60 * C_ONE_SECOND;
    public static final int  C_ONE_HOUR   = 60 * C_ONE_MINUTE;
    public static final long C_ONE_DAY    = 24 * C_ONE_HOUR;

    /**
     * Return the current date
     *
     * @return － DATE<br>
     */
    public static Date getCurrentDate()
    {
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();

        return currDate;
    }

    /**
     * Return the current date string
     *
     * @return － 产生的日期字符串<br>
     */
    public static String getCurrentDateStr()
    {
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();

        return format(currDate);
    }

    /**
     * Return the current date in the specified format
     *
     * @param strFormat
     * @return
     */
    public static String getCurrentDateStr(String strFormat)
    {
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();

        return format(currDate, strFormat);
    }

    /**
     * Parse a string and return a date value
     *
     * @param dateValue
     * @return
     * @throws Exception
     */
    public static Date parseDate(String dateValue)
    {
        return parseDate(C_DATE_PATTON_DEFAULT, dateValue);
    }

    /**
     * Parse a strign and return a datetime value
     *
     * @param dateValue
     * @return
     */
    public static Date parseDateTime(String dateValue)
    {
        return parseDate(C_TIME_PATTON_DEFAULT, dateValue);
    }

    /**
     * Parse a string and return the date value in the specified format
     *
     * @param strFormat
     * @param dateValue
     * @return
     * @throws ParseException
     * @throws Exception
     */
    public static Date parseDate(String strFormat, String dateValue)
    {
        if (dateValue == null)
            return null;

        if (strFormat == null)
            strFormat = C_TIME_PATTON_DEFAULT;

        SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
        Date newDate = null;

        try
        {
            newDate = dateFormat.parse(dateValue);
        }
        catch (ParseException pe)
        {
            newDate = null;
        }

        return newDate;
    }


    /**
     * 将Timestamp类型的日期转换为系统参数定义的格式的字符串。
     * @param aTs_Datetime 需要转换的日期。
     * @return 转换后符合给定格式的日期字符串
     */
    public static String format(Date aTs_Datetime)
    {
        return format(aTs_Datetime, C_DATE_PATTON_DEFAULT);
    }

    /**
     * 将Timestamp类型的日期转换为系统参数定义的格式的字符串。
     * @param aTs_Datetime 需要转换的日期。
     * @return 转换后符合给定格式的日期字符串
     */
    public static String formatTime(Date aTs_Datetime)
    {
        return format(aTs_Datetime, C_TIME_PATTON_DEFAULT);
    }

    /**
     * 将Date类型的日期转换为系统参数定义的格式的字符串。
     * @param aTs_Datetime
     * @param as_Pattern
     * @return
     */
    public static String format(Date aTs_Datetime, String as_Pattern)
    {
        if (aTs_Datetime == null || as_Pattern == null)
            return null;

        SimpleDateFormat dateFromat = new SimpleDateFormat();
        dateFromat.applyPattern(as_Pattern);

        return dateFromat.format(aTs_Datetime);
    }

    /**
     * @param aTs_Datetime
     * @param as_Format
     * @return
     */
    public static String formatTime(Date aTs_Datetime, String as_Format)
    {
        if (aTs_Datetime == null || as_Format == null)
            return null;

        SimpleDateFormat dateFromat = new SimpleDateFormat();
        dateFromat.applyPattern(as_Format);

        return dateFromat.format(aTs_Datetime);
    }

    public static String getFormatTime(Date dateTime)
    {
        return formatTime(dateTime, C_TIME_PATTON_HHMMSS);
    }
    /**
     * @param aTs_Datetime
     * @param as_Pattern
     * @return
     */
    public static String format(Timestamp aTs_Datetime, String as_Pattern)
    {
        if (aTs_Datetime == null || as_Pattern == null)
            return null;

        SimpleDateFormat dateFromat = new SimpleDateFormat();
        dateFromat.applyPattern(as_Pattern);

        return dateFromat.format(aTs_Datetime);
    }
    /**
     * 取得指定日期N天后的日期
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.DAY_OF_MONTH, days);

        return cal.getTime();
    }

    /**
     * 计算两个日期之间相差的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int daysBetween(Date date1,Date date2)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算当前日期相对于"1977-12-01"的天数
     * @param date
     * @return
     */
    public static long getRelativeDays(Date date)
    {
        Date relativeDate = DateUtil.parseDate("yyyy-MM-dd","1977-12-01");

        return DateUtil.daysBetween(relativeDate,date);
    }

    public static Date getDateBeforTwelveMonth()
    {
        String date = "";
        Calendar cla = Calendar.getInstance();
        cla.setTime(getCurrentDate());
        int year = cla.get(Calendar.YEAR) - 1;
        int month = cla.get(Calendar.MONTH) + 1 ;
        if (month >9 )
        {
            date = String.valueOf(year) + C_DATE_DIVISION + String.valueOf(month)
                    + C_DATE_DIVISION + "01";
        }
        else
        {
            date = String.valueOf(year) + C_DATE_DIVISION + "0" + String.valueOf(month)
                    + C_DATE_DIVISION + "01" ;
        }

        Date dateBefore = parseDate(date);
        return dateBefore;
    }

    /**
     * 传入时间字符串,加一天后返回Date
     * @param date 时间 格式 YYYY-MM-DD
     * @return
     */
    public static Date addDate(String date)
    {
        if (date == null)
        {
            return null;
        }

        Date tempDate = parseDate(C_DATE_PATTON_DEFAULT, date);
        String year = format(tempDate, "yyyy");
        String month = format(tempDate, "MM");
        String day = format(tempDate, "dd");


        GregorianCalendar calendar =
                new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));

        calendar.add(GregorianCalendar.DATE, 1);
        return calendar.getTime();
    }

    public static void main(String[] args)
    {
        String str=getFormatTime(new Date(System.currentTimeMillis()));
        System.out.println(str);
    }
}