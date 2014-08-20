package com.wq.common.mapper;

import java.util.HashMap;
import java.util.Map;

import com.wq.common.mapper.adapter.IMapperAdapter;
import com.wq.common.mapper.adapter.JsonMapperAdapter;
import com.wq.common.mapper.adapter.MapMapperAdapter;
import com.wq.common.mapper.adapter.XmlMapperAdapter;

/**
 * 对象映射工具类.
 * 
 * @author qingwu
 * @date 2014-8-19 下午4:14:59
 */
public class MapperUtil {

	private static Map<String, IMapperAdapter> mappers = new HashMap<String, IMapperAdapter>();

	public static final String MAPPER_MAP = MapMapperAdapter.class.getName();
	public static final String MAPPER_XML = XmlMapperAdapter.class.getName();
	public static final String MAPPER_JSON = JsonMapperAdapter.class.getName();

	static {
		registeMapperAdapter(new MapMapperAdapter());
		registeMapperAdapter(new XmlMapperAdapter());
		registeMapperAdapter(new JsonMapperAdapter());
	}

	/**
	 * 注册映射适配器.
	 * 
	 * @param mapper
	 *            映射器
	 * @author qingwu
	 * @date 2014-8-20 下午3:10:23
	 */
	public static void registeMapperAdapter(IMapperAdapter mapper) {
		if (!mappers.containsKey(mapper.getClass().getName())) {
			mappers.put(mapper.getClass().getName(), mapper);
		}
	}

	/**
	 * 转Bean.
	 * 
	 * @param source
	 *            源数据
	 * @param clz
	 *            目标对象
	 * @param type
	 *            转化类型
	 * @return
	 * @author qingwu
	 * @date 2014-8-20 下午2:59:25
	 */
	public static <T> T toBean(Object source, Class<T> clz, String type) {
		IMapperAdapter mapper = mappers.get(type);
		if (mapper == null) {
			throw new MapperException("There is no MapperAdapter type of "
					+ type + "...");
		}
		return mapper.toBean(source, clz);
	}

	/**
	 * 转目标对象.
	 * 
	 * @param obj
	 *            bean对象
	 * @param type
	 *            类型
	 * @return
	 * @author qingwu
	 * @date 2014-8-20 下午3:01:46
	 */
	public static <T> Object toTarget(T obj, String type) {
		IMapperAdapter mapper = mappers.get(type);
		if (mapper == null) {
			throw new MapperException("There is no MapperAdapter type of "
					+ type + "...");
		}
		return mapper.toTarget(obj);
	}

	/**
	 * map转bean.
	 * 
	 * @param map
	 * @param obj
	 * @author qingwu
	 * @date 2014-8-19 下午4:17:14
	 */
	public static <T> T mapToBean(Map<String, Object> map, Class<T> clz) {
		return toBean(map, clz, MAPPER_MAP);
	}

	/**
	 * bean转map.
	 * 
	 * @param obj
	 * @return
	 * @author qingwu
	 * @date 2014-8-19 下午4:17:24
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> beanToMap(T obj) {
		return (Map<String, Object>) toTarget(obj, MAPPER_MAP);
	}

	/**
	 * xml转bean.
	 * 
	 * @param xml
	 * @param obj
	 * @author qingwu
	 * @date 2014-8-19 下午4:18:44
	 */
	public static <T> T xmlToBean(String xml, Class<T> clz) {
		return toBean(xml, clz, MAPPER_XML);
	}

	/**
	 * bean转xml.
	 * 
	 * @param obj
	 * @return
	 * @author qingwu
	 * @date 2014-8-19 下午4:19:26
	 */
	public static <T> String beanToXml(T obj) {
		return (String) toTarget(obj, MAPPER_XML);
	}

	/**
	 * json转bean.
	 * 
	 * @param json
	 * @param obj
	 * @author qingwu
	 * @date 2014-8-19 下午4:20:35
	 */
	public static <T> T jsonToBean(String json, Class<T> clz) {
		return toBean(json, clz, MAPPER_JSON);
	}

	/**
	 * bean转json.
	 * 
	 * @param obj
	 * @return
	 * @author qingwu
	 * @date 2014-8-19 下午4:21:11
	 */
	public static <T> String beanToJson(T obj) {
		return (String) toTarget(obj, MAPPER_JSON);
	}

}
