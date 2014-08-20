package com.wq.common.mapper.adapter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.wq.common.mapper.Mapper;
import com.wq.common.mapper.MapperException;
import com.wq.common.mapper.util.DateUtil;
import com.wq.common.mapper.util.ReflectUtil;
import com.wq.common.mapper.util.ValueUtil;

/**
 * java.util.Map映射适配器.
 * 
 * @author wuqing
 * @date 2014年8月19日 下午10:36:35
 */
public class MapMapperAdapter extends AbstractMapperAdapter implements
		IMapperAdapter {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T1, T2> T2 toBean(T1 source, Class<T2> clz) {
		if (source == null) {
			return null;
		}
		if (source.getClass().isAssignableFrom(Map.class)) {
			throw new MapperException("soruce type must be java.util.Map");
		}
		Map<String, Object> map = (Map<String, Object>) source;
		T2 obj = ReflectUtil.newInstance(clz);
		Field[] fields = ReflectUtil.getFields(obj.getClass());
		for (Field field : fields) {
			// 校验字段是否需要映射
			if (checkField(field) == false) {
				continue;
			}
			// 校验Map是否符合映射
			String key = field.getName();
			Mapper mapper = (Mapper) field.getAnnotation(Mapper.class);
			if (mapper != null) {
				key = mapper.value();
			}
			if (!map.containsKey(key)) {
				continue;
			}
			if (map.get(key) == null) {
				continue;
			}
			// 注入对象
			String fieldName = field.getName();
			Class fieldType = field.getType();
			Object mapValue = map.get(key);
			Object setValue = null;
			// map值转obj对象值
			if (ValueUtil.isOriginal(fieldType)) {// 原始类型
				setValue = ValueUtil.castValue(mapValue, fieldType);
			} else if (DateUtil.isDate(fieldType)) {// 日期类型
				String format = DateUtil.format_01;
				if (mapper != null && mapper.format() != null
						&& !mapper.format().equals("")) {
					format = mapper.format();
				}
				setValue = DateUtil.castDate(mapValue, fieldType, format);
			} else {// 自定义类型
				if (ReflectUtil.isImplementsInterface(mapValue.getClass(),
						Map.class)) {// 如果是嵌套map，递归执行
					setValue = toBean((Map<String, Object>) mapValue, fieldType);
				} else if (!ValueUtil.isOriginal(mapValue.getClass())
						&& !DateUtil.isDate(mapValue.getClass())) {// 如果是自定义类型
					setValue = mapValue;
				}
			}
			// 注入对象成员
			if (setValue != null) {
				ReflectUtil.setFieldValue(obj, fieldName, setValue);
			}
		}
		return obj;
	}

	@Override
	public <T> Object toTarget(T obj) {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = ReflectUtil.getFields(obj.getClass());
		for (Field field : fields) {
			// 校验字段是否需要映射
			if (checkField(field) == false) {
				continue;
			}
			// 校验Map是否符合映射
			String key = field.getName();
			Mapper mapper = (Mapper) field.getAnnotation(Mapper.class);
			if (mapper != null) {
				key = mapper.value();
			}
			// 取成员值
			Object value = ReflectUtil.getFieldValue(obj, field.getName());
			// set
			if (ValueUtil.isOriginal(field.getType())
					|| DateUtil.isDate(field.getType())) {
				map.put(key, value);
			} else {
				map.put(key, toTarget(value));
			}
		}
		return map;
	}

}
