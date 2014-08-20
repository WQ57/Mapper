package com.wq.common.mapper.adapter;

/**
 * 映射适应器接口.
 * 
 * @author wuqing
 * @date 2014年8月19日 下午10:32:43
 */
public interface IMapperAdapter {

	/**
	 * 转bean.
	 * 
	 * @param source
	 * @param clz
	 * @return
	 * @author wuqing
	 * @date 2014年8月19日 下午10:35:07
	 */
	public <T1, T2> T2 toBean(T1 source, Class<T2> clz);

	/**
	 * 转目标对象.
	 * 
	 * @param obj
	 * @return
	 * @author wuqing
	 * @date 2014年8月19日 下午10:35:15
	 */
	public <T> Object toTarget(T obj);

}
