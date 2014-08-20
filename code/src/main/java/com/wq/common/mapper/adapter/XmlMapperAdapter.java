package com.wq.common.mapper.adapter;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wq.common.mapper.Mapper;
import com.wq.common.mapper.MapperException;
import com.wq.common.mapper.util.DateUtil;
import com.wq.common.mapper.util.ReflectUtil;
import com.wq.common.mapper.util.ValueUtil;
import com.wq.common.mapper.util.XmlUtil;

/**
 * xml映射适配器.
 * 
 * @author qingwu
 * @date 2014-8-20 上午10:18:45
 */
public class XmlMapperAdapter extends AbstractMapperAdapter implements
		IMapperAdapter {

	public static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	@Override
	public <T1, T2> T2 toBean(T1 source, Class<T2> clz) {
		if (source == null) {
			return null;
		}
		if (!source.getClass().isAssignableFrom(String.class)) {
			throw new MapperException("source must be String...");
		}
		String xml = (String) source;
		if (!xml.startsWith("<?xml")) {
			xml = XML_HEAD + xml;
		}
		Document doc = XmlUtil.getXmlDocument(xml);
		Node root = doc.getChildNodes().item(0);
		return toBean(root, clz);
	}

	/**
	 * Node转Bean.
	 * 
	 * @param node
	 * @param clz
	 * @return
	 * @author qingwu
	 * @date 2014-8-20 上午10:54:16
	 */
	@SuppressWarnings("rawtypes")
	private <T> T toBean(Node node, Class<T> clz) {
		if (node == null) {
			return null;
		}
		T obj = ReflectUtil.newInstance(clz);
		Map<String, Field> fields = super.getMapperFieldMap(clz);
		NodeList nodes = node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node subNode = nodes.item(i);
			String nodeName = subNode.getNodeName();
			if (!fields.containsKey(nodeName)) {
				continue;
			}
			Field field = fields.get(nodeName);
			String fieldName = field.getName();
			Class fieldType = field.getType();
			Object setValue = null;
			// 转obj对象值
			if (ValueUtil.isOriginal(fieldType)) {// 原始类型
				setValue = ValueUtil.castValue(subNode.getTextContent(),
						fieldType);
			} else if (DateUtil.isDate(fieldType)) {// 日期类型
				Mapper mapper = field.getAnnotation(Mapper.class);
				String format = DateUtil.format_01;
				if (mapper != null && mapper.format() != null
						&& !mapper.format().equals("")) {
					format = mapper.format();
				}
				setValue = DateUtil.castDate(subNode.getTextContent(),
						fieldType, format);
			} else {// 自定义类型
				setValue = toBean(subNode, field.getType());
			}
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
		Document doc = XmlUtil.getXmlDocument();
		doc.appendChild(toNode(doc, "root", obj));
		return XmlUtil.documentToString(doc);
	}

	/**
	 * bean转node.
	 * 
	 * @param doc
	 * @param rootName
	 * @param obj
	 * @return
	 * @author qingwu
	 * @date 2014-8-20 下午2:04:54
	 */
	@SuppressWarnings("rawtypes")
	public <T> Node toNode(Document doc, String rootName, T obj) {
		Node root = doc.createElement(rootName);
		Map<String, Field> fields = super.getMapperFieldMap(obj.getClass());
		for (Entry<String, Field> entry : fields.entrySet()) {
			String nodeName = entry.getKey();
			Field field = entry.getValue();
			String fieldName = field.getName();
			Class fieldType = field.getType();
			Object value = ReflectUtil.getFieldValue(obj, fieldName);
			// 转obj对象值
			Node node = doc.createElement(nodeName);
			if (ValueUtil.isOriginal(fieldType)) {// 原始类型
				node.setTextContent(ValueUtil.getString(value));
			} else if (DateUtil.isDate(fieldType)) {// 日期类型
				Mapper mapper = field.getAnnotation(Mapper.class);
				String format = DateUtil.format_01;
				if (mapper != null && mapper.format() != null
						&& !mapper.format().equals("")) {
					format = mapper.format();
				}
				node.setTextContent(DateUtil.dateToStr(value, format));
			} else {// 自定义类型
				node = toNode(doc, nodeName, value);
			}
			root.appendChild(node);
		}
		return root;
	}

}
