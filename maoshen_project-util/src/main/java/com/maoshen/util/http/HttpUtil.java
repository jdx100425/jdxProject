package com.maoshen.util.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.vipshop.util.ErrorCacheUtil;





import com.maoshen.util.JsonUtil;
import com.maoshen.util.date.DateUtil;

/**
 * 提供通过HTTP协议获取内容的方法 <br/>
 * 所有提供方法中的params参数在内部不会进行自动的url encode，如果提交参数需要进行url encode，请调用方自行处理
 */
@Deprecated
public class HttpUtil {
	/**
	 * logger
	 */
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);


	private static Logger methodMonitor = LoggerFactory.getLogger("methodMonitorLogger");
	/**
	 * 支持的Http method
	 * 
	 */
	private static enum HttpMethod {
		POST, DELETE, GET, PUT, HEAD;
	};

	private static String invokeUrl(String url, Map params,
			Map<String, String> headers, int connectTimeout, int readTimeout,
			String encoding, HttpMethod method) {
		// 构造请求参数字符串
		String monitorUrl = url ;
		StringBuilder paramsStr = null;
		if (params != null) {
			paramsStr = new StringBuilder();
			Set<Map.Entry> entries = params.entrySet();
			for (Map.Entry entry : entries) {
				String value = (entry.getValue() != null) ? (String
						.valueOf(entry.getValue())) : "";
				paramsStr.append(entry.getKey() + "=" + value + "&");
			}
			// 只有POST方法才能通过OutputStream(即form的形式)提交参数
			if (method != HttpMethod.POST) {
				url += "?" + paramsStr.toString();
			}
		}

		URL uUrl = null;
		HttpURLConnection conn = null;
		BufferedWriter out = null;
		BufferedReader in = null;
		try {
			// 创建和初始化连接
			logger.info("url:" + url);
			uUrl = new URL(url);
			conn = (HttpURLConnection) uUrl.openConnection();
			conn.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			conn.setRequestMethod(method.toString());
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 设置连接超时时间
			conn.setConnectTimeout(connectTimeout);
			// 设置读取超时时间
			conn.setReadTimeout(readTimeout);
			// 指定请求header参数
			if (headers != null && headers.size() > 0) {
				Set<String> headerSet = headers.keySet();
				for (String key : headerSet) {
					conn.setRequestProperty(key, headers.get(key));
				}
			}

			if (paramsStr != null && method == HttpMethod.POST) {
				// 发送请求参数
				out = new BufferedWriter(new OutputStreamWriter(
						conn.getOutputStream(), encoding));
				out.write(paramsStr.toString());
				out.flush();
			}

			// 接收返回结果
			StringBuilder result = new StringBuilder();
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), encoding));
			if (in != null) {
				String line = "";
				while ((line = in.readLine()) != null) {
					result.append(line);
				}
			}
			return result.toString();
		} catch (Exception e) {
			// 处理错误流，提高http连接被重用的几率
			
			// 接口异常记录请求日志
//			ErrorCacheUtil.getHttpErrorCache(monitorUrl, JsonUtil.toJson(params));		
						
			try {
				byte[] buf = new byte[100];
				InputStream es = null ;
				if( null != conn ){
					es= conn.getErrorStream();
				}				
				if (es != null) {
					while (es.read(buf) > 0) {
						;
					}
					es.close();
				}
			} catch (Exception e1) {
				logger.error("处理错误流出错：", e1);
			}
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				logger.error("关闭输出流出错：", e);
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				logger.error("关闭输入流出错：", e);
			}
			// 关闭连接
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}
	
	private static Map<String, Object> getResponseCode(String url, Map params,
            Map<String, String> headers, int connectTimeout, int readTimeout,
            String encoding, HttpMethod method) {
	    
	    Map<String, Object> rtv = new HashMap<String, Object>();
	    int responseCode = -1;
	    String responseMsg = null;
	    
        // 构造请求参数字符串
        String monitorUrl = url ;
        StringBuilder paramsStr = null;
        if (params != null) {
            paramsStr = new StringBuilder();
            Set<Map.Entry> entries = params.entrySet();
            for (Map.Entry entry : entries) {
                String value = (entry.getValue() != null) ? (String
                        .valueOf(entry.getValue())) : "";
                paramsStr.append(entry.getKey() + "=" + value + "&");
            }
            // 只有POST方法才能通过OutputStream(即form的形式)提交参数
            if (method != HttpMethod.POST) {
                url += "?" + paramsStr.toString();
            }
        }

        URL uUrl = null;
        HttpURLConnection conn = null;
        BufferedWriter out = null;
        BufferedReader in = null;
        try {
            // 创建和初始化连接
            logger.info("url:" + url);
            uUrl = new URL(url);
            conn = (HttpURLConnection) uUrl.openConnection();
            conn.setRequestProperty("content-type",
                    "application/x-www-form-urlencoded");
            conn.setRequestMethod(method.toString());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置连接超时时间
            conn.setConnectTimeout(connectTimeout);
            // 设置读取超时时间
            conn.setReadTimeout(readTimeout);
            // 指定请求header参数
            if (headers != null && headers.size() > 0) {
                Set<String> headerSet = headers.keySet();
                for (String key : headerSet) {
                    conn.setRequestProperty(key, headers.get(key));
                }
            }

            if (paramsStr != null && method == HttpMethod.POST) {
                // 发送请求参数
                out = new BufferedWriter(new OutputStreamWriter(
                        conn.getOutputStream(), encoding));
                out.write(paramsStr.toString());
                out.flush();
                responseCode = conn.getResponseCode();
            }
            
            // 接收返回结果
            StringBuilder result = new StringBuilder();
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), encoding));
            if (in != null) {
                String line = "";
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            }
            responseMsg = result.toString();
            
        } catch (Exception e) {
            // 处理错误流，提高http连接被重用的几率
            
            // 接口异常记录请求日志
//          ErrorCacheUtil.getHttpErrorCache(monitorUrl, JsonUtil.toJson(params));      
                        
            try {
                byte[] buf = new byte[100];
                InputStream es = null ;
                if( null != conn ){
                    es= conn.getErrorStream();
                }               
                if (es != null) {
                    while (es.read(buf) > 0) {
                        ;
                    }
                    es.close();
                }
            } catch (Exception e1) {
                logger.error("处理错误流出错：", e1);
            }
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.error("关闭输出流出错：", e);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                logger.error("关闭输入流出错：", e);
            }
            // 关闭连接
            if (conn != null) {
                conn.disconnect();
            }
        }
        
        rtv.put("responseCode", responseCode);
        rtv.put("responseMsg", responseMsg);
        return rtv;
    }

	/**
	 * POST方法提交Http请求，语义为“增加” <br/>
	 * 注意：Http方法中只有POST方法才能使用body来提交内容
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String post(String url, Map params, int connectTimeout,
			int readTimeout, String charset) {
		return invokeUrl(url, params, null, connectTimeout, readTimeout,
				charset, HttpMethod.POST);
	}

    public static Map<String, Object> postResponse(String url, Map params, int connectTimeout,
            int readTimeout, String charset) {
        return getResponseCode(url, params, null, connectTimeout, readTimeout,
                charset, HttpMethod.POST);
    }
	
	/**
	 * POST方法提交Http请求，语义为“增加” <br/>
	 * 注意：Http方法中只有POST方法才能使用body来提交内容
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param headers
	 *            请求头参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String post(String url, Map params,
			Map<String, String> headers, int connectTimeout, int readTimeout,
			String charset) {
		return invokeUrl(url, params, headers, connectTimeout, readTimeout,
				charset, HttpMethod.POST);
	}

	/**
	 * GET方法提交Http请求，语义为“查询”
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String get(String url, Map params, int connectTimeout,
			int readTimeout, String charset) {
		return invokeUrl(url, params, null, connectTimeout, readTimeout,
				charset, HttpMethod.GET);
	}

	/**
	 * GET方法提交Http请求，语义为“查询”
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param headers
	 *            请求头参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String get(String url, Map params,
			Map<String, String> headers, int connectTimeout, int readTimeout,
			String charset) {
		return invokeUrl(url, params, headers, connectTimeout, readTimeout,
				charset, HttpMethod.GET);
	}

	/**
	 * PUT方法提交Http请求，语义为“更改” <br/>
	 * 注意：PUT方法也是使用url提交参数内容而非body，所以参数最大长度收到服务器端实现的限制，Resin大概是8K
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String put(String url, Map params, int connectTimeout,
			int readTimeout, String charset) {
		return invokeUrl(url, params, null, connectTimeout, readTimeout,
				charset, HttpMethod.PUT);
	}

	/**
	 * PUT方法提交Http请求，语义为“更改” <br/>
	 * 注意：PUT方法也是使用url提交参数内容而非body，所以参数最大长度收到服务器端实现的限制，Resin大概是8K
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param headers
	 *            请求头参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String put(String url, Map params,
			Map<String, String> headers, int connectTimeout, int readTimeout,
			String charset) {
		return invokeUrl(url, params, headers, connectTimeout, readTimeout,
				charset, HttpMethod.PUT);
	}

	/**
	 * DELETE方法提交Http请求，语义为“删除”
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String delete(String url, Map params, int connectTimeout,
			int readTimeout, String charset) {
		return invokeUrl(url, params, null, connectTimeout, readTimeout,
				charset, HttpMethod.DELETE);
	}

	/**
	 * DELETE方法提交Http请求，语义为“删除”
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param headers
	 *            请求头参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String delete(String url, Map params,
			Map<String, String> headers, int connectTimeout, int readTimeout,
			String charset) {
		return invokeUrl(url, params, headers, connectTimeout, readTimeout,
				charset, HttpMethod.DELETE);
	}

	/**
	 * HEAD方法提交Http请求，语义同GET方法 <br/>
	 * 跟GET方法不同的是，用该方法请求，服务端不返回message body只返回头信息，能节省带宽
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String head(String url, Map params, int connectTimeout,
			int readTimeout, String charset) {
		return invokeUrl(url, params, null, connectTimeout, readTimeout,
				charset, HttpMethod.HEAD);
	}

	/**
	 * HEAD方法提交Http请求，语义同GET方法 <br/>
	 * 跟GET方法不同的是，用该方法请求，服务端不返回message body只返回头信息，能节省带宽
	 * 
	 * @param url
	 *            资源路径（如果url中已经包含参数，则params应该为null）
	 * @param params
	 *            参数
	 * @param headers
	 *            请求头参数
	 * @param connectTimeout
	 *            连接超时时间（单位为ms）
	 * @param readTimeout
	 *            读取超时时间（单位为ms）
	 * @param charset
	 *            字符集（一般该为“utf-8”）
	 * @return
	 */
	public static String head(String url, Map params,
			Map<String, String> headers, int connectTimeout, int readTimeout,
			String charset) {
		return invokeUrl(url, params, headers, connectTimeout, readTimeout,
				charset, HttpMethod.HEAD);
	}

}
