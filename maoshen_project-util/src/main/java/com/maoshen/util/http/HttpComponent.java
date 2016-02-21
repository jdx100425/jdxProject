package com.maoshen.util.http;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行http操作（尽量复用，不要创建多个实例） 
 * @author zy
 *
 */
public class HttpComponent {
	private static final Logger LOG = LoggerFactory.getLogger(HttpComponent.class);

	private HttpClient httpClient;
	private PoolingHttpClientConnectionManager connectionManager;
	DefaultHttpResponseHandler httpHandler = new DefaultHttpResponseHandler();

	/**
	 * 
	 * @param maxPerRoute 
	 * @param maxTotal
	 * @param connTimeout 连接超时时间
	 * @param soTimeout 读数据超时时间
	 * @param staleConnCheck 是否开启坏连接检测线程
	 * @throws Exception
	 */
	public HttpComponent(int maxPerRoute, int maxTotal, int connTimeout, int soTimeout, boolean staleConnCheck)
			throws Exception {
		SSLContext sslcontext = SSLContext.getInstance("TLS");
		TrustManager truseAllManager = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;

			}
		};
		sslcontext.init(null, new TrustManager[] { truseAllManager }, null);
		
		SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(sslcontext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		PlainConnectionSocketFactory plainsf = PlainConnectionSocketFactory.INSTANCE;
		Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", ssf)
				.register("http", plainsf)
				.build();
		
		

		this.connectionManager = new PoolingHttpClientConnectionManager(r);
		
		connectionManager.setDefaultMaxPerRoute(maxPerRoute);
		connectionManager.setMaxTotal(maxTotal);
	
		if (staleConnCheck) {
			new IdleConnectionMonitorThread(connectionManager).start();
		}
		
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(connTimeout)
				.setSocketTimeout(soTimeout)
				.build();
		
		httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.setDefaultRequestConfig(requestConfig)
				.setRetryHandler(new DefaultHttpRetryHandler())
				.build();		

	}


	private <T> T execute(HttpUriRequest httpUriRequest, ResponseHandler<T> handler) throws IOException {
		return this.httpClient.execute(httpUriRequest, handler, new BasicHttpContext());
	}

	private <T> T executeWithLog(HttpUriRequest httpUriRequest, ResponseHandler<T> handler) throws IOException {
		long start = System.currentTimeMillis();
		
		T result = this.httpClient.execute(httpUriRequest, handler, new BasicHttpContext());
		LOG.info("send request to: {}, execute time: {}ms", httpUriRequest.getURI(),
				Long.valueOf(System.currentTimeMillis() - start));
		return result;
	}

	/**
	 * 执行http操作，并用handler解析返回结果
	 * @param url
	 * @param params 请求参数
	 * @param method get|post|put|delete
	 * @param handler
	 * @return
	 * @throws IOException
	 */
	public <T> T execute(String url, Map<String, Object> params,String method,ResponseHandler<T> handler) throws IOException{
		return this.execute(url, params, method, "utf8", handler,null);
	}
	
	private <T> T execute(String url, Map<String, Object> params,String method,String requestEncoding,ResponseHandler<T> handler,RequestConfig config) throws IOException{
		String baseUrl = url;
		if(!"post".equals(method)){
			List pair = new ArrayList();
			if (params != null && params.size() > 0) {
				for (Map.Entry entry : params.entrySet()) {
					pair.add(new BasicNameValuePair((String) entry.getKey(),String.valueOf(entry.getValue())));
				}
				if(url.indexOf("?")>=0) url = baseUrl + "&" + URLEncodedUtils.format(pair, requestEncoding);
				else url = baseUrl + "?" + URLEncodedUtils.format(pair, requestEncoding);
			}
			LOG.info("{} request url:{}",method,url);
		}

		HttpRequestBase httpType = null;
		
		if("get".equals(method)){
			httpType = new HttpGet(url);
		}
		else if("post".equals(method)){
			HttpPost post = new HttpPost(baseUrl);
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();  
            if(params!=null && params.size()>0){
            	StringBuilder  paramStr = new StringBuilder();
                for(Map.Entry<String, Object> entry:params.entrySet()){
               	 	nvps.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
               	 	paramStr.append(entry.getKey()).append("=").append(String.valueOf(entry.getValue())).append("&");
               }               
               post.setEntity(new UrlEncodedFormEntity(nvps, requestEncoding)); 
               
               LOG.info("post request url:{} ,params:{} ",baseUrl, paramStr.toString());
            }
            else{
            	 LOG.info("post request url:{}",baseUrl);
            }
            
			httpType = post;
		}
		else if("put".equals(method)){
			httpType = new HttpPut(url);
		}
		else if("delete".equals(method)){
			httpType = new HttpDelete(url);
		}
		else{
			throw new IOException("Unknown http method:"+method);
		}
		if(config!=null){
			httpType.setConfig(config);
		}
		return this.executeWithLog(httpType, handler);
	}
	
	public String get(String url, Map<String, Object> params) throws IOException{
		return execute(url,params,"get",httpHandler);
	}
	
	public String get(String url, Map<String, Object> params,int readTimeout) throws IOException{
		RequestConfig config = RequestConfig.custom().setSocketTimeout(readTimeout).build();
		return execute(url,params,"get","utf8",httpHandler,config);
	}
	

	public String post(String url, Map<String, Object> params) throws IOException{
		return execute(url,params,"post",httpHandler);
	}
	public String post(String url, Map<String, Object> params,int readTimeout) throws IOException{
		RequestConfig config = RequestConfig.custom().setSocketTimeout(readTimeout).build();
		return execute(url,params,"post","utf8",httpHandler,config);
	}
	
	public String put(String url, Map<String, Object> params) throws IOException{
		return execute(url,params,"put",httpHandler);
	}
	public String put(String url, Map<String, Object> params,int readTimeout) throws IOException{
		RequestConfig config = RequestConfig.custom().setSocketTimeout(readTimeout).build();
		return execute(url,params,"put","utf8",httpHandler,config);
	}
	
	public String delete(String url, Map<String, Object> params) throws IOException{
		return execute(url,params,"delete",httpHandler);
	}
	public String delete(String url, Map<String, Object> params,int readTimeout) throws IOException{
		RequestConfig config = RequestConfig.custom().setSocketTimeout(readTimeout).build();
		return execute(url,params,"delete","utf8",httpHandler,config);
	}
	
	public void shutdown() {
		LOG.debug("Connection manager is shutting down");
		connectionManager.shutdown();
		LOG.debug("Connection manager shut down");
	}

	public class IdleConnectionMonitorThread extends Thread {
		private final PoolingHttpClientConnectionManager connMgr;
		private volatile boolean shutdown;

		public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager paramClientConnectionManager) {
			this.connMgr = paramClientConnectionManager;
		}

		public void run() {
			try {
				while (!(this.shutdown))
					synchronized (this) {
						super.wait(5000L);

						this.connMgr.closeExpiredConnections();

						this.connMgr.closeIdleConnections(30L, TimeUnit.SECONDS);
					}
			} catch (InterruptedException ex) {
				HttpComponent.LOG.warn("exception occur, " + ex.getMessage());
			}
		}

		public void shutdown() {
			this.shutdown = true;
			synchronized (this) {
				super.notifyAll();
			}
		}
	}
}
