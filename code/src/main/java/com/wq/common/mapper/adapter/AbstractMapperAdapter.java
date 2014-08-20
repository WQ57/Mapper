package com.wq.common.mapper.adapter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.wq.common.mapper.Mapper;
import com.wq.common.mapper.util.ReflectUtil;

/**
 * 映射适配器抽象类.
 * 
 * @author wuqing
 * @date 2014年8月19日 下午10:39:25
 */
public class AbstractMapperAdapter {

	/**
	 * 过滤的字段名称.
	 */
	private static Map<String, String> ignoreFieldNames = new HashMap<String, String>();
	/**
	 * 忽略的字段类型.
	 */
	@SuppressWarnings("rawtypes")
	private static Map<Class, Class> ignoreFieldTypes = new HashMap<Class, Class>();

	static {
		ignoreFieldNames.put("serialVersionUID", "serialVersionUID");
		ignoreFieldNames.put("class", "class");

		ignoreFieldTypes.put(Map.class, Map.class);
	}

	/**
	 * @return the ignoreFieldNames
	 */
	public static Map<String, String> getIgnoreFieldNames() {
		return ignoreFieldNames;
	}

	/**
	 * @return the ignoreFieldTypes
	 */
	@SuppressWarnings("rawtypes")
	public static Map<Class, Class> getIgnoreFieldTypes() {
		return ignoreFieldTypes;
	}

	/**
	 * 校验这个字段是否需要映射.
	 * 
	 * @param field
	 * @return
	 * @author qingwu
	 * @date 2014-8-19 下午4:44:28
	 */
	public boolean checkField(Field field) {
		if (ignoreFieldTypes.containsKey(field.getType())) {
			return false;
		}
		if (field.getClass().isArray()) {
			return false;
		}
		String fieldName = field.getName();
		if (ignoreFieldNames.containsKey(fieldName)) {
			return false;
		}
		return true;
	}

	/**
	 * 获得标签映射的字段map.
	 * 
	 * @param clz
	 * @return
	 * @author qingwu
	 * @date 2014-8-20 上午11:07:01
	 */
	public <T> Map<String, Field> getMapperFieldMap(Class<T> clz) {
		Field[] fields = ReflectUtil.getFields(clz);
		Map<String, Field> map = new HashMap<String, Field>();
		for (Field field : fields) {
			if (checkField(field) == false) {
				continue;
			}
			Mapper mapper = field.getAnnotation(Mapper.class);
			if (mapper != null) {
				map.put(mapper.value(), field);
			} else {
				map.put(field.getName(), field);
			}
		}
		return map;
	}
}
