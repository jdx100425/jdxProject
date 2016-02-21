package com.maoshen.util.sql;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * SQL 工具类
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class SqlUtil {

	/**
	 * date format
	 */
	private static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static ThreadLocal threadlocal = new ThreadLocal() {
		@Override
		protected Object initialValue() {
			return new SimpleDateFormat(DATE_FORMAT);
		}
	};

	/**
	 * 返回当前线程date format。
	 * 
	 * @return
	 */
	public static DateFormat getDateFormat() {
		return (DateFormat) threadlocal.get();
	}

	/**
	 * 设置date format
	 * 
	 * @param format
	 *            -- date format
	 */
	public static void setDateFormat(String format) {
		DATE_FORMAT = format;
	}

	/**
	 * 拼接 select * SQL语句
	 * 
	 * @param tableName
	 *            -- db 表名称
	 * @param where
	 *            -- where 子句map
	 * @return -- SQL语句
	 */
	@Deprecated
	public static String makeSelectAllSql(String tableName,
			Map<String, Object> where) {

		int ws = (null != where) ? where.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		sql.append("\n select * from ").append(tableName);
		sql.append("\n where ");// 拼接where子句
		int index = 0;
		if (null != where) {
			for (String key : where.keySet()) {
				Object v = where.get(key);
				sql.append(key).append("=").append(sqlValue(v));
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接 insert SQL语句
	 * 
	 * @param tableName
	 *            -- db 表名称
	 * @param columns
	 *            -- 列集合map
	 * @return -- SQL语句
	 * @deprecated use getInsertSql() instead
	 */
	@Deprecated
	public static String makeInsertSql(String tableName,
			Map<String, Object> columns) {
		return getInsertSql(tableName, columns);
	}

	/**
	 * 拼接update SQL语句
	 * 
	 * @param tableName
	 *            -- db表名称
	 * @param set
	 *            -- update子句map
	 * @param where
	 *            -- where 子句map
	 * @return -- SQL语句
	 */
	@Deprecated
	public static String makeUpdateSql(String tableName,
			Map<String, Object> set, Map<String, Object> where) {

		if (null == set) {
			throw new IllegalArgumentException("update的字段集合不能为null");
		}
		int ss = set.size();
		int ws = (null != where) ? where.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ss * 32 + ws * 32);
		// 拼接set子句
		sql.append("\n update ").append(tableName).append("\n set ");
		int index = 0;
		for (String key : set.keySet()) {
			Object v = set.get(key);
			sql.append("\t").append(key).append("=").append(sqlValue(v));
			index++;
			if (index < ss) {
				sql.append(",\n");
			}
		}
		// 没有where子句
		if (ws == 0) {
			return sql.toString();
		}
		// 拼接where子句
		sql.append("\n where ");
		index = 0;
		for (String key : where.keySet()) {
			Object v = where.get(key);
			sql.append(key).append("=").append(sqlValue(v));
			index++;
			if (index < ws) {
				sql.append("\n   and ");
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接delete SQL语句
	 * 
	 * @param tableName
	 *            -- db表名称
	 * @param where
	 *            -- where子句map
	 * @return -- SQL语句
	 */
	@Deprecated
	public static String makeDeleteSql(String tableName,
			Map<String, Object> where) {

		int ws = (null != where) ? where.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		sql.append("\n delete from ").append(tableName);
		if (0 == ws) {
			return sql.toString();
		}
		sql.append("\n where ");
		int index = 0;
		for (String key : where.keySet()) {
			Object v = where.get(key);
			sql.append(key).append("=").append(sqlValue(v));
			index++;
			if (index < ws) {
				sql.append("\n   and ");
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接 动态SQL
	 * 
	 * @param dynamicSql
	 * @param params
	 * @return
	 * @see makeDynamicSql
	 */
	@Deprecated
	public static String makeDynamicSql(String dynamicSql, Object params) {

		Map<String, Object> b = null;
		if (params instanceof Map) {
			b = (Map<String, Object>) params;
		} else {
			try {
				b = BeanUtils.describe(params);
			} catch (Exception ex) {
				throw new IllegalArgumentException(
						"BeanUtils.describe(bean)异常", ex);
			}
		}
		return makeDynamicSql(dynamicSql, b);
	}

	/**
	 * 拼接 动态SQL
	 * 
	 * <pre>
	 *    String dynamicSql = "select * from article where {id = #id } {and val = $val } {vas in($vas )} {and creation > $createTime}";
	 *    <1>使用map作为参数
	 *    Map<String, Object> params = new HashMap<String, Object>();
	 *    params.put("id", 20060601);
	 *    params.put("val", "demo_value");
	 *    String sql = makeDynamicSql(dynamicSql, params);
	 *    // sql --> "select * from article where id = 20060601 and val = 'demo_value'"
	 *    
	 *    
	 *     <2>使用java bean作为参数
	 *     public static class MyParamBean {	 *  		
	 *  		private String id = null;
	 *  		private String val = null;	 *  		
	 *  		public String getId() {
	 *  			return id;
	 *  		}
	 *  		public void setId(String id) {
	 *  			this.id = id;
	 *  		}
	 *  		public String getVal() {
	 *  			return val;
	 *  		}
	 *  		public void setVal(String val) {
	 *  			this.val = val;
	 *  		}
	 *  	}	 *  
	 *     MyParamBean param = new MyParamBean();
	 * 	param.setId("demo_id");
	 * 	param.setVal("demo_value");
	 * 	sql = SqlUtil.makeDynamicSql(dynamicSql, param.setVal);
	 *     //  sql --> "select * from article where id = 'demo_id' and val = 'demo_value'"
	 * </pre>
	 * 
	 * @param dynamicSql
	 *            -- 动态SQL语句
	 * @param bean
	 *            -- 替换数据源
	 * @return -- SQL语句
	 */
	@Deprecated
	public static String makeDynamicSql(String dynamicSql,
			Map<String, Object> params) {

		int ps = dynamicSql.length();
		StringBuilder sql = new StringBuilder(128 + ps * 2);
		StringBuilder item = new StringBuilder(128);

		boolean isDynamicStart = false;
		char c = 0;
		for (int i = 0; i < ps; i++) {
			c = dynamicSql.charAt(i);
			if (isDynamicStart) {
				if ('}' == c) {
					isDynamicStart = false;
					String ii = item.toString();
					item.setLength(0);
					sql.append(makeDynamicItem(ii, params));
				} else {
					item.append(c);
				}
			} else {
				if ('{' == c) {
					isDynamicStart = true;
				} else {
					sql.append(c);
				}
			}
		}
		if (item.length() > 0) {
			String ii = item.toString();
			sql.append(makeDynamicItem(ii, params));
		}
		return sql.toString();
	}

	/**
	 * 拼接动态SQL中的一个动态项 <br>
	 * 备注: 动态SQL中 在一对'{'和'}'之间的字符串就是一个动态性
	 * 
	 * @param dynamicItem
	 *            -- 动态项字符串
	 * @param params
	 *            -- 替换变量的参数
	 * @return -- SQL语句片段
	 */
	@Deprecated
	private static String makeDynamicItem(String dynamicItem,
			Map<String, Object> params) {

		int ps = dynamicItem.length();
		StringBuilder sqlItem = new StringBuilder(64 + ps * 2);
		StringBuilder param = new StringBuilder(64);

		boolean isParamStart = false;
		char c = 0;
		char flag = 0;
		for (int i = 0; i < ps; i++) {
			c = dynamicItem.charAt(i);
			if (isParamStart) {
				if (' ' == c || '\n' == c || '\t' == c || '\r' == c) {
					isParamStart = false;
					String p = param.toString();
					param.setLength(0);
					Object v = params.get(p);
					if ('$' == flag) {
						if (null == v) {
							return "";
						}
						sqlItem.append(sqlValue(v));
					} else if ('#' == flag) {
						sqlItem.append(sqlValue(v));
					} else if ('&' == flag) {
						if (null == v) {
							return "";
						}
						sqlItem.append(v);
					}
				} else {
					param.append(c);
				}
			} else {
				if ('$' == c || '#' == c || '&' == c || '?' == c || '@' == c) {
					flag = c;
					isParamStart = true;
				} else {
					sqlItem.append(c);
				}
			}
		}
		if (param.length() > 0) {
			String p = param.toString();
			Object v = params.get(p);
			if ('$' == flag) {
				if (null == v) {
					return "";
				}
				sqlItem.append(sqlValue(v));
			} else if ('#' == flag) {
				sqlItem.append(sqlValue(v));
			}
		}
		return sqlItem.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * 
	 * @param value
	 *            -- 数据
	 * @return -- SQL片段字符串
	 */
	private static String sqlValue(String value) {
		if (null == value) {
			return "''";
		}
		String v = value.trim();
		int vs = v.length();
		StringBuilder sb = new StringBuilder(2 + vs * 2);
		char c = 0;
		sb.append('\'');
		for (int i = 0; i < vs; i++) {
			c = v.charAt(i);
			// 防止sql注入处理，替换'为''，替换\为\\
			if ('\'' == c) {
				sb.append('\'');
				sb.append('\'');
			} else if ('\\' == c) {
				sb.append('\\');
				sb.append('\\');
			} else {
				sb.append(c);
			}
		}
		sb.append('\'');
		return sb.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值，默认日期格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param value
	 *            -- 数据
	 * @return -- SQL片段字符串
	 */
	private static String sqlValue(Date value) {
		return "'" + getDateFormat().format(value) + "'";
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * 
	 * @param value
	 *            -- 数据
	 * @param simpleDateFormat
	 *            -- 自定义日期格式
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(Date value, SimpleDateFormat simpleDateFormat) {
		return "'" + simpleDateFormat.format(value) + "'";
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * 
	 * @param value
	 *            -- 数据
	 * @return -- SQL片段字符串
	 */
	private static String sqlValue(Timestamp value) {
		return "'" + value + "'";
	}

	/**
	 * 拼接SQL语法的字段字符串值，适用于基本数据类型
	 * 
	 * @param value
	 * @return
	 */
	private static <T> String sqlValuePrimitive(T value) {
		return value.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值，适用于数组类型
	 * 
	 * @param value
	 * @return
	 */
	private static <T> String sqlValueArray(T[] value) {
		if (null == value) {
			return "''";
		}
		StringBuilder sql = new StringBuilder(64 + value.length * 32);
		for (int i = 0; i < value.length; i++) {
			sql.append(sqlValue(value[i]));
			if (i < value.length - 1) {
				sql.append(",");
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * 
	 * @param value
	 *            -- 数据
	 *            <p>
	 *            注意：如果字段为datetime类型，object
	 *            value不能为null，因为通过jdbc访问mysql时，datetime类型值为''时，会抛出异常
	 * @return -- SQL片段字符串，如果value为null，返回字符串：''
	 */
	public static String sqlValue(Object value) {
		if (null == value) {
			return "''";
		} else if (value instanceof String) {
			return sqlValue((String) value);
		} else if (value instanceof Date) {
			return sqlValue((Date) value);
		} else if (value instanceof Timestamp) {
			return sqlValue((Timestamp) value);
		} else if (value instanceof Integer || value instanceof Long
				|| value instanceof Short || value instanceof Float
				|| value instanceof Double) {
			// 基本数字类型
			return sqlValuePrimitive(value);
		} else if (value instanceof List) {
			return sqlValueArray(((List) value).toArray());
		} else if (value.getClass().isArray()) {
			// 数组类型，（基本数据类型没法进行autoboxing，需要进行额外处理）
			Class ct = value.getClass().getComponentType();
			if (ct == String.class) {
				return sqlValueArray(String[].class.cast(value));
			} else if (ct == int.class) {
				return sqlValueArray(boxedPrimitiveArray((int[]) value));
			} else if (ct == long.class) {
				return sqlValueArray(boxedPrimitiveArray((long[]) value));
			} else if (ct == short.class) {
				return sqlValueArray(boxedPrimitiveArray((short[]) value));
			} else if (ct == float.class) {
				return sqlValueArray(boxedPrimitiveArray((float[]) value));
			} else if (ct == double.class) {
				return sqlValueArray(boxedPrimitiveArray((double[]) value));
			}
			// 默认,转成Object对象数组
			return sqlValueArray((Object[]) value);
		} else {
			return "'" + value.toString() + "'";
		}
	}

	/**
	 * boxed int array
	 * 
	 * @param array
	 * @return
	 */
	private static Integer[] boxedPrimitiveArray(int[] array) {
		Integer[] result = new Integer[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}

	/**
	 * boxed short array
	 * 
	 * @param array
	 * @return
	 */
	private static Short[] boxedPrimitiveArray(short[] array) {
		Short[] result = new Short[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}

	/**
	 * boxed long array
	 * 
	 * @param array
	 * @return
	 */
	private static Long[] boxedPrimitiveArray(long[] array) {
		Long[] result = new Long[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}

	/**
	 * boxed float array
	 * 
	 * @param array
	 * @return
	 */
	private static Float[] boxedPrimitiveArray(float[] array) {
		Float[] result = new Float[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}

	/**
	 * boxed double array
	 * 
	 * @param array
	 * @return
	 */
	private static Double[] boxedPrimitiveArray(double[] array) {
		Double[] result = new Double[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}

	/**
	 * 用类似PreparedStatement的方式获取需要执行的sql
	 * 
	 * @param prepareSql
	 *            PreparedStatement形式的sql模板，如：select * from tba where dept= ?
	 *            and level > ?
	 * @param params
	 *            参数列表 <br/>
	 *            注意：params不能为对象数组，包括String数组，如：SqlUtil.getSql(
	 *            "select * from tba where name in (?)", new
	 *            String[]{"harry","demo"}) <br/>
	 *            但是基本类型数组是可以的，例如 new int[]{1,2,3}，如需使用对象数组请使用getSqlByList方法
	 * @return 被执行的sql
	 */
	public static String getSql(String prepareSql, Object... params) {
		if (params != null) {
			int length = prepareSql.length();
			StringBuilder result = new StringBuilder(2 + length * 2);
			int paramIndex = 0;
			for (int i = 0; i < length; i++) {
				char c = prepareSql.charAt(i);
				if (c == '?') {
					result.append(sqlValue(params[paramIndex]));
					paramIndex++;
				} else {
					result.append(c);
				}
			}
			return result.toString();
		}
		return prepareSql;
	}

	/**
	 * 用类似PreparedStatement的方式获取需要执行的sql，参数通过List传递
	 * 
	 * @param prepareSql
	 *            PreparedStatement形式的sql模板
	 * @param params
	 *            参数列表
	 * @return
	 */
	public static String getSqlByList(String prepareSql, List<Object> params) {
		if (params != null) {
			int length = prepareSql.length();
			StringBuilder result = new StringBuilder(2 + length * 2);
			int paramIndex = 0;
			for (int i = 0; i < length; i++) {
				char c = prepareSql.charAt(i);
				if (c == '?') {
					result.append(sqlValue(params.get(paramIndex)));
					paramIndex++;
				} else {
					result.append(c);
				}
			}
			return result.toString();
		}
		return prepareSql;
	}

	/**
	 * 拼接 insert SQL语句
	 * 
	 * @param tableName
	 *            -- db 表名称
	 * @param columns
	 *            -- 列集合map
	 * @return -- SQL语句
	 */
	public static String getInsertSql(String tableName,
			Map<String, Object> columns) {
		int columnSize = columns.size();
		StringBuilder sql = new StringBuilder(64 + columnSize * 32);
		sql.append("\n insert into ").append(tableName);
		sql.append(" ( ");
		int index = 0;
		for (String item : columns.keySet()) {
			sql.append(item);
			index++;
			if (index != columnSize) {
				sql.append(",");
			}
		}
		sql.append(" )\n");
		sql.append(" values ( ");
		index = 0;
		for (String item : columns.keySet()) {
			Object value = columns.get(item);
			sql.append(sqlValue(value));
			index++;
			if (index != columnSize) {
				sql.append(",");
			}
		}
		sql.append(" )");
		return sql.toString();
	}

}
