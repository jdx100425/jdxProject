package com.maoshen.util;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.TypeReference;

public final class JsonUtil {
	 
	private static JsonFactory jsonFactory = new JsonFactory();
	
	private static ObjectMapper mapper = null;
	
	private static TypeFactory t = null;
	
	static {
		jsonFactory.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		jsonFactory.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper = new ObjectMapper(jsonFactory);
	}
	
	/**
	 * 
	 * @Description: 返回默认的类型工厂
	 * @author qinfeng.lin
	 * @Email  qinfeng.lin@vipshop.com
	 * @Date 2014年5月26日 下午10:37:53
	 * @return
	 */
	public static TypeFactory getTypeFactory() {
		if (t == null)
			t = TypeFactory.defaultInstance(); 
		return t;
	}
	
	/**
	 * 获取jackson json lib的ObjectMapper对象
	 * @return -- ObjectMapper对象
	 */
	public static ObjectMapper getMapper() {
		return mapper;
	}

	/**
	 * 获取jackson json lib的JsonFactory对象
	 * @return --  JsonFactory对象
	 */
	public static JsonFactory getJsonFactory() {
		return jsonFactory;
	}

	/**
	 * 将json转成java bean
	 * @param <T> -- 多态类型
	 * @param json -- json字符串
	 * @param clazz -- java bean类型(Class)
	 * @return -- java bean对象
	 */
	public static <T> T toBean(String json, Class<T> clazz) {
		
		T rtv = null;
		try {
			rtv = mapper.readValue(json, clazz); 
		} catch (Exception ex) {  
			throw new IllegalArgumentException("json字符串转成java bean异常", ex);
		}
		return rtv;		
	}
	
	
	/**
	 * 
	 * @Description: 将json转化为list集合
	 * @author qinfeng.lin
	 * @Email  qinfeng.lin@vipshop.com
	 * @Date 2014年5月26日 下午10:39:00
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> toListBean(String json, Class<T> clazz) {
		List<T> list = null;
		try {
			list = mapper.readValue(json,getTypeFactory().constructCollectionType(ArrayList.class,clazz));
		} catch (Exception ex) {  
			throw new IllegalArgumentException("json字符串转成java bean异常", ex);
		}
		return list;
	}
	
	/**
	 * 将java bean转成json
	 * @param bean -- java bean
	 * @return -- json 字符串
	 */
	public static String toJson(Object bean) {
		
		String rtv = null;
		try {
			rtv = mapper.writeValueAsString(bean);
		} catch (Exception ex) {  
			throw new IllegalArgumentException("java bean转成json字符串异常", ex);
		}
		return rtv;		
	}
}
