package com.wq.common.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 映射标签.
 * 
 * @author qingwu
 * @date 2014-8-19 下午4:23:25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface Mapper {

	/**
	 * 映射的别名.
	 * 
	 * @return
	 * @author qingwu
	 * @date 2014-8-19 下午4:24:02
	 */
	public String value();

	/**
	 * 格式化.<br>
	 * 1.时间参数：必须符合JAVA时间格式化，默认是：YYYY-MM-DD HH24:mm:SS<br>
	 * 2.浮点数：是数字，表示精确到小数点后几位，默认是2位，默认四舍五入<br>
	 * 
	 * @return
	 * @author qingwu
	 * @date 2014-8-19 下午4:24:25
	 */
	public String format() default "";

}
