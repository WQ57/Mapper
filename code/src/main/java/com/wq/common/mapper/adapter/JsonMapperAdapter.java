package com.wq.common.mapper.adapter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wq.common.mapper.Mapper;
import com.wq.common.mapper.MapperException;
import com.wq.common.mapper.util.DateUtil;
import com.wq.common.mapper.util.ReflectUtil;
import com.wq.common.mapper.util.ValueUtil;

/**
 * Json映射适配器.
 * 
 * @author qingwu
 * @date 2014-8-20 下午2:50:03
 */
public class JsonMapperAdapter extends AbstractMapperAdapter implements
		IMapperAdapter {

	/**
	 * 校验这个字段是否需要映射.
	 * 
	 * @param field
	 * @return
	 * @author qingwu
	 * @date 2014-8-19 下午4:44:28
	 */
	public boolean checkField(Field field) {
		String fieldName = field.getName();
		if (super.getIgnoreFieldNames().containsKey(fieldName)) {
			return false;
		}
		return true;
	}

	@Override
	public <T1, T2> T2 toBean(T1 source, Class<T2> clz) {
		if (source == null) {
			return null;
		}
		if (!source.getClass().isAssignableFrom(String.class)) {
			throw new MapperException("source type must be String...");
		}
		try {
			JSONObject json = new JSONObject((String) source);
			return toBean(json, clz);
		} catch (JSONException e) {
			throw new MapperException(e);
		}
	}

	/**
	 * json转bean.
	 * 
	 * @param json
	 * @param clz
	 * @return
	 * @author qingwu
	 * @throws JSONException
	 * @date 2014-8-20 下午3:56:53
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T> T toBean(JSONObject json, Class<T> clz) throws JSONException {
		if (json == null) {
			return null;
		}
		T obj = ReflectUtil.newInstance(clz);
		Map<String, Field> fields = super.getMapperFieldMap(obj.getClass());
		for (Entry<String, Field> entry : fields.entrySet()) {
			String jsonName = entry.getKey();
			Field field = entry.getValue();
			String fieldName = field.getName();
			Class fieldType = field.getType();
			Object setValue = null;
			// 获得json值
			if (ValueUtil.isOriginal(fieldType)) {// 原始类型
				setValue = ValueUtil.castValue(json.get(jsonName), fieldType);
			} else if (DateUtil.isDate(fieldType)) {// 日期类型
				Mapper mapper = field.getAnnotation(Mapper.class);
				String format = DateUtil.format_01;
				if (mapper != null && mapper.format() != null
						&& !mapper.format().equals("")) {
					format = mapper.format();
				}
				setValue = DateUtil.castDate(json.get(jsonName), fieldType,
						format);
			} else if (fieldType.isAssignableFrom(List.class)
					|| ReflectUtil.isImplementsInterface(fieldType, List.class)) {// List类型
				ParameterizedType pt = (ParameterizedType) field
						.getGenericType();
				Class listClz = pt.getActualTypeArguments().getClass();
				List list = new ArrayList();
				JSONArray arry = json.getJSONArray(jsonName);
				for (int i = 0; i < arry.length(); i++) {
					list.add(toBean(arry.getJSONObject(i), listClz));
				}
				setValue = list;
			} else {// 自定义类型
				setValue = toBean(json.getJSONObject(jsonName), fieldType);
			}
			// 注入对象.
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
		JSONObject json = new JSONObject();
		try {
			toJSONObject(json, obj);
		} catch (JSONException e) {
			throw new MapperException(e);
		}
		return json.toString();
	}

	/**
	 * bean转json.
	 * 
	 * @param json
	 * @param obj
	 * @author qingwu
	 * @throws JSONException
	 * @date 2014-8-20 下午5:07:30
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> void toJSONObject(JSONObject json, T obj) throws JSONException {
		Map<String, Field> fields = super.getMapperFieldMap(obj.getClass());
		for (Entry<String, Field> entry : fields.entrySet()) {
			String jsonName = entry.getKey();
			Field field = entry.getValue();
			String fieldName = field.getName();
			Class fieldType = field.getType();
			Object value = ReflectUtil.getFieldValue(obj, fieldName);
			// 注入json
			if (ValueUtil.isOriginal(fieldType)) {// 原始类型
				json.put(jsonName, value);
			} else if (DateUtil.isDate(fieldType)) {// 日期类型
				Mapper mapper = field.getAnnotation(Mapper.class);
				String format = DateUtil.format_01;
				if (mapper != null && mapper.format() != null
						&& !mapper.format().equals("")) {
					format = mapper.format();
				}
				json.put(jsonName, DateUtil.dateToStr(value, format));
			} else if (fieldType.isAssignableFrom(List.class)
					|| ReflectUtil.isImplementsInterface(fieldType, List.class)) {// List类型
				List list = (List) value;
				if (list == null) {
					json.append(jsonName, null);
				} else {
					JSONArray arry = new JSONArray();
					for (int i = 0; i < list.size(); i++) {
						JSONObject subJson = new JSONObject();
						toJSONObject(subJson, list.get(i));
						arry.put(subJson);
					}
					json.put(jsonName, arry);
				}
			} else {// 自定义类型
				JSONObject subJson = new JSONObject();
				toJSONObject(subJson, value);
				json.put(jsonName, subJson);
			}
		}
	}
}
