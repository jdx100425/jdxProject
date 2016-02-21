package com.maoshen.util.http;

import java.io.IOException;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器主动断开连接时重试处理类
 * @author zy
 *
 */
public class DefaultHttpRetryHandler implements HttpRequestRetryHandler{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultHttpRetryHandler.class);
	
	@Override
	public boolean retryRequest(IOException e, int executionCount, HttpContext context) {
		if(executionCount>1){
			return false;
		}
		if (e instanceof NoHttpResponseException) {
            // Retry if the server dropped connection on us
			LOG.warn("Remote server close http connection,request again...");
            return true;
        }
        return false;
	}

}
