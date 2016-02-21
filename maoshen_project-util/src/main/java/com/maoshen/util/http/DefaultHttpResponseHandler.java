package com.maoshen.util.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public class DefaultHttpResponseHandler implements ResponseHandler<String> {

	@Override
	public String handleResponse(HttpResponse response) throws IOException {
		StatusLine statusLine = response.getStatusLine();
		HttpEntity entity = response.getEntity();
		if (statusLine.getStatusCode() >= 300) {
			String errMsg = EntityUtils.toString(entity);
			throw new HttpResponseException(statusLine.getStatusCode(), errMsg);
		}
		return EntityUtils.toString(entity);
	}

}