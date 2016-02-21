package com.maoshen.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maoshen.util.JsonUtil;

/**
 * 
 * @Description:请求rest方式的接口
 * @author Richard.Chen
 * @Email  richard.chen@vipshop.com
 * @Date 2014年5月12日 下午5:18:03
 * @Version V1.0
 */
public class HttpJsonUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpJsonUtil.class);

	/**
	 * 
	 * @Description: get方式请求REST方法URL
	 * @author Richard.Chen
	 * @Email  richard.chen@vipshop.com
	 * @Date 2014年5月12日 下午5:12:03
	 * @param url
	 * @param connectTimeout
	 * @param readTimeout
	 * @param charset
	 * @return
	 */
	public static String getJson(String url,int connectTimeout, int readTimeout, String charset){
		StringBuilder build=new StringBuilder();
		DefaultHttpClient httpClient=null;
		BufferedReader reader=null;
		try {
			httpClient = new DefaultHttpClient();
			String reqUrl =url ;
			HttpGet getRequest = new HttpGet(reqUrl);
			getRequest.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(getRequest);
	 		if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "  + response.getStatusLine().getStatusCode());
			}
	 
	 		reader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
	 
			String output;
			while ((output = reader.readLine()) != null) {
				build.append(output);
			}
	 	 
		  } catch (Exception e) {
			  e.printStackTrace();
		  } finally {
			  if(reader!=null){
					try{
					reader.close();// 关闭输入流
					}catch(Exception e){
						e.printStackTrace();
					}
					}
				if (httpClient != null) {
					httpClient.getConnectionManager().shutdown();// 最后关掉链接。
					httpClient = null;
				}
		  }
		return build.toString();
	}

	
	/**
	 * 
	 * @Description: POST方法提交JSON数据
	 * @author Richard.Chen
	 * @Email  richard.chen@vipshop.com
	 * @Date 2014年5月12日 下午5:12:22
	 * @param url
	 * @param jsonObject
	 * @param connectTimeout
	 * @param readTimeout
	 * @param charset
	 * @return
	 */
	public static String postJson(String url, Object jsonObject,
			int connectTimeout, int readTimeout, String charset) {

		DefaultHttpClient httpClient;
		BufferedReader reader=null;
		HttpParams paramsw = createHttpParams(connectTimeout, readTimeout);
		httpClient = new DefaultHttpClient(paramsw);
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-type", "application/json");
		StringBuilder result = new StringBuilder();
		try {
			// 向服务器写json
			StringEntity se = new StringEntity(JsonUtil.toJson(jsonObject),
					charset);
			post.setEntity(se);
			logger.info(url);
			logger.info(JsonUtil.toJson(jsonObject));
			HttpResponse httpResponse = httpClient.execute(post);


				HttpEntity entity = httpResponse.getEntity();
				// 读取服务器返回的json数据（接受json服务器数据）
				InputStream inputStream = entity.getContent();
				
				//接收数据需要定义编码格式
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream, charset);
				reader = new BufferedReader(inputStreamReader);// 读字符串用的。
				String s;
				while (((s = reader.readLine()) != null)) {
					result.append(s);
				}
				reader.close();// 关闭输入流
			
		
		} catch (UnsupportedEncodingException e) {
			logger.error("postJson", e);
		} catch (ClientProtocolException e) {
			logger.error("postJson", e);
		} catch (IOException e) {
		} finally {
			 if(reader!=null){
					try{
					reader.close();// 关闭输入流
					}catch(Exception e){
						e.printStackTrace();
					}
					}
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();// 最后关掉链接。
				httpClient = null;
			}
		}

		return result.toString();
	}

	private static HttpParams createHttpParams(int connectTimeout,
			int readTimeout) {
		final HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, connectTimeout);
		HttpConnectionParams.setSoTimeout(params, readTimeout);
		HttpConnectionParams.setSocketBufferSize(params, 8192 * 5);
		return params;
	}

	/**
	 * 
	 * @Description: 以form的形式post数据
	 * @author Richard.Chen
	 * @Email  richard.chen@vipshop.com
	 * @Date 2014年5月13日 下午2:28:24
	 * @param url
	 * @param params
	 * @param connectTimeout
	 * @param readTimeout
	 * @param charset
	 * @return
	 */
	public static String postForm(String url, HashMap params,int connectTimeout, int readTimeout, String charset) {
			StringBuilder result = new StringBuilder();
        	DefaultHttpClient httpClient = new DefaultHttpClient();
        	BufferedReader reader=null;
        	HttpResponse httpResponse=null;
	        HttpPost httpPost = new HttpPost(url);  
	        httpPost.addHeader("charset", "UTF-8");
	        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  	          
	        Set<String> keySet = params.keySet();  
	        for(String key : keySet) {  
	            nvps.add(new BasicNameValuePair(key, (String)params.get(key)));  
	        }  
	   try {
	    httpPost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
	
        httpResponse = httpClient.execute(httpPost);

		HttpEntity entity = httpResponse.getEntity();
		// 读取服务器返回的json数据（接受json服务器数据）
		InputStream inputStream = entity.getContent();
			
		//接收数据需要定义编码格式
		reader = new BufferedReader(new InputStreamReader(inputStream, charset));// 读字符串用的。
		String s;
		while (((s = reader.readLine()) != null)) {
				result.append(s);
		}
		
	} catch (UnsupportedEncodingException e) {
		logger.error("postForm", e);
	} catch (ClientProtocolException e) {
		logger.error("postForm", e);
	} catch (IOException e) {
	} finally {
		if(reader!=null){
			try{
			reader.close();// 关闭输入流
			}catch(Exception e){
				e.printStackTrace();
			}
			}
		if (httpClient != null) {
			httpClient.getConnectionManager().shutdown();// 最后关掉链接。
			httpClient = null;
		}
	}
     return result.toString();
	}       

	/**
	 * 
	 * @Description:PUT数据
	 * @author Richard.Chen
	 * @Email  richard.chen@vipshop.com
	 * @Date 2014年5月13日 下午2:28:24
	 * @param url
	 * @param params
	 * @param connectTimeout
	 * @param readTimeout
	 * @param charset
	 * @return
	 */
	public static String postPut(String url, HashMap params,int connectTimeout, int readTimeout, String charset) {
			StringBuilder result = new StringBuilder();
        	DefaultHttpClient httpClient = new DefaultHttpClient();
        	BufferedReader reader=null;
        	HttpResponse httpResponse=null;
        	HttpPut httpPut = new HttpPut(url);  
        	httpPut.addHeader("charset", "UTF-8");
	        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  	          
	        Set<String> keySet = params.keySet();  
	        for(String key : keySet) {  
	            nvps.add(new BasicNameValuePair(key, (String)params.get(key)));  
	        }  
	   try {
		   httpPut.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
	    httpResponse = httpClient.execute(httpPut);


			HttpEntity entity = httpResponse.getEntity();
			// 读取服务器返回的json数据（接受json服务器数据）
			InputStream inputStream = entity.getContent();
			
			//接收数据需要定义编码格式
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, charset);
			reader = new BufferedReader(inputStreamReader);// 读字符串用的。
			String s;
			while (((s = reader.readLine()) != null)) {
				result.append(s);
			}
			reader.close();// 关闭输入流
	
	} catch (UnsupportedEncodingException e) {
		logger.error("postPut", e);
	} catch (ClientProtocolException e) {
		logger.error("postPut", e);
	} catch (IOException e) {
	} finally {
		if(reader!=null){
			try{
			reader.close();// 关闭输入流
			}catch(Exception e){
				e.printStackTrace();
			}
			}
		if (httpClient != null) {
			httpClient.getConnectionManager().shutdown();// 最后关掉链接。
			httpClient = null;
		}
	}
     return result.toString();
	}       
	

/**
 * 
 * @Description:DELETE数据
 * @author Richard.Chen
 * @Email  richard.chen@vipshop.com
 * @Date 2014年5月13日 下午2:28:24
 * @param url
 * @param params
 * @param connectTimeout
 * @param readTimeout
 * @param charset
 * @return
 */
public static String postDelete(String url, HashMap params,int connectTimeout, int readTimeout, String charset) {
		StringBuilder result = new StringBuilder();
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	BufferedReader reader=null;
    	HttpResponse httpResponse=null;
    	HttpDelete httpDelete = new HttpDelete(url);  
    	httpDelete.addHeader("charset", "UTF-8");
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  	          
        Set<String> keySet = params.keySet();  
        for(String key : keySet) {  
            nvps.add(new BasicNameValuePair(key, (String)params.get(key)));  
        }  
   try {
    httpResponse = httpClient.execute(httpDelete);


		HttpEntity entity = httpResponse.getEntity();
		// 读取服务器返回的json数据（接受json服务器数据）
		InputStream inputStream = entity.getContent();
		
		//接收数据需要定义编码格式
		InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream, charset);
		reader = new BufferedReader(inputStreamReader);// 读字符串用的。
		String s;
		while (((s = reader.readLine()) != null)) {
			result.append(s);
		}
		reader.close();// 关闭输入流

   } catch (UnsupportedEncodingException e) {
	   logger.error("postDelete", e);
   } catch (ClientProtocolException e) {
	   logger.error("postDelete", e);
   } catch (IOException e) {
   } finally {
	if(reader!=null){
		try{
		reader.close();// 关闭输入流
		}catch(Exception e){
			e.printStackTrace();
		}
		}
	if (httpClient != null) {
		httpClient.getConnectionManager().shutdown();// 最后关掉链接。
		httpClient = null;
	}
}
 return result.toString();
}     

}


