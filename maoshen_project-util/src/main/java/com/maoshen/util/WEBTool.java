package com.maoshen.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maoshen.util.encry.MD5Util;

/**
 * 
 * @Description:封装常用的WEB工具
 * @author Richard.Chen
 * @Email  richard.chen@vipshop.com
 * @Date 2014年4月8日 下午2:33:37
 * @Version V1.0
 */
public class WEBTool {
	
	/**
	 * 以MD5形式生成ETAG标记
	 * @param str
	 * @return
	 */
	public static String generateEtag(String str)
	{
		//etag在HTTP1.1中没有具体标名要求，只要双引号包含的字符串就可以
		StringBuilder etag=new StringBuilder();
		etag.append("\"");
		etag.append(MD5Util.MD5(str));
		etag.append("\"");
		return etag.toString();

	}
	
	/**
	 * 返回用户请求的URL已添加上下文路径 /xxx/xxx/xx.do?xx=aa
	 * @param request
	 * @return
	 */
	public static String getRequestURL(HttpServletRequest request){
		StringBuilder requestURL=new StringBuilder();
		requestURL.append(request.getRequestURI());
		if(request.getQueryString()!=null && !"".equals(request.getQueryString())){
			requestURL.append("?"+request.getQueryString()); //路径与参数中由?隔开  
		}
		return requestURL.toString();
	}
	
	/**
	 * 返回用户请求URL 不带参数 不带上下文路径 /xx.do
	 * @param request
	 * @return
	 */
	public static String getServletPath(HttpServletRequest request){
		return request.getServletPath();
	}
	
	/**
	 * 返回用户请求的完整URL http://11.11.11.11:8080/xxx/xxx/xx.do?xx=aa
	 * @param request
	 * @return
	 */
	public static String getRequestFullURL(HttpServletRequest request){
		StringBuilder requestURL=new StringBuilder();
		requestURL.append(request.getRequestURL().toString());
		if(request.getQueryString()!=null && !"".equals(request.getQueryString())){
			requestURL.append("?"+request.getQueryString()); //路径与参数中由?隔开  
		}
		return requestURL.toString();
	}
	
	/**
	 * 返回请求客户端的IP地址
	 * @param request
	 * @return
	 */
	public static String getClientIP(HttpServletRequest request){
		String s = request.getHeader("X-Forwarded-For");  
	    if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))  
	        s = request.getHeader("Proxy-Client-IP");  
	    if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))  
	        s = request.getHeader("WL-Proxy-Client-IP");  
	    if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))  
	        s = request.getHeader("HTTP_CLIENT_IP");  
	    if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))  
	        s = request.getHeader("HTTP_X_FORWARDED_FOR");  
	    if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))  
	        s = request.getRemoteAddr();  
	    return s;  

	}
	
	
	/**
	 * 返回带服务器上下文的绝对路径	/servletContext/url
	 * @param request
	 * @param url 你的相对根路径，不添加上下文的URL地址
	 * @return
	 */
	public static String getWEBURL(HttpServletRequest request,String url){
		StringBuilder weburl=new StringBuilder();
		String contextPath=request.getContextPath();
		//如果上下文路径默认是/，那么先置这空，再按以下规则生成
		if("/".equals(contextPath)) contextPath="";
		
		if(url==null){//如果是空URL，返回上下文路径
			weburl.append(contextPath);
			weburl.append("/");		
		}else if(url.startsWith("/")){//如果url以/开头，contextPath加url
			weburl.append(contextPath);
			weburl.append(url);			
		}else if(!url.startsWith("/")){//如果url不是以/开头，contextPath先加/再加url				
			weburl.append(contextPath);
			weburl.append("/");
			weburl.append(url);
		}
		
		return weburl.toString();
		
	}
	
	/**
	 * 编码URL地址
	 * @param response
	 * @param url
	 * @return
	 */
	public static String encodeURL(HttpServletResponse response,String url){
		return response.encodeURL(url);
	}
}
