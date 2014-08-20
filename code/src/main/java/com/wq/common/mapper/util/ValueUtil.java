package com.wq.common.mapper.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.wq.common.mapper.MapperException;

public class ValueUtil {

	/**
	 * 原始参数类型列表.
	 */
	@SuppressWarnings("rawtypes")
	private static List<Class> constantDefinitionObject = new ArrayList<Class>();

	static {
		// 原始类型参数配置初始化
		constantDefinitionObject.add(int.class);
		constantDefinitionObject.add(Integer.class);
		constantDefinitionObject.add(float.class);
		constantDefinitionObject.add(Float.class);
		constantDefinitionObject.add(double.class);
		constantDefinitionObject.add(Double.class);
		constantDefinitionObject.add(long.class);
		constantDefinitionObject.add(Long.class);
		constantDefinitionObject.add(short.class);
		constantDefinitionObject.add(Short.class);
		constantDefinitionObject.add(char.class);
		constantDefinitionObject.add(Character.class);
		constantDefinitionObject.add(String.class);
		constantDefinitionObject.add(BigDecimal.class);
	}

	/**
	 * 是否是原始类型.
	 * 
	 * @param clz
	 * @author wuqing
	 * @date 2014年8月19日 下午9:18:09
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isOriginal(Class clz) {
		return constantDefinitionObject.contains(clz);
	}

	/**
	 * 值对象 --> long.
	 * 
	 * @param value
	 * @return
	 * @author wuqing
	 * @date 2012-7-28 下午03:27:21
	 */
	public static long getLong(Object value) {
		try {
			return Long.parseLong(getString(value));
		} catch (Exception e) {
			return 0L;
		}
	}

	/**
	 * 值对象 --> double.
	 * 
	 * @param value
	 * @return
	 * @author wuqing
	 * @date 2012-7-28 下午03:29:25
	 */
	public static float getFloat(Object value) {
		try {
			return Float.parseFloat(getString(value));
		} catch (Exception e) {
			return 0.0f;
		}
	}

	/**
	 * 值对象 --> double.
	 * 
	 * @param value
	 * @return
	 * @author wuqing
	 * @date 2012-7-28 下午03:29:25
	 */
	public static double getDouble(Object value) {
		try {
			return Double.parseDouble(getString(value));
		} catch (Exception e) {
			return 0.0;
		}
	}

	/**
	 * 值对象 --> BigDecimal.
	 * 
	 * @param value
	 * @return
	 * @author wuqing
	 * @date 2013-3-31 下午4:12:37
	 */
	public static BigDecimal getBigDecimal(Object value) {
		return new BigDecimal(getString(value));
	}

	/**
	 * 值对象 --> int.
	 * 
	 * @param value
	 * @return
	 * @author wuqing
	 * @date 2012-7-28 下午03:29:35
	 */
	public static int getInt(Object value) {
		try {
			return Integer.parseInt(getString(value));
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 值对象 --> boolean.
	 * 
	 * @param value
	 * @return
	 * @author wuqing
	 * @date 2012-10-12 上午09:00:16
	 */
	public static boolean getBoolean(Object value) {
		try {
			String v = getString(value);
			if ("1".equals(v)) {
				return true;
			} else if ("0".equals(v)) {
				return false;
			} else if ("Y".equals(v)) {
				return true;
			} else if ("N".equals(v)) {
				return false;
			} else {
				return Boolean.parseBoolean(v);
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 强行值类型转换.
	 * 
	 * @param value
	 * @param type
	 * @return
	 * @author wuqing
	 * @date 2013-2-8 下午5:26:26
	 */
	public static Object castValue(Object value, Class<?> type) {
		if (int.class.isAssignableFrom(type)
				|| Integer.class.isAssignableFrom(type)) {
			return ValueUtil.getInt(value);
		} else if (long.class.isAssignableFrom(type)
				|| Long.class.isAssignableFrom(type)) {
			return ValueUtil.getLong(value);
		} else if (double.class.isAssignableFrom(type)
				|| Double.class.isAssignableFrom(type)) {
			return ValueUtil.getDouble(value);
		} else if (float.class.isAssignableFrom(type)
				|| Float.class.isAssignableFrom(type)) {
			return ValueUtil.getFloat(value);
		} else if (boolean.class.isAssignableFrom(type)
				|| Boolean.class.isAssignableFrom(type)) {
			return ValueUtil.getBoolean(value);
		} else if (String.class.isAssignableFrom(type)) {
			return ValueUtil.getString(value);
		} else if (BigDecimal.class.isAssignableFrom(type)) {
			return ValueUtil.getBigDecimal(value);
		} else if (char.class.isAssignableFrom(type)
				|| Character.class.isAssignableFrom(type)) {
			return (char) ValueUtil.getInt(value);
		} else {
			throw new MapperException("unknow value type:" + type);
		}
	}

	/**
	 * 值对象 --> String.
	 * 
	 * @param value
	 * @return
	 * @author wuqing
	 * @date 2012-7-28 下午03:15:11
	 */
	public static String getString(Object value) {
		String result = "";
		if (value != null) {
			String sValue = value.toString().trim();
			if (value instanceof Number) {
				if (value instanceof Double || value instanceof BigDecimal) {
					if (!"Infinity".equals(sValue) && !"NaN".equals(sValue)) {
						result = toNuSicen(value);
					} else {
						result = "0";
					}
				} else {
					result = sValue;
				}
			} else {
				result = sValue;
			}
		}
		return result.trim();
	}

	/**
	 * 四舍五入并去掉科学计数法, 默认小数点2位.
	 * 
	 * @param value
	 *            String, double, Double, BigDecimal
	 * @return
	 * @author wuqing
	 * @date 2012-7-28 下午03:44:05
	 */
	public static String toNuSicen(Object value) {
		return toNuSicen(value, 2);
	}

	/**
	 * 四舍五入并去掉科学计数法.
	 * 
	 * @param value
	 *            String, double, Double, BigDecimal
	 * @param precision
	 *            保留几位小数
	 * @return
	 * @author wuqing
	 * @date 2012-7-28 下午03:47:25
	 */
	public static String toNuSicen(Object value, int precision) {
		Object result = "";
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(precision);
		df.setMaximumFractionDigits(precision);
		df.setGroupingUsed(false);
		try {
			if (value instanceof BigDecimal) {
				result = value;
			} else if (value instanceof String) {
				result = new BigDecimal(value.toString());
			} else if (value instanceof Number) {
				result = Double.valueOf(value.toString());
			} else {
				return df.format(0);
			}
		} catch (Exception e) {
			return df.format(0);
		}
		return df.format(result);
	}

}
