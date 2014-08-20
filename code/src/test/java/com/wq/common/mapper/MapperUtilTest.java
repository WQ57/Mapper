package com.wq.common.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.wq.common.vo.TestVO_01;
import com.wq.common.vo.TestVO_02;

/**
 * 测试类.
 * 
 * @author wuqing
 * @date 2014年8月19日 下午9:45:28
 */
public class MapperUtilTest {

	private TestVO_01 vo = new TestVO_01();

	@Before
	public void init() {
		vo.setS("abcede");
		vo.setDate(new Date());
		vo.setF(3.14f);
		vo.setI(123);
		vo.setL(123456789l);
		vo.setD(88.888888d);

		TestVO_02 vo2 = new TestVO_02();
		vo2.setS("abcede");
		vo2.setDate(new Date());
		vo2.setF(3.14f);
		vo2.setI(123);
		vo2.setL(123456789l);
		vo2.setD(88.888888d);

		vo.setVo(vo2);
	}

	@Test
	public void testMap() {
		Map<String, Object> map = MapperUtil.beanToMap(vo);
		System.out.println("=======>beanToMap:");
		System.out.println(map);
		System.out.println("=======>mapToBean:");
		System.out.println(MapperUtil.mapToBean(map, TestVO_01.class)
				.toString());
	}

	@Test
	public void testXml() {
		String xml = MapperUtil.beanToXml(vo);
		System.out.println("=======>beanToXml:");
		System.out.println(xml);
		System.out.println("=======>xmlToBean:");
		System.out.println(MapperUtil.xmlToBean(xml, TestVO_01.class)
				.toString());
	}

	@Test
	public void testJson() {
		String json = MapperUtil.beanToJson(vo);
		System.out.println("=======>beanToJson:");
		System.out.println(json);
		System.out.println("=======>jsonToJson:");
		System.out.println(MapperUtil.jsonToBean(json, TestVO_01.class)
				.toString());
	}
}
