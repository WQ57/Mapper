package com.wq.common.mapper.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.wq.common.mapper.MapperException;

/**
 * Xml工具类.
 * 
 * @author qingwu
 * @date 2014-8-7 下午3:48:53
 */
public class XmlUtil {

	/**
	 * 获得xml文档.
	 * 
	 * @return
	 * @author wuqing
	 * @date 2014年8月19日 下午10:23:15
	 */
	public static Document getXmlDocument() {
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return builder.newDocument();
		} catch (ParserConfigurationException e) {
			throw new MapperException(e);
		}
	}

	/**
	 * 获得xml文档.
	 * 
	 * @param xml
	 * @return
	 * @author wuqing
	 * @date 2014年8月19日 下午10:23:15
	 */
	public static Document getXmlDocument(String xml) {
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return builder.parse(new InputSource(new StringReader(xml)));
		} catch (ParserConfigurationException e) {
			throw new MapperException(e);
		} catch (SAXException e) {
			throw new MapperException(e);
		} catch (IOException e) {
			throw new MapperException(e);
		}
	}

	/**
	 * 获得xml文档对象.
	 * 
	 * @param protocolXML
	 *            文档位置
	 * @return
	 * @author qingwu
	 * @date 2014-8-7 下午3:56:06
	 */
	public static Document getXmlDocument(InputStream in) {
		try {
			DocumentBuilder builder;
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return builder.parse(in);
		} catch (ParserConfigurationException e) {
			throw new MapperException(e);
		} catch (SAXException e) {
			throw new MapperException(e);
		} catch (IOException e) {
			throw new MapperException(e);
		}
	}

	/**
	 * xml文档转字符串.
	 * 
	 * @param document
	 * @return
	 * @author qingwu
	 * @date 2014-8-20 下午2:20:02
	 */
	public static String documentToString(Document document) {
		String result = null;

		if (document != null) {
			StringWriter strWtr = new StringWriter();
			StreamResult strResult = new StreamResult(strWtr);
			TransformerFactory tfac = TransformerFactory.newInstance();
			try {
				javax.xml.transform.Transformer t = tfac.newTransformer();
				t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				t.setOutputProperty(OutputKeys.INDENT, "yes");
				t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,
				// text
				t.setOutputProperty(
						"{http://xml.apache.org/xslt}indent-amount", "4");
				t.transform(new DOMSource(document.getDocumentElement()),
						strResult);
			} catch (Exception e) {
				throw new MapperException("XML.toString(Document): " + e);
			}
			result = strResult.getWriter().toString();
			try {
				strWtr.close();
			} catch (IOException e) {
				throw new MapperException(e);
			}
		}

		return result;
	}
}
