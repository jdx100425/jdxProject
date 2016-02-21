package com.maoshen.util.encry;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;

import com.maoshen.util.StringUtil;


public class MD5Util {

	public final static String getMd5ApiSign(String apiKey,
			ArrayList<Object> paramList, String saltParam) {
		
		StringBuffer str = new StringBuffer();
		if (!StringUtil.isNullOrBlank(apiKey)) {
			str.append(apiKey);
		}

		if (null != paramList && paramList.size() > 0) {
			for (Object object : paramList) {
				str.append(object);
			}
		}
		if (!StringUtil.isNullOrBlank(apiKey)) {
			str.append(saltParam);
		}

		return MD5(str.toString(), null);
	}
	
	public final static String MD5(String s) {
	    return MD5(s,null);
	}
	
	
	public final static String MD5(String s,String charsetname) {
	     byte[] btInput =null;
	     if (charsetname==null){
	    	 btInput = s.getBytes();
	     }else{
	    	 try {
				btInput=s.getBytes(charsetname);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	     }
	     return MD5(btInput);
	}
	public final static String MD5(byte[] data) {
	    try {
	     MessageDigest mdInst = MessageDigest.getInstance("MD5");
	     mdInst.update(data);
	     byte[] md = mdInst.digest();
	     StringBuffer sb = new StringBuffer();
	     for (int i = 0; i < md.length; i++) {
	      int val = (md[i]) & 0xff;
	      if (val < 16)
	       sb.append("0");
	      sb.append(Integer.toHexString(val));

	     }
	     return sb.toString();
	    } catch (Exception e) {
	     return null;
	    }
	}

	public final static byte[] MD5Encode(byte[] data) {
	    try {
		     MessageDigest mdInst = MessageDigest.getInstance("MD5");
		     mdInst.update(data);
		     byte[] md = mdInst.digest();
		     
		     return md;
		    } catch (Exception e) {
	     return null;
	    }
	}
	public static void main(String[] args) {
		StringBuilder checkStr = new StringBuilder();
		checkStr.append("qwe123111");
		checkStr.append("19293");
		checkStr.append("13800138000");
		
		String checkStrs = null;
		checkStrs = MD5Util.MD5("funinfo268447843862088383566058user=w=woshibantang&id=43862088;temp=k=383566058&t=1308305823;sso=r=829650&sid=63FB479CEE202F60DEE59B3DDBD05786&wsid=38F125108FBD1D1FC68C6E843DEF423B;afjlk2340rsdjk92pp");
		System.out.println(checkStrs);

	}
	}