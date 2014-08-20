package com.wq.common.mapper.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wq.common.mapper.MapperException;

/**
 * 反射工具类.
 * 
 * @author qingwu
 * @date 2014-8-7 下午5:14:07
 */
public class ReflectUtil {

	/**
	 * 创建一个对象.
	 * 
	 * @param c
	 *            类型
	 * @return
	 * @author wuqingMapperException
	 * @date 2014年8月8日 下午10:58:46
	 */
	public static <T> T newInstance(Class<T> c) {
		try {
			return c.newInstance();
		} catch (InstantiationException e) {
			throw new MapperException(e);
		} catch (IllegalAccessException e) {
			throw new MapperException(e);
		}
	}

	/**
	 * 执行某个对象的方法(包括父类的方法).
	 * 
	 * @param owner
	 * @param methodName
	 * @param list
	 * @param paramsClass
	 * @return
	 * @author wuqing
	 * @date 2014年8月9日 上午12:35:48
	 */
	@SuppressWarnings("rawtypes")
	public static Object invokeMethod(Object owner, String methodName,
			List<Object> list, Class[] paramsClass) {
		Object[] args = new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			args[i] = list.get(i);
		}
		return invokeMethod(owner, methodName, args, paramsClass);
	}

	/**
	 * 执行某个对象的方法(包括父类的方法).
	 * 
	 * @param owner
	 * @param methodName
	 * @param args
	 * @param paramsClass
	 * @return
	 * @author wuqing
	 * @date 2014年8月9日 上午12:36:02
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Object invokeMethod(Object owner, String methodName,
			Object[] args, Class[] paramsClass) {
		try {
			Class ownerClass = owner.getClass();
			Method method = getMethod(ownerClass, paramsClass, methodName);
			if (method == null) {
				throw new Exception("没有该方法(" + methodName + ")");
			}
			return method.invoke(owner, args);
		} catch (SecurityException e) {
			throw new MapperException(e);
		} catch (IllegalArgumentException e) {
			throw new MapperException(e);
		} catch (IllegalAccessException e) {
			throw new MapperException(e);
		} catch (InvocationTargetException e) {
			throw new MapperException(e);
		} catch (Exception e) {
			throw new MapperException(e);
		}
	}

	/**
	 * 递归父类查找方法.
	 * 
	 * @param ownerClass
	 * @param argsClass
	 * @param methodName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Method getMethod(Class ownerClass, Class[] argsClass,
			String methodName) throws SecurityException {
		if (ownerClass == null) {
			return null;
		}
		Method method = null;
		try {
			method = ownerClass.getDeclaredMethod(methodName, argsClass);
			method.setAccessible(true);
		} catch (NoSuchMethodException e) {
			return getMethod(ownerClass.getSuperclass(), argsClass, methodName);
		}
		return method;
	}

	/**
	 * 获得包下面的所有类.
	 * 
	 * @param packageName
	 *            包名
	 * @return
	 * @author qingwu
	 * @date 2014-8-7 下午5:16:21
	 */
	@SuppressWarnings("rawtypes")
	public static List<Class> getClassFromPackage(String packageName) {
		try {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();
			String path = packageName.replace(".", "/");
			Enumeration<URL> resources = classLoader.getResources(path);
			List<File> fileList = new ArrayList<File>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				fileList.add(new File(resource.getFile()));
			}
			List<Class> classes = new ArrayList<Class>();
			for (File file : fileList) {
				classes.addAll(getClassFromPackage(file, packageName));
			}
			return classes;
		} catch (IOException e) {
			throw new MapperException(e);
		}
	}

	/**
	 * 获取包路径下的所有类.
	 * 
	 * @param file
	 *            文件目录
	 * @param packageName
	 *            包名
	 * @return
	 * @author qingwu
	 * @date 2014-8-7 下午5:26:05
	 */
	@SuppressWarnings("rawtypes")
	public static List<Class> getClassFromPackage(File direcotry,
			String packageName) {
		List<Class> classes = new ArrayList<Class>();

		if (!direcotry.exists()) {
			return classes;
		}
		File[] files = direcotry.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				String filePath = file.getPath();
				filePath = filePath.replace("\\", ".");
				filePath = filePath.replace("/", ".");
				String[] arry = filePath.split("\\.");
				classes.addAll(getClassFromPackage(file, packageName + "."
						+ arry[arry.length - 1]));
			} else if (file.getName().endsWith(".class")) {
				String className = packageName
						+ '.'
						+ file.getName().substring(0,
								file.getName().length() - 6);
				try {
					classes.add(Class.forName(className));
				} catch (ClassNotFoundException e) {
					throw new MapperException(e);
				}
			}
		}
		return classes;
	}

	/**
	 * 强制获得字段值.
	 * 
	 * @param obj
	 *            对象
	 * @param fieldName
	 *            成员名称
	 * @return
	 * @throws Exception
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Object result = null;
		Field field = ReflectUtil.getField(obj, fieldName);
		if (field != null) {
			field.setAccessible(true);
			try {
				result = field.get(obj);
			} catch (IllegalArgumentException e) {
				throw new MapperException(e);
			} catch (IllegalAccessException e) {
				throw new MapperException(e);
			}
		}
		return result;
	}

	/**
	 * 强制设置对象的成员值.
	 * 
	 * @param obj
	 *            对象
	 * @param fieldName
	 *            成员名称
	 * @param value
	 *            成员值
	 * @author wuqing
	 * @date 2014年3月23日 上午11:28:45
	 */
	public static void setFieldValue(Object obj, String fieldName, Object value) {
		Field field = ReflectUtil.getField(obj, fieldName);
		if (field != null) {
			try {
				field.setAccessible(true);
				field.set(obj, value);
			} catch (IllegalArgumentException e) {
				throw new MapperException(e);
			} catch (IllegalAccessException e) {
				throw new MapperException(e);
			}
		}
	}

	/**
	 * 利用反射获取指定对象里面的指定属性
	 * 
	 * @param obj
	 *            目标对象
	 * @param fieldName
	 *            目标属性
	 * @return 目标字段
	 */
	private static Field getField(Object obj, String fieldName) {
		Field field = null;
		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz
				.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				break;
			} catch (NoSuchFieldException e) {
				// 这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
			}
		}
		return field;
	}

	/**
	 * 获得一个类的所有成员对象（包括父类）
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Field[] getFields(Class c) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {
			list.add(field);
		}
		getFieldList(c, list);
		Field[] arry = new Field[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arry[i] = list.get(i);
		}
		return arry;
	}

	/**
	 * 获得类字段map<fieldName,Field>.
	 * 
	 * @return
	 * @author wuqing
	 * @date 2014年8月10日 下午5:56:10
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Field> getFieldMap(Class c) {
		Field[] fields = getFields(c);
		Map<String, Field> map = new HashMap<String, Field>();
		for (Field field : fields) {
			map.put(field.getName(), field);
		}
		return map;
	}

	/**
	 * 递归执行查找父类的字段.
	 * 
	 * @param c
	 * @param subFieldList
	 */
	@SuppressWarnings("rawtypes")
	private static void getFieldList(Class c, List<Field> subFieldList) {
		Class superClass = c.getSuperclass();
		if (superClass != null) {
			Field[] fields = superClass.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				subFieldList.add(field);
			}
			getFieldList(superClass, subFieldList);
		}
	}

	/**
	 * 是否实现了接口.
	 * 
	 * @param c
	 * @param szInterface
	 * @return
	 * @author wuqing
	 * @date 2014年8月19日 下午10:12:30
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isImplementsInterface(Class c, Class szInterface) {
		return isImplementsInterface(c, szInterface.getName());
	}

	/**
	 * 是否实现了接口.
	 * 
	 * @param c
	 * @param szInterface
	 * @return
	 * @author wuqing
	 * @date 2014年8月10日 下午9:05:12
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isImplementsInterface(Class c, String szInterface) {
		Class[] face = c.getInterfaces();
		for (int i = 0, j = face.length; i < j; i++) {
			if (face[i].getName().equals(szInterface)) {
				return true;
			} else {
				Class[] face1 = face[i].getInterfaces();
				for (int x = 0; x < face1.length; x++) {
					if (face1[x].getName().equals(szInterface)) {
						return true;
					} else if (isImplementsInterface(face1[x], szInterface)) {
						return true;
					}
				}
			}
		}
		if (null != c.getSuperclass()) {
			return isImplementsInterface(c.getSuperclass(), szInterface);
		}
		return false;
	}
}
