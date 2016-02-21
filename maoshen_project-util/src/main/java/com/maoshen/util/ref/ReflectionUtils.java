package com.maoshen.util.ref;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射工具类
 * 
 * @author seara
 * @Email Zhixiang.tian@vipshop.com
 * @Date May 5, 2014 1:02:22 PM
 */
public class ReflectionUtils {

	/**
	 * 获取方法，适用于有继承关系的类
	 * 
	 * @author seara
	 * @Email Zhixiang.tian@vipshop.com
	 * @Date May 5, 2014 1:02:30 PM
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
		Method method = null;

		for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
				return method;
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 获取属性，适用于有继承关系的类
	 * 
	 * @author seara
	 * @Email Zhixiang.tian@vipshop.com
	 * @Date May 5, 2014 1:03:07 PM
	 * @param object
	 * @param fieldName
	 * @return
	 */
	public static Field getDeclaredField(Object object, String fieldName) {
		Field field = null;

		Class<?> clazz = object.getClass();

		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				return field;
			} catch (Exception e) {

			}
		}

		return null;
	}

	/**
	 * 获取所有属性，适用于有继承关系的类
	 * 
	 * @author seara
	 * @Email Zhixiang.tian@vipshop.com
	 * @Date May 5, 2014 1:03:21 PM
	 * @param object
	 * @return
	 */
	public static List<Field> getDeclaredFields(Object object) {
		Class<?> clazz = object.getClass();

		List<Field> list = new ArrayList<Field>();

		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				Field[] fields = clazz.getDeclaredFields();
				if (fields != null) {
					list.addAll(Arrays.asList(fields));
				}
			} catch (Exception e) {

			}
		}
		return list;
	}
}
