package com.wq.common.mapper.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.wq.common.mapper.MapperException;

/**
 * 时间工具类.
 * 
 * @author wuqing
 * @date 2014年8月10日 下午7:57:20
 */
public class DateUtil {

	public static final String format_01 = "yyyy-MM-dd HH:mm:ss";

	public static final String format_02 = "yyyy-MM-dd";

	public static final String format_03 = "Long";

	public static Map<Integer, String> formatMap = new HashMap<Integer, String>();

	/**
	 * 时间类型.
	 */
	@SuppressWarnings("rawtypes")
	private static Map<Class, Class> dateTypeMap = new HashMap<Class, Class>();

	static {
		// 时间格式化
		formatMap.put(format_01.length(), format_01);
		formatMap.put(format_02.length(), format_02);

		// 时间类型
		dateTypeMap.put(java.util.Date.class, java.util.Date.class);
		dateTypeMap.put(java.sql.Date.class, java.sql.Date.class);
		dateTypeMap.put(java.sql.Timestamp.class, java.sql.Timestamp.class);
	}

	/**
	 * 是否是日期类型.
	 * 
	 * @param clz
	 * @return
	 * @author wuqing
	 * @date 2014年8月19日 下午9:15:28
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isDate(Class clz) {
		return dateTypeMap.containsKey(clz);
	}

	/**
	 * 将值转化为目标时间类型.
	 * 
	 * @param value
	 *            传入值（只能是Long或者String类型）
	 * @param type
	 *            转化的时间类型
	 * @param format
	 *            时间格式化
	 * @return
	 * @author qingwu
	 * @date 2014-8-19 下午6:00:12
	 */
	public static <T> Object castDate(T value, Class<?> type, String format) {
		// 如果源对象与目标对象是同一类型则直接返回源对象
		if (type.isAssignableFrom(value.getClass())) {
			return value;
		}
		// 先将传入的值统一转化为java.util.Date类型
		Date date = null;
		if (value.getClass().isAssignableFrom(String.class)) {
			date = strToDate((String) value, format);
		} else if (value.getClass().isAssignableFrom(Long.class)
				|| value.getClass().isAssignableFrom(long.class)) {
			date = new Date(ValueUtil.getLong(value));
		} else {
			throw new MapperException(
					"Date value type must be String or Long...");
		}
		// 根据目标类型转化
		if (type.isAssignableFrom(java.util.Date.class)) {
			return date;
		} else if (type.isAssignableFrom(java.sql.Timestamp.class)) {
			return new java.sql.Timestamp(date.getTime());
		} else if (type.isAssignableFrom(java.sql.Date.class)) {
			return new java.sql.Date(date.getTime());
		} else {
			throw new MapperException("Unknow date type of " + type.getName());
		}
	}

	/**
	 * 字符串转日期.
	 * 
	 * @param str
	 * @return
	 * @author wuqing
	 * @date 2014年8月10日 下午8:01:10
	 */
	public static Date strToDate(String str) {
		if (str == null) {
			return null;
		}
		return strToDate(str, formatMap.get(str.length()));
	}

	/**
	 * 字符串转日期.
	 * 
	 * @param str
	 * @param format
	 * @return
	 * @author wuqing
	 * @date 2014年8月10日 下午8:01:19
	 */
	public static Date strToDate(String str, String format) {
		if (str == null) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(str);
		} catch (ParseException e) {
			throw new MapperException(e);
		}
	}

	/**
	 * 日期转字符串.
	 * 
	 * @param date
	 * @return
	 * @author qingwu
	 * @date 2014-8-20 下午1:57:13
	 */
	public static String dateToStr(Object date) {
		return dateToStr(date, format_01);
	}

	/**
	 * 日期转字符串.
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @author qingwu
	 * @date 2014-8-20 下午1:52:53
	 */
	public static String dateToStr(Object date, String format) {
		Date d;
		if (date.getClass().isAssignableFrom(Long.class)
				|| date.getClass().isAssignableFrom(long.class)) {
			d = new Date((Long) date);
		} else if (date.getClass().isAssignableFrom(Date.class)) {
			d = (Date) date;
		} else if (date.getClass().isAssignableFrom(java.sql.Date.class)) {
			d = new Date(((java.sql.Date) date).getTime());
		} else if (date.getClass().isAssignableFrom(java.sql.Timestamp.class)) {
			d = new Date(((java.sql.Timestamp) date).getTime());
		} else {
			throw new MapperException(
					"Param of date must be 'long' or 'Long' or 'java.util.Date' or 'java.sql.Date' or 'java.sql.Timestamp'...");
		}
		if (format.equals("Long")) {
			return String.valueOf(d.getTime());
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}
}
