package com.maoshen.util.http;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.maoshen.util.StringUtil;
import com.maoshen.util.date.DateUtil;

public class CommonUtil {
	
	//得到网址的顶级域
	public static String getClientDomain(String clientUrl){
		if(clientUrl==null )return null;
		Pattern pattern=Pattern.compile("^https?://([a-zA-Z0-9]*\\.)?([a-zA-Z0-9]*\\.[a-zA-Z0-9]*)[\\?/#]?.*");
		Matcher matcher=pattern.matcher(clientUrl);
		if(matcher.find()){
			return matcher.group(2);
		}
		return null;
	}
	/**
	 * 判断是否为数字。
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
		if(StringUtil.isNullOrBlank(str))return false;
		str=str.trim();
	    Pattern pattern = Pattern.compile("[0-9]+"); 
	    return pattern.matcher(str).matches();    
	 } 
	public static String urlEncode(String str,String charset){
		try{
		return URLEncoder.encode(str, charset);
		}catch(Exception err){
			
		}
		return str;
	}
	public static String urlDecode(String str,String charset){
		try{
		return URLDecoder.decode(str,charset);
		}catch(Exception err){
			
		}
		return str;
	}
	/**
	 * 是否为整数！
	 * 
	 * @return
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^\\d+$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static String getStrValueFromRequest(HttpServletRequest request,
			String name, String defaultValue) {
		String value = request.getParameter(name) == null ? defaultValue
				: request.getParameter(name);
		return value;
	}
	public static boolean isIntOrFloat(String str){
		if(StringUtil.isNullOrBlank(str))return false;
		str=str.trim();
	    Pattern pattern = Pattern.compile("(-)*[0-9]+(\\.[0-9]+)*"); 
	    return pattern.matcher(str).matches(); 
	}
	/**
	 * 显示时间
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public static String compareTime(String beginDate, String endDate) {
		String strs = "";
		if (!DateUtil.compareTime(beginDate, endDate)) {
			return strs;
		}
		Date end = DateUtil.convertStrToDate(endDate, "yyyy-M-d HH:mm:ss");
		Date begin = DateUtil.convertStrToDate(beginDate, "yyyy-M-d HH:mm:ss");
		int differDay = (int)DateUtil.dateDiff("dd", DateUtil.convertStrToDate(beginDate, "yyyy-M-d"), DateUtil.convertStrToDate(endDate, "yyyy-M-d"));
		
		String str =  differDay == 2?"前天": differDay == 1?"昨天":"";
		strs = str+DateUtil.convertDateToStr(DateUtil.convertStrToDate(beginDate, "yyyy-M-d HH:mm:ss"), "HH:mm");
		if (differDay>2&&differDay<30) {
			 strs = formatDate(beginDate);
		} else if (differDay >= 30 && differDay < 365) {
			strs = (int) DateUtil.dateDiff("MM", begin, end) + "月前";
		} else if(differDay>=365){
			strs = (int) DateUtil.dateDiff("yy", begin, end) + "年前";
		}
		return strs;
	}
	
	/**
	 * 不显示年份 如: 3-8 12:12:12
	 * @param dateStr 
	 * @return
	 */
	public static String formatDate(String dateStr){
		String reDateStr=DateUtil.convertDateToStr(DateUtil.convertStrToDate(dateStr, "yyyy-M-d HH:mm:ss"), "yyyy-M-d HH:mm");
		
		if(!StringUtil.isNullOrBlank(reDateStr)){
			String nowYear=Calendar.getInstance().get(Calendar.YEAR)+"";
			String tempYear=reDateStr.substring(0,4);
			if(!nowYear.equals(tempYear))return reDateStr;
			return reDateStr.substring(5);
		}
		return reDateStr;
	}

}
