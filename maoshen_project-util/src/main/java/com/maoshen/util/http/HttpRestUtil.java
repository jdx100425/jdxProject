package com.maoshen.util.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 访问rest api的工具类
 * 如果需要设置不同的连接超时、读取超时时间，需要直接使用HttpComponent来操作
 * @author zy
 *
 */
@Deprecated
public class HttpRestUtil {
	
	public static Logger logger = LoggerFactory.getLogger(HttpRestUtil.class);
	
	public static HttpComponent httpComponent ;
	public static ResponseHandler<String> handler = new DefaultHttpResponseHandler();
	static{
		try {
			httpComponent = new HttpComponent(200, 200, 1000, 5000, true);
		} catch (Exception e) {
			logger.error("init httpcomponent error:",e);
		}
	}
	
	/**
	 * get方式访问一个URL，并将结果转成字符串返回
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, Map<String, Object> params) throws IOException {
		return httpComponent.execute(url,params,"get",handler);
	}
	

	public static String post(String url, Map<String, Object> params) throws IOException {
		return httpComponent.execute(url,params,"post",handler);
	}

	public static String put(String url, Map<String, Object> params) throws IOException {
		return httpComponent.execute(url,params,"put",handler);
	}
	
	
	public static String delete(String url, Map<String, Object> params) throws IOException {
		return httpComponent.execute(url,params,"delete",handler);
	}
	
	
	public static void main(String[] args){
		try {
			Map param = new HashMap();
			param.put("ver","1.0.0");
			param.put("service", "Schedule.BrandList.getListById");
			System.out.println(HttpRestUtil.post("http://goods.api.vipshop.com/gw.php?sdf=sd", param));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
