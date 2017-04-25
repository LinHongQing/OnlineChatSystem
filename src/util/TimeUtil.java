package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import exception.IllegalEmptyParameterException;

public final class TimeUtil {
	
	private static final String defaultFormatString = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 取得当前时间戳
	 *
	 * @return nowTimeStamp 当前时间戳(单位:s)
	 */
	public static String getNowTimeStamp() {
		long time = System.currentTimeMillis();
		String nowTimeStamp = String.valueOf(time / 1000);
		return nowTimeStamp;
	}

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param dateStr 字符串日期;
     * @param format 如：yyyy-MM-dd HH:mm:ss;
     *
     * @return
     * @throws IllegalEmptyParameterException 日期字符串为空异常;
     * @throws ParseException 日期字符串转换异常
     */
    public static String Date2TimeStamp(String dateStr, String format) throws IllegalEmptyParameterException, ParseException {
    	if (dateStr == null || "".equals(dateStr))
    		throw new IllegalEmptyParameterException("日期字符串非法!");
    	if (format == null || "".equals(format))
    		throw new IllegalEmptyParameterException("日期格式字符串非法!");
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return String.valueOf(sdf.parse(dateStr).getTime() / 1000);
    }
    
    /**
     * 将默认日期格式字符串转换成时间戳
     *
     * @param dateStr 字符串日期(默认格式 yyyy-MM-dd HH:mm:ss);
     *
     * @return
     * @throws IllegalEmptyParameterException 日期字符串为空异常;
     * @throws ParseException 日期字符串转换异常
     */
    public static String Date2TimeStamp(String dateStr) throws IllegalEmptyParameterException, ParseException {
    	if (dateStr == null || "".equals(dateStr))
    		throw new IllegalEmptyParameterException("日期字符串非法!");
    	return Date2TimeStamp(dateStr, defaultFormatString);
    }
    
    /**
     * 将Unix时间戳转换成指定格式日期字符串
     * @param timestampString 时间戳 如："1473048265";
     * @param formats 要格式化的格式;
     *
     * @return 返回结果;
     * @throws IllegalEmptyParameterException 日期字符串为空异常;
     * @throws NumberFormatException 时间戳字符串转换异常
     */
    public static String TimeStamp2Date(String timestampString, String formats) throws IllegalEmptyParameterException, NumberFormatException {
    	if (timestampString == null || "".equals(timestampString))
    		throw new IllegalEmptyParameterException("时间戳字符串非法!");
    	if (formats == null || "".equals(formats))
    		throw new IllegalEmptyParameterException("日期格式字符串非法!");
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }
    
    /**
     * 将Unix时间戳转换成默认格式日期字符串(yyyy-MM-dd HH:mm:ss)
     * @param timestampString 时间戳 如："1473048265";
     * 
     *
     * @return 返回结果 如:"2016-09-05 16:06";
     * @throws IllegalEmptyParameterException 日期字符串为空异常;
     * @throws NumberFormatException 时间戳字符串转换异常
     */
    public static String TimeStamp2Date(String timestampString) throws IllegalEmptyParameterException, NumberFormatException {
        if (timestampString == null || "".equals(timestampString))
            throw new IllegalEmptyParameterException("日期格式字符串非法!");
        return TimeStamp2Date(timestampString, defaultFormatString);
    }
}
