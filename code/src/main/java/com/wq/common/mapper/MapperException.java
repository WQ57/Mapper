package com.wq.common.mapper;

/**
 * 映射错误.
 * 
 * @author qingwu
 * @date 2014-8-19 下午4:12:45
 */
public class MapperException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5991090075115183901L;

	public MapperException() {
		super();
	}

	public MapperException(String message, Throwable cause) {
		super(message, cause);
	}

	public MapperException(String message) {
		super(message);
	}

	public MapperException(Throwable cause) {
		super(cause);
	}

}
