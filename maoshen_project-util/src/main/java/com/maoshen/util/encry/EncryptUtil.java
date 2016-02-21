package com.maoshen.util.encry;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 加密有关的工具类
 * @author whx
 *
 */
public class EncryptUtil {

	/**
	 * 获取内容md5加密后的字符串
	 * @param content 加密内容
	 * @param charset 字符集
	 * @return
	 */
	public static String encryptMD5(String content, String charset){
		StringBuilder result = new StringBuilder();
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(content.getBytes(charset));
	        byte[] byteDigest = md.digest();
	        for (int i = 0; i < byteDigest.length; i++) { 
	        	String tmpStr = Integer.toHexString(0xFF & byteDigest[i]);
	            if (tmpStr.length() == 1) {  
	            	result.append("0").append(tmpStr);  
	            } else  
	            	result.append(tmpStr);  
	        } 	        
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 not supported", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Charset not supported", e);
		}
		return result.toString();
	}

}
